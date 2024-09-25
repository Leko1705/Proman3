package proman.threading;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class BGT {

    private static final BackgroundThread bgThread = new BackgroundThread();


    public static void invokeLater(Runnable runnable) {
        bgThread.lock.release();
        bgThread.tasks.add(runnable);
    }

    public static boolean isBGT(){
        return Thread.currentThread() == bgThread;
    }

    public static void shutdown(){
        bgThread.lock.release();
        bgThread.running = false;
    }


    private static class BackgroundThread extends Thread {

        private volatile boolean running = true;

        private final Semaphore lock = new Semaphore(1);

        Queue<Runnable> tasks = new LinkedBlockingQueue<>();

        public BackgroundThread() {
            setDaemon(false);
            start();
        }

        @Override
        public void run() {
            while (running) {
                if (!tasks.isEmpty()) {
                    Runnable task = tasks.poll();
                    task.run();
                }
                else {
                    try {
                        lock.acquire();
                    }
                    catch (InterruptedException ignored){}
                }
            }
            super.run();
        }
    }

}

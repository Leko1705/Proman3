import org.junit.jupiter.api.Test;
import proman.diagrams.Element;
import proman.diagrams.coordsys.CoordinateSystem2D;
import proman.diagrams.types.UMLClassDiagramType;
import proman.view.NavigableMapPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.Semaphore;

public class DiagramUITest {

    @Test
    public void test(){
        testView(new NavigableMapPanel(true, true, true), true);
        //testCoord2D();
    }

    private void testCoord2D(){
        testView(new CoordinateSystem2D(0, new UMLClassDiagramType()) {
            @Override
            public Iterable<Element> getElements() {
                return List.of();
            }
        }, false);
    }

    private void testView(JPanel panel, boolean includeDummyComponent){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());
        if(includeDummyComponent) {
            JPanel testPanel = new JPanel();
            testPanel.setSize(100, 100);
            testPanel.setBackground(Color.BLACK);
            panel.add(testPanel);
        }
        frame.getContentPane().add(panel);
        frame.setVisible(true);


        Semaphore sem = new Semaphore(0);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                sem.release();
            }
        });

        try {
            sem.acquire();
        }
        catch (Exception ignored){}
    }

}

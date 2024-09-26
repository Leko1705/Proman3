import proman.plugin.PluginManager;
import proman.plugin.eps.ExampleBean;
import proman.threading.BGT;

import java.util.List;

public class PluginLoadingTest {

    public static void main(String[] args) {
        PluginManager manager = PluginManager.getInstance();
        BGT.invokeLater(() -> {
            try {

                manager.reload();

            /*
            PluginDescriptor descriptor = manager.getPlugin("unique.id.of.the.plugin");
            Assertions.assertNotNull(descriptor);
            Assertions.assertEquals("unique.id.of.the.plugin", descriptor.getId());
            Assertions.assertEquals("Name of this plugin", descriptor.getName());
            System.out.println("\n" + descriptor);
             */

                List<ExampleBean> types = manager.getExtensionPoint("other").getExtensions(ExampleBean.class);
                if (types.size() != 1)
                    throw new RuntimeException("size != 1");
                System.out.println(types + "\n");

                BGT.shutdown();
            }
            catch (Exception e) {
                e.printStackTrace();
                BGT.shutdown();
            }
        });

        BGT.flush();
    }

}

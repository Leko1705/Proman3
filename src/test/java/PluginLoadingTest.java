import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import proman.diagrams.DiagramType;
import proman.diagrams.types.UMLClassDiagramType;
import proman.plugin.PluginDescriptor;
import proman.plugin.PluginManager;
import proman.threading.BGT;

import java.util.List;

public class PluginLoadingTest {

    @Test
    public void test(){
        PluginManager manager = PluginManager.getInstance();
        BGT.invokeLater(() -> {
            manager.reload();

            PluginDescriptor descriptor = manager.getPlugin("unique.id.of.the.plugin");
            Assertions.assertNotNull(descriptor);
            Assertions.assertEquals("unique.id.of.the.plugin", descriptor.getId());
            Assertions.assertEquals("Name of this plugin", descriptor.getName());
            System.out.println("\n" + descriptor);

            List<DiagramType> types = manager.getExtension("diagramType", DiagramType.class);
            Assertions.assertNotNull(types);
            if (types.size() != 1)
                Assertions.fail("size != 1");
            Assertions.assertInstanceOf(UMLClassDiagramType.class, types.get(0));
            System.out.println(types + "\n");

            BGT.shutdown();
        });

        BGT.flush();
    }

}

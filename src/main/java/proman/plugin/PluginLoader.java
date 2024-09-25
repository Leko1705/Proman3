package proman.plugin;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import proman.filesys.FileSystem;
import proman.utils.Assertions;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginLoader {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected static void load(){
        Assertions.assertBGT("PluginLoader.load requires BGT");

        File pluginDir = new File(FileSystem.getInstallationPath() + File.separator + "plugins");
        pluginDir.mkdirs();

        File[] pluginFiles = pluginDir.listFiles((dir, name) -> name.endsWith(".xml"));
        if (pluginFiles == null) return;
        loadFromFiles(pluginFiles);
    }

    private static void loadFromFiles(File[] files){
        List<PluginDescriptor> plugins = new ArrayList<>();

        PluginManager manager = PluginManager.getInstance();
        for (File file : files) {
            try {

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dbFactory.newDocumentBuilder();
                Element root = builder.parse(file).getDocumentElement();

                Map<String, String> header = loadMetaDate(root);
                Map<String, Class<?>> extensionPoints = loadExtensionPoints(root);

                for (Map.Entry<String, Class<?>> entry : extensionPoints.entrySet()) {
                    manager.registerExtensionPoint(entry.getKey(), entry.getValue());
                }

                if (header != null) {
                    Map<String, Class<?>> extensions = loadExtensions(root);

                    PluginDescriptor descriptor = new PluginDescriptorImpl(
                            file.getAbsolutePath(),
                            header.get("id"),
                            header.get("name"),
                            header.get("description"),
                            extensionPoints,
                            extensions);

                    plugins.add(descriptor);
                }
            }
            catch (Exception e) {
                // TODO log error
            }
        }

        for (PluginDescriptor descriptor : plugins) {
            for (Map.Entry<String, Class<?>> entry : descriptor.getExtensions().entrySet()) {
                manager.checkAndRegisterUsage(entry.getKey(), entry.getValue());
            }
            manager.registerPlugin(descriptor);
        }
    }


    private PluginLoader(){

    }


    private static Map<String, String> loadMetaDate(Element root) {
        NodeList list = root.getElementsByTagName("id");
        if (list.getLength() != 1) return null;
        String id = list.item(0).getTextContent();

        list = root.getElementsByTagName("name");
        if (list.getLength() != 1) return null;
        String name = list.item(0).getTextContent();

        String description = "";
        list = root.getElementsByTagName("description");
        if (list.getLength() != 0){
            if (list.getLength() > 1) return null;
            description = list.item(0).getTextContent();
        }

        return Map.of("id", id, "name", name, "description", description);
    }


    private static Map<String, Class<?>> loadExtensionPoints(Element root) {
        Map<String, Class<?>> localExtensionPoint = new HashMap<>();

        NodeList list = root.getElementsByTagName("extensionPoints");
        for (int i = 0; i < list.getLength(); i++) {
            Element extensionPoint = (Element) list.item(i);

            NodeList subList = extensionPoint.getElementsByTagName("extensionPoint");
            for (int j = 0; j < subList.getLength(); j++) {
                Element extension = (Element) subList.item(j);
                String epName = extension.getAttribute("name");
                String interfaceName = extension.getAttribute("interface");

                Class<?> interfaceClass;
                try {
                    interfaceClass = Class.forName(interfaceName);
                }
                catch (ClassNotFoundException e) {
                    // TODO log error
                    continue;
                }

                localExtensionPoint.put(epName, interfaceClass);
            }
        }

        return localExtensionPoint;
    }

    private static Map<String, Class<?>> loadExtensions(Element root) {
        Map<String, Class<?>> localExtensions = new HashMap<>();

        NodeList list = root.getElementsByTagName("extensions");

        for (int i = 0; i < list.getLength(); i++) {
            Element extension = (Element) list.item(i);

            NodeList extensionList = extension.getChildNodes();
            for (int j = 0; j < extensionList.getLength(); j++) {
                Node extensionNode = extensionList.item(j);

                if (extensionNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element extensionElement = (Element) extensionNode;

                    String epName = extensionElement.getTagName();
                    String implementationClassName = extensionElement.getAttribute("implementationClass");

                    Class<?> implementationClass;
                    try {
                        implementationClass = Class.forName(implementationClassName);
                    }
                    catch (ClassNotFoundException e) {
                        // TODO log error
                        continue;
                    }

                    localExtensions.put(epName, implementationClass);
                }

            }
        }

        return localExtensions;
    }


}

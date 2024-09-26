package proman.plugin;

import org.w3c.dom.*;
import proman.filesys.FileSystem;
import proman.utils.Assertions;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

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
        Map<String, Map<String, String>> extensions = new HashMap<>();

        PluginManager manager = PluginManager.getInstance();
        for (File file : files) {
            try {

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dbFactory.newDocumentBuilder();
                Element root = builder.parse(file).getDocumentElement();

                if (!root.getTagName().equals("plugin"))
                    continue;

                Map<String, String> header = loadMetaData(root);

                // extension points are then registered in PluginManager
                loadExtensionPoints(root);

                if (header != null) {
                    extensions.putAll(loadExtensions(root));

                    PluginDescriptor descriptor = new PluginDescriptorImpl(
                            file.getAbsolutePath(),
                            header.get("id"),
                            header.get("name"),
                            header.get("description")
                    );

                    if (manager.getPlugin(descriptor.getId()) != null){
                        // TODO log duplicate plugin definition error
                        continue;
                    }
                    manager.registerPlugin(descriptor);
                }
            }
            catch (Exception e) {
                // TODO log error
            }
        }

        for (Map.Entry<String, Map<String, String>> entry : extensions.entrySet()) {
            Map<String, String> args = entry.getValue();
            manager.checkAndRegisterExtension(entry.getKey(), args);
        }
    }


    private PluginLoader(){

    }


    private static Map<String, String> loadMetaData(Element root) {
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



    private static void loadExtensionPoints(Element root) {

        NodeList list = root.getElementsByTagName("extensionPoints");
        for (int i = 0; i < list.getLength(); i++) {
            Element extensionPoint = (Element) list.item(i);

            NodeList subList = extensionPoint.getElementsByTagName("extensionPoint");
            for (int j = 0; j < subList.getLength(); j++) {
                Element extension = (Element) subList.item(j);
                String epName = extension.getAttribute("name");

                Class<?> interfaceClass = null;
                Class<?> beanClass = null;

                if (extension.hasAttribute("interface")) {
                    interfaceClass = getClassFromAttribute("interface", extension);
                }

                if (extension.hasAttribute("beanClass")) {
                    beanClass = getClassFromAttribute("beanClass", extension);
                }

                ExtensionPoint ep = createEP(interfaceClass, beanClass, epName);

                PluginManager.getInstance().registerExtensionPoint(ep);
            }
        }

    }

    private static ExtensionPoint createEP(Class<?> interfaceClass,
                                           Class<?> beanClass,
                                           String epName) {

        if (interfaceClass == null && beanClass == null) {
            throw new IllegalStateException(
                    "neither interface nor bean class is defined for ep");
        }

        if (interfaceClass != null && beanClass != null) {
            throw new IllegalArgumentException(
                    "both, interface and bean class are defined for ep");
        }

        Class<?> clazz = interfaceClass == null ? beanClass : interfaceClass;
        boolean isInterface = clazz == interfaceClass;


        if (isInterface)
            return new InterfaceExtensionPoint(epName, clazz);
        else
            return new BeanClassExtensionPoint(epName, clazz);
    }


    private static Class<?> getClassFromAttribute(String attributeName, Element element) {
        String interfaceName = element.getAttribute(attributeName);

        Class<?> interfaceClass = null;
        try {
            interfaceClass = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            // TODO log error
        }
        return interfaceClass;
    }


    private static Map<String, Map<String, String>> loadExtensions(Element root) {
        Map<String, Map<String, String>> localExtensions = new HashMap<>();

        NodeList list = root.getElementsByTagName("extensions");

        for (int i = 0; i < list.getLength(); i++) {
            Element extension = (Element) list.item(i);

            NodeList extensionList = extension.getChildNodes();
            for (int j = 0; j < extensionList.getLength(); j++) {
                Node extensionNode = extensionList.item(j);

                if (extensionNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element extensionElement = (Element) extensionNode;

                    String epName = extensionElement.getTagName();

                    Map<String, String> args = new HashMap<>();
                    NamedNodeMap attrMap = extensionElement.getAttributes();

                    for (int k = 0; k < attrMap.getLength(); k++) {
                        Node attrNode = attrMap.item(k);
                        if (attrNode.getNodeType() != Node.ATTRIBUTE_NODE) continue;
                        Attr attr = (Attr) attrNode;
                        args.put(attr.getName(), attr.getValue());
                    }

                    localExtensions.put(epName, args);
                }

            }
        }

        return localExtensions;
    }

}

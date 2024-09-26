package proman.plugin;

import proman.threading.BGT;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class PluginManager {


    private static PluginManager instance;

    public static PluginManager getInstance() {
        if (instance == null) {
            instance = new PluginManager();
        }
        return instance;
    }

    private PluginManager() {}


    private final Map<String, ExtensionPoint> extensionPoints = new HashMap<>();
    private final Map<String, PluginDescriptor> plugins = new HashMap<>();


    protected void registerExtensionPoint(ExtensionPoint ep) {
        Objects.requireNonNull(ep);

        if (extensionPoints.containsKey(ep.getName())) {
            // TODO log error "ep already defined"
            return;
        }

        extensionPoints.put(ep.getName(), ep);
    }


    protected void checkAndRegisterExtension(String epName, Map<String, String> args) {
        Objects.requireNonNull(epName);
        Objects.requireNonNull(args);


        ExtensionPoint ep = this.extensionPoints.get(epName);

        if (ep == null) {
            // TODO log error "no such ep defined"
            return;
        }

        ep.registerExtension(args);
    }


    protected void registerPlugin(PluginDescriptor descriptor) {
        Objects.requireNonNull(descriptor);
        plugins.put(descriptor.getId(), descriptor);
    }

    /**
     * Returns a List of all Extensions from a specific extensionPoint that
     * subclasses a specific class
     * @param epName the name of the extension point to load
     * @return a list of registered extensions from the specified {@code extensionName},
     *      subclassing {@code interfaceClass}
     * @throws IllegalArgumentException if the specified {@code interfaceClass}
     * argument is not an implementation of the EPs provided interface.
     */
    public ExtensionPoint getExtensionPoint(String epName) {
        return extensionPoints.get(epName);
    }

    /**
     * Loads A plugin from a given id.
     * @param id the id associated with the returned plugin
     * @return a plugin descriptor for a given id, or null if the id does not
     * map to any plugin
     */
    public PluginDescriptor getPlugin(String id) {
        Objects.requireNonNull(id);
        return plugins.get(id);
    }

    /**
     * Flushes all currently loaded plugin elements, such as PluginDescriptors,
     * ExtensionPoints and Extensions, and reloads them
     */
    public void reload(){
        flush();
        PluginLoader.load();
    }

    /**
     * Reloads asynchronous
     * @see #reload()
     */
    public void reloadAsync(){
        BGT.invokeLater(this::reload);
    }

    /**
     * Flushes all currently loaded plugin elements, such as PluginDescriptors,
     * ExtensionPoints and Extensions
     */
    public void flush(){
        extensionPoints.clear();
        plugins.clear();
    }


    @SuppressWarnings("unchecked")
    private static <T> T checkedCast(Object object) {
        return (T) object;
    }
}

package proman.plugin;

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


    private final Map<String, Class<?>> extensionPoints = new HashMap<>();
    private final Map<String, List<Object>> extensions = new HashMap<>();
    private final Map<String, PluginDescriptor> plugins = new HashMap<>();


    protected void registerExtensionPoint(String extensionPointName, Class<?> extensionPointClass) {
        Objects.requireNonNull(extensionPointName);
        Objects.requireNonNull(extensionPointClass);

        if (extensionPoints.containsKey(extensionPointName)) {
            // TODO log error "ep already defined"
            return;
        }

        extensionPoints.put(extensionPointName, extensionPointClass);
    }

    protected void checkAndRegisterUsage(String name, Class<?> clazz) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(clazz);

        if (!extensionPoints.containsKey(name)) {
            // TODO log error "no such ep defined"
            return;
        }

        Object instance;
        try {
            instance = clazz.getConstructor().newInstance();
        }
        catch (Exception e) {
            // TODO log error (unexpected instantiation exception; maybe non-empty constructor?)
            return;
        }

        List<Object> extensions = this.extensions.computeIfAbsent(name, k -> new ArrayList<>());
        extensions.add(instance);
    }

    protected void registerPlugin(PluginDescriptor descriptor) {
        Objects.requireNonNull(descriptor);
        plugins.put(descriptor.getId(), descriptor);
    }

    /**
     * Returns a List of all Extensions from a specific extensionPoint that
     * subclasses a specific class
     * @param extensionName the name of the extension type to load
     * @param interfaceClass the specific class to which
     * @return a list of registered extensions from the specified {@code extensionName},
     *      subclassing {@code interfaceClass}
     * @param <T> the type if specified class for correct evaluation
     * @throws IllegalArgumentException if the specified {@code interfaceClass}
     * argument is not an implementation of the EPs provided interface.
     */
    public <T> List<T> getExtension(String extensionName, Class<T> interfaceClass) {
        Class<?> epClass = extensionPoints.get(extensionName);
        if (epClass == null) {
            return List.of();
        }

        if (!epClass.isAssignableFrom(interfaceClass)) {
            throw new IllegalArgumentException("Extension '" + extensionName + "' is not of type '" + interfaceClass.getName() + "'");
        }

        List<Object> classes = extensions.get(extensionName);

        if (classes == null || classes.isEmpty()) {
            return List.of();
        }

        List<T> extensionSet = new ArrayList<>(classes.size());
        for (Object instance : classes) {
            if (interfaceClass.isAssignableFrom(instance.getClass())) {
                extensionSet.add(checkedCast(instance));
            }
        }

        return Collections.unmodifiableList(extensionSet);
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
     * Flushes all currently loaded plugin elements, such as PluginDescriptors,
     * ExtensionPoints and Extensions
     */
    public void flush(){
        extensionPoints.clear();
        extensions.clear();
        plugins.clear();
    }


    @SuppressWarnings("unchecked")
    private static <T> T checkedCast(Object object) {
        return (T) object;
    }
}

package proman.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public abstract class ExtensionPoint {

    private final String name;
    private final Class<?> epClass;

    private final List<Object> extensions = new ArrayList<>();

    public ExtensionPoint(String name,
                          Class<?> epClass) {
        this.name = name;
        this.epClass = epClass;
    }

    /**
     * Returns the name for this Extension Point
     * @return the name for this EP
     */
    public String getName() {
        return name;
    }

    /**
     * The class of Extensions that can be used with this Extension Point.
     * @return this EPs' supported Extension Class
     */
    public Class<?> getEpClass() {
        return epClass;
    }

    /**
     * registers a new Extension for this EP
     * @param extension the new Extension
     * @throws IllegalArgumentException if the provided
     * exception type is not supported by this EP. To be more precise if
     * the extension does not subclasses {@link #getEpClass()}
     */
    public void registerExtension(Object extension) {
        if (!epClass.isAssignableFrom(extension.getClass()))
            throw new IllegalArgumentException("extension is not a subclass of required " + epClass.getName() + " type");
        extensions.add(extension);
    }

    protected abstract void registerExtension(Map<String, String> args);

    /**
     * Unregisters the given extension. If the extension is null or is not present
     * in this EP nothing happens.
     * @param extension the extension to remove
     */
    public void unregisterExtension(Object extension) {
        extensions.remove(extension);
    }

    /**
     * Returns a List of all Extensions for this EP that subclass the specific class
     * @param clazz the requested type
     * @return a list of registered extensions subclassing {@code clazz}
     * @throws IllegalArgumentException if the specified {@code clazz}
     * argument is not an implementation of {@link #getEpClass()}.
     * @throws NullPointerException if the given class is null
     */
    public <T> List<T> getExtensions(Class<T> clazz) {

        if (clazz == null)
            throw new NullPointerException("given class is null");

        if (!epClass.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Extension '" + name + "' is not of type '" + clazz.getName() + "'");
        }

        if (extensions.isEmpty()) {
            return List.of();
        }

        List<T> extensionSet = new ArrayList<>(extensions.size());
        for (Object instance : extensions) {
            if (clazz.isAssignableFrom(instance.getClass())) {
                extensionSet.add(checkedCast(instance));
            }
        }

        return Collections.unmodifiableList(extensionSet);
    }


    @SuppressWarnings("unchecked")
    private static <T> T checkedCast(Object object) {
        return (T) object;
    }


    static Object instance(String classNam){
        Object instance;
        try {
            Class<?> clazz = Class.forName(classNam);
            instance = clazz.getConstructor().newInstance();
        }
        catch (Exception e) {
            // TODO log error (unexpected instantiation exception; maybe non-empty constructor?)
            return null;
        }
        return instance;
    }

}

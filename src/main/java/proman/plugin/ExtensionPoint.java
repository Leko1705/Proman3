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

    public String getName() {
        return name;
    }

    public Class<?> getEpClass() {
        return epClass;
    }

    public void registerExtension(Object extension) {
        extensions.add(extension);
    }

    public abstract void registerExtension(Map<String, String> args);

    public void unregisterExtension(Object extension) {
        extensions.remove(extension);
    }

    public List<Object> getExtensions() {
        return Collections.unmodifiableList(extensions);
    }

    public Stream<Object> stream(){
        return extensions.stream();
    }

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

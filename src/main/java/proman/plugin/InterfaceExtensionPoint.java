package proman.plugin;

import java.util.Map;

public class InterfaceExtensionPoint extends ExtensionPoint {

    public InterfaceExtensionPoint(String name, Class<?> epClass) {
        super(name, epClass);
    }

    @Override
    public void registerExtension(Map<String, String> args) {
        String implClass = args.get("implementationClass");

        if (implClass == null) {
            // TODO log missing implementationClass attribute
            return;
        }
        Object instance = instance(implClass);

        if (instance == null)
            return;

        registerExtension(instance);
    }
}

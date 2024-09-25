package proman.plugin;

import java.util.Map;

class PluginDescriptorImpl implements PluginDescriptor {

    private final String path;
    private final String id;
    private final String name;
    private final String description;
    private final Map<String, Class<?>> extensionPoints;
    private final Map<String, Class<?>> extensions;

    public PluginDescriptorImpl(String path,
                                String id,
                                String name,
                                String description,
                                Map<String, Class<?>> extensionPoint,
                                Map<String, Class<?>> extensions) {
        this.path = path;
        this.id = id;
        this.name = name;
        this.description = description;
        this.extensionPoints = extensionPoint;
        this.extensions = extensions;
    }

    @Override
    public String getFilePath() {
        return path;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Map<String, Class<?>> getExtensionPoints() {
        return extensionPoints;
    }

    @Override
    public Map<String, Class<?>> getExtensions() {
        return extensions;
    }
}

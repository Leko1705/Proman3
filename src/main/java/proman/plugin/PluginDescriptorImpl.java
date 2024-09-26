package proman.plugin;

class PluginDescriptorImpl implements PluginDescriptor {

    private final String path;
    private final String id;
    private final String name;
    private final String description;

    public PluginDescriptorImpl(String path,
                                String id,
                                String name,
                                String description) {
        this.path = path;
        this.id = id;
        this.name = name;
        this.description = description;
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
}

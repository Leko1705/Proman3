package proman.plugin;

import java.util.Map;

public interface PluginDescriptor {

    String getFilePath();

    String getId();

    String getName();

    String getDescription();

    Map<String, Class<?>> getExtensionPoints();

    Map<String, Class<?>> getExtensions();

}

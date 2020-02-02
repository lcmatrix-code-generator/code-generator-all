package top.lcmatrix.util.codegenerator.pluginloader;

public class PluginJarLoaderException extends RuntimeException{
    public PluginJarLoaderException() {
    }

    public PluginJarLoaderException(String message) {
        super(message);
    }

    public PluginJarLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginJarLoaderException(Throwable cause) {
        super(cause);
    }

    public PluginJarLoaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

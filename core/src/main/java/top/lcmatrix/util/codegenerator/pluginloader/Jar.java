package top.lcmatrix.util.codegenerator.pluginloader;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

public class Jar extends JarFile {

    private String jarFilePath;

    public Jar(String name) throws IOException {
        super(name);
    }

    public Jar(String name, boolean verify) throws IOException {
        super(name, verify);
    }

    public Jar(File file) throws IOException {
        super(file);
    }

    public Jar(File file, boolean verify) throws IOException {
        super(file, verify);
    }

    public Jar(File file, boolean verify, int mode) throws IOException {
        super(file, verify, mode);
    }

    public String getJarFilePath() {
        return jarFilePath;
    }

    public void setJarFilePath(String jarFilePath) {
        this.jarFilePath = jarFilePath;
    }
}

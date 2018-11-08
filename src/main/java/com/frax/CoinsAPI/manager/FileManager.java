package com.frax.CoinsAPI.manager;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private String path;
    private File f;
    private Configuration c;

    public FileManager(String path) {
        this.path = path;

        f = new File(path);

        if (!f.exists()) {
            try {
                f.createNewFile();
                c = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createDefaults() {
        if (c.getString("username") == null) {
            c.set("username", "username");
            c.set("password", "username");
            c.set("database", "username");
            c.set("port", "username");
            c.set("host", "username");
        }

        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(c, f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getF() {
        return f;
    }

    public void setF(File f) {
        this.f = f;
    }

    public Configuration getC() {
        return c;
    }

    public void setC(Configuration c) {
        this.c = c;
    }
}

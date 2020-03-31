package net.akarian.akarian.utils;

import net.akarian.akarian.Akarian;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private File file;
    private YamlConfiguration config;


    public void createFile(String name) {
        this.file = new File(Akarian.getInstance().getDataFolder(), name + ".yml");

        if (!Akarian.getInstance().getDataFolder().exists()) {
            Akarian.getInstance().getDataFolder().mkdir();
        }

        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.config = YamlConfiguration.loadConfiguration(file);
        } else {
            try {
                throw new IOException();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public File getFile(String name) {
        this.file = new File(Akarian.getInstance().getDataFolder(), name + ".yml");

        return this.file;
    }

    public YamlConfiguration getConfig(String name) {
        this.file = new File(Akarian.getInstance().getDataFolder(), name + ".yml");

        this.config = YamlConfiguration.loadConfiguration(this.file);
        return this.config;
    }

}


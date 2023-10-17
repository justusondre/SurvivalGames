package utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigUtils {

	public static FileConfiguration getConfig(final JavaPlugin plugin, final String filename) {
	    final File file = new File(plugin.getDataFolder(), filename + ".yml");
	    if (filename.contains(File.separator)) {
	        new File(plugin.getDataFolder(), filename.replace(filename.substring(filename.indexOf(File.separator)), "")).mkdirs();
	    }
	    if (!file.exists()) {
	        plugin.saveResource(filename + ".yml", false);
	    }
	    final YamlConfiguration config = new YamlConfiguration();
	    try {
	        config.load(file);
	    }
	    catch (InvalidConfigurationException | IOException ex) {
	        ex.printStackTrace();
	    }
	    return config;
	}

	public static void saveConfig(final JavaPlugin plugin, final FileConfiguration config, final String name) {
	    try {
	        config.save(new File(plugin.getDataFolder(), name + ".yml"));
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
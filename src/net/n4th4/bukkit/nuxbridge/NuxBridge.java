package net.n4th4.bukkit.nuxbridge;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class NuxBridge extends JavaPlugin {

    public Logger log;
  
    private FileConfiguration customConfig = null;
    private File configFile = null;

    public void onEnable() {
        log = this.getServer().getLogger();
        new NBPlayerListener(this);

        configFile = new File("plugins/NuxBridge/config.yml");
        
        if (configFile.exists()) {
            customConfig = YamlConfiguration.loadConfiguration(configFile);
        } else {
            log.severe("[NuxBridge] File not found : plugins/NuxBridge/config.yml");
        }

        PluginDescriptionFile pdfFile = this.getDescription();
        log.info("[NuxBridge] " + pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    }

    public void onDisable() {
    }
}


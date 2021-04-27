package doubleos.deathgame.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Set;

import doubleos.deathgame.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Config {
    private static YamlConfiguration config = null;
    // some code

    // this method has to be static if you want to use it
    // in another static method
    public static YamlConfiguration getConfig()
    {
        // load the configuration if it hasn't been loaded yet
        if(config == null)
        {
            config = YamlConfiguration.loadConfiguration(new File("path/to/config.yml"));
        }
        // some code
        return config;
    }

    // this will return the boolean you want to get
    public static Boolean getValue(Player p)
    {
        return getConfig().getBoolean("users."+p.getName());
    }

    // use this code to set the value
    public static void setValue(Player p, Boolean value)
    {
        getConfig().set("users."+p.getName(), value);
    }

    // save the configuration
    public static void save() {
        try{
            config.save(new File("path/to/config.yml"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
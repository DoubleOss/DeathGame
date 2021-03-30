package doubleos.deathgame;

import doubleos.deathgame.command.GameCommand;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;


public class Main extends JavaPlugin {


    public ArrayList<Player> adminList = new ArrayList<>();

    public HashMap<Player, PlayerVariable> variablePlayer = new HashMap<>();


    public static Main instance;


    public void onEnable()
    {

        instance = this;
        Bukkit.getPluginManager().registerEvents(new Death(), this);
        registercommand();

        getServer().getMessenger().registerOutgoingPluginChannel((Plugin) this, "ExampleMod");


    }

    public void onDisaable()
    {


    }

    public void registercommand()
    {
        GameCommand gameCommand = new GameCommand();
        getCommand("죽술").setExecutor(gameCommand);
    }


}


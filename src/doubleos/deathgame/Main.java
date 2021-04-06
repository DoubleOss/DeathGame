package doubleos.deathgame;

import doubleos.deathgame.ablilty.*;
import doubleos.deathgame.command.GameCommand;
import doubleos.deathgame.gui.PotionMakeGui;
import doubleos.deathgame.variable.PlayerVariable;
import doubleos.deathgame.variable.PotionRecipe;
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

        registercommand();
        getServer().getMessenger().registerOutgoingPluginChannel((Plugin) this, "ExampleMod");

        Bukkit.getPluginManager().registerEvents(new Death(), this);

        Bukkit.getPluginManager().registerEvents(new KillerCommon(), this);
        Bukkit.getPluginManager().registerEvents(new KillerHidden1(), this);
        Bukkit.getPluginManager().registerEvents(new KillerHidden2(), this);
        Bukkit.getPluginManager().registerEvents(new KillerHidden3(), this);

        Bukkit.getPluginManager().registerEvents(new Hidden2Gui(), this);
        Bukkit.getPluginManager().registerEvents(new PotionMakeGui(), this);

        PotionRecipe.Instance().initPotionRecipe();




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


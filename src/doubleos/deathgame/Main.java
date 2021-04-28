package doubleos.deathgame;

import doubleos.deathgame.ablilty.*;
import doubleos.deathgame.command.GameCommand;
import doubleos.deathgame.event.*;
import doubleos.deathgame.gui.CellularGame;
import doubleos.deathgame.gui.DefectiveGame;
import doubleos.deathgame.gui.MechanicalRepair;
import doubleos.deathgame.gui.PotionMakeGui;
import doubleos.deathgame.util.Utils;
import doubleos.deathgame.variable.GameItem;
import doubleos.deathgame.variable.PlayerVariable;
import doubleos.deathgame.variable.PotionRecipe;
import doubleos.deathgame.variable.RepairBox;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;


public class Main extends JavaPlugin
{




    //public HashMap<Player, PlayerVariable> variablePlayer = new HashMap<>();


    public static Main instance;



    public void onEnable()
    {
        instance = this;


        registercommand();

        getServer().getMessenger().registerOutgoingPluginChannel( this, "DeathGame");
        
        Bukkit.getPluginManager().registerEvents(new Join(), this);
        Bukkit.getPluginManager().registerEvents(new Kill(), this);
        Bukkit.getPluginManager().registerEvents(new Damage(), this);
        Bukkit.getPluginManager().registerEvents(new Quit(), this);



        Bukkit.getPluginManager().registerEvents(new KillerCommon(), this);
        Bukkit.getPluginManager().registerEvents(new KillerHidden1(), this);
        Bukkit.getPluginManager().registerEvents(new KillerHidden2(), this);
        Bukkit.getPluginManager().registerEvents(new KillerHidden3(), this);

        Bukkit.getPluginManager().registerEvents(new Hidden2Gui(), this);
        Bukkit.getPluginManager().registerEvents(new PotionMakeGui(), this);

        Bukkit.getPluginManager().registerEvents(new CellularGame(), this);
        Bukkit.getPluginManager().registerEvents(new DefectiveGame(), this);

        Bukkit.getPluginManager().registerEvents(new MechanicalRepair(), this);

        Bukkit.getPluginManager().registerEvents(new RepairBoxClick(), this);





        PotionRecipe.Instance().initPotionRecipe();

        GameItem.Instance().initGameItem();
        Utils.Instance().initTeleportLocation();




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


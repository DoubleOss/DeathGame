package doubleos.deathgame;

import doubleos.deathgame.ablilty.*;
import doubleos.deathgame.command.GameCommand;
import doubleos.deathgame.event.*;
import doubleos.deathgame.gui.*;
import doubleos.deathgame.util.Utils;
import doubleos.deathgame.variable.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


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
        Bukkit.getPluginManager().registerEvents(new HealthRegen(), this);

        Bukkit.getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getPluginManager().registerEvents(new MissionBoxClick(), this);
        Bukkit.getPluginManager().registerEvents(new FactoryHidden(), this);
        Bukkit.getPluginManager().registerEvents(new OpenShulkerBox(), this);
        Bukkit.getPluginManager().registerEvents(new RepairBoxClick(), this);




        Bukkit.getPluginManager().registerEvents(new KillerCommon(), this);
        Bukkit.getPluginManager().registerEvents(new KillerHidden1(), this);
        Bukkit.getPluginManager().registerEvents(new KillerHidden2(), this);
        Bukkit.getPluginManager().registerEvents(new KillerHidden3(), this);

        Bukkit.getPluginManager().registerEvents(new Hidden2Gui(), this);
        Bukkit.getPluginManager().registerEvents(new PotionMakeGui(), this);

        Bukkit.getPluginManager().registerEvents(new CellularGame(), this);
        Bukkit.getPluginManager().registerEvents(new DefectiveGame(), this);

        Bukkit.getPluginManager().registerEvents(new MechanicalRepair(), this);

        Bukkit.getPluginManager().registerEvents(new Mission_clean(), this);
        Bukkit.getPluginManager().registerEvents(new MissionBoxGui(), this);


        PotionRecipe.Instance().initPotionRecipe();

        GameItem.Instance().initGameItem();
        Utils.Instance().initTeleportLocation();

        MissionManager.Instance().initFactoryHiddenLoc();




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


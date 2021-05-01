package doubleos.deathgame.variable;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import doubleos.deathgame.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Mission_clean implements Listener
{


    public void initCleaning(Player player)
    {
        player.setWalkSpeed(0f);
        player.sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "true" + "_" + "15" + "_" +"0").getBytes());
        ActionBarAPI.sendActionBar(player, "소독중 입니다.",  300);
        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                player.setWalkSpeed(0.2f);
                player.sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "false").getBytes());
                MissionManager.Instance().setMission1Success(true);
                MissionManager.Instance().successMissionbox();
                this.cancel();
            }
        }.runTaskTimer(Main.instance, 300l, 300l);
    }


}

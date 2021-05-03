package doubleos.deathgame.variable;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import doubleos.deathgame.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Mission_Hacking
{

    public void initHacking(Player player)
    {
        player.setWalkSpeed(0f);
        player.sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "true" + "_" + "15" + "_" +"0").getBytes());
        ActionBarAPI.sendActionBar(player, "해킹중 입니다.",  300);
        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                player.setWalkSpeed(0.2f);
                player.sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "false").getBytes());

                MissionManager.Instance().setMission2Success(true);
                MissionManager.Instance().successMissionbox();
                this.cancel();
            }
        }.runTaskTimer(Main.instance, 300l, 300l);

    }


}
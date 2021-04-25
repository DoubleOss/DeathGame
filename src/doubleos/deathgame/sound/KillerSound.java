package doubleos.deathgame.sound;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class KillerSound implements Listener
{
    Player m_player;

    public void initSound(Player player)
    {
        m_player = player;
        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for(Player p : GameVariable.Instance().getGamePlayerList())
                {
                    if(!p.equals(m_player))
                    {
                        if(p.getLocation().distance(m_player.getLocation()) <= 50)
                        {
                            int soundSpeed = getDistanceSoundSpeed(p.getLocation().distance(m_player.getLocation()));
                            p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "true" + "_" + "%d",soundSpeed).getBytes());

                        }

                    }

                }
                if(GameVariable.Instance().getGameState().equals(GameVariable.GameState.END))
                {
                    for(Player p : GameVariable.Instance().getGamePlayerList())
                    {
                        p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false").getBytes());
                    }
                    this.cancel();
                }

            }

        }.runTaskTimer(Main.instance, 0l , 20l);
    }

    int getDistanceSoundSpeed(double distance)
    {
        if(distance <= 5)
        {
            return 450;
        }
        else if (distance <= 10)
        {
            return 500;
        }
        else if (distance <= 20)
        {
            return  600;
        }
        else if (distance <= 30)
        {
            return  700;
        }
        else if (distance <= 40)
        {
            return  800;
        }
        else
        {
            return  900;
        }
    }
}

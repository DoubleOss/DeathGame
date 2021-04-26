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

        final int[] m_soundSpeed = {2000};
        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for(Player p : GameVariable.Instance().getGamePlayerList())
                {
                    m_player.sendMessage("@");
                    if(!p.equals(m_player))
                    {
                        m_player.sendMessage("@@");
                        if(p.getLocation().distance(m_player.getLocation()) <= 50)
                        {
                            m_player.sendMessage(String.format("%d",m_soundSpeed[0]));
                            if(m_soundSpeed[0] != getDistanceSoundSpeed(p.getLocation().distance(m_player.getLocation())))
                            {
                                m_soundSpeed[0] = getDistanceSoundSpeed(p.getLocation().distance(m_player.getLocation()));
                                m_player.sendMessage("@@@@");
                                if(Main.instance.variablePlayer.get(p).getSoundPlaying())
                                {
                                    m_player.sendMessage("@@@@@");

                                    p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false").getBytes());
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            m_player.sendMessage("@@@@@@");
                                            p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "true" + "_" + "%d", m_soundSpeed[0]).getBytes());
                                        }
                                    }, 10l);
                                }
                                else
                                {
                                    m_player.sendMessage("@@@@@@@");
                                    Main.instance.variablePlayer.get(p).setSoundPlaying(true);
                                    p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "true" + "_" + "%d", m_soundSpeed[0]).getBytes());
                                }
                            }
                        }
                        else
                        {
                            Main.instance.variablePlayer.get(p).setSoundPlaying(false);
                            p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false").getBytes());

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

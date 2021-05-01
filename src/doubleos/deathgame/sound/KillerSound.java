package doubleos.deathgame.sound;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class KillerSound implements Listener
{
    String m_player;

    public void initSound(Player player)
    {
        m_player = player.getName();
        HashMap<String, PlayerVariable> variableMap = GameVariable.Instance().getPlayerVariableMap();
        Player killer = Bukkit.getPlayer(m_player);
        final int[] m_soundSpeed = {2000};
        /*
        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for(String stringPlayer : GameVariable.Instance().getGamePlayerList())
                {
                    Player p = Bukkit.getPlayer(stringPlayer);
                    if(p.getName() != null)
                    {
                        if(!p.getName().equals(killer.getName()))
                        {
                            if(p.getLocation().distance(killer.getLocation()) <= 50)
                            {
                                if(!variableMap.get(p.getName()).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                                {
                                    if(m_soundSpeed[0] != getDistanceSoundSpeed(p.getLocation().distance(killer.getLocation())))
                                    {
                                        m_soundSpeed[0] = getDistanceSoundSpeed(p.getLocation().distance(killer.getLocation()));
                                        if(variableMap.get(p.getName()).getSoundPlaying())
                                        {
                                            p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false").getBytes());
                                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {

                                                    p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "true" + "_" + "%d", m_soundSpeed[0]).getBytes());
                                                }
                                            }, 20l);
                                        }
                                        else
                                        {
                                            variableMap.get(p.getName()).setSoundPlaying(true);
                                            //p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "true" + "_" + "%d", m_soundSpeed[0]).getBytes());
                                        }
                                    }
                                }
                                else
                                {
                                    variableMap.get(p.getName()).setSoundPlaying(false);
                                    p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false").getBytes());
                                }
                            }
                            else
                            {
                                variableMap.get(p.getName()).setSoundPlaying(false);
                                p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false").getBytes());

                            }

                        }
                    }


                }
                if(GameVariable.Instance().getGameState().equals(GameVariable.GameState.END))
                {
                    for(String stringPlayer : GameVariable.Instance().getGamePlayerList())
                    {
                        Player p = Bukkit.getPlayer(stringPlayer);
                        variableMap.get(p.getName()).setSoundPlaying(false);
                        p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false").getBytes());
                    }
                    this.cancel();
                }

            }

        }.runTaskTimer(Main.instance, 0l , 20l);

         */
    }

    int getDistanceSoundSpeed(double distance)
    {
        if (distance <= 5)
        {
            return 500;
        }
        else if (distance <= 25)
        {
            return  600;
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

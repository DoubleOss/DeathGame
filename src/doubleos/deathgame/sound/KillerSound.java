package doubleos.deathgame.sound;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class KillerSound
{
    public void initSound(Player player)
    {

        final int[] m_soundSpeed = {2000};
        HashMap<String, PlayerVariable> variableMap = GameVariable.Instance().getPlayerVariableMap();
        GameVariable gameVariable = GameVariable.Instance();
        Player killer = gameVariable.getOrignalKillerPlayer();
        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for(String stringPlayer : gameVariable.getGamePlayerList())
                {
                    Player p = Bukkit.getPlayer(stringPlayer);
                    if(killer.isOnline() && p.isOnline())
                    {
                        if(p.getName() != null)
                        {
                            if(!p.getName().equals(killer.getName()))
                            {
                                if(p.getLocation().distance(killer.getLocation()) <= 30)
                                {
                                    if(variableMap.get(p.getName()).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
                                    {
                                        if(m_soundSpeed[0] != getDistanceSoundSpeed(p.getLocation().distance(killer.getLocation())))
                                        {
                                            m_soundSpeed[0] = getDistanceSoundSpeed(p.getLocation().distance(killer.getLocation()));
                                            if(m_soundSpeed[0] != 2000)
                                            {
                                                if (!variableMap.get(p.getName()).getSoundKillerPlaying())
                                                {
                                                    variableMap.get(p.getName()).setSoundKillerPlaying(true);
                                                    p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "true" + "_" + "killer" + "_" + "%d", m_soundSpeed[0]).getBytes());
                                                }
                                            }
                                            else
                                            {
                                                variableMap.get(p.getName()).setSoundKillerPlaying(false);
                                                p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false" + "_" + "killer").getBytes());
                                            }
                                        }
                                    }
                                    else
                                    {
                                        variableMap.get(p.getName()).setSoundKillerPlaying(false);
                                        p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false" + "_" + "killer").getBytes());
                                    }
                                }
                                else if (variableMap.get(p.getName()).getSoundKillerPlaying())
                                {
                                    variableMap.get(p.getName()).setSoundKillerPlaying(false);
                                    p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false" + "_" + "killer").getBytes());
                                }

                            }
                        }
                    }

                }
                if(GameVariable.Instance().getGameState().equals(GameVariable.GameState.END))
                {
                    for(String stringPlayer : gameVariable.getGamePlayerList())
                    {
                        Player p = Bukkit.getPlayer(stringPlayer);
                        variableMap.get(p.getName()).setSoundKillerPlaying(false);
                        p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false" + "_" + "killer").getBytes());
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
        else if (distance <= 30)
        {
            return  800;
        }
        else
            return  2000;
    }
}

package doubleos.deathgame.sound;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import org.bukkit.Bukkit;
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
                            p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, SoundCategory.AMBIENT, 1, 1);
                        }
                    }

                }
                if(GameVariable.Instance().getGameState().equals(GameVariable.GameState.END))
                {
                    this.cancel();
                }

            }

        }.runTaskTimer(Main.instance, 0l , 20l);
    }
}

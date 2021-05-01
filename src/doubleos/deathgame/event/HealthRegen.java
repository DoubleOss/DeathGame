package doubleos.deathgame.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class HealthRegen implements Listener
{
    @EventHandler
    public void RezenHealth(EntityRegainHealthEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            event.setCancelled(true);
        }

    }
}

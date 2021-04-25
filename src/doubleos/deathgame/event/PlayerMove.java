package doubleos.deathgame.event;

import doubleos.deathgame.variable.GameVariable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener
{
    @EventHandler
    void onPlayerMoveEvent(PlayerMoveEvent event)
    {
        if(GameVariable.Instance().getTeleporting())
            event.getPlayer().setWalkSpeed(0);
        else
            event.getPlayer().setWalkSpeed(0.2f);
    }

}

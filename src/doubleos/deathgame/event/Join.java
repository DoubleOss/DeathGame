package doubleos.deathgame.event;

import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener
{
    @EventHandler
    public void fristJoin(PlayerJoinEvent event)
    {
        if(GameVariable.Instance().getGameState().equals(GameVariable.GameState.END))
        {
            PlayerVariable variable = new PlayerVariable(event.getPlayer());
        }
    }
}

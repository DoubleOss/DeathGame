package doubleos.deathgame.event;

import doubleos.deathgame.scoreboard.Scoreboard;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.entity.Player;
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

    @EventHandler
    public void Join(PlayerJoinEvent event)
    {
        if(GameVariable.Instance().getGameState().equals(GameVariable.GameState.PLAY))
        {
            for(Player p : GameVariable.Instance().getGamePlayerList())
            {
                if(event.getPlayer().equals(p))
                {
                    Scoreboard scoreboard = new Scoreboard(event.getPlayer());
                }
            }

        }
    }
}

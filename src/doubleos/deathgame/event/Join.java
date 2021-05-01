package doubleos.deathgame.event;

import doubleos.deathgame.Main;
import doubleos.deathgame.scoreboard.Scoreboard;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.inventivetalent.glow.GlowAPI;

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
        for(Player p : Bukkit.getOnlinePlayers())
        {
            GlowAPI.setGlowing(event.getPlayer(), false, p);
        }
        if(GameVariable.Instance().getGameState().equals(GameVariable.GameState.PLAY))
        {
            for(String stringPlayer : GameVariable.Instance().getGamePlayerList())
            {
                Player p = Bukkit.getPlayer(stringPlayer);
                if(event.getPlayer().equals(p))
                {
                    Scoreboard scoreboard = new Scoreboard(event.getPlayer());

                }
            }

        }
    }
}

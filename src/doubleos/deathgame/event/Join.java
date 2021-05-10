package doubleos.deathgame.event;

import doubleos.deathgame.Main;
import doubleos.deathgame.scoreboard.Scoreboard;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        if(GameVariable.Instance().getPlayerListVariableMap().get(event.getPlayer().getName()) == null)
        {
            PlayerVariable variable = new PlayerVariable(event.getPlayer());
            event.setJoinMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE +event.getPlayer().getName() +" 님이 접속하셨습니다.");
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
                    GameVariable.Instance().getPlayerVariableMap().get(p.getName()).setSoundPlaying(true);
                    p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "true" + "_" + "heart").getBytes());
                }
            }

        }
    }
}

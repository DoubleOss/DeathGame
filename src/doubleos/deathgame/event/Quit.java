package doubleos.deathgame.event;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import jdk.nashorn.internal.parser.JSONParser;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class Quit implements Listener
{
    @EventHandler
    void onPlayerQuitEvent(PlayerQuitEvent event)
    {
        HashMap<String, PlayerVariable> variableMap = GameVariable.Instance().getPlayerVariableMap();
        variableMap.get(event.getPlayer().getName()).setSoundPlaying(false);
        event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false").getBytes());

    }


}

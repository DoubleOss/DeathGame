package doubleos.deathgame.util;

import doubleos.deathgame.variable.MissionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils
{

    static private Utils _instance = null;

    static public Utils Instance()
    {
        if(_instance == null)
        {
            _instance = new Utils();
        }
        return _instance;
    }


    public void sendTitle(Player player, String mainString, String subString, int fadeInTime, int stayTime, int fadeOutTime, ChatColor subStringColor)
    {
        player.sendTitle(mainString, subStringColor + subString, fadeInTime , stayTime, fadeOutTime);

    }
    public void broadcastTitle(String mainString, String subString, int fadeInTime, int stayTime, int fadeOutTime, ChatColor subStringColor)
    {
        for(Player p : Bukkit.getOnlinePlayers())
        {
            p.sendTitle(mainString, subStringColor + subString, fadeInTime , stayTime, fadeOutTime);
        }


    }

}

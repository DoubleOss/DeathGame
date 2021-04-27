package doubleos.deathgame.util;

import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

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

    ArrayList<Location> lab_locations = new ArrayList<>();
    ArrayList<Location> factory_locations = new ArrayList<>();
    ArrayList<Location> cathedral_locations = new ArrayList<>();



    public HashMap<GameVariable.GameStage, ArrayList<Location>> m_stageTeleportLoc = new HashMap<>();

    public void randomAllTeleport()
    {
        ArrayList<String> stringPlayers = new ArrayList<String>(GameVariable.Instance().getGamePlayerList());

        Collections.shuffle(stringPlayers);
        int i = 0;
        for(String stringPlayer : stringPlayers)
        {
            Player p = Bukkit.getPlayer(stringPlayer);
            p.teleport(m_stageTeleportLoc.get(GameVariable.Instance().getGameStage()).get(i));
            i++;
        }
    }

    public void randomTeleport(Player player)
    {
        ArrayList<Location> locations = m_stageTeleportLoc.get(GameVariable.Instance().getGameStage());
        int i = new Random().nextInt((locations.size() - 0) + 1) + 0;
        player.teleport(locations.get(i));
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

    public void initTeleportLocation()
    {
        lab_locations.add(new Location(Bukkit.getWorld("world"), -366, 71, -49));
        lab_locations.add(new Location(Bukkit.getWorld("world"), -323, 71, 39));
        lab_locations.add(new Location(Bukkit.getWorld("world"), -364, 71, 41));
        lab_locations.add(new Location(Bukkit.getWorld("world"), -361, 79, -50));
        lab_locations.add(new Location(Bukkit.getWorld("world"), -323, 79, 18));
        lab_locations.add(new Location(Bukkit.getWorld("world"), -313, 62, 19));
        lab_locations.add(new Location(Bukkit.getWorld("world"), -325, 62, -28));

        factory_locations.add(new Location(Bukkit.getWorld("world"), -513, 62, 100));
        factory_locations.add(new Location(Bukkit.getWorld("world"), -515, 62, 48));
        factory_locations.add(new Location(Bukkit.getWorld("world"), -524, 69, 27));
        factory_locations.add(new Location(Bukkit.getWorld("world"), -552, 62, 48));
        factory_locations.add(new Location(Bukkit.getWorld("world"), -575, 69, 85));
        factory_locations.add(new Location(Bukkit.getWorld("world"), -540, 62, 64));
        factory_locations.add(new Location(Bukkit.getWorld("world"), -507, 62, 72));


        cathedral_locations.add(new Location(Bukkit.getWorld("world"), -507, 62, 72));

        m_stageTeleportLoc.put(GameVariable.GameStage.LAB, lab_locations);
        m_stageTeleportLoc.put(GameVariable.GameStage.FACTORY, factory_locations);
        m_stageTeleportLoc.put(GameVariable.GameStage.FACTORY, cathedral_locations);


    }

}

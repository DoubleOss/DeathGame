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
        ArrayList<String> stringPlayers = new ArrayList<>(GameVariable.Instance().getGamePlayerList());

        Collections.shuffle(stringPlayers);
        int i = 0;
        for(String stringPlayer : stringPlayers)
        {
            Player p = Bukkit.getPlayer(stringPlayer);
            p.teleport(m_stageTeleportLoc.get(GameVariable.Instance().getGameStage()).get(i));
            i++;
        }
    }

    public void inventoryClear(Player player)
    {
        player.getInventory().clear();
        player.getEquipment().clear();
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
        lab_locations.add(new Location(Bukkit.getWorld("world"), -312, 62, 20));
        lab_locations.add(new Location(Bukkit.getWorld("world"), -325, 62, -28));
        lab_locations.add(new Location(Bukkit.getWorld("world"), -339, 62, -13));


        cathedral_locations.add(new Location(Bukkit.getWorld("world"), -552, 62, 64));
        cathedral_locations.add(new Location(Bukkit.getWorld("world"), -498, 63, 85));
        cathedral_locations.add(new Location(Bukkit.getWorld("world"), -577, 62, 54));
        cathedral_locations.add(new Location(Bukkit.getWorld("world"), -537, 54, 49));
        cathedral_locations.add(new Location(Bukkit.getWorld("world"), -513, 54, 48));
        cathedral_locations.add(new Location(Bukkit.getWorld("world"), -533, 54, 25));
        cathedral_locations.add(new Location(Bukkit.getWorld("world"), -579, 54, 31));
        cathedral_locations.add(new Location(Bukkit.getWorld("world"), -547, 70, 47));


        factory_locations.add(new Location(Bukkit.getWorld("world"), -424, 62, 79));
        factory_locations.add(new Location(Bukkit.getWorld("world"), -361, 62, 81));
        factory_locations.add(new Location(Bukkit.getWorld("world"), -414, 62, 89));
        factory_locations.add(new Location(Bukkit.getWorld("world"), -404, 74, 157));
        factory_locations.add(new Location(Bukkit.getWorld("world"), -366, 74, 114));
        factory_locations.add(new Location(Bukkit.getWorld("world"), -401, 62, 140));
        factory_locations.add(new Location(Bukkit.getWorld("world"), -374, 62, 71));
        factory_locations.add(new Location(Bukkit.getWorld("world"), -371, 62, 129));



        m_stageTeleportLoc.put(GameVariable.GameStage.LAB, lab_locations);
        m_stageTeleportLoc.put(GameVariable.GameStage.FACTORY, factory_locations);
        m_stageTeleportLoc.put(GameVariable.GameStage.CATHEDRAL, cathedral_locations);


    }

}

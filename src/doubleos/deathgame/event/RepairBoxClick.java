package doubleos.deathgame.event;

import doubleos.deathgame.Main;
import doubleos.deathgame.util.Utils;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.PlayerVariable;
import doubleos.deathgame.variable.RepairBox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class RepairBoxClick implements Listener
{
    @EventHandler
    void onRepairBoxClick(PlayerInteractEvent event)
    {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if(event.getClickedBlock().getType().equals(Material.PISTON_BASE))
            {
                MissionManager mission = MissionManager.Instance();
                ArrayList<Location> blocks = mission.getRepairBoxList();
                HashMap<Location, RepairBox> map = mission.getRepairBoxClassMap();
                HashMap<String, PlayerVariable> variableMap = GameVariable.Instance().getPlayerVariableMap();

                for(int i = 0 ; i<blocks.size(); i++)
                {
                    Block block = Bukkit.getWorld("world").getBlockAt(blocks.get(i));
                    if(block.getLocation().equals(event.getClickedBlock().getLocation()))
                    {
                        if(event.getPlayer().getLocation().distance(event.getClickedBlock().getLocation()) <= 1)
                        {
                            if(!variableMap.get(event.getPlayer().getName()).getRepair() && !mission.getRepairBoxClassMap().get(event.getClickedBlock()).getRepair())
                            {
                                float boxHealth = mission.getRepairBoxClassMap().get(event.getClickedBlock()).gethealth();
                                variableMap.get(event.getPlayer().getName()).setRepair(true);
                                event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "true" + "_" + "30" +"_"+ "%f",boxHealth).getBytes());
                                event.getPlayer().sendTitle("[!]", ChatColor.GREEN+ "배전박스를 수리합니다" +ChatColor.RED+" 움직일 시 수리가 중단됩니다.", 1, 30, 1);
                                BukkitTask task = new BukkitRunnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if(event.getPlayer().getLocation().distance(event.getClickedBlock().getLocation()) > 1)
                                        {
                                            variableMap.get(event.getPlayer().getName()).setRepair(false);
                                            event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "false").getBytes());
                                            this.cancel();

                                        }
                                        else if ((map.get(event.getClickedBlock()).gethealth() >= 30))
                                        {
                                            variableMap.get(event.getPlayer().getName()).setRepair(false);
                                            mission.setBoxRepair(MissionManager.Instance().getBoxRepair()+1);
                                            mission.getRepairBoxClassMap().get(event.getClickedBlock()).setRepair(true);
                                            event.getPlayer().sendTitle("[!]", ChatColor.GREEN+ "수리가 완료되었습니다.", 1, 30, 1);
                                            this.cancel();
                                        }
                                        else if (map.get(event.getClickedBlock()).gethealth() < 30)
                                        {
                                            map.get(event.getClickedBlock()).sethealth(map.get(event.getClickedBlock()).gethealth() + 1);
                                        }

                                    }
                                }.runTaskTimer(Main.instance, 0, 20l);
                            }
                            else
                            {
                                variableMap.get(event.getPlayer().getName()).setRepair(false);
                                event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "false").getBytes());
                                Utils.Instance().sendTitle(event.getPlayer(), "[!]", "수리가 중단 되었습니다", 1, 30, 1, ChatColor.RED);
                            }
                        }
                    }
                }
            }
        }
    }

}

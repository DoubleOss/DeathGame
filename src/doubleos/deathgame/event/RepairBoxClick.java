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
import org.bukkit.inventory.EquipmentSlot;
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
                if(GameVariable.Instance().getPlayerListVariableMap().get(event.getPlayer().getName()).getObserver())
                    return;
                MissionManager mission = MissionManager.Instance();
                ArrayList<Location> blocks = mission.getRepairBoxList();
                HashMap<Location, RepairBox> map = mission.getRepairBoxClassMap();
                HashMap<String, PlayerVariable> variableMap = GameVariable.Instance().getPlayerVariableMap();
                if (event.getHand() != EquipmentSlot.HAND)
                    return;
                for(int i = 0 ; i<blocks.size(); i++)
                {
                    if(blocks.get(i).equals(event.getClickedBlock().getLocation()))
                    {
                        if(event.getPlayer().getLocation().distance(event.getClickedBlock().getLocation()) <= 3)
                        {
                            if(!mission.getRepairBoxClassMap().get(event.getClickedBlock().getLocation()).getRepairing())
                            {
                                if(variableMap.get(event.getPlayer().getName()).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                                    return;
                                if(!variableMap.get(event.getPlayer().getName()).getRepair() && !mission.getRepairBoxClassMap().get(event.getClickedBlock().getLocation()).getRepair())
                                {
                                    int boxHealth = (int)mission.getRepairBoxClassMap().get(event.getClickedBlock().getLocation()).gethealth();
                                    variableMap.get(event.getPlayer().getName()).setRepair(true);
                                    event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "true" + "_" + "60" +"_"+ "%d",boxHealth).getBytes());
                                    event.getPlayer().sendTitle("[!]", ChatColor.GREEN+ "배전박스를 수리합니다" +ChatColor.RED+" 움직일 시 수리가 중단됩니다.", 1, 30, 1);
                                    BukkitTask task = new BukkitRunnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            if((map.get(event.getClickedBlock().getLocation()).gethealth() == 7) || (map.get(event.getClickedBlock().getLocation()).gethealth() == 30) || (map.get(event.getClickedBlock().getLocation()).gethealth() == 45))
                                            {
                                                if(map.get(event.getClickedBlock().getLocation()).getMiniGameCount() < 3)
                                                {
                                                    if(!map.get(event.getClickedBlock().getLocation()).getMiniGame())
                                                    {
                                                        map.get(event.getClickedBlock().getLocation()).setMiniGameCount(map.get(event.getClickedBlock().getLocation()).getMiniGameCount()+1);
                                                        event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("MiniGame" + "_" + "true").getBytes());
                                                    }
                                                }

                                            }

                                            if(event.getPlayer().getLocation().distance(event.getClickedBlock().getLocation()) > 3)
                                            {
                                                mission.getRepairBoxClassMap().get(event.getClickedBlock().getLocation()).setRepairing(false);
                                                variableMap.get(event.getPlayer().getName()).setRepair(false);
                                                event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "false").getBytes());
                                                event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("MiniGame" + "_" + "false").getBytes());
                                                Utils.Instance().sendTitle(event.getPlayer(), "[!]", "수리가 취소되었습니다.", 1, 30, 1, ChatColor.RED);
                                                this.cancel();

                                            }
                                            else if ((map.get(event.getClickedBlock().getLocation()).gethealth() > 60))
                                            {
                                                mission.getRepairBoxClassMap().get(event.getClickedBlock().getLocation()).setRepairing(false);
                                                variableMap.get(event.getPlayer().getName()).setRepair(false);
                                                mission.setBoxRepair(mission.getBoxRepair()+1);
                                                //Bukkit.broadcastMessage(String.format("%d", mission.getBoxRepair()));
                                                mission.getRepairBoxClassMap().get(event.getClickedBlock().getLocation()).setRepair(true);
                                                mission.getRepairBoxClassMap().get(event.getClickedBlock().getLocation()).setMiniGame(true);
                                                event.getPlayer().sendTitle("[!]", ChatColor.GREEN+ "수리가 완료되었습니다.", 1, 30, 1);
                                                this.cancel();
                                            }
                                            else if (map.get(event.getClickedBlock().getLocation()).gethealth() <= 60)
                                            {
                                                mission.getRepairBoxClassMap().get(event.getClickedBlock().getLocation()).setRepairing(true);
                                                map.get(event.getClickedBlock().getLocation()).sethealth(map.get(event.getClickedBlock().getLocation()).gethealth() + 1);
                                            }

                                        }
                                    }.runTaskTimer(Main.instance, 0, 20l);
                                }
                                else
                                {
                                    mission.getRepairBoxClassMap().get(event.getClickedBlock().getLocation()).setRepairing(false);
                                    variableMap.get(event.getPlayer().getName()).setRepair(false);
                                    event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "false").getBytes());
                                    event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("MiniGame" + "_" + "false").getBytes());
                                    Utils.Instance().sendTitle(event.getPlayer(), "[!]", "수리가 이미 완료된 박스 입니다.", 1, 30, 1, ChatColor.RED);
                                }
                            }

                        }
                    }
                }
            }
        }
    }

}

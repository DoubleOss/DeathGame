package doubleos.deathgame.event;

import doubleos.deathgame.Main;
import doubleos.deathgame.util.Utils;
import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.RepairBox;
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
                ArrayList<Block> blocks = mission.getRepairBoxList();
                HashMap<Block, RepairBox> map = mission.getRepairBoxClassMap();

                for(int i = 0 ; i<blocks.size(); i++)
                {
                    if(blocks.get(i).getLocation().equals(event.getClickedBlock().getLocation()))
                    {
                        if(event.getPlayer().getLocation().distance(event.getClickedBlock().getLocation()) <= 1)
                        {
                            if(!Main.instance.variablePlayer.get(event.getPlayer()).getRepair() && !mission.getRepairBoxClassMap().get(event.getClickedBlock()).getRepair())
                            {
                                Main.instance.variablePlayer.get(event.getPlayer()).setRepair(true);
                                event.getPlayer().sendTitle("[!]", ChatColor.GREEN+ "배전박스를 수리합니다" +ChatColor.RED+" 움직일 시 수리가 중단됩니다.", 1, 30, 1);
                                BukkitTask task = new BukkitRunnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if(event.getPlayer().getLocation().distance(event.getClickedBlock().getLocation()) > 1)
                                        {
                                            Main.instance.variablePlayer.get(event.getPlayer()).setRepair(false);
                                            this.cancel();

                                        }
                                        else if ((map.get(event.getClickedBlock()).gethealth() >= 30))
                                        {
                                            Main.instance.variablePlayer.get(event.getPlayer()).setRepair(false);
                                            mission.setBoxRepair(MissionManager.Instance().getBoxRepair()+1);
                                            mission.getRepairBoxClassMap().get(event.getClickedBlock()).setRepair(true);
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
                                Main.instance.variablePlayer.get(event.getPlayer()).setRepair(false);
                                Utils.Instance().sendTitle(event.getPlayer(), "[!]", "수리가 중단 되었습니다", 1, 30, 1, ChatColor.RED);
                            }
                        }
                    }
                }
            }
        }
    }

}

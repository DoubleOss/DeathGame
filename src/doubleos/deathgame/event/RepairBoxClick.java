package doubleos.deathgame.event;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.MissionManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class RepairBoxClick implements Listener
{
    @EventHandler
    void onRepairBoxClick(PlayerInteractEvent event)
    {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if(event.getClickedBlock().getType().equals(Material.PISTON_BASE))
            {
                ArrayList<Block> blocks = MissionManager.Instance().getRepairBoxList();
                for(int i = 0 ; i<blocks.size(); i++)
                {
                    if(blocks.get(i).getLocation().equals(event.getClickedBlock().getLocation()))
                    {
                        if(event.getPlayer().getLocation().distance(event.getClickedBlock().getLocation()) <= 1)
                        {
                            if(!Main.instance.variablePlayer.get(event.getPlayer()).getRepair())
                            {
                                Main.instance.variablePlayer.get(event.getPlayer()).setRepair(true);
                            }
                        }
                    }
                }
            }
        }
    }

}

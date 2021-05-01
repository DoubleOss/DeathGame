package doubleos.deathgame.event;

import doubleos.deathgame.variable.GameItem;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class FactoryHidden implements Listener
{



    /*
    @EventHandler
    void onRightClickatBlock(BlockPlaceEvent event)
    {
        if(!event.getPlayer().isOp())
            event.setCancelled(true);
        if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.FACTORY))
        {
            for(Location loc : MissionManager.Instance().getFactoryLoc())
            {
                if(loc.equals(event.getBlockAgainst().getLocation()))
                {
                    event.getBlockAgainst().setType(event.getBlockPlaced().getType());
                    event.getPlayer().getInventory().remove(event.getBlockAgainst().getType());
                    event.setCancelled(true);
                }
            }
        }
    }

     */

    @EventHandler
    void onRightClickatBlock(PlayerInteractEvent event)
    {
        if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.FACTORY))
        {
            MissionManager mission = MissionManager.Instance();
            if(event.getItem().getType().equals(Material.DROPPER) && !event.getPlayer().isOp())
                event.setCancelled(true);
            for(Location loc : MissionManager.Instance().getFactoryLoc())
            {
                if(loc.equals(event.getClickedBlock().getLocation()))
                {
                    if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                    {
                        if (event.getHand() != EquipmentSlot.HAND)
                        {
                            return;
                        }
                        for(ItemStack item : GameItem.Instance().m_itemList)
                        {
                            if(item.getType().equals(event.getItem().getType()))
                            {
                                event.getClickedBlock().setType(event.getItem().getType());
                                event.getPlayer().getInventory().remove(event.getItem().getType());
                                mission.setFactoryHiddenCount(mission.getFactoryHiddenCount()+1);
                                if(mission.getFactoryHiddenCount() == 6)
                                {
                                    Bukkit.broadcastMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE +" 제단의 봉인이 풀렸습니다.");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

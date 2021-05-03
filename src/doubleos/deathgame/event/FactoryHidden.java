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

            if(event.getClickedBlock().getType().equals(Material.POWERED_RAIL))
            {
                if(!event.getPlayer().isOp())
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
                                    if(event.getItem().getType().equals(Material.CHORUS_FRUIT))
                                    {
                                        event.getClickedBlock().setType(Material.IRON_TRAPDOOR);
                                    }
                                    else if(event.getItem().getType().equals(Material.CHORUS_FRUIT_POPPED))
                                    {
                                        //event.getClickedBlock().setType(Material.DIODE);
                                        event.getClickedBlock().setType(Material.getBlockMaterial(356));
                                        event.getClickedBlock().setType(Material.getBlockMaterial(356));

                                    }
                                    else if(event.getItem().getType().equals(Material.PRISMARINE_SHARD))
                                    {
                                        //event.getClickedBlock().setType(Material.REDSTONE_COMPARATOR);
                                        event.getClickedBlock().setType(Material.getBlockMaterial(404));
                                    }
                                    else if(event.getItem().getType().equals(Material.PRISMARINE_CRYSTALS))
                                    {
                                        event.getClickedBlock().setType(Material.ACTIVATOR_RAIL);
                                    }
                                    else if(event.getItem().getType().equals(Material.QUARTZ))
                                    {
                                        event.getClickedBlock().setType(Material.DETECTOR_RAIL);
                                    }
                                    else if(event.getItem().getType().equals(Material.GLOWSTONE_DUST))
                                    {
                                        event.getClickedBlock().setType(Material.REDSTONE_WIRE);
                                    }
                                    event.getPlayer().getInventory().remove(event.getItem().getType());
                                    mission.setFactoryHiddenCount(mission.getFactoryHiddenCount()+1);

                                    if(mission.getFactoryHiddenCount() == 12)
                                    {
                                        Bukkit.broadcastMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE +" 인형으로 둔갑한 사악한 귀신이 제단에 봉인되었습니다");
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }



}
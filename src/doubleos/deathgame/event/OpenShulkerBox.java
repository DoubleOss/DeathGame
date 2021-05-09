package doubleos.deathgame.event;

import doubleos.deathgame.Main;
import doubleos.deathgame.gui.MissionBoxGui;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class OpenShulkerBox implements Listener
{
    @EventHandler
    void onShulkerBox(PlayerInteractEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        String stringPlayer = event.getPlayer().getName();
        Block eventBlock = event.getClickedBlock();
        MissionManager missionManager = MissionManager.Instance();
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if(eventBlock.getType().equals(Material.RED_SHULKER_BOX))
            {
                if(gameVariable.getPlayerListVariableMap().get(event.getPlayer().getName()).getObserver())
                    return;
                if(gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
                {
                    event.setCancelled(true);
                    return;
                }

            }
            else if(eventBlock.getType().equals(Material.LIME_SHULKER_BOX) || eventBlock.getType().equals(Material.GREEN_SHULKER_BOX))
            {
                if(gameVariable.getPlayerListVariableMap().get(event.getPlayer().getName()).getObserver() || event.getPlayer().isOp())
                    return;
                if(gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                {
                    event.setCancelled(true);
                    return;
                }
                if(gameVariable.getPlayerVariableMap().get(stringPlayer).getMiniGamePlaying())
                {
                    event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("MiniGame" + "_" + "false").getBytes());
                    gameVariable.getPlayerVariableMap().get(stringPlayer).setMiniGamePlaying(false);
                    event.setCancelled(true);
                    return;
                }
                if(!gameVariable.getPlayerVariableMap().get(stringPlayer).getBoxOpen())
                {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" + ChatColor.WHITE +" 상자를 한번더 클릭하면 미니게임을 취소 할 수 있습니다.");
                    gameVariable.getPlayerVariableMap().get(stringPlayer).setMiniGamePlaying(true);
                    event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("MiniGame" + "_" + "true" +"_"+ "box").getBytes());
                    BukkitTask task = new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            if(!gameVariable.getPlayerVariableMap().get(stringPlayer).getMiniGamePlaying())
                            {
                                event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("MiniGame" + "_" + "false").getBytes());
                                this.cancel();
                            }
                            if(event.getPlayer().getLocation().distance(eventBlock.getLocation()) >= 4)
                            {
                                event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("MiniGame" + "_" + "false").getBytes());
                                gameVariable.getPlayerVariableMap().get(stringPlayer).setMiniGamePlaying(false);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Main.instance, 0l, 20l);
                    return;
                }
                else
                {
                    return;
                }
            }
        }
    }

    @EventHandler
    void onShulkerInventoryCloseEvent(InventoryCloseEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        String stringPlayer = event.getPlayer().getName();
        MissionManager missionManager = MissionManager.Instance();
        //event.getPlayer().sendMessage(event.getInventory().getTitle());
        if(event.getInventory().getTitle().equalsIgnoreCase("container.shulkerBox"))
        {
            gameVariable.getPlayerVariableMap().get(stringPlayer).setBoxOpen(false);
        }
    }
}

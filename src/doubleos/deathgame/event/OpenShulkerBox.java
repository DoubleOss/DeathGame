package doubleos.deathgame.event;

import doubleos.deathgame.gui.MissionBoxGui;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class OpenShulkerBox implements Listener
{
    @EventHandler
    void onShulkerBox(PlayerInteractEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        String player = event.getPlayer().getName();
        Block eventBlock = event.getClickedBlock();
        MissionManager missionManager = MissionManager.Instance();
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if(eventBlock.getType().equals(Material.RED_SHULKER_BOX))
            {
                if(GameVariable.Instance().getPlayerListVariableMap().get(event.getPlayer().getName()).getObserver())
                    return;
                if(gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
                {
                    event.setCancelled(true);
                    return;
                }

            }
            else if(eventBlock.getType().equals(Material.LIME_SHULKER_BOX))
            {
                if(GameVariable.Instance().getPlayerListVariableMap().get(event.getPlayer().getName()).getObserver())
                    return;
                if(gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}

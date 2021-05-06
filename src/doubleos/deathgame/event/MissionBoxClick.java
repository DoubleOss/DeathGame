package doubleos.deathgame.event;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import doubleos.deathgame.gui.MissionBoxGui;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class MissionBoxClick implements Listener
{
    @EventHandler
    void onMissionBoxClick(PlayerInteractEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        String player = event.getPlayer().getName();
        Block eventBlock = event.getClickedBlock();
        MissionManager missionManager = MissionManager.Instance();
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if(eventBlock.getType().equals(Material.PISTON_STICKY_BASE))
            {
                if (event.getHand() != EquipmentSlot.HAND)
                {
                    return;
                }
                if(GameVariable.Instance().getPlayerListVariableMap().get(event.getPlayer().getName()).getObserver())
                    return;
                for(int i =0; i<missionManager.getMissionBoxList().size(); i++)
                {
                    if(missionManager.getMissionBoxList().get(i).equals(event.getClickedBlock().getLocation()))
                    {
                        if(!missionManager.getMissionBoxMap().get(eventBlock.getLocation()).getBoxUse())
                        {
                            //event.getPlayer().sendMessage(String.format(gameVariable.getPlayerVariableMap().get(player).getHumanType().toString()));

                            if(gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                            {
                                missionManager.m_MissionBoxUseLocation = eventBlock.getLocation();
                                MissionBoxGui boxgui = new MissionBoxGui();
                                boxgui.initGuiItem(event.getPlayer());
                                boxgui.openInventory(event.getPlayer());

                            }
                        }
                        else
                        {
                            ActionBarAPI.sendActionBar(event.getPlayer(), "이미 사용한 미션 블럭입니다.", 20);
                        }
                    }

                }


            }
        }
    }
}

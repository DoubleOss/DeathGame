package doubleos.deathgame.event;

import doubleos.deathgame.variable.GameItem;
import doubleos.deathgame.variable.GameVariable;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GlowStoneInventoryEvent implements Listener
{
    @EventHandler
    public void onInventoryEventClickGlowStone(InventoryClickEvent event)
    {
        if(GameVariable.Instance().getGameState().equals(GameVariable.GameState.PLAY))
        {
            if(event.getWhoClicked().isOp())
                return;
            if(event.getCurrentItem().getType().equals(GameItem.Instance().m_glowStone_Item.getType()))
            {
                event.setCancelled(true);
                if(event.getWhoClicked().getInventory().getItem(0).getType().equals(GameItem.Instance().m_glowStone_Item.getType()))
                {
                    event.getInventory().addItem(event.getWhoClicked().getInventory().getItem(0));
                    event.getWhoClicked().getInventory().setItem(0, event.getCurrentItem());
                    event.getWhoClicked().getInventory().getItem(event.getRawSlot()).setType(Material.AIR);
                }

            }
        }

    }

}

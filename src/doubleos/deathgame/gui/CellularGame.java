package doubleos.deathgame.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CellularGame implements Listener
{
    Inventory m_inv;

    ArrayList<String> value = new ArrayList<>();

    public void initGuiItem()
    {
        for(int i = 0; i<49; i++)
        {
            value.add("꽝");
        }
        for(int i = 0; i<5; i++)
        {
            value.add("당첨");
        }

        Collections.shuffle(value);

        m_inv = Bukkit.createInventory(null, 54, "세포 게임");

        for(int i = 0; i<53; i++)
        {
            if(value.get(i).equalsIgnoreCase("꽝"))
            {
                m_inv.setItem(i, createGuiItem(Material.COAL, "꽝", ""));
            }
            else
            {
                m_inv.setItem(i, createGuiItem(Material.DIAMOND, "당첨", ""));
            }
        }

    }



    ItemStack createGuiItem(final Material material, final String name, final String lore)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public void openInventory(final HumanEntity ent)
    {
        ent.openInventory(m_inv);
    }
}

package doubleos.deathgame.gui;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;

public class PostionMakeGui implements Listener
{

    Inventory m_inv;

    Player m_target;

    public void initGuiItem()
    {
        m_inv = Bukkit.createInventory(null, 45, "포션 제작");

        int[] green = new int[]{0, 1, 9, 18, 27, 36};
        int[] white = new int[]{2, 3, 4, 5, 6, 11, 12, 14, 15, 20, 21, 23, 24, 29, 33, 38, 39, 40, 41, 42};
        int[] gray = new int[]{7, 8 ,17, 26, 35, 43, 44};


        for(int i = 0; i< green.length; i++)
        {
            m_inv.setItem(green[i], createGuiGlassItem(Material.STAINED_GLASS_PANE , (short) 5, "", null));
        }
        for(int i = 0; i< white.length; i++)
        {
            m_inv.setItem(white[i], createGuiGlassItem(Material.STAINED_GLASS_PANE , (short) 0, "", null));
        }
        for(int i = 0; i< gray.length; i++)
        {
            m_inv.setItem(gray[i], createGuiGlassItem(Material.STAINED_GLASS_PANE , (short) 7, "", null));
        }

        //포션목록
        m_inv.setItem(10, createPotionGuiItem(Material.POTION, PotionEffectType.JUMP, "", ""));
        m_inv.setItem(11, createPotionGuiItem(Material.POTION, PotionEffectType.REGENERATION, "", ""));
        m_inv.setItem(12, createPotionGuiItem(Material.POTION, PotionEffectType.FIRE_RESISTANCE, "", ""));

        //양조기
        m_inv.setItem(13, createGuiItem(Material.BREWING_STAND, "", ""));








    }

    public void openInventory(final HumanEntity ent)
    {
        ent.openInventory(m_inv);
    }

    ItemStack createGuiGlassItem(final Material material, short number, final String name, final String lore)
    {
        final ItemStack item = new ItemStack(material, 1, number);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;

    }
    ItemStack createPotionGuiItem(final Material material,PotionEffectType type ,final String name, final String lore)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        PotionMeta potionmeta = (PotionMeta) meta;
        PotionEffect effect1 = new PotionEffect(type, 15, 15);
        ((PotionMeta) meta).addCustomEffect(effect1, false);

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);
        return item;
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

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("포션 제작"))
        {

            event.setCancelled(true);

        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("포션 제작"))
        {
            event.setCancelled(true);
        }
    }




}

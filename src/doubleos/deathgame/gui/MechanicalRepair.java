package doubleos.deathgame.gui;

import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.PotionRecipe;
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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;

public class MechanicalRepair implements Listener
{
    Inventory m_inv;

    Player m_target;


    public void initGuiItem(Player player)
    {
        m_inv = Bukkit.createInventory(null, 27, "기계 수리");

        for(int i =0; i<27; i++)
        {
            m_inv.setItem(i, createGuiGlassItem(Material.STAINED_GLASS_PANE , (short) 0, "", null));
        }


        m_inv.setItem(10, createGuiItem(Material.AIR, "", ""));
        m_inv.setItem(12, createGuiItem(Material.AIR, "", ""));
        m_inv.setItem(14, createGuiItem(Material.AIR, "", ""));



        m_inv.setItem(16, createGuiGlassItem(Material.STAINED_GLASS_PANE , (short) 5, ChatColor.WHITE+"재가동", null));

        m_target = player;
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

    ItemStack createGuiItem(final Material material, final String name, final String lore)
    {

        final ItemStack item = new ItemStack(material, 1);

        if(material.equals(Material.AIR))
            return item;
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    @EventHandler
    public void onInventoryClickSelectGear(InventoryClickEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("기계 수리"))
        {
            if(MissionManager.Instance().getMission2Success() == false)
            {
                event.setCancelled(true);
                if(event.getCurrentItem().getType().equals(Material.GOLD_NUGGET))
                {
                    event.getInventory().setItem(getGuiSlotAir(event.getInventory()), createGuiItem(Material.GOLD_NUGGET, "톱니바퀴", ""));
                }
                else
                {
                    event.getWhoClicked().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" + ChatColor.WHITE + ": 해당 아이템을 올릴 수 없습니다.");
                }
            }

        }
    }

    @EventHandler
    public void onInventoryClickRestart(InventoryClickEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("기계 수리"))
        {
            if(MissionManager.Instance().getMission2Success() == false)
            {
                event.setCancelled(true);
                if(event.getRawSlot() == 16)
                {
                    mechanicalReStart(event);

                }
            }

        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("기계 수리"))
        {
            event.setCancelled(true);
        }
    }

    public void openInventory(final HumanEntity ent)
    {
        if(MissionManager.Instance().getMission2Success() == false)
            ent.openInventory(m_inv);
    }



    void mechanicalReStart(InventoryClickEvent event)
    {
        if(event.getInventory().getItem(10) != null && event.getInventory().getItem(12) != null && event.getInventory().getItem(14) != null)
        {
            event.getWhoClicked().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" + ChatColor.WHITE + ": 기계를 재작동 시키셨습니다.");
            //event.getWhoClicked().getInventory().remove(Material.GOLD_NUGGET);
            event.getWhoClicked().getInventory().removeItem(new ItemStack[]{ new ItemStack(Material.GOLD_NUGGET, 3)});
            MissionManager.Instance().setMission2Success(true);
            event.getWhoClicked().closeInventory();
            for(Player p :Bukkit.getOnlinePlayers())
            {
                if(p.isOp())
                {
                    p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.WHITE + "살인마가 기계수리 미션을 클리어 하셨습니다.");
                }
            }
        }
    }


    int getGuiSlotAir(Inventory inven)
    {
        if(inven.getItem(10) == null)
        {
            return 10;
        }
        else if(inven.getItem(12) == null)
        {
            return 12;
        }
        else if (inven.getItem(14) == null)
        {
            return 14;
        }
        return 100;
    }
}
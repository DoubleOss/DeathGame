package doubleos.deathgame.gui;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MissionBoxGui implements Listener
{
    Inventory m_inv;

    ArrayList<String> m_value = new ArrayList<>();

    Player m_player;

    boolean m_gameStart = false;

    boolean m_shuffling = false;


    public void initGuiItem(Player player)
    {
        m_player = player;


        m_inv = Bukkit.createInventory(null, 9, "미션");

        MissionManager missionManager = MissionManager.Instance();
        String missingName1 = missionManager.getMission1Title();
        String missingName2 = missionManager.getMission2Title();


        m_inv.setItem(2, createGuiItem(Material.DIAMOND_BLOCK, missingName1, ""));
        m_inv.setItem(6,createGuiItem(Material.EMERALD, missingName2, ""));

    }


    @EventHandler
    public void onInventoryClickMission(InventoryClickEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("미션"))
        {
            event.setCancelled(true);
            if(event.getCurrentItem() != null)
            {
                if(event.getCurrentItem().getItemMeta().getDisplayName() == null)
                    return;
                if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("물약제조"))
                {
                    if(!MissionManager.Instance().getMission1Success())
                    {
                        PotionMakeGui potiongui = new PotionMakeGui();
                        potiongui.initGuiItem();
                        potiongui.openInventory(event.getWhoClicked());
                    }


                }
                else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("해킹"))
                {
                    if(!MissionManager.Instance().getMission2Success())
                    {
                        event.getWhoClicked().closeInventory();
                        Mission_Hacking hacking = new Mission_Hacking();
                        hacking.initHacking((Player) event.getWhoClicked());
                    }

                }
                else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("소독"))
                {
                    if(!MissionManager.Instance().getMission1Success())
                    {
                        event.getWhoClicked().closeInventory();
                        Mission_clean clean = new Mission_clean();
                        clean.initCleaning((Player) event.getWhoClicked());
                    }

                }
                else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("기계 수리"))
                {
                    if(!MissionManager.Instance().getMission2Success())
                    {
                        MechanicalRepair repair = new MechanicalRepair();
                        repair.initGuiItem(((Player) event.getWhoClicked()).getPlayer());
                        repair.openInventory(event.getWhoClicked());

                    }

                }
                else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("세포 찾기"))
                {
                    if(!MissionManager.Instance().getMission1Success())
                    {
                        CellularGame cellGame = new CellularGame();
                        cellGame.initGuiItem((Player) event.getWhoClicked());
                        cellGame.openInventory(event.getWhoClicked());
                    }

                }
                else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("불량품 찾기"))
                {
                    if(!MissionManager.Instance().getMission2Success())
                    {
                        DefectiveGame defgame = new DefectiveGame();
                        defgame.initGuiItem(((Player) event.getWhoClicked()));
                        defgame.openInventory(event.getWhoClicked());
                    }

                }
            }
        }
    }
    @EventHandler
    public void closeInventory(InventoryCloseEvent event)
    {

    }
    public void openInventory(final HumanEntity ent)
    {
        ent.openInventory(m_inv);
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

    ItemStack createGuiGlassItem(final Material material, short number, final String name, final String lore)
    {
        final ItemStack item = new ItemStack(material, 1, number);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }


    public ArrayList<String> getResultArray()
    {
        return  m_value;
    }

}

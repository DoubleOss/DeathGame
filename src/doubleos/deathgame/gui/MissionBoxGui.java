package doubleos.deathgame.gui;

import doubleos.deathgame.ablilty.HealKit;
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

import java.util.ArrayList;
import java.util.Arrays;

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


        String loreName1 = "";
        String loreName2 = "";

        if (missingName1.equalsIgnoreCase("물약제조"))
            loreName1 = "좌측 창에서 제작하고 싶은 물약을 선택한 후, \n우측 창에 뜨는 재료들을 파밍해 빈 칸에 재료를 넣어주십시오.";
        if (missingName2.equalsIgnoreCase("해킹"))
            loreName2 = "버튼을 클릭하여 대기하는 미션입니다.";
        if (missingName1.equalsIgnoreCase("소독"))
            loreName1 = "버튼을 클릭하여 대기하는 미션입니다.";
        if (missingName2.equalsIgnoreCase("기계 수리"))
            loreName2 = "맵에 숨겨져있는 톱니바퀴3개를 찾아 \n빈 칸에 넣고 수리버튼을 누르는 미션입니다.";
        if (missingName1.equalsIgnoreCase("세포 찾기"))
            loreName1 = " 빠르게 지나다니는 인형들 사이에 \n숨어있는 불량품을 찾으시오. (무제한으로 클릭가능) ";
        if (missingName2.equalsIgnoreCase("불량품 찾기"))
            loreName2 = "빠르게 지나다니는 인형들 사이에  \n숨어있는 불량품을 찾으시오. (무제한으로 클릭가능)";

        m_inv.setItem(2, createGuiItem(Material.DIAMOND_BLOCK, ChatColor.WHITE + missingName1, ChatColor.WHITE + loreName1));
        m_inv.setItem(6,createGuiItem(Material.EMERALD, ChatColor.WHITE + missingName2, ChatColor.WHITE + loreName2));

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
                if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "물약제조"))
                {
                    if(!MissionManager.Instance().getMission1Success())
                    {
                        PotionMakeGui potiongui = new PotionMakeGui();
                        potiongui.initGuiItem();
                        potiongui.openInventory(event.getWhoClicked());
                    }


                }
                else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "해킹"))
                {
                    if(!MissionManager.Instance().getMission2Success())
                    {
                        event.getWhoClicked().closeInventory();
                    }

                }
                else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "소독"))
                {
                    if(!MissionManager.Instance().getMission1Success())
                    {
                        event.getWhoClicked().closeInventory();
                        Mission_clean clean = new Mission_clean();
                        clean.initCleaning((Player) event.getWhoClicked());
                    }

                }
                else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "기계 수리"))
                {
                    if(!MissionManager.Instance().getMission2Success())
                    {
                        MechanicalRepair repair = new MechanicalRepair();
                        repair.initGuiItem(((Player) event.getWhoClicked()).getPlayer());
                        repair.openInventory(event.getWhoClicked());

                    }

                }
                else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "세포 찾기"))
                {
                    if(!MissionManager.Instance().getMission1Success())
                    {
                        CellularGame cellGame = new CellularGame();
                        cellGame.initGuiItem((Player) event.getWhoClicked());
                        cellGame.openInventory(event.getWhoClicked());
                    }

                }
                else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "불량품 찾기"))
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

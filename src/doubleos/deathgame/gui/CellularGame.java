package doubleos.deathgame.gui;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CellularGame implements Listener
{
    Inventory m_inv;

    ArrayList<String> m_value = new ArrayList<>();

    Player m_player;

    boolean m_gameStart = false;

    boolean m_shuffling = false;

    int m_guiAnimationCount = 0;

    int m_prizeCount = 0;

    public void initGuiItem(Player player)
    {
        m_player = player;
        for(int i = 0; i<49; i++)
        {
            m_value.add("꽝");
        }
        for(int i = 0; i<5; i++)
        {
            m_value.add("당첨");
        }

        Collections.shuffle(m_value);

        m_inv = Bukkit.createInventory(null, 54, "세포 게임");

        for(int i = 0; i<54; i++)
        {

            m_inv.setItem(i, createGuiGlassItem(Material.STAINED_GLASS_PANE, (short)7, "", ""));
            /*
            if(value.get(i).equalsIgnoreCase("꽝"))
            {
                m_inv.setItem(i, createGuiItem(Material.COAL, ChatColor.WHITE + "꽝", ""));
            }
            else
            {
                m_inv.setItem(i, createGuiItem(Material.DIAMOND, ChatColor.WHITE + "당첨", ""));
            }

             */
        }
        m_inv.setItem(22,createGuiItem(Material.REDSTONE_BLOCK, ChatColor.WHITE+"게임시작", ""));

        GameVariable.Instance().addCellGameClassGetPlayer(this, m_player);

    }


    @EventHandler
    public void onInventoryClickGameStart(InventoryClickEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("세포 게임"))
        {
            event.setCancelled(true);
            if(event.getCurrentItem() != null)
            {
                if(m_gameStart == false)
                {
                    if(event.getCurrentItem().getType() == Material.REDSTONE_BLOCK)
                    {
                        playGame((Player) event.getWhoClicked(), event);
                        GameVariable.Instance().getCellGameClassPlayer(event.getWhoClicked().getName()).m_gameStart = true;
                        GameVariable.Instance().getCellGameClassPlayer(event.getWhoClicked().getName()).m_shuffling = true;

                    }
                }
            }


        }
    }
    @EventHandler
    public void onInventoryClickGameClick(InventoryClickEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("세포 게임"))
        {
            CellularGame cell = GameVariable.Instance().getCellGameClassPlayer(event.getWhoClicked().getName());
            event.setCancelled(true);
            if(event.getCurrentItem() != null)
            {
                if(cell.m_gameStart == true && cell.m_shuffling == false)
                {
                    if(event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE))
                    {
                        if(cell.m_value.get(event.getRawSlot()).equalsIgnoreCase("꽝"))
                        {
                            event.getInventory().setItem(event.getRawSlot(), createGuiItem(Material.COAL, ChatColor.WHITE + "꽝", ""));
                        }
                        else
                        {
                            event.getInventory().setItem(event.getRawSlot(), createGuiItem(Material.DIAMOND, ChatColor.WHITE + "당첨", ""));
                            ++cell.m_prizeCount;
                            if(cell.m_prizeCount == 5)
                            {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        cell.m_gameStart = false;
                                        event.getWhoClicked().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" + ChatColor.WHITE + "세포찾기 미션을 클리어 하셨습니다.");
                                        event.getWhoClicked().closeInventory();
                                        cell.m_prizeCount = 0;
                                        for(Player p :Bukkit.getOnlinePlayers())
                                        {
                                            if(p.isOp())
                                            {
                                                p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.WHITE + "살인마가 세포찾기 미션을 클리어 하셨습니다.");
                                            }
                                        }
                                    }
                                }, 20l);
                            }
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void closeInventory(InventoryCloseEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("세포 게임"))
        {
            CellularGame cell = GameVariable.Instance().getCellGameClassPlayer(event.getPlayer().getName());
            GameVariable.Instance().getcellClassHash().remove(m_player);
            cell.m_prizeCount = 0;
            cell.m_guiAnimationCount = 0;
        }
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



    void playGame(Player player, InventoryClickEvent event)
    {
        event.getInventory().setItem(22, createGuiGlassItem(Material.STAINED_GLASS_PANE, (short)7, "", ""));
        BukkitTask task = new BukkitRunnable()
        {

            ArrayList<String> arr = GameVariable.Instance().getCellGameClassPlayer(player.getName()).getResultArray();
            CellularGame cell = GameVariable.Instance().getCellGameClassPlayer(player.getName());
            @Override
            public void run()
            {


                if(cell.m_guiAnimationCount <= 53)
                {
                    if(arr.get(cell.m_guiAnimationCount).equalsIgnoreCase("꽝"))
                    {
                        event.getInventory().setItem(cell.m_guiAnimationCount, createGuiItem(Material.COAL, ChatColor.WHITE + "꽝", ""));
                    }
                    else
                    {
                        event.getInventory().setItem(cell.m_guiAnimationCount, createGuiItem(Material.DIAMOND, ChatColor.WHITE + "당첨", ""));
                    }

                }
                if(cell.m_guiAnimationCount - 2 >= 0)
                {
                    event.getInventory().setItem(cell.m_guiAnimationCount-2, createGuiGlassItem(Material.STAINED_GLASS_PANE, (short)7, "", ""));
                }

                if(cell.m_guiAnimationCount > 53)
                {
                    if(event.getInventory().getItem(cell.m_guiAnimationCount-2).getType().equals(Material.STAINED_GLASS_PANE))
                    {
                        event.getInventory().setItem(53, createGuiGlassItem(Material.STAINED_GLASS_PANE, (short)7, "", ""));
                        GameVariable.Instance().getCellGameClassPlayer(player.getName()).m_shuffling = false;
                        cell.m_guiAnimationCount = 0;
                        this.cancel();
                    }
                }
                cell.m_guiAnimationCount++;
            }

        }.runTaskTimer(Main.instance, 0l, 1l);
    }



    public boolean getGameStart()
    {
        return m_gameStart;
    }
    public void setGameStart(boolean bool)
    {
        m_gameStart = bool;
    }
    public boolean getGameShuffling()
    {
        return m_shuffling;
    }
    public void setshuffling(boolean bool)
    {
        m_shuffling = bool;
    }

    public ArrayList<String> getResultArray()
    {
        return  m_value;
    }
}

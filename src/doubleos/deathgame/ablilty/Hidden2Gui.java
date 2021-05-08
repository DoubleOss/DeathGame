package doubleos.deathgame.ablilty;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.UUID;

public class Hidden2Gui implements Listener
{

    Inventory m_inv;

    Player m_target;

    public Hidden2Gui()
    {

    }

    public void initGuiItem()
    {
        m_inv = Bukkit.createInventory(null, 45, "기도 지정");

        int[] slot = new int[]{10, 12, 14, 16, 28, 30, 32, 34};

        int loopnumber = 0;
        for(String stringPlayer : GameVariable.Instance().getGamePlayerList())
        {
            Player p = Bukkit.getPlayer(stringPlayer);
            if(GameVariable.Instance().getPlayerVariableMap().get(p.getName()).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
            {
                loopnumber++;
                m_inv.setItem(slot[loopnumber],createGuiItem(Material.RED_ROSE, String.format(ChatColor.WHITE + p.getName()), null));
            }

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


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("기도 지정"))
        {
            event.setCancelled(true);
            if(!event.getClick().equals(ClickType.LEFT))
            {
                return;
            }
            if(event.getCurrentItem().getType() != Material.AIR)
            {
                if(GameVariable.Instance().getKillerHidden2().m_skill2Cooltime <= 0)
                {
                    hidden2Ablilty(event);
                }

            }
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("기도 지정"))
        {
            event.setCancelled(true);
        }
    }


    @EventHandler
    void hidden2Ablilty(InventoryClickEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("기도 지정"))
        {

            if(!event.getClick().equals(ClickType.LEFT))
            {
                return;
            }
            m_target = Bukkit.getServer().getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§f", ""));
            Player p = (Player) event.getWhoClicked();
            p.sendMessage(ChatColor.RED + "[죽음의 술래잡기]"+ ChatColor.WHITE + " " + ChatColor.RED + m_target.getName() + ChatColor.WHITE +" 님을 전도 대상으로 고르셨습니다.");
            GameVariable.Instance().setHidden2Targer(m_target);
            GameVariable.Instance().getKillerHidden2().m_skill2Cooltime = 120;
            for(Player player :Bukkit.getOnlinePlayers())
            {
                if(player.isOp())
                {
                    player.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.WHITE + "살인마가 " + ChatColor.RED +m_target.getPlayer().getName()+ ChatColor.WHITE + " 님을 전도 대상으로 고르셨습니다.");
                }
            }

            BukkitTask task = new BukkitRunnable()
            {
                @Override
                public void run()
                {

                    if(GameVariable.Instance().getKillerHidden2().m_skill2Cooltime <= 0)
                    {
                        GameVariable.Instance().setHidden2Targer(null);
                        this.cancel();
                    }
                    else
                    {
                        GameVariable.Instance().getKillerHidden2().m_skill2Cooltime--;
                    }
                }
            }.runTaskTimer(Main.instance, 0l, 20l);
        }

    }
}

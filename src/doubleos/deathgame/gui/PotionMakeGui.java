package doubleos.deathgame.gui;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;

public class PotionMakeGui implements Listener
{

    Inventory m_inv;

    Player m_target;

    PotionRecipe.Potion m_potion;

    public void initGuiItem()
    {
        m_inv = Bukkit.createInventory(null, 45, "포션 제작");

        int[] green = new int[]{0, 1, 9, 18, 27, 36, 37};
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
        m_inv.setItem(10, createPotionGuiItem(Material.POTION, PotionEffectType.JUMP, PotionType.JUMP, "", ""));
        m_inv.setItem(19, createPotionGuiItem(Material.POTION, PotionEffectType.INCREASE_DAMAGE, PotionType.STRENGTH , "", ""));
        m_inv.setItem(28, createPotionGuiItem(Material.POTION, PotionEffectType.FIRE_RESISTANCE, PotionType.FIRE_RESISTANCE, "", ""));

        //양조기
        m_inv.setItem(13, createGuiItem(Material.BREWING_STAND_ITEM, "", ""));


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
    ItemStack createPotionGuiItem(final Material material,PotionEffectType type, PotionType ptype,final String name, final String lore)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        PotionMeta potionmeta = (PotionMeta) meta;


        PotionEffect effect1 = new PotionEffect(type, 3600, 0);

        ((PotionMeta) meta).setBasePotionData(new PotionData(ptype, false, false));
        ((PotionMeta) meta).addItemFlags(new ItemFlag[] {ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES});
        //((PotionMeta) meta).addCustomEffect(effect1, true);

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
    public void onInventoryClickPotionSelect(InventoryClickEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("포션 제작"))
        {
            if(event.getRawSlot() == 16|| event.getRawSlot() == 25|| event.getRawSlot() == 34)
                event.setCancelled(true);

            event.setCancelled(true);
            if (event.getRawSlot() == 10 || event.getRawSlot() == 19 || event.getRawSlot() == 28)
            {
                ArrayList<ItemStack> potionrecipe = PotionRecipe.Instance().getPotionMatrial().get(getPotionRecipe(event.getRawSlot()));
                if(potionrecipe != null)
                {
                    m_potion = getPotionRecipe(event.getRawSlot());
                    event.getInventory().setItem(16,potionrecipe.get(0));
                    event.getInventory().setItem(25,potionrecipe.get(1));
                    event.getInventory().setItem(34,potionrecipe.get(2));

                    event.getInventory().setItem(22, event.getInventory().getItem(event.getRawSlot()));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClickPotionMaterialClick(InventoryClickEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("포션 제작"))
        {
            event.setCancelled(true);
            if (event.getCurrentItem() != null)
            {
                if (event.getRawSlot() != 16 || event.getRawSlot() != 25 || event.getRawSlot() != 34)
                {
                    int[] slot = new int[]{16, 25, 34};

                    for (int i = 0; i<3; i++)
                    {
                        if(event.getCurrentItem().getType().equals(event.getInventory().getItem(slot[i]).getType()))
                        {
                            Bukkit.broadcastMessage(event.getInventory().getItem(slot[i]).toString());
                            if(getGuiSlotAir(event.getInventory()) != 100)
                            {
                                event.getInventory().setItem(getGuiSlotAir(event.getInventory()), event.getInventory().getItem(slot[i]));
                                break;
                            }

                        }
                    }
                }
            }

        }
    }
    @EventHandler
    public void onInventoryClickPotionMake(InventoryClickEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("포션 제작"))
        {
            event.setCancelled(true);
            if (event.getRawSlot() == 22)
            {
                boolean item_check1 = event.getWhoClicked().getInventory().containsAtLeast(PotionRecipe.Instance().getPotionMatrial().get(m_potion).get(0), PotionRecipe.Instance().getPotionMatrial().get(m_potion).get(0).getAmount());
                boolean item_check2 = event.getWhoClicked().getInventory().containsAtLeast(PotionRecipe.Instance().getPotionMatrial().get(m_potion).get(1), PotionRecipe.Instance().getPotionMatrial().get(m_potion).get(1).getAmount());
                boolean item_check3 = event.getWhoClicked().getInventory().containsAtLeast(PotionRecipe.Instance().getPotionMatrial().get(m_potion).get(2), PotionRecipe.Instance().getPotionMatrial().get(m_potion).get(2).getAmount());
                event.getWhoClicked().sendMessage(Boolean.toString(item_check1)+" " + Boolean.toString(item_check2) +" " + Boolean.toString(item_check3));
                if(item_check1 && item_check2 && item_check3)
                {
                    ArrayList<ItemStack> itemlist= PotionRecipe.Instance().getPotionMatrial().get(m_potion);
                    event.getWhoClicked().getInventory().removeItem(new ItemStack[]{ new ItemStack(itemlist.get(0).getType(), itemlist.get(0).getAmount())});
                    event.getWhoClicked().getInventory().removeItem(new ItemStack[]{ new ItemStack(itemlist.get(1).getType(), itemlist.get(1).getAmount())});
                    event.getWhoClicked().getInventory().removeItem(new ItemStack[]{ new ItemStack(itemlist.get(2).getType(), itemlist.get(2).getAmount())});
                    MissionManager mission = MissionManager.Instance();
                    mission.setMissionPotionCount(mission.getMission1PotionCount() + 1);
                    event.getWhoClicked().sendMessage("포션을 1개 만드셨습니다 " + mission.getMission1PotionCount() + " | 3");
                    ItemStack item = new ItemStack(Material.AIR);
                    event.getInventory().setItem(30, item);
                    event.getInventory().setItem(31, item);
                    event.getInventory().setItem(32, item);

                    if(mission.getMission1PotionCount() == 3)
                    {
                        event.getWhoClicked().closeInventory();
                    }
                }

            }
        }

    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event)
    {
        if(event.getInventory().getTitle().equalsIgnoreCase("포션 제작"))
        {
            event.setCancelled(true);
        }
    }

    public void openInventory(final HumanEntity ent)
    {
        ent.openInventory(m_inv);
    }

    int getGuiSlotAir(Inventory inven)
    {
        if(inven.getItem(30) == null)
        {
            return 30;
        }
        else if(inven.getItem(31) == null)
        {
            return 31;
        }
        else if (inven.getItem(32) == null)
        {
            return 32;
        }
        return 100;
    }



    PotionRecipe.Potion getPotionRecipe(int slot)
    {
        if (slot == 10)
        {
            return PotionRecipe.Potion.GREEN_POTION;
        }
        else if (slot == 19)
        {
            return PotionRecipe.Potion.PINK_POTION;
        }
        else if (slot == 28)
        {
            return PotionRecipe.Potion.ORANGE_POTION;
        }
        return null;
    }


}

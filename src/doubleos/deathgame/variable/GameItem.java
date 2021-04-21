package doubleos.deathgame.variable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GameItem
{

    static private GameItem _instance = null;

    static public GameItem Instance()
    {
        if(_instance == null)
        {
            _instance = new GameItem();
        }
        return _instance;
    }

    public ItemStack m_killerCom_Ability1_Item;
    public ItemStack m_killerCom_Ability1_Block;

    public ItemStack m_killerCom_Ability2_Item;

    public ItemStack m_killerHidden1_Ability1_Item;
    public ItemStack m_killerHidden1_Ability2_Item;

    public ItemStack m_killerHidden2_Ability1_Item;
    public ItemStack m_killerHidden2_Ability2_Item;

    public ItemStack m_killerHidden3_Ability1_Item;
    public ItemStack m_killerHidden3_Ability2_Item;




    public void initGameItem()
    {
        m_killerCom_Ability1_Item = createItem(Material.FLINT, "덫", "", 3);
        m_killerCom_Ability1_Block = createItem(Material.CARPET, "덫", "", 1);

        m_killerCom_Ability2_Item = createItem(Material.FEATHER, "탐지", "");

        m_killerHidden1_Ability1_Item = createItem(Material.STICK, "랜덤텔포", "");
        m_killerHidden1_Ability2_Item = createItem(Material.CLAY_BALL, "위산분비", "");

        m_killerHidden2_Ability1_Item = createItem(Material.CLAY_BRICK, "점멸", "");
        m_killerHidden2_Ability2_Item = createItem(Material.BOOK, "전도", "");

        m_killerHidden3_Ability1_Item = createItem(Material.BLAZE_ROD, "은신", "");
        m_killerHidden3_Ability2_Item = createItem(Material.SUGAR, "눈알던지기", "");




    }

    ItemStack createItem(final Material material, final String name, final String lore)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.WHITE + name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;

    }

    ItemStack createItem(final Material material, final String name, final String lore, final int amount)
    {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.WHITE + name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;

    }
}

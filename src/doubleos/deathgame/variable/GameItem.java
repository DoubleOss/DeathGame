package doubleos.deathgame.variable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
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

    public ItemStack m_humanCom_Ability1_Item;
    public ItemStack m_humanHeal_Ability1_Item;


    public ItemStack m_humanCom_Ability1_Block;

    public ItemStack m_killerCom_Ability2_Item;

    public ItemStack m_killerHidden1_Ability1_Item;
    public ItemStack m_killerHidden1_Ability2_Item;

    public ItemStack m_killerHidden2_Ability1_Item;
    public ItemStack m_killerHidden2_Ability2_Item;
    public ItemStack m_killerHidden2_Ability3_Item;


    public ItemStack m_killerHidden3_Ability1_Item;
    public ItemStack m_killerHidden3_Ability2_Item;

    public ItemStack m_glowStone_Item;

    public ItemStack m_FactoryHiddenItem_1;
    public ItemStack m_FactoryHiddenItem_2;
    public ItemStack m_FactoryHiddenItem_3;
    public ItemStack m_FactoryHiddenItem_4;
    public ItemStack m_FactoryHiddenItem_5;
    public ItemStack m_FactoryHiddenItem_6;

    public ArrayList<ItemStack> m_itemList = new ArrayList<>();



    public void initGameItem()
    {
        m_humanHeal_Ability1_Item = createItem(Material.MAGMA_CREAM, "구급상자", "");
        m_glowStone_Item = createItem(Material.GLOWSTONE, "랜턴", "");

        m_humanCom_Ability1_Item = createItem(Material.FLINT, "덫", "", 2);
        m_humanCom_Ability1_Block = createItem(Material.CARPET, "덫", "");

        m_killerCom_Ability2_Item = createItem(Material.FEATHER, "탐지", "");


        m_killerHidden1_Ability1_Item = createItem(Material.STICK, "랜덤텔포", "");
        m_killerHidden1_Ability2_Item = createItem(Material.CLAY_BALL, "위산분비", "");

        m_killerHidden2_Ability1_Item = createItem(Material.CLAY_BRICK, "점멸", "");
        m_killerHidden2_Ability2_Item = createItem(Material.BOOK, "전도", "");


        m_killerHidden3_Ability1_Item = createItem(Material.BLAZE_ROD, "은신", "");
        m_killerHidden3_Ability2_Item = createItem(Material.SUGAR, "눈알던지기", "");

        m_FactoryHiddenItem_1 = new ItemStack(Material.QUARTZ); // 빨간구슬
        m_FactoryHiddenItem_2 = new ItemStack(Material.PRISMARINE_CRYSTALS); // 보라구슬
        m_FactoryHiddenItem_3 = new ItemStack(Material.GLOWSTONE_DUST); // 초록구슬
        m_FactoryHiddenItem_4 = new ItemStack(Material.CHORUS_FRUIT); // 곰인형 재단
        m_FactoryHiddenItem_5 = new ItemStack(Material.CHORUS_FRUIT_POPPED); // 토끼인형 재단
        m_FactoryHiddenItem_6 = new ItemStack(Material.PRISMARINE_SHARD); // 개인형 재단



        addFactoryItemList();
    }

    void addFactoryItemList()
    {
        m_itemList.add(m_FactoryHiddenItem_1);
        m_itemList.add(m_FactoryHiddenItem_2);
        m_itemList.add(m_FactoryHiddenItem_3);
        m_itemList.add(m_FactoryHiddenItem_4);
        m_itemList.add(m_FactoryHiddenItem_5);
        m_itemList.add(m_FactoryHiddenItem_6);

    }

    ItemStack createItem(final Material material, final String name, final String lore)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.WHITE + name);

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

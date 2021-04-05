package doubleos.deathgame.variable;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class PotionRecipe
{

    public enum Potion
    {
        GREEN_POTION,
        PINK_POTION,
        ORANGE_POTION;
    }
    static private PotionRecipe _instance = null;

    static public PotionRecipe Instance()
    {
        if(_instance == null)
        {
            _instance = new PotionRecipe();
        }
        return _instance;
    }

    ArrayList<ItemStack> m_greenPotionMaterial;
    ArrayList<ItemStack> m_pinkPotionMaterial;
    ArrayList<ItemStack> m_orangePotionMaterial;


    HashMap<PotionRecipe.Potion, ArrayList<ItemStack>> m_PotionMatrial = new HashMap();

    void initPotionRecipe()
    {
        m_PotionMatrial.put(Potion.GREEN_POTION, m_greenPotionMaterial);
        m_PotionMatrial.put(Potion.PINK_POTION, m_pinkPotionMaterial);
        m_PotionMatrial.put(Potion.ORANGE_POTION, m_orangePotionMaterial);

    }

    void setPotionRecipe()
    {
        
    }




}

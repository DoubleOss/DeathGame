package doubleos.deathgame.variable;

import org.bukkit.Material;
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

    ArrayList<ItemStack> m_greenPotionMaterial = new ArrayList<>();
    ArrayList<ItemStack> m_pinkPotionMaterial = new ArrayList<>();;
    ArrayList<ItemStack> m_orangePotionMaterial = new ArrayList<>();;




    HashMap<PotionRecipe.Potion, ArrayList<ItemStack>> m_PotionMatrial = new HashMap();


    public void initPotionRecipe()
    {
        setPotionRecipe();
        m_PotionMatrial.put(Potion.GREEN_POTION, m_greenPotionMaterial);
        m_PotionMatrial.put(Potion.PINK_POTION, m_pinkPotionMaterial);
        m_PotionMatrial.put(Potion.ORANGE_POTION, m_orangePotionMaterial);
    }

    void setPotionRecipe()
    {

        setGreenPotionRecipe();
        setPinkPotionRecipe();
        setOrangePotionRecipe();

    }

    ItemStack addPotionRecipeMaterial(Material material, int amount)
    {
        ItemStack item = new ItemStack(material, amount);
        return item;
    }

    void setGreenPotionRecipe()
    {
        m_greenPotionMaterial.add(addPotionRecipeMaterial(Material.MAGMA_CREAM, 1));
        m_greenPotionMaterial.add(addPotionRecipeMaterial(Material.SULPHUR, 1));
        m_greenPotionMaterial.add(addPotionRecipeMaterial(Material.FERMENTED_SPIDER_EYE, 1));
    }

    void setPinkPotionRecipe()
    {
        m_pinkPotionMaterial.add(addPotionRecipeMaterial(Material.BLAZE_POWDER, 1));
        m_pinkPotionMaterial.add(addPotionRecipeMaterial(Material.GLOWSTONE_DUST, 1));
        m_pinkPotionMaterial.add(addPotionRecipeMaterial(Material.ROTTEN_FLESH, 1));
    }

    void setOrangePotionRecipe()
    {
        m_orangePotionMaterial.add(addPotionRecipeMaterial(Material.BLAZE_ROD, 1));
        m_orangePotionMaterial.add(addPotionRecipeMaterial(Material.SLIME_BALL, 1));
        m_orangePotionMaterial.add(addPotionRecipeMaterial(Material.RABBIT_FOOT, 1));
    }


    public HashMap<Potion, ArrayList<ItemStack>> getPotionMatrial()
    {
        return m_PotionMatrial;
    }


}

package doubleos.deathgame.ablilty;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KillerCommon implements Listener
{

    enum KillerState
    {
        NORMAL,
        MAD;
    }
    Player m_killer;


    int m_skill2_Cooltime = 0;

    ArrayList<Location> m_skill1_pos = new ArrayList();


    public KillerCommon()
    {

    }


    @EventHandler
    void onMoveEvent(PlayerMoveEvent event)
    {
        if(GameVariable.Instance().getGameState().equals(GameVariable.GameState.PLAY))
        {
            if(event.getPlayer().equals(GameVariable.Instance().getKillerListName(event.getPlayer())) == false)
            {
                for(int i = 0; i<m_skill1_pos.size(); i++)
                {
                    //event.getPlayer().sendMessage(String.format("%f",event.getFrom().distance(m_skill1_pos.get(i))));
                    if(event.getFrom().distance(m_skill1_pos.get(i)) <= 1.5)
                    {
                        m_skill1_pos.remove(i);
                        setSkill1Effect(event.getPlayer());
                    }
                }

            }

        }

    }

    @EventHandler
    void onRightClickEvent(PlayerInteractEvent event)
    {
        if(event.getPlayer().equals(GameVariable.Instance().getKillerListName(event.getPlayer())))
        {
            if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            {

                Player player = event.getPlayer();
                ItemStack stack1 = new ItemStack(Material.FLINT);
                if(event.getPlayer().getInventory().getItemInMainHand().equals(stack1))
                {

                    player.getInventory().remove(stack1);
                    m_skill1_pos.add(event.getClickedBlock().getLocation());
                    Block block = event.getClickedBlock();
                    block.setType(Material.DIAMOND_BLOCK);


                }
            }
            if(event.getAction().equals(Action.RIGHT_CLICK_AIR))
            {
                //능력2번째
                ItemStack stack2 = new ItemStack(Material.FEATHER);
                if(event.getPlayer().getInventory().getItemInMainHand().equals(stack2))
                {
                    if (m_skill2_Cooltime <= 0)
                    {
                        event.getPlayer().sendMessage("@@@@");
                        initGlowing();
                        m_skill2_Cooltime = 20;
                        StartSkill2Cooltime();
                    }
                }
            }


        }
    }

    void initGlowing()
    {
        for (Player p : GameVariable.Instance().getGamePlayerList())
        {
            if(GameVariable.Instance().getKillerListName(p).equals(null))
            {

                PotionEffect effect = new PotionEffect(PotionEffectType.GLOWING, 60, 0);
                p.addPotionEffect(effect, true);

            }

        }

    }
    void setSkill1Effect(Player p)
    {
        PotionEffect effect1 = new PotionEffect(PotionEffectType.GLOWING, 60, 0);
        p.addPotionEffect(effect1, true);

        PotionEffect effect2 = new PotionEffect(PotionEffectType.SLOW, 100, 100);
        p.addPotionEffect(effect2, true);
        ActionBarAPI.sendActionBar(p, "당신은 덫을 밟으셨습니다.");
    }

    void StartSkill2Cooltime()
    {
        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(m_skill2_Cooltime <=0)
                {
                    this.cancel();
                }
                else
                {
                    m_skill2_Cooltime--;
                }
            }
        }.runTaskTimer(Main.instance, 0l, 20l);

    }


}

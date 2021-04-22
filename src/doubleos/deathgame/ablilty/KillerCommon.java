package doubleos.deathgame.ablilty;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameItem;
import doubleos.deathgame.variable.GameVariable;

import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;


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


    public void initCommon(Player player)
    {
        player.getInventory().addItem(GameItem.Instance().m_killerCom_Ability1_Item);
        player.getInventory().addItem(GameItem.Instance().m_killerCom_Ability2_Item);


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
                    event.getPlayer().sendMessage(String.format("%f",event.getFrom().distance(m_skill1_pos.get(i))));
                    if(event.getFrom().distance(m_skill1_pos.get(i)) <= 0.5)
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
                ItemStack stack1 = GameItem.Instance().m_killerCom_Ability1_Item;
                if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(stack1.getType()))
                {
                    player.getInventory().remove(stack1);
                    Location blockpos = event.getClickedBlock().getLocation().add( new Vector(0, 1, 0));
                    m_skill1_pos.add(blockpos);
                    blockpos.getBlock().setType(Material.CARPET);


                }
            }
            if(event.getAction().equals(Action.RIGHT_CLICK_AIR))
            {
                //탐지능력
                ItemStack stack2 = GameItem.Instance().m_killerCom_Ability2_Item;
                if(event.getPlayer().getInventory().getItemInMainHand().getType().equals((stack2.getType())))
                {
                    if (m_skill2_Cooltime <= 0)
                    {
                        initGlowing(event.getPlayer());
                        event.getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" + ChatColor.WHITE +":탐지 능력을 사용하셨습니다.");
                        m_skill2_Cooltime = 20;
                        StartSkill2Cooltime();
                        for(Player p :Bukkit.getOnlinePlayers())
                        {
                            if(p.isOp())
                            {
                                p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.WHITE + "살인마가 탐지능력을 사용 하셨습니다.");
                            }
                        }
                    }
                }
            }


        }
    }

    void initGlowing(Player viewer)
    {
        for (Player p : GameVariable.Instance().getGamePlayerList())
        {
            if(GameVariable.Instance().getKillerListName(p) == null)
            {
                GlowAPI.setGlowing(p, GlowAPI.Color.WHITE, viewer);

            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, () ->
        {
            for(Player p : GameVariable.Instance().getGamePlayerList())
            {
                GlowAPI.setGlowing(p, false, viewer);
            }
        }, 60l);

    }

    void setBerserker(Player player)
    {
        Main.instance.variablePlayer.get(player).setKillerType(PlayerVariable.KillerType.BERSERKER);
        PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, 120, 0);
        player.addPotionEffect(effect, true);
    }
    void setSkill1Effect(Player p)
    {
        PotionEffect effect2 = new PotionEffect(PotionEffectType.SLOW, 100, 100);
        p.addPotionEffect(effect2, true);
        ActionBarAPI.sendActionBar(p, "[" + ChatColor.RED + "!" + ChatColor.WHITE + "]" + ChatColor.WHITE + "당신은 덫을 밟으셨습니다.");
        for(Player player :Bukkit.getOnlinePlayers())
        {
            if(player.isOp())
            {
                player.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.RED + p.getName() + ChatColor.WHITE +"님이 덫을 밟으셨습니다.");
            }
        }

        for (Player k : GameVariable.Instance().getKillerPlayerList())
        {
            if(GameVariable.Instance().getKillerListName(p) == null)
            {
                GlowAPI.setGlowing(p, GlowAPI.Color.WHITE, k);

            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, () ->
        {
            for(Player k : GameVariable.Instance().getKillerPlayerList())
            {
                GlowAPI.setGlowing(p, false, k);
            }
        }, 100l);


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

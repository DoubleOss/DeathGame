package doubleos.deathgame.ablilty;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameItem;
import doubleos.deathgame.variable.GameVariable;

import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
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
    String m_killer;


    int m_skill2_Cooltime = 0;
    int m_skillArrowCoolTime = 0;

    //ArrayList<Location> m_skill1_pos = new ArrayList();


    public KillerCommon()
    {

    }


    public void initCommon(Player player)
    {
        player.getInventory().addItem(GameItem.Instance().m_killerCom_Ability2_Item);
        //PotionEffect effect1 = new PotionEffect(PotionEffectType.SPEED, 24000, 0);
        //player.addPotionEffect(effect1, true);
    }
    /*
    @EventHandler
    void onMoveEvent(PlayerMoveEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        if(gameVariable.getGameState().equals(GameVariable.GameState.PLAY))
        {
            if(!event.getPlayer().getName().equalsIgnoreCase(gameVariable.getKillerListName(event.getPlayer())))
            {
                for(int i = 0; i<m_skill1_pos.size(); i++)
                {
                    //event.getPlayer().sendMessage(String.format("%f",event.getFrom().distance(m_skill1_pos.get(i))));
                    if(event.getFrom().distance(m_skill1_pos.get(i)) <= 0.8)
                    {
                        Bukkit.getWorld("world").getBlockAt(m_skill1_pos.get(i)).setType(Material.AIR);
                        m_skill1_pos.remove(i);
                        setSkill1Effect(event.getPlayer());
                    }
                }

            }

        }

    }

     */

    @EventHandler
    void onRightClickEvent(PlayerInteractEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        if(gameVariable.getGameState().equals(GameVariable.GameState.PLAY))
        {
            if(gameVariable.getPlayerListVariableMap().get(event.getPlayer().getName()).getObserver())
                return;
            if(gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).getHumanType().equals(PlayerVariable.HumanType.KILLER))
            {

                Player killer = Bukkit.getPlayer(gameVariable.getKillerListName(event.getPlayer()));
                if(event.getPlayer().equals(killer))
                {
                    if (event.getHand() != EquipmentSlot.HAND)
                    {
                        return;
                    }
                    if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                    {
                        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_AXE))
                        {

                            if(m_skillArrowCoolTime <= 0)
                            {
                                shootArrow(event.getPlayer());
                            }
                            else
                                event.getPlayer().sendMessage(ChatColor.RED + "[????????? ????????????]" + ChatColor.WHITE +" ???????????? " + m_skillArrowCoolTime+ "??? ??????????????????.");

                        }
                    }

                    /*
                    if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                    {
                        ItemStack stack1 = GameItem.Instance().m_killerCom_Ability1_Item;
                        stack1.setAmount(1);
                        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(stack1.getType()))
                        {
                            event.getPlayer().getInventory().removeItem(stack1);
                            Location blockpos = event.getClickedBlock().getLocation().add( new Vector(0, 1, 0));
                            m_skill1_pos.add(blockpos);
                            blockpos.getBlock().setType(Material.RAILS);
                        }
                    }
                     */
                    if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                    {
                        //????????????
                        ItemStack stack2 = GameItem.Instance().m_killerCom_Ability2_Item;
                        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals((stack2.getType())))
                        {
                            if (m_skill2_Cooltime <= 0)
                            {
                                initGlowing(event.getPlayer());
                                event.getPlayer().sendMessage(ChatColor.RED + "[????????? ????????????]" + ChatColor.WHITE +" ?????? ????????? ?????????????????????.");
                                m_skill2_Cooltime = 60;
                                StartSkill2Cooltime();
                                for(Player p :Bukkit.getOnlinePlayers())
                                {
                                    if(p.isOp())
                                    {
                                        p.sendMessage(ChatColor.GOLD + "[??????] "+ ChatColor.WHITE + " ???????????? ??????????????? ?????? ???????????????.");
                                    }
                                }
                            }
                            else
                            {
                                event.getPlayer().sendMessage(ChatColor.RED + "[????????? ????????????]" + ChatColor.WHITE +" ???????????? " + m_skill2_Cooltime+ "??? ??????????????????.");
                            }
                        }
                    }

                }
            }

        }

    }

    void initGlowing(Player viewer)
    {
        for (String stringPlayer : GameVariable.Instance().getGamePlayerList())
        {
            Player p = Bukkit.getPlayer(stringPlayer);
            if(GameVariable.Instance().getPlayerVariableMap().get(stringPlayer).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
            {
                GlowAPI.setGlowing(p, GlowAPI.Color.WHITE, viewer);
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, () ->
        {
            for(String stringPlayer : GameVariable.Instance().getGamePlayerList())
            {
                Player p = Bukkit.getPlayer(stringPlayer);
                GlowAPI.setGlowing(p, false, viewer);
            }
        }, 60l);

    }


    /*
    void setSkill1Effect(Player p)
    {
        PotionEffect effect2 = new PotionEffect(PotionEffectType.SLOW, 100, 100);
        p.addPotionEffect(effect2, true);
        ActionBarAPI.sendActionBar(p, "[" + ChatColor.RED + "!" + ChatColor.WHITE + "]" + ChatColor.WHITE + " ????????? ?????? ??????????????????.");
        for(Player player :Bukkit.getOnlinePlayers())
        {
            if(player.isOp())
            {
                player.sendMessage(ChatColor.GOLD + "[??????] "+ ChatColor.RED + p.getName() + ChatColor.WHITE +" ?????? ?????? ??????????????????.");
            }
        }

        for (String String_killer : GameVariable.Instance().getKillerPlayerList())
        {
            if(GameVariable.Instance().getPlayerListVariableMap().get(String_killer).getHumanType().equals(PlayerVariable.HumanType.KILLER))
            {
                Player k = Bukkit.getPlayer(String_killer);
                GlowAPI.setGlowing(p, GlowAPI.Color.WHITE, k);

            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, () ->
        {
            for(String stringKiller : GameVariable.Instance().getKillerPlayerList())
            {
                if(GameVariable.Instance().getPlayerListVariableMap().get(stringKiller).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                {
                    Player k = Bukkit.getPlayer(stringKiller);
                    GlowAPI.setGlowing(p, false, k);
                }

            }
        }, 100l);

    }

     */

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


    void shootArrow(Player p)
    {
        Arrow arrow = p.launchProjectile(Arrow.class);

        arrow.setShooter(p);
        arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);

        arrow.setVelocity(p.getLocation().getDirection().normalize().multiply(2));
        m_skillArrowCoolTime = 10;

        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(m_skillArrowCoolTime <= 0)
                {
                    this.cancel();
                }
                else
                {
                    m_skillArrowCoolTime--;
                }
            }
        }.runTaskTimer(Main.instance, 0l, 20l);

    }





}

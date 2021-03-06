package doubleos.deathgame.ablilty;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameItem;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class KillerHidden3 implements Listener , Hidden
{

    int m_skill1Cooltime = 0;
    int m_skill2Cooltime = 0;

    boolean m_skill1Active = false;


    String m_killerName;

    public int m_hiddenAbliltyTime = 120;


    public void initKillerHidden3()
    {
        this.m_killerName = GameVariable.Instance().getOrignalKillerPlayer().getName();
        m_hiddenAbliltyTime = 120;

        m_skill1Cooltime = 0;
        m_skill2Cooltime = 0;
        Player killer = Bukkit.getPlayer(this.m_killerName);
        GameVariable.Instance().addKillerHiddenClass(killer, this);

        killer.getInventory().addItem(GameItem.Instance().m_killerHidden3_Ability1_Item);
        killer.getInventory().addItem(GameItem.Instance().m_killerHidden3_Ability2_Item);

    }

    @EventHandler
    void hidden3OnDamageEvent(EntityDamageByEntityEvent event)
    {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player)
        {
            if(event.getDamager().equals(GameVariable.Instance().getOrignalKillerPlayer()))
            {
                if(m_skill1Active == true)
                {
                    event.setCancelled(true);
                    event.getDamager().sendMessage(ChatColor.RED + "[????????? ????????????]" + ChatColor.WHITE +" ?????? ??????????????? ???????????? ??? ??? ????????????.");
                }
            }
        }
    }
    @EventHandler
    void hidden3RightClickEvent(PlayerInteractEvent event)
    {
        MissionManager mission = MissionManager.Instance();
        GameVariable gameVariable = GameVariable.Instance();
        if(gameVariable.getGameState().equals(GameVariable.GameState.PLAY))
        {
            if(gameVariable.getPlayerListVariableMap().get(event.getPlayer().getName()).getObserver())
                return;
            if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            {
                if (event.getHand() != EquipmentSlot.HAND)
                {
                    return;
                }
                if(event.getPlayer().equals(GameVariable.Instance().getOrignalKillerPlayer()))
                {
                    ItemStack stack1  = GameItem.Instance().m_killerHidden3_Ability1_Item;
                    if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(stack1.getType()))
                    {
                        if(m_skill1Active == false)
                        {
                            if(m_skill1Cooltime <= 0)
                            {
                                setInvisible(event.getPlayer());
                            }
                            else
                            {
                                event.getPlayer().sendMessage(ChatColor.RED + "[????????? ????????????]" + ChatColor.WHITE +" ???????????? " + m_skill1Cooltime+ "??? ??????????????????.");
                            }
                        }
                        else if(m_skill1Active == true)
                        {
                            removeInvisible(event.getPlayer());
                        }
                    }
                    ItemStack stack2  = GameItem.Instance().m_killerHidden3_Ability2_Item;
                    if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(stack2.getType()))
                    {
                        if(!m_skill1Active)
                        {
                            if(m_skill2Cooltime <= 0)
                            {
                                shootSnowBall(event.getPlayer());
                            }
                            else
                            {
                                event.getPlayer().sendMessage(ChatColor.RED + "[????????? ????????????]" + ChatColor.WHITE +" ???????????? " + m_skill2Cooltime+ "??? ??????????????????.");

                            }
                        }
                        else
                        {
                            event.getPlayer().sendMessage(ChatColor.RED + "[????????? ????????????]" + ChatColor.WHITE +" ??????????????? ?????? ??? ??? ????????????.");
                        }

                    }
                }
            }
        }

    }

    @EventHandler
    void onProjectileEvent(ProjectileHitEvent event)
    {
        if(event.getEntity().getShooter().equals(GameVariable.Instance().getOrignalKillerPlayer()))
        {
            if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.FACTORY))
            {
                if(event.getEntity() instanceof Snowball)
                {
                    PotionEffect effect1 = new PotionEffect(PotionEffectType.SLOW, 40, 2);
                    if(event.getHitEntity() instanceof Player)
                    {
                        ((Player) event.getHitEntity()).addPotionEffect(effect1, true);
                        ((Player) event.getHitEntity()).getPlayer().sendMessage("????????? ????????? ????????? ?????? ?????? ????????? ????????????.");
                        ((Player) event.getHitEntity()).getKiller().sendMessage(ChatColor.GOLD + "[????????? ????????????] "+ ChatColor.RED+ ((Player) event.getHitEntity()).getPlayer().getName() +
                                ChatColor.WHITE+ " ????????? ????????? ?????? ?????? ????????? ????????????.");

                        for(Player p :Bukkit.getOnlinePlayers())
                        {
                            if(GameVariable.Instance().getPlayerListVariableMap().get(p.getName()).getObserver())
                            {
                                p.sendMessage(ChatColor.GOLD + "[??????] "+ ChatColor.RED+ ((Player) event.getHitEntity()).getPlayer().getName() +
                                        ChatColor.WHITE+ " ????????? ????????? ?????? ?????? ????????? ????????????.");
                            }
                        }
                    }
                }
            }
        }
    }

    void shootSnowBall(Player p)
    {
        Snowball snowball = p.launchProjectile(Snowball.class);

        snowball.setShooter(p);

        snowball.setVelocity(p.getLocation().getDirection().normalize().multiply(2));
        GameVariable gameVariable = GameVariable.Instance();
        if(gameVariable.getPlayerVariableMap().get(p.getName()).getKillerType().equals(PlayerVariable.KillerType.BERSERKER))
        {
            m_skill2Cooltime = 25;
        }
        else
            m_skill2Cooltime = 50;

        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(m_skill2Cooltime <= 0)
                {
                    this.cancel();
                }
                else
                {
                    m_skill2Cooltime--;
                }
            }
        }.runTaskTimer(Main.instance, 0l, 20l);

    }


    void setInvisible(Player p)
    {
        m_skill1Active = true;
        PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 0);
        p.addPotionEffect(effect, true);
        GameVariable gameVariable = GameVariable.Instance();
        p.sendMessage(ChatColor.RED + "[????????? ????????????]"+ ChatColor.WHITE+ " ????????? ?????? ???????????????.");
        if(gameVariable.getPlayerVariableMap().get(p.getName()).getKillerType().equals(PlayerVariable.KillerType.BERSERKER))
        {
            m_skill2Cooltime = 10;
        }
        else
            m_skill1Cooltime = 20;
        for(Player player :Bukkit.getOnlinePlayers())
        {
            if(GameVariable.Instance().getPlayerListVariableMap().get(player.getName()).getObserver())
            {
                player.sendMessage(ChatColor.GOLD + "[??????] "+ ChatColor.WHITE + "???????????? " + ChatColor.RED +p.getPlayer().getName()+ ChatColor.WHITE + " ?????? ????????? ?????????????????????.");
            }
        }
        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(m_skill1Cooltime<=0)
                {
                    m_skill1Cooltime = 0;
                    this.cancel();
                }
                else
                {
                    m_skill1Cooltime--;
                }
            }
        }.runTaskTimer(Main.instance, 0l, 20l);

    }

    void removeInvisible(Player p)
    {
        m_skill1Active = false;
        p.removePotionEffect(PotionEffectType.INVISIBILITY);
        PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 40, 0);
        p.addPotionEffect(effect, true);
        p.sendMessage(ChatColor.RED + "[????????? ????????????]"+ ChatColor.WHITE+ " ????????? ?????? ???????????????.");
        for(Player player :Bukkit.getOnlinePlayers())
        {
            if(player.isOp())
            {
                player.sendMessage(ChatColor.GOLD + "[??????] "+ ChatColor.WHITE + "???????????? " + ChatColor.RED +p.getPlayer().getName()+ ChatColor.WHITE + " ?????? ????????? ?????? ???????????????.");
            }
        }
    }
}

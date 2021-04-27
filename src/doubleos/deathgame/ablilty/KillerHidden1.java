package doubleos.deathgame.ablilty;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import doubleos.deathgame.Main;
import doubleos.deathgame.util.Utils;
import doubleos.deathgame.variable.GameItem;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.*;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class KillerHidden1 implements Listener, Hidden
{

    String m_killerName;

    int m_skill1Cooltime = 0;
    int m_skill2Cooltime = 0;


    public int m_hiddenAbliltyTime = 120;

    public void initKillerHidden1()
    {
        this.m_killerName = GameVariable.Instance().getOrignalKillerPlayer().getName();
        Player killer = Bukkit.getPlayer(m_killerName);
        m_hiddenAbliltyTime = 120;
        m_skill1Cooltime = 0;

        killer.getInventory().addItem(GameItem.Instance().m_killerHidden1_Ability1_Item);
        killer.getInventory().addItem(GameItem.Instance().m_killerHidden1_Ability2_Item);

        GameVariable.Instance().addKillerHiddenClass(killer, this);

        ItemStack helmet = new ItemStack(Material.PUMPKIN);
        ItemStack air = new ItemStack(Material.AIR);

        killer.getInventory().setHelmet(helmet);
        for(Player p :Bukkit.getOnlinePlayers())
        {
            if(p.isOp())
            {
                p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.RED+ killer.getName() + ChatColor.WHITE+ " 님이 변신 하였습니다.");
            }
        }

        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(m_hiddenAbliltyTime <= 0)
                {
                    killer.getInventory().setHelmet(air);
                    killer.sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE +": 변신이 풀렸습니다!");
                    killer.getInventory().remove(GameItem.Instance().m_killerHidden1_Ability1_Item);
                    killer.getInventory().remove(GameItem.Instance().m_killerHidden1_Ability2_Item);

                    m_hiddenAbliltyTime = 0;
                    GameVariable.Instance().setMissionRotateNumber(GameVariable.Instance().getMissionRotateNumber()+1);
                    GameVariable.Instance().setMissionRotate();
                    GameVariable.Instance().setIsKillerCheckTras(false);
                    GameVariable.Instance().getKillerHiddenClass().remove(killer.getName());
                    for(Player p :Bukkit.getOnlinePlayers())
                    {
                        if(p.isOp())
                        {
                            p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.RED+ killer.getName() + ChatColor.WHITE+ " 님이 변신이 풀렸습니다.");
                        }
                    }
                    this.cancel();
                }
                else
                {
                    if(!GameVariable.Instance().getGameState().equals(GameVariable.GameState.PAUSE) &&
                            !GameVariable.Instance().getPlayerVariableMap().get(killer.getName()).getKillerType().equals(PlayerVariable.KillerType.BERSERKER))
                    {
                        m_hiddenAbliltyTime--;
                    }

                }
            }
        }.runTaskTimer(Main.instance, 0l, 20l);
    }


    @EventHandler
    void hidden1RightClickEvent(PlayerInteractEvent event)
    {
        MissionManager mission = MissionManager.Instance();
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR))
        {
            if(event.getPlayer().getName().equals(GameVariable.Instance().getOrignalKillerPlayer()))
            {
                if(GameVariable.Instance().getIsKillerCheckTras() == true)
                {
                    ItemStack stack1 = GameItem.Instance().m_killerHidden1_Ability1_Item;
                    if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(stack1.getType()))
                    {
                        if(mission.getMission1Success() == true && mission.getMission2Success() == true)
                        {
                            randomLocation(event.getPlayer());
                        }
                    }
                    ItemStack stack2  = GameItem.Instance().m_killerHidden1_Ability2_Item;
                    if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(stack2.getType()))
                    {
                        if(mission.getMission1Success() == true && mission.getMission2Success() == true)
                        {

                            if(m_skill2Cooltime <= 0)
                            {
                                shootEgg(event.getPlayer());
                            }

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
            if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.LAB))
            {
                if(event.getEntity() instanceof Egg)
                {
                    PotionEffect effect1 = new PotionEffect(PotionEffectType.POISON, 100, 0);
                    PotionEffect effect2 = new PotionEffect(PotionEffectType.CONFUSION, 100, 0);
                    if(event.getHitEntity() instanceof Player)
                    {
                        ((Player) event.getHitEntity()).getPlayer().damage(4);
                        ((Player) event.getHitEntity()).addPotionEffect(effect1, true);
                        ((Player) event.getHitEntity()).addPotionEffect(effect2, true);
                        ((Player) event.getHitEntity()).getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + "당신은 실험체의 위산 분비 공격에 당해 독과 멀미에 걸립니다.");
                        for(Player p :Bukkit.getOnlinePlayers())
                        {
                            if(p.isOp())
                            {
                                p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.RED+ ((Player) event.getHitEntity()).getPlayer().getName() + ChatColor.WHITE+ " 위산 분비 공격에 당해 독과 멀미에 걸립니다.");
                            }
                        }
                    }
                }
            }

        }
    }

    void shootEgg(Player p)
    {
        Egg egg = p.launchProjectile(Egg.class);

        egg.setShooter(p);

        egg.setVelocity(p.getLocation().getDirection().normalize().multiply(2));
        m_skill2Cooltime = 30;

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

    void randomLocation(Player p)
    {
        m_skill1Cooltime = 120;
        Utils.Instance().randomTeleport(p);

        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(m_skill1Cooltime <= 0)
                {
                    this.cancel();
                }
                else
                {
                    m_skill1Cooltime--;
                }
            }
        }.runTaskTimer(Main.instance, 0l, 20l);
    }

}

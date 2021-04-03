package doubleos.deathgame.ablilty;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
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

public class KillerHidden1 implements Listener
{

    Player m_killerName;

    int m_skill1Cooltime = 0;
    int m_skill2Cooltime = 0;


    int m_hiddenAbliltyTime = 120;

    public void initKillerHidden1()
    {
        this.m_killerName = GameVariable.Instance().getKillerName();
        m_hiddenAbliltyTime = 120;
        m_skill1Cooltime = 0;

        ItemStack helmet = new ItemStack(Material.PUMPKIN);
        ItemStack air = new ItemStack(Material.AIR);

        m_killerName.getInventory().setHelmet(helmet);

        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(m_hiddenAbliltyTime <= 0)
                {
                    m_killerName.getInventory().setHelmet(air);
                    m_killerName.sendMessage("변신이 풀렸습니다!");
                    this.cancel();
                }
                else
                {
                    m_hiddenAbliltyTime--;
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
            if(event.getPlayer().equals(GameVariable.Instance().getKillerName()))
            {
                if(m_hiddenAbliltyTime != 0)
                {
                    ItemStack stack1 = new ItemStack(Material.STRING);
                    if(event.getPlayer().getInventory().getItemInMainHand().equals(stack1))
                    {
                        if(mission.getMission1Success() == true && mission.getMission2Success() == true)
                        {
                            randomLocation(event.getPlayer());
                        }
                    }
                    ItemStack stack2  = new ItemStack(Material.CLAY_BALL);
                    if(event.getPlayer().getInventory().getItemInMainHand().equals(stack2))
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
        if(event.getEntity().getShooter().equals(GameVariable.Instance().getKillerName()))
        {
            PotionEffect effect1 = new PotionEffect(PotionEffectType.POISON, 100, 0);
            PotionEffect effect2 = new PotionEffect(PotionEffectType.CONFUSION, 100, 0);
            if(event.getHitEntity() instanceof Player)
            {
                ((Player) event.getHitEntity()).getPlayer().damage(4);
                ((Player) event.getHitEntity()).addPotionEffect(effect1, true);
                ((Player) event.getHitEntity()).addPotionEffect(effect2, true);
                ((Player) event.getHitEntity()).getPlayer().sendMessage("당신은 실험체의 위산 분비 공격에 당해 독과 멀미에 걸립니다.");


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
        if(MissionManager.Instance().getActiveMission().equals(MissionManager.ActiveMission.MISSION1))
        {

        }
        else if (MissionManager.Instance().getActiveMission().equals(MissionManager.ActiveMission.MISSION2))
        {

        }
        else if (MissionManager.Instance().getActiveMission().equals(MissionManager.ActiveMission.MISSION3))
        {

        }
        else if (MissionManager.Instance().getActiveMission().equals(MissionManager.ActiveMission.MISSION4))
        {

        }

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

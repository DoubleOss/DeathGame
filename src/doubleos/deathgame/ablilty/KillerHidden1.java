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
import org.bukkit.inventory.EquipmentSlot;
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

import java.util.*;

public class KillerHidden1 implements Listener, Hidden
{

    String m_killerName;

    int m_skill1Cooltime = 0;
    int m_skill2Cooltime = 0;


    public int m_hiddenAbliltyTime = 120;

    public void initKillerHidden1()
    {
        GameVariable gameVariable = GameVariable.Instance();
        this.m_killerName = GameVariable.Instance().getOrignalKillerPlayer().getName();
        Player killer = Bukkit.getPlayer(m_killerName);
        m_hiddenAbliltyTime = 120;
        m_skill1Cooltime = 0;

        killer.getInventory().addItem(GameItem.Instance().m_killerHidden1_Ability1_Item);
        killer.getInventory().addItem(GameItem.Instance().m_killerHidden1_Ability2_Item);

        gameVariable.addKillerHiddenClass(killer, this);

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
                if(m_hiddenAbliltyTime <= 0 || gameVariable.getGameState().equals(GameVariable.GameState.END))
                {
                    killer.getInventory().setHelmet(air);
                    killer.sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE +" 변신이 풀렸습니다!");
                    killer.getInventory().remove(GameItem.Instance().m_killerHidden1_Ability1_Item);
                    killer.getInventory().remove(GameItem.Instance().m_killerHidden1_Ability2_Item);
                    m_hiddenAbliltyTime = 0;
                    if(gameVariable.getGameState().equals(GameVariable.GameState.END))
                    {
                        gameVariable.setMissionRotateNumber(gameVariable.getMissionRotateNumber()+1);
                        gameVariable.setMissionRotate();
                        gameVariable.setIsKillerCheckTras(false);
                        gameVariable.getKillerHiddenClass().remove(killer.getName());
                        gameVariable.getPlayerVariableMap().get(m_killerName).setKillerType(PlayerVariable.KillerType.COMMON);
                        MissionManager.Instance().resetMissionBox();
                        for(Player p :Bukkit.getOnlinePlayers())
                        {
                            if(p.isOp())
                            {
                                p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.RED+ killer.getName() + ChatColor.WHITE+ " 님의 변신이 풀렸습니다.");
                            }
                        }
                    }
                    this.cancel();
                }
                else
                {
                    if(!gameVariable.getGameState().equals(GameVariable.GameState.PAUSE) &&
                            !gameVariable.getPlayerVariableMap().get(killer.getName()).getKillerType().equals(PlayerVariable.KillerType.BERSERKER))
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
        GameVariable gameVariable = GameVariable.Instance();
        if(gameVariable.getGameState().equals(GameVariable.GameState.PLAY))
        {
            if(gameVariable.getPlayerListVariableMap().get(event.getPlayer().getName()).getObserver())
                return;
            if(event.getAction().equals(Action.RIGHT_CLICK_AIR))
            {
                if (event.getHand() != EquipmentSlot.HAND)
                {
                    return;
                }
                if(event.getPlayer().equals(gameVariable.getOrignalKillerPlayer()))
                {
                    if(gameVariable.getIsKillerCheckTras() == true)
                    {
                        ItemStack stack1 = GameItem.Instance().m_killerHidden1_Ability1_Item;
                        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(stack1.getType()))
                        {
                            if(gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).getKillerType().equals(PlayerVariable.KillerType.BERSERKER)
                                    ||(mission.getMission1Success() == true && mission.getMission2Success() == true))
                            {
                                if(m_skill1Cooltime <= 0)
                                    randomLocation(event.getPlayer());
                                else
                                {
                                    event.getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" + ChatColor.WHITE +" 쿨타임이 " + m_skill1Cooltime+ "초 남으셨습니다.");
                                }
                            }
                        }
                        ItemStack stack2  = GameItem.Instance().m_killerHidden1_Ability2_Item;
                        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(stack2.getType()))
                        {
                            if(gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).getKillerType().equals(PlayerVariable.KillerType.BERSERKER)
                                    ||(mission.getMission1Success() == true && mission.getMission2Success() == true))
                            {

                                if(m_skill2Cooltime <= 0)
                                {
                                    shootEgg(event.getPlayer());
                                }
                                else
                                    event.getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" + ChatColor.WHITE +" 쿨타임이 " + m_skill2Cooltime+ "초 남으셨습니다.");

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
                        ((Player) event.getHitEntity()).getPlayer().damage(4, GameVariable.Instance().getOrignalKillerPlayer());
                        ((Player) event.getHitEntity()).addPotionEffect(effect1, true);
                        ((Player) event.getHitEntity()).addPotionEffect(effect2, true);
                        ((Player) event.getHitEntity()).getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + " 당신은 실험체의 위산 분비 공격에 당해 독과 멀미에 걸립니다.");
                        ((Player) event.getHitEntity()).getKiller().sendMessage(ChatColor.GOLD + "[죽음의 술래잡기]"+ ChatColor.RED+ ((Player) event.getHitEntity()).getPlayer().getName() + ChatColor.WHITE+ " 님이 위산 분비 공격에 당해 독과 멀미에 걸립니다.");

                        for(Player p :Bukkit.getOnlinePlayers())
                        {
                            if(p.isOp())
                            {
                                p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.RED+ ((Player) event.getHitEntity()).getPlayer().getName() + ChatColor.WHITE+ " 님이 위산 분비 공격에 당해 독과 멀미에 걸립니다.");
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
        m_skill1Cooltime = 60;
        radnomTeleport(p);

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

    void radnomTeleport(Player p)
    {

        ArrayList<Location> teleportloc = new ArrayList<>();

        teleportloc.add(new Location(Bukkit.getWorld("world"), -338, 62, 10));
        teleportloc.add(new Location(Bukkit.getWorld("world"), -361, 71, -34));
        teleportloc.add(new Location(Bukkit.getWorld("world"), -319, 71, 23));
        teleportloc.add(new Location(Bukkit.getWorld("world"), -323, 79, -15));
        teleportloc.add(new Location(Bukkit.getWorld("world"), -362, 79, -28));
        teleportloc.add(new Location(Bukkit.getWorld("world"), -339, 62, -28));
        teleportloc.add(new Location(Bukkit.getWorld("world"), -357, 71, -10));

        int random = new Random().nextInt((6-1)+1);
        p.teleport(teleportloc.get(random));

    }

}

package doubleos.deathgame.ablilty;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameItem;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import org.bukkit.Bukkit;
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


    Player m_killerName;

    public int m_hiddenAbliltyTime = 120;


    public void initKillerHidden3()
    {
        this.m_killerName = GameVariable.Instance().getOrignalKillerPlayer();
        m_hiddenAbliltyTime = 120;

        m_skill1Cooltime = 0;
        m_skill2Cooltime = 0;
        GameVariable.Instance().addKillerHiddenClass(this.m_killerName, this);

        m_killerName.getInventory().addItem(GameItem.Instance().m_killerHidden3_Ability1_Item);
        m_killerName.getInventory().addItem(GameItem.Instance().m_killerHidden3_Ability2_Item);


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
                    m_killerName.getInventory().remove(GameItem.Instance().m_killerHidden3_Ability1_Item);
                    m_killerName.getInventory().remove(GameItem.Instance().m_killerHidden3_Ability2_Item);

                    GameVariable.Instance().setMissionRotateNumber(GameVariable.Instance().getMissionRotateNumber()+1);
                    GameVariable.Instance().setMissionRotate();
                    GameVariable.Instance().setIsKillerCheckTras(false);
                    GameVariable.Instance().getKillerHiddenClass().remove(m_killerName);
                    this.cancel();
                }
                if(!GameVariable.Instance().getGameState().equals(GameVariable.GameState.PAUSE))
                {
                    m_hiddenAbliltyTime--;
                }
            }
        }.runTaskTimer(Main.instance, 0l, 20l);
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
                    event.getDamager().sendMessage("은신 상태에서는 데미지를 줄 수 없습니다.");
                }
            }
        }
    }
    @EventHandler
    void hidden3RightClickEvent(PlayerInteractEvent event)
    {
        MissionManager mission = MissionManager.Instance();
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR))
        {
            if(event.getPlayer().equals(GameVariable.Instance().getOrignalKillerPlayer()))
            {
                if(GameVariable.Instance().getIsKillerCheckTras() == true)
                {
                    ItemStack stack1  = GameItem.Instance().m_killerHidden3_Ability1_Item;
                    if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(stack1.getType()))
                    {
                        if(mission.getMission1Success() == true && mission.getMission2Success() == true)
                        {
                            if(m_skill1Active == false)
                            {
                                if(m_skill1Cooltime <= 0)
                                {
                                    m_skill1Cooltime = 20;
                                    setInvisible(event.getPlayer());
                                }
                            }
                            else if(m_skill1Active == true)
                            {
                                removeInvisible(event.getPlayer());
                            }

                        }
                    }
                    ItemStack stack2  = GameItem.Instance().m_killerHidden3_Ability2_Item;
                    if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(stack2.getType()))
                    {
                        if(mission.getMission1Success() == true && mission.getMission2Success() == true)
                        {
                            if(m_skill2Cooltime <= 0)
                            {
                                shootSnowBall(event.getPlayer());
                            }
                            else
                            {
                                event.getPlayer().sendMessage("아직 스킬을 사용할 수 없습니다.");

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
            if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.FACTORY))
            {
                if(event.getEntity() instanceof Snowball)
                {
                    PotionEffect effect1 = new PotionEffect(PotionEffectType.SLOW, 40, 0);
                    if(event.getHitEntity() instanceof Player)
                    {
                        ((Player) event.getHitEntity()).addPotionEffect(effect1, true);
                        ((Player) event.getHitEntity()).getPlayer().sendMessage("당신은 인형의 눈알에 맞아 잠시 구속에 걸립니다.");
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


    void setInvisible(Player p)
    {
        m_skill1Active = true;
        PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 0);
        p.addPotionEffect(effect, true);
        p.hidePlayer(Main.instance, p);



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
    }
}

package doubleos.deathgame.ablilty;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class KillerHidden2 implements Listener , Hidden
{

    int m_skill1Cooltime = 0;

    int m_skill2Cooltime = 0;

    public int m_hiddenAbliltyTime = 120;

    Player m_killerName;


    public void initKillerHidden2()
    {
        this.m_killerName = GameVariable.Instance().getOrignalKillerPlayer();
        m_hiddenAbliltyTime = 120;

        m_skill1Cooltime = 0;
        m_skill2Cooltime = 0;
        GameVariable.Instance().addKillerHiddenClass(this.m_killerName, this);

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
    void hidden2RightClickEvent(PlayerInteractEvent event)
    {
        MissionManager mission = MissionManager.Instance();
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR))
        {
            if(GameVariable.Instance().getIsKillerCheckTras() == true)
            {
                if(event.getPlayer().equals(GameVariable.Instance().getOrignalKillerPlayer()))
                {
                    ItemStack stack1  = new ItemStack(Material.CLAY_BRICK);
                    if(event.getPlayer().getInventory().getItemInMainHand().equals(stack1))
                    {
                        if(mission.getMission1Success() == true && mission.getMission2Success() == true)
                        {
                            if(m_skill1Cooltime <= 0)
                            {
                                m_skill1Cooltime = 30;
                                flash(event.getPlayer());
                            }

                        }
                    }
                    ItemStack stack2  = new ItemStack(Material.BOOK);
                    if(event.getPlayer().getInventory().getItemInMainHand().equals(stack2))
                    {
                        if(mission.getMission1Success() == true && mission.getMission2Success() == true)
                        {
                            if(m_skill2Cooltime <= 0)
                            {
                                GameVariable.Instance().setKillerHidden2(this);
                                Bukkit.dispatchCommand(event.getPlayer(), "죽술 전도");
                            }

                        }
                    }
                }
            }

        }
    }

    void flash(Player p)
    {
        Vector unitVector = new Vector(p.getLocation().getDirection().getX(), 0, p.getLocation().getDirection().getZ());

        unitVector = unitVector.normalize();
        p.setVelocity(unitVector.multiply(5));
        PotionEffect effect2 = new PotionEffect(PotionEffectType.SLOW, 40, 100);
        p.addPotionEffect(effect2, true);

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


    public int getSkill2Cooltime()
    {
        return m_skill2Cooltime;
    }
}

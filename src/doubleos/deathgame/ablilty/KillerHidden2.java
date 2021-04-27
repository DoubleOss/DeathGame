package doubleos.deathgame.ablilty;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameItem;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    String m_killerName;


    public void initKillerHidden2()
    {
        this.m_killerName = GameVariable.Instance().getOrignalKillerPlayer().getName();
        m_hiddenAbliltyTime = 120;

        m_skill1Cooltime = 0;
        m_skill2Cooltime = 0;
        Player killer = Bukkit.getPlayer(this.m_killerName);
        GameVariable.Instance().addKillerHiddenClass(killer, this);

        killer.getInventory().addItem(GameItem.Instance().m_killerHidden2_Ability1_Item);
        killer.getInventory().addItem(GameItem.Instance().m_killerHidden2_Ability2_Item);


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
        GameVariable gamevariable = GameVariable.Instance();
        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(m_hiddenAbliltyTime <= 0)
                {
                    killer.getInventory().setHelmet(air);
                    killer.sendMessage(ChatColor.RED + "[죽음의 술래잡기]" + ChatColor.WHITE +": 변신이 풀렸습니다!");
                    killer.getInventory().remove(GameItem.Instance().m_killerHidden2_Ability1_Item);
                    killer.getInventory().remove(GameItem.Instance().m_killerHidden2_Ability2_Item);

                    gamevariable.setMissionRotateNumber(GameVariable.Instance().getMissionRotateNumber()+1);
                    gamevariable.setMissionRotate();
                    gamevariable.setIsKillerCheckTras(false);
                    gamevariable.getKillerHiddenClass().remove(killer.getName());
                    for(Player p :Bukkit.getOnlinePlayers())
                    {
                        if(p.isOp())
                        {
                            p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.RED+ killer.getName() + ChatColor.WHITE+ " 님이 변신이 풀렸습니다.");
                        }
                    }
                    this.cancel();
                }
                if(!gamevariable.getGameState().equals(GameVariable.GameState.PAUSE)&&
                        !gamevariable.getPlayerVariableMap().get(killer.getName()).getKillerType().equals(PlayerVariable.KillerType.BERSERKER))
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
        GameVariable gamevariable = GameVariable.Instance();
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR))
        {
            if(gamevariable.getIsKillerCheckTras() == true)
            {
                if(event.getPlayer().equals(gamevariable.getOrignalKillerPlayer()))
                {
                    ItemStack stack1 = GameItem.Instance().m_killerHidden2_Ability1_Item;
                    if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(stack1.getType()))
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
                    ItemStack stack2 = GameItem.Instance().m_killerHidden2_Ability2_Item;
                    if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(stack2.getType()))
                    {
                        if(mission.getMission1Success() == true && mission.getMission2Success() == true)
                        {
                            if(m_skill2Cooltime <= 0)
                            {
                                gamevariable.setKillerHidden2(this);
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

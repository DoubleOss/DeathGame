package doubleos.deathgame.ablilty;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameItem;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;

public class HumanCommon implements Listener
{

    @EventHandler
    void onMoveEvent(PlayerMoveEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        String stringPlayer = event.getPlayer().getName();
        if(gameVariable.getGameState().equals(GameVariable.GameState.PLAY))
        {
            if(gameVariable.getPlayerVariableMap().get(stringPlayer) != null)
            {
                if(gameVariable.getPlayerVariableMap().get(stringPlayer).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                {
                    for(int i = 0; i<gameVariable.getHumanTrapLocList().size(); i++)
                    {
                        //event.getPlayer().sendMessage(String.format("%f",event.getFrom().distance(m_skill1_pos.get(i))));
                        if(event.getFrom().distance(gameVariable.getHumanTrapLocList().get(i)) <= 0.8)
                        {
                            Bukkit.getWorld("world").getBlockAt(gameVariable.getHumanTrapLocList().get(i)).setType(Material.AIR);
                            gameVariable.getHumanTrapLocList().remove(i);
                            setSkill1Effect(event.getPlayer());
                        }
                    }

                }
            }

        }

    }
    @EventHandler
    void onRightClickEvent(PlayerInteractEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        if (gameVariable.getGameState().equals(GameVariable.GameState.PLAY))
        {
            if (gameVariable.getPlayerListVariableMap().get(event.getPlayer().getName()).getObserver())
                return;
            if (gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
            {
                if (event.getHand() != EquipmentSlot.HAND)
                {
                    return;
                }
                if(!gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).getRepair())
                {
                    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                    {
                        if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.FLINT))
                        {
                            GameItem.Instance().m_humanCom_Ability1_Item.setAmount(1);
                            event.getPlayer().getInventory().removeItem(GameItem.Instance().m_humanCom_Ability1_Item);
                            Location blockpos = event.getClickedBlock().getLocation().add(new Vector(0, 1, 0));
                            gameVariable.getHumanTrapLocList().add(blockpos);
                            blockpos.getBlock().setType(Material.RAILS);
                        }
                    }
                    if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR))
                    {
                        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.MAGMA_CREAM))
                        {
                            if(!gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).getHealKit())
                            {
                                event.getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" + ChatColor.WHITE +" 회복상자를 한번더 클릭하면 회복을 취소 할 수 있습니다.");
                                gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).setHealKit(true);
                                HealKit healKit = new HealKit();
                                healKit.initHealKit(event.getPlayer());
                            }
                            else
                            {
                                gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).setHealKit(false);
                                event.getPlayer().setWalkSpeed(0.2f);
                                event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "false").getBytes());
                            }
                        }
                    }
                }




            }
        }
    }

    void setSkill1Effect(Player p)
    {
        PotionEffect effect2 = new PotionEffect(PotionEffectType.SLOW, 100, 100);
        p.addPotionEffect(effect2, true);
        ActionBarAPI.sendActionBar(p, "[" + ChatColor.RED + "!" + ChatColor.WHITE + "]" + ChatColor.WHITE + " 당신은 덫을 밟으셨습니다.");
        for(Player player :Bukkit.getOnlinePlayers())
        {
            if(player.isOp())
            {
                player.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.RED + p.getName() + ChatColor.WHITE +" 님이 덫을 밟으셨습니다.");
            }
        }

    }

}

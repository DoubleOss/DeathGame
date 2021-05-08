package doubleos.deathgame.ablilty;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import doubleos.deathgame.Main;
import doubleos.deathgame.util.Utils;
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
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;

import java.util.HashMap;

public class HumanCommon implements Listener
{

    @EventHandler
    void onMoveEvent(PlayerMoveEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        if(gameVariable.getGameState().equals(GameVariable.GameState.PLAY))
        {
            if(gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).getHumanType().equals(PlayerVariable.HumanType.KILLER))
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
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                {
                    ItemStack stack1 = GameItem.Instance().m_humanCom_Ability1_Item;
                    stack1.setAmount(1);
                    if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(stack1.getType()))
                    {
                        event.getPlayer().getInventory().removeItem(stack1);
                        Location blockpos = event.getClickedBlock().getLocation().add(new Vector(0, 1, 0));
                        gameVariable.getHumanTrapLocList().add(blockpos);
                        blockpos.getBlock().setType(Material.RAILS);
                    }
                }
            }
        }
    }
    @EventHandler
    void onRightClickedPlayerEvent(PlayerInteractEntityEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        if (gameVariable.getGameState().equals(GameVariable.GameState.PLAY))
        {
            if (gameVariable.getPlayerListVariableMap().get(event.getPlayer().getName()).getObserver())
                return;
            if (gameVariable.getPlayerVariableMap().get(event.getPlayer().getName()).getHumanType().equals(PlayerVariable.HumanType.HUMAN)) {
                if (event.getHand() != EquipmentSlot.HAND)
                {
                    return;
                }
                if (event.getRightClicked() instanceof Player)
                {
                    Player clickEntity = ((Player) event.getRightClicked()).getPlayer();
                    PlayerVariable variableEntity = gameVariable.getPlayerVariableMap().get(clickEntity.getName());

                    if (variableEntity.getLifeType().equals(PlayerVariable.LifeType.MORIBUND))
                    {
                        if (!variableEntity.getStructure())
                        {
                            if (clickEntity.getLocation().distance(event.getPlayer().getLocation()) <= 2)
                            {
                                variableEntity.setStructure(true);
                                event.getPlayer().sendTitle("[!]", ChatColor.GREEN+ "구조를 시작합니다." +ChatColor.RED+" 움직일 시 구조가 중단됩니다..", 1, 30, 1);
                                BukkitTask task = new BukkitRunnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if(clickEntity.getLocation().distance(event.getPlayer().getLocation()) > 2)
                                        {
                                            variableEntity.setStructure(false);
                                            event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "false").getBytes());
                                            Utils.Instance().sendTitle(event.getPlayer(), "[!]", "구조가 취소되었습니다.", 1, 30, 1, ChatColor.RED);
                                            this.cancel();
                                        }
                                        else if ((variableEntity.getStructureHealth() >= 5))
                                        {
                                            variableEntity.setStructure(true);
                                            variableEntity.setLifeType(PlayerVariable.LifeType.NORMAL);
                                            event.getPlayer().sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "false").getBytes());
                                            event.getPlayer().sendTitle("[!]", ChatColor.GREEN+ "플레이어를 구조하였습니다.", 1, 30, 1);
                                            for(Player p : Bukkit.getOnlinePlayers())
                                            {
                                                if(p.isOp())
                                                {
                                                    p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.RED+ event.getPlayer().getName() + ChatColor.WHITE+ " 님이 "+ChatColor.GREEN+ clickEntity.getName() +ChatColor.WHITE+ "님을 구조 하셨습니다.");
                                                }
                                            }
                                            this.cancel();
                                        }
                                        else if (variableEntity.getStructureHealth() <= 5)
                                        {
                                            variableEntity.setStructureHealth(variableEntity.getStructureHealth() +1 );
                                        }

                                    }
                                }.runTaskTimer(Main.instance, 0, 20l);
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

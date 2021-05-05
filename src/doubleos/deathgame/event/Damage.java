package doubleos.deathgame.event;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Damage implements Listener
{

    @EventHandler
    void onDamage(EntityDamageByEntityEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player)
        {
            if(gameVariable.getGameState().equals(GameVariable.GameState.PLAY))
            {
                if(gameVariable.getPlayerVariableMap().get(((Player) event.getDamager()).getPlayer().getName()).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
                {
                    event.setCancelled(true);
                    return;
                }
                if(!gameVariable.getKillCoolTime())
                {
                    if(gameVariable.getPlayerVariableMap().get(event.getDamager().getName()).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                    {
                        double damage = 0;
                        if(gameVariable.getPlayerVariableMap().get(event.getDamager().getName()).getKillerType().equals(PlayerVariable.KillerType.COMMON))
                            damage = 1.2;
                            //damage = 0.7;
                        else if (gameVariable.getPlayerVariableMap().get(event.getDamager().getName()).getKillerType().equals(PlayerVariable.KillerType.HIDDEN))
                            damage = 1.5;
                            //damage = 1.2;
                        else
                            damage = 2;
                            //damage = 1.7;
                        event.setDamage(damage);
                    }
                }
                else
                {
                    event.getDamager().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + " 다음 킬은 " +gameVariable.getKillCoolTimeTimer()+ " 초 이후에 가능합니다.");
                }


            }
        }
    }

    @EventHandler
    void ondamages(EntityDamageEvent event)
    {
        if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
        {
            event.setCancelled(true);
            return;
        }

        if(event.getCause().equals(EntityDamageEvent.DamageCause.POISON))
        {
            event.setDamage(2);
            return;
        }
    }

}

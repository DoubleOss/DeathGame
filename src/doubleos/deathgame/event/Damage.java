package doubleos.deathgame.event;

import doubleos.deathgame.Main;
import doubleos.deathgame.sound.KillerSound;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Damage implements Listener
{

    @EventHandler
    void onDamage(EntityDamageByEntityEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player || event.getDamager() instanceof Arrow)
        {
            if(gameVariable.getGameState().equals(GameVariable.GameState.PLAY))
            {
                String killerName = "";
                if(event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE))
                {
                    if (event.getDamager() instanceof  Arrow)
                    {
                        Arrow arrow = (Arrow) event.getDamager();
                        Player killer = (Player) arrow.getShooter();
                        killerName = killer.getName();
                    }
                }
                else
                {
                    killerName = event.getDamager().getName();
                }
                if(gameVariable.getPlayerVariableMap().get(killerName).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
                {
                    event.setCancelled(true);
                    return;
                }
                if(!gameVariable.getKillCoolTime())
                {
                    if(gameVariable.getPlayerVariableMap().get(killerName).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                    {
                        double damage = 0;
                        if(gameVariable.getPlayerVariableMap().get(killerName).getKillerType().equals(PlayerVariable.KillerType.COMMON))
                            damage = 1;
                            //damage = 0.7;
                        else if (gameVariable.getPlayerVariableMap().get(killerName).getKillerType().equals(PlayerVariable.KillerType.HIDDEN))
                            damage = 1.5;
                            //damage = 1.2;
                        else
                            damage = 2;
                            //damage = 1.7;
                        event.setDamage(damage);
                        Bukkit.broadcastMessage(String.valueOf(damage));
                        KillerSound killerSound = new KillerSound();
                        killerSound.initSound(((Player) event.getEntity()));
                    }
                }
                else
                {
                    event.getDamager().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + " 다음 킬은 " +gameVariable.getKillCoolTimeTimer()+ " 초 이후에 가능합니다.");
                    event.setCancelled(true);
                }

            }
        }
    }

    @EventHandler
    void onProjectileHitPlayer(ProjectileHitEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        if(event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof Player)
        {
            Player damager = ((Player) event.getEntity().getShooter()).getPlayer();
            if(event.getHitEntity()instanceof Arrow)
            {
                if(gameVariable.getGameState().equals(GameVariable.GameState.PLAY))
                {
                    if(gameVariable.getPlayerVariableMap().get(damager.getName()).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                    {
                        double damage = 0;
                        if(gameVariable.getPlayerVariableMap().get(damager.getName()).getKillerType().equals(PlayerVariable.KillerType.COMMON))
                            damage = 1.2;
                            //damage = 0.7;
                        else if (gameVariable.getPlayerVariableMap().get(damager.getName()).getKillerType().equals(PlayerVariable.KillerType.HIDDEN))
                            damage = 1.5;
                            //damage = 1.2;
                        else
                            damage = 2;
                        //damage = 1.7;
                        //((Player) event.getHitEntity()).damage(damage, damager);
                    }

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

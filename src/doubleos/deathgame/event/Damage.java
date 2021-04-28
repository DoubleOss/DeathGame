package doubleos.deathgame.event;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

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
                for(String stringPlayer : gameVariable.getGamePlayerList())
                {
                    if(event.getEntity().equals(Bukkit.getPlayer(stringPlayer)))
                    {

                        double damage = 0;
                        if(gameVariable.getPlayerVariableMap().get(event.getEntity().getName()).getKillerType().equals(PlayerVariable.KillerType.COMMON))
                            damage = 2;
                        else if (gameVariable.getPlayerVariableMap().get(event.getEntity().getName()).getKillerType().equals(PlayerVariable.KillerType.HIDDEN))
                            damage = 4;
                        else
                            damage = 5;
                        event.setDamage(damage);
                    }
                }

            }
        }
    }
}

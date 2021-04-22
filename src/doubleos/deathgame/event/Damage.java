package doubleos.deathgame.event;

import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Damage implements Listener
{

    @EventHandler
    void onDamage(EntityDamageByEntityEvent event)
    {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player)
        {
            if(GameVariable.Instance().getGameState().equals(GameVariable.GameState.PLAY))
            {
                for(Player p : GameVariable.Instance().getGamePlayerList())
                {
                    if(event.getEntity().equals(GameVariable.Instance().getGamePlayerList()))
                    {
                        double damage = 0;
                        if(Main.instance.variablePlayer.get(event.getEntity()).getKillerType().equals(PlayerVariable.KillerType.COMMON))
                            damage = 2;
                        else if (Main.instance.variablePlayer.get(event.getEntity()).getKillerType().equals(PlayerVariable.KillerType.HIDDEN))
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

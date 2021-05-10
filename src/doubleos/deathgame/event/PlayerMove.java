package doubleos.deathgame.event;

import doubleos.deathgame.variable.GameVariable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener
{
    @EventHandler
    void onPlayerMoveEvent(PlayerMoveEvent event)
    {
        GameVariable gameVariable = GameVariable.Instance();
        String stringPlayer = event.getPlayer().getName();
        if(gameVariable.getGameState().equals(GameVariable.GameState.PLAY))
        {
            if(!gameVariable.getPlayerListVariableMap().get(stringPlayer).getObserver())
            {
                if(gameVariable.getTeleporting())
                    event.getPlayer().setWalkSpeed(0);
                else if(gameVariable.getPlayerVariableMap().get(stringPlayer) != null)
                {
                    if(!gameVariable.getPlayerVariableMap().get(stringPlayer).getHealKit())
                        event.getPlayer().setWalkSpeed(0.2f);
                }
            }


        }


    }

}

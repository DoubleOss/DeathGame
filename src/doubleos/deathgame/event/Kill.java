package doubleos.deathgame.event;

import doubleos.deathgame.ablilty.KillerCommon;
import doubleos.deathgame.variable.GameVariable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Kill implements Listener
{

    @EventHandler
    void onDeathEvent(PlayerDeathEvent event)
    {
        if(GameVariable.Instance().getKillerListName(event.getEntity().getKiller()) != null)
        {
            if(checkPlayingGamePlayer(event.getEntity().getPlayer()))
            {
                event.setDeathMessage("");
                if (GameVariable.Instance().getHidden2Target() == null)
                {
                    event.getEntity().getPlayer().sendMessage("당신은 사망 하셨습니다.");
                    GameVariable.Instance().getPlayerVariable().get(event.getEntity().getPlayer()).setObserver(true);
                }
                else if(GameVariable.Instance().getHidden2Target().equals(event.getEntity().getPlayer()))
                {
                    KillerCommon common = new KillerCommon();
                    common.initCommon(event.getEntity());
                    event.getEntity().getPlayer().sendMessage("당신은 전도를 받아 살인마로 다시 부활합니다.");
                    GameVariable.Instance().addKillerListName(event.getEntity());
                    GameVariable.Instance().setHidden2Targer(null);

                }
                else
                {
                    event.getEntity().getPlayer().sendMessage("당신은 사망 하셨습니다.");
                    GameVariable.Instance().getPlayerVariable().get(event.getEntity().getPlayer()).setObserver(true);
                    GameVariable.Instance().setHidden2Targer(null);
                    GameVariable.Instance().getOrignalKillerPlayer().sendMessage("전도에 실패 하셨습니다.");
                }

            }

        }
    }

    @EventHandler
    void onRespawn(PlayerRespawnEvent event)
    {
        if(checkPlayingGamePlayer(event.getPlayer()))
        {
            if(GameVariable.Instance().getPlayerVariable().get(event.getPlayer()).getObserver())
            {
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
        }

    }



    boolean checkPlayingGamePlayer(Player player)
    {
        for(Player p : GameVariable.Instance().getGamePlayerList())
        {
            if(p.getPlayer().equals(player))
            {
                return true;
            }
        }
        return false;
    }
}

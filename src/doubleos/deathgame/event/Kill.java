package doubleos.deathgame.event;

import doubleos.deathgame.ablilty.KillerCommon;
import doubleos.deathgame.command.GameCommand;
import doubleos.deathgame.variable.GameVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
                for(Player p : Bukkit.getOnlinePlayers())
                {
                    if(p.isOp())
                    {
                        p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.WHITE + "살인마 " + ChatColor.RED + event.getEntity().getKiller().getName() + ChatColor.WHITE + "님이 "
                                +ChatColor.GOLD+ event.getEntity().getPlayer().getName() +ChatColor.WHITE + " 님을 살해 하셨습니다." );
                    }
                }
                if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.CATHEDRAL))
                {
                    if(GameVariable.Instance().getHidden2Target().equals(event.getEntity().getPlayer().getName()))
                    {

                        KillerCommon common = new KillerCommon();
                        common.initCommon(event.getEntity());
                        event.getEntity().getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + " 당신은 전도를 받아 살인마로 다시 부활합니다.");
                        GameVariable.Instance().addKillerListName(event.getEntity());
                        GameVariable.Instance().setHidden2Targer(null);
                        for(Player p1 : Bukkit.getOnlinePlayers())
                        {
                            if(p1.isOp())
                            {
                                p1.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.WHITE + "살인마의 전도가 성공하여 " + ChatColor.RED + event.getEntity().getPlayer().getName() + ChatColor.WHITE + " 님이 살인마로 다시 부활 하셨습니다.");
                            }
                        }
                        if(GameVariable.Instance().getGamePlayerList().size() == GameVariable.Instance().getKillerPlayerList().size())
                        {
                            for(Player p2 : Bukkit.getOnlinePlayers())
                            {
                                p2.sendTitle("[!]", ChatColor.GREEN+ "모든 기자들이 살인마로 변하여 게임이 종료됩니다.", 1, 30, 1);
                                p2.sendMessage( ChatColor.GREEN+ "모든 기자들이 살인마로 변하여 게임이 종료됩니다.");
                                GameVariable.Instance().GameReset();
                            }

                        }

                    }
                    else
                    {
                        event.getEntity().getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + ": 당신은 살인마에 의해 살해 당하였습니다.");
                        GameVariable.Instance().getPlayerVariable().get(event.getEntity().getPlayer().getName()).setObserver(true);
                        GameVariable.Instance().setHidden2Targer(null);
                        GameVariable.Instance().getOrignalKillerPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + ": 전도에 실패 하셨습니다.");

                        for(Player p : Bukkit.getOnlinePlayers())
                        {
                            if(p.isOp())
                            {
                                p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.WHITE + "살인마의 전도에 실패 하였습니다");
                            }
                        }
                    }
                }
                else
                {
                    if(GameVariable.Instance().getHidden2Target() == null)
                    {
                        event.getEntity().getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + ": 당신은 살인마에 의해 살해 당하였습니다.");
                        GameVariable.Instance().getPlayerVariable().get(event.getEntity().getPlayer()).setObserver(true);
                    }

                }

            }

        }
    }

    @EventHandler
    void onRespawn(PlayerRespawnEvent event)
    {
        if(checkPlayingGamePlayer(event.getPlayer()))
        {
            if(GameVariable.Instance().getPlayerVariable().get(event.getPlayer().getName()).getObserver())
            {
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
        }

    }



    boolean checkPlayingGamePlayer(Player player)
    {
        for(String stringPlayer : GameVariable.Instance().getGamePlayerList())
        {
            Player p = Bukkit.getPlayer(stringPlayer);
            if(p.getPlayer().equals(player))
            {
                return true;
            }
        }
        return false;
    }
}

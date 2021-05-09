package doubleos.deathgame.scoreboard;


import doubleos.deathgame.Main;
import doubleos.deathgame.ablilty.Hidden;
import doubleos.deathgame.ablilty.KillerHidden1;
import doubleos.deathgame.ablilty.KillerHidden2;
import doubleos.deathgame.ablilty.KillerHidden3;
import doubleos.deathgame.util.SimpleScoreboard;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class Scoreboard
{

    Player m_player;

    public Scoreboard(Player player)
    {
        this.m_player = player;
        setScoreBoard(player);

    }

    void setScoreBoard(Player player)
    {
        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(!GameVariable.Instance().getGameState().equals(GameVariable.GameState.PAUSE) || GameVariable.Instance().getGameState().equals(GameVariable.GameState.END))
                {
                    GameVariable gamevariable = GameVariable.Instance();
                    HashMap<String, PlayerVariable> variableMap = gamevariable.getPlayerListVariableMap();
                    MissionManager mission = MissionManager.Instance();
                    SimpleScoreboard scoreboard = new SimpleScoreboard(ChatColor.RED + "[죽음의 술래잡기]");
                    if(gamevariable.getGameState() == GameVariable.GameState.END)
                    {
                        this.cancel();
                        scoreboard.reset();
                        scoreboard.update();

                    }
                    if(variableMap.get(player.getName()).getObserver() || player.isOp())
                    {
                        if(gamevariable.getKillerPlayerList().size() <= 1)
                        {
                            scoreboard.add("현재 살인마 " + gamevariable.getKillerPlayerList().get(0), 12);
                        }
                        else
                        {
                            scoreboard.add("현재 살인마 " + gamevariable.getKillerPlayerList().get(0) + " 외 " + gamevariable.getKillerPlayerList().size() + "인", 12);
                        }
                    }

                    scoreboard.add("&f", 11);
                    scoreboard.add("남은 시간: " + gamevariable.getGameTimeMin() + ChatColor.DARK_GREEN +" 분" + ChatColor.WHITE+" : " +
                            gamevariable.getGameTimeSec() +ChatColor.DARK_GREEN +ChatColor.WHITE + " 초", 10);
                    scoreboard.add("", 9);

                    int deathCount = 0;
                    int escapeCount = 0;
                    for(Player p : Bukkit.getOnlinePlayers())
                    {
                        if(gamevariable.getPlayerVariableMap().get(p.getName()).getObserver())
                        {
                            deathCount++;
                        }
                        if(gamevariable.getPlayerVariableMap().get(p.getName()).getEscape())
                            escapeCount++;
                    }
                    if(player.isOp() || variableMap.get(player.getName()).getObserver())
                    {

                        int count = gamevariable.getGamePlayerList().size() - gamevariable.getKillerPlayerList().size() - deathCount;
                        scoreboard.add("모든 플레이어 수: " + gamevariable.getGamePlayerList().size() + " 명", 8);
                        scoreboard.add("모든 살인마 수: " + gamevariable.getKillerPlayerList().size() + " 명", 7);
                        scoreboard.add("모든 생존자 수: " + (gamevariable.getGamePlayerList().size() - gamevariable.getKillerPlayerList().size())+ " 명", 6);
                        scoreboard.add("             ", 5);
                        scoreboard.add("남은 생존자 수: " + (gamevariable.getGamePlayerList().size() - gamevariable.getKillerPlayerList().size() - deathCount - escapeCount)+ " 명", 4);
                        scoreboard.add("죽은 생존자 수: " + (deathCount)+ " 명", 3);
                        scoreboard.add("탈출한 생존자 수: " + escapeCount+ " 명", 2);

                    }
                    if (player.isOp() || variableMap.get(player.getName()).getObserver())
                    {
                        scoreboard.add("       ", 1);
                        scoreboard.add("켜진 배전박스 " + gamevariable.getRepairBoxCount() + "/" + 8+ChatColor.DARK_GREEN+ " 개", 0);
                    }
                    else if (variableMap.get(player.getName()).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
                    {
                        scoreboard.add("       ", 9);
                        scoreboard.add("켜진 배전박스: " + gamevariable.getRepairBoxCount() + "/" + 8+ChatColor.DARK_GREEN+ " 개", 8);
                        scoreboard.add("     ", 7);
                        scoreboard.add("남은 목숨: " + gamevariable.getPlayerVariableMap().get(player.getName()).getLife() + " 개", 6);
                        scoreboard.add("               ", 5);

                    }
                    else if (variableMap.get(player.getName()).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                    {
                        scoreboard.add("     ", 6);
                        scoreboard.add("켜진 배전박스 " + gamevariable.getRepairBoxCount() + "/" + 8+ChatColor.DARK_GREEN+ " 개", 5);
                        scoreboard.add("             ", 4);
                        scoreboard.add("남은 생존자 수: " + (gamevariable.getGamePlayerList().size() - gamevariable.getKillerPlayerList().size() - deathCount - escapeCount)+ " 명", 3);
                        scoreboard.add("               ", 2);
                    }
                    scoreboard.send(player);
                    scoreboard.update();

                }
                else
                {
                    this.cancel();
                }


            }

        }.runTaskTimer(Main.instance,0l,20l);



    }



}

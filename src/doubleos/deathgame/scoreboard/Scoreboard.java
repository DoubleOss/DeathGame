package doubleos.deathgame.scoreboard;


import doubleos.deathgame.Main;
import doubleos.deathgame.ablilty.Hidden;
import doubleos.deathgame.ablilty.KillerHidden1;
import doubleos.deathgame.ablilty.KillerHidden2;
import doubleos.deathgame.ablilty.KillerHidden3;
import doubleos.deathgame.util.SimpleScoreboard;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
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
                if(!GameVariable.Instance().getGameState().equals(GameVariable.GameState.PAUSE))
                {
                    GameVariable gamevariable = GameVariable.Instance();
                    SimpleScoreboard scoreboard = new SimpleScoreboard(ChatColor.RED + "[죽음의 술래잡기]");
                    if(Main.instance.variablePlayer.get(player).getObserver() || player.isOp())
                        scoreboard.add("현재 살인마" + gamevariable.getKillerPlayerList().get(0) + "외 " + gamevariable.getKillerPlayerList().size() + "인", 12);
                    scoreboard.add("&f", 11);
                    scoreboard.add("남은 시간: " + gamevariable.getGameTimeMin() + ChatColor.DARK_GREEN +" 분" +" : " +
                            gamevariable.getGameTimeSec() +ChatColor.DARK_GREEN + " 초", 10);
                    scoreboard.add("", 9);
                    if(player.equals(GameVariable.Instance().getOrignalKillerPlayer()) || player.isOp() || Main.instance.variablePlayer.get(player).getObserver())
                    {
                        MissionManager mission = MissionManager.Instance();
                        String mission1_Suc = (mission.getMission1Success() ? "완료" : "미수행");
                        String mission2_Suc = (mission.getMission2Success() ? "완료" : "미수행");

                        scoreboard.add(mission.getMission1Title() + ": " + mission1_Suc, 8);
                        scoreboard.add(mission.getMission2Title() + ": " + mission2_Suc, 7);

                        HashMap<Player, Hidden> hiddenclass = GameVariable.Instance().getKillerHiddenClass();
                        if(hiddenclass.isEmpty() == false)
                        {
                            String hidden_Time = "        ";
                            if((hiddenclass.get(player)) instanceof KillerHidden1)
                            {
                                hidden_Time = String.format("%d "+ChatColor.DARK_GREEN+ " 초", ((KillerHidden1)(hiddenclass.get(player))).m_hiddenAbliltyTime);
                            }
                            else if ((hiddenclass.get(player)) instanceof KillerHidden2)
                            {
                                hidden_Time = String.format("%d"+ChatColor.DARK_GREEN+  " 초", ((KillerHidden2)(hiddenclass.get(player))).m_hiddenAbliltyTime);
                            }
                            else if((hiddenclass.get(player)) instanceof KillerHidden3)
                            {
                                hidden_Time = String.format("%d"+ChatColor.DARK_GREEN+ " 초", ((KillerHidden3)(hiddenclass.get(player))).m_hiddenAbliltyTime);
                            }
                            scoreboard.add("     ", 6);
                            scoreboard.add( "변신시간: " + hidden_Time, 5);
                        }

                    }
                    else if (player.isOp() || Main.instance.variablePlayer.get(player).getObserver())
                    {
                        scoreboard.add("켜진 배전박스 " + GameVariable.Instance().getRepairBoxCount() + "/" + MissionManager.Instance().getBoxRepair() +ChatColor.DARK_GREEN+ " 개", 8);
                    }
                    else
                    {
                        scoreboard.add("켜진 배전박스 " + GameVariable.Instance().getRepairBoxCount() + "/" + MissionManager.Instance().getBoxRepair() +ChatColor.DARK_GREEN+ " 개", 8);
                    }
                    scoreboard.send(player);
                    scoreboard.update();
                    if(GameVariable.Instance().getGameState() == GameVariable.GameState.END)
                    {
                        this.cancel();
                        scoreboard.reset();
                        scoreboard.update();
                    }
                }


            }

        }.runTaskTimer(Main.instance,0l,20l);



    }



}

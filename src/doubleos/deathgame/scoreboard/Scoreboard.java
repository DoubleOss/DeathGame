package doubleos.deathgame.scoreboard;


import doubleos.deathgame.Main;
import doubleos.deathgame.ablilty.Hidden;
import doubleos.deathgame.ablilty.KillerHidden1;
import doubleos.deathgame.ablilty.KillerHidden2;
import doubleos.deathgame.ablilty.KillerHidden3;
import doubleos.deathgame.util.SimpleScoreboard;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
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
                    SimpleScoreboard scoreboard = new SimpleScoreboard("[죽음의 술래잡기]");
                    scoreboard.add("&f", 11);
                    scoreboard.add("게임 시간: " + gamevariable.getGameTimeMin() + " : " + gamevariable.getGameTimeSec(), 10);
                    scoreboard.add("", 9);
                    if(player.equals(GameVariable.Instance().getOrignalKillerPlayer()))
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
                                hidden_Time = String.format("%d 초", ((KillerHidden1)(hiddenclass.get(player))).m_hiddenAbliltyTime);
                            }
                            else if ((hiddenclass.get(player)) instanceof KillerHidden2)
                            {
                                hidden_Time = String.format("%d 초", ((KillerHidden2)(hiddenclass.get(player))).m_hiddenAbliltyTime);
                            }
                            else if((hiddenclass.get(player)) instanceof KillerHidden3)
                            {
                                hidden_Time = String.format("%d 초", ((KillerHidden3)(hiddenclass.get(player))).m_hiddenAbliltyTime);
                            }
                            scoreboard.add("     ", 6);
                            scoreboard.add( "변신시간: " + hidden_Time, 5);
                        }

                    }
                    else
                    {
                        scoreboard.add("배전박스 수리 " + GameVariable.Instance().getRepairBoxCount() + " | " + MissionManager.Instance().getBoxRepair() , 8);
                    }
                    scoreboard.send(player);
                    scoreboard.update();
                    if(GameVariable.Instance().getGameState() == GameVariable.GameState.END)
                    {
                        this.cancel();
                        scoreboard.reset();
                    }
                }


            }

        }.runTaskTimer(Main.instance,0l,20l);



    }

    void initScoreboard(Player player)
    {






        /*
        EasyScoreboard scoreboard = EasyScoreboardLib.createScoreboard(player, "[죽음의 술래잡기]");
        scoreboard.setLineText(11, "");
        scoreboard.setLineText(10, "게임 시간: " + gamevariable.getGameTimeMin() + " : " + gamevariable.getGameTimeSec());
        scoreboard.setLineText(9, "");
        if(player.equals(GameVariable.Instance().getKillerName()))
        {
            MissionManager mission = MissionManager.Instance();
            String mission1_Suc = (mission.getMission1Success() ? "완료" : "미수행");
            String mission2_Suc = (mission.getMission2Success() ? "완료" : "미수행");

            scoreboard.setLineText(8, mission.getMission1Title() + ": " + mission1_Suc);
            scoreboard.setLineText(7, mission.getMission2Title() + ": " + mission2_Suc);


        }
        scoreboard.enable();
        scoreboard.setUpdateSpeed(1);

         */

    }


}

package doubleos.deathgame.scoreboard;


import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import nl.itslars.scoreboardlib.EasyScoreboardLib;
import nl.itslars.scoreboardlib.scoreboard.EasyScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

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
                initScoreboard(player);
                if(GameVariable.Instance().getGameState() == GameVariable.GameState.END)
                    this.cancel();
            }

        }.runTaskTimer(Main.instance,0l,20l);



    }

    void initScoreboard(Player player)
    {
        GameVariable gamevariable = GameVariable.Instance();
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

    }


}

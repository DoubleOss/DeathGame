package doubleos.deathgame.scoreboard;


import doubleos.deathgame.Main;
import doubleos.deathgame.util.SimpleScoreboard;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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

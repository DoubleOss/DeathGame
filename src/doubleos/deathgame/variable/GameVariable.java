package doubleos.deathgame.variable;

import doubleos.deathgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GameVariable
{

    public enum GameState
    {
        PLAY,
        PAUSE,
        END;

    }

    public enum GameStage
    {
        LAB,
        CATHEDRAL,
        FACTORY;
    }

    //싱글톤
    static private GameVariable _instance = null;

    static public GameVariable Instance()
    {
        if(_instance == null)
        {
            _instance = new GameVariable();
        }
        return _instance;
    }
    

    GameStage m_GameStage = GameStage.LAB;

    GameState m_GameState = GameState.END;

    Boolean m_Checkkiller = false;

    Player m_killerPlayer;

    boolean m_TimeStart = false;

    int m_GameTime_Min = 20;

    int m_GameTime_Sec = 0;



    public void setTimeStart(boolean bool)
    {
        m_TimeStart = bool;
        //Bukkit.broadcastMessage(String.valueOf(m_TimeStart));
        if(m_TimeStart == true)
        {
            BukkitTask task = new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    //Bukkit.broadcastMessage("@");
                    if(m_TimeStart == false)
                        this.cancel();
                    if(m_GameTime_Min == 0)
                    {
                        if(m_GameTime_Sec == 0)
                        {
                            this.cancel();
                        }
                        else
                        {
                            m_GameTime_Sec--;
                        }
                    }
                    else if (m_GameTime_Sec == 0)
                    {
                        m_GameTime_Sec = 59;
                        m_GameTime_Min--;
                    }
                    else
                    {
                        m_GameTime_Sec--;
                    }
                    //Bukkit.broadcastMessage(m_GameTime_Min + "분 " + m_GameTime_Sec + "초");

                }

            }.runTaskTimer(Main.instance, 0l, 20l);
        }
    }

    public void setGameState(GameState state)
    {
        m_GameState = state;
    }

    public GameState getGameState()
    {
        return m_GameState;
    }

    public void setGameStage(GameStage stage)
    {
        m_GameStage = stage;
    }
    public GameStage getGameStage()
    {
        return m_GameStage;
    }

    public void setCheckKiller(boolean bool)
    {
        m_Checkkiller = bool;
    }
    public boolean getCheckKiller()
    {
        return m_Checkkiller;
    }


    public void setKillerName(Player killer)
    {
        m_killerPlayer = killer;
    }
    public Player getKillerName()
    {
        return m_killerPlayer;
    }

    public void setGameTimeMin(int min)
    {
        m_GameTime_Min = min;
    }

    public int getGameTimeMin()
    {
        return m_GameTime_Min;
    }

    public void setGameTimeSec(int sec)
    {
        m_GameTime_Sec = sec;
    }
    public int getGameTimeSec()
    {
        return m_GameTime_Sec;
    }

    public void GameReset()
    {
        m_GameTime_Min = 20;
        m_GameTime_Sec = 0;
        setTimeStart(false);
    }

}

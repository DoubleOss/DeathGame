package doubleos.deathgame.variable;

import org.bukkit.entity.Player;

public class GameVariable
{

    public enum GameState
    {
        PLAY,
        PAUSE,
        END;

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
    

    GameState m_GameState = GameState.END;

    Boolean m_Checkkiller = false;

    Player m_killerPlayer;



    public void setGameState(GameState state)
    {
        m_GameState = state;
    }

    public GameState getGameState()
    {
        return m_GameState;
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

}

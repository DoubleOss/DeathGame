package doubleos.deathgame.variable;

public class RepairBox
{

    float m_health = 0;

    boolean m_repair = false;
    boolean m_miniGame = false;

    boolean m_repairing = false;

    int m_MiniGameCount = 3;


    public boolean getRepairing()
    {
        return m_repairing;
    }
    public void setRepairing(boolean bool)
    {
        m_repairing = bool;
    }

    public int getMiniGameCount()
    {
        return m_MiniGameCount;
    }
    public void setMiniGameCount(int count)
    {
        m_MiniGameCount = count;
    }


    public void sethealth(float hp)
    {
        m_health = hp;
    }
    public float gethealth()
    {
        return m_health;
    }

    public void setMiniGame(boolean bool)
    {
        m_miniGame = bool;
    }

    public boolean getMiniGame()
    {
        return  m_miniGame;
    }
    public void setRepair(boolean bool)
    {
        m_repair = bool;
    }
    public boolean getRepair()
    {
        return m_repair;
    }
}

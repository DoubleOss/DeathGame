package doubleos.deathgame.variable;

public class RepairBox
{


    float m_health;
    boolean m_repair = false;



    public void sethealth(float hp)
    {
        m_health = hp;
    }
    public float gethealth()
    {
        return m_health;
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

package doubleos.deathgame.variable;

import org.bukkit.event.Listener;

public class MissionBox implements Listener
{
    boolean m_boxUse = false;


    public boolean getBoxUse()
    {
        return m_boxUse;
    }

    public void setBoxUse(boolean bool)
    {
        m_boxUse = bool;
    }
}

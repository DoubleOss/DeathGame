package doubleos.deathgame.variable;

import doubleos.deathgame.Main;
import org.bukkit.entity.Player;

public class PlayerVariable
{

    public enum HumanType
    {
        HUMAN,
        KILLER;

    }

    Player m_player;

    HumanType m_humanType;

    boolean m_observer = false;


    public PlayerVariable(Player m_player)
    {
        this.m_player = m_player;
        this.m_humanType = HumanType.HUMAN;
        Main.instance.variablePlayer.put(m_player, this);
    }


    public void resetPlayerVariable()
    {
        this.m_humanType = HumanType.HUMAN;
    }

    public void setHumanType(HumanType type)
    {
        this.m_humanType = type;
    }

    public HumanType getHumanType()
    {
        return this.m_humanType;
    }

    public boolean getObserver()
    {
        return m_observer;
    }
    public void setObserver(boolean bool)
    {
        m_observer = bool;
    }


}

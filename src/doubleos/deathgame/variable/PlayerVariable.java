package doubleos.deathgame.variable;

import doubleos.deathgame.Main;
import doubleos.deathgame.event.Kill;
import org.bukkit.entity.Player;

public class PlayerVariable
{

    public enum HumanType
    {
        HUMAN,
        KILLER;
    }

    public enum KillerType
    {
        COMMON,
        HIDDEN,
        BERSERKER;
    }

    Player m_player;

    HumanType m_humanType;

    boolean m_observer = false;
    boolean m_repair = false;

    KillerType m_killerType;


    public PlayerVariable(Player m_player)
    {
        this.m_player = m_player;
        this.m_humanType = HumanType.HUMAN;
        this.m_killerType = KillerType.HIDDEN;
        Main.instance.variablePlayer.put(m_player, this);
    }


    public void resetPlayerVariable()
    {
        this.m_humanType = HumanType.HUMAN;
        this.m_observer = false;
    }


    public void setRepair(boolean repair)
    {
        m_repair = repair;
    }
    public boolean getRepair()
    {
        return m_repair;
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

    public KillerType getKillerType()
    {
        return m_killerType;
    }
    public void setKillerType(KillerType type)
    {
        m_killerType = type;
    }


}

package doubleos.deathgame.variable;

import doubleos.deathgame.Main;
import doubleos.deathgame.event.Kill;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerVariable
{

    public enum HumanType
    {
        HUMAN,
        KILLER,
    }

    public enum LifeType
    {
        NORMAL,
        MORIBUND,
    }
    public enum KillerType
    {
        COMMON,
        HIDDEN,
        BERSERKER,
        NONE;
    }

    String m_player;

    HumanType m_humanType;

    LifeType m_lifeType;

    boolean m_observer = false;
    boolean m_repair = false;
    boolean m_soundPlaying = false;
    boolean m_escape = false;
    boolean m_structure = false;

    int m_Life = 2;
    int m_structureHealth = 5;


    KillerType m_killerType;


    public PlayerVariable(Player player)
    {
        m_player = player.getName();
        m_lifeType = LifeType.NORMAL;
        m_humanType = HumanType.HUMAN;
        m_killerType = KillerType.NONE;
        m_structure = false;
        m_escape = false;
        m_Life = 2;
        m_structureHealth = 5;
        //Main.instance.variablePlayer.put(m_player, this);
        GameVariable.Instance().getPlayerListVariableMap().put(player.getName(), this);
    }


    public void addGameVariable()
    {
        GameVariable.Instance().getPlayerVariableMap().put(this.m_player, this);
    }

    public void resetPlayerVariable()
    {
        m_lifeType = LifeType.NORMAL;
        m_humanType = HumanType.HUMAN;
        m_killerType = KillerType.NONE;
        m_observer = false;
        m_repair = false;
        m_soundPlaying = false;
        m_escape = false;
        m_structure = false;
        m_Life = 2;
        m_structureHealth = 5;

    }

    public boolean getEscape()
    {
        return m_escape;
    }
    public void setEscape(boolean bool)
    {
       m_escape = bool;
    }
    public void setRepair(boolean repair)
    {
        m_repair = repair;
    }
    public boolean getRepair()
    {
        return m_repair;
    }

    public void setStructureHealth(int health)
    {
        m_structureHealth = health;
    }
    public int getStructureHealth()
    {
        return m_structureHealth;
    }

    public void setLifeType(LifeType type)
    {
        m_lifeType = type;
    }
    public LifeType getLifeType()
    {
        return m_lifeType;
    }
    public void setHumanType(HumanType type)
    {
        m_humanType = type;
    }

    public HumanType getHumanType()
    {
        return m_humanType;
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

    public boolean getSoundPlaying()
    {
        return m_soundPlaying;
    }
    public void setSoundPlaying(boolean bool)
    {
        m_soundPlaying = bool;
    }
    public boolean getStructure()
    {
        return m_structure;
    }
    public void setStructure(boolean bool)
    {
        m_structure = bool;
    }

    public int getLife()
    {
        return  m_Life;
    }
    public void setLife(int life)
    {
        m_Life = life;
    }


}

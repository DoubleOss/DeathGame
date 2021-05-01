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

    public enum KillerType
    {
        COMMON,
        HIDDEN,
        BERSERKER,
        NONE;
    }

    String m_player;

    HumanType m_humanType;

    boolean m_observer = false;
    boolean m_repair = false;
    boolean m_soundPlaying = false;

    FileConfiguration config;


    KillerType m_killerType;


    public PlayerVariable(Player player)
    {
        m_player = player.getName();
        m_humanType = HumanType.HUMAN;
        m_killerType = KillerType.NONE;
        //Main.instance.variablePlayer.put(m_player, this);
        GameVariable.Instance().getPlayerListVariableMap().put(player.getName(), this);
    }


    public void addGameVariable()
    {
        GameVariable.Instance().getPlayerVariableMap().put(this.m_player, this);
    }

    public void resetPlayerVariable()
    {
        m_humanType = HumanType.HUMAN;
        m_killerType = KillerType.NONE;
        m_observer = false;
        m_repair = false;
        m_soundPlaying = false;
    }

    public void saveConfig()
    {

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


}

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
        BERSERKER,
        NONE;
    }

    String m_player;

    HumanType m_humanType;

    boolean m_observer = false;
    boolean m_repair = false;
    boolean m_soundPlaying = false;
    boolean m_escape = false;
    boolean m_soundKillerPlaying = false;
    boolean m_boxOpen = false;
    boolean m_miniGamePlaying = false;
    boolean m_healKit = false;
    boolean m_checkShulkerBoxCool = false;

    FileConfiguration config;

    int m_life = 2;


    KillerType m_killerType;


    public PlayerVariable(Player player)
    {
        m_player = player.getName();
        m_humanType = HumanType.HUMAN;
        m_killerType = KillerType.NONE;
        m_escape = false;
        m_life = 2;
        m_soundKillerPlaying = false;
        m_boxOpen = false;
        m_miniGamePlaying = false;
        m_healKit = false;
        m_checkShulkerBoxCool = false;
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
        m_escape = false;
        m_life = 2;
        m_soundKillerPlaying = false;
        m_boxOpen = false;
        m_miniGamePlaying = false;
        m_healKit = false;
        m_checkShulkerBoxCool = false;
    }


    public boolean getCheckShulkerBoxCool()
    {
        return m_checkShulkerBoxCool;
    }
    public void setCheckShulkerBoxCool(boolean bool)
    {
        m_checkShulkerBoxCool = bool;
    }
    public boolean getHealKit()
    {
        return  m_healKit;
    }
    public void setHealKit(boolean bool)
    {
        m_healKit = bool;
    }
    public boolean getMiniGamePlaying()
    {
        return m_miniGamePlaying;
    }
    public void setMiniGamePlaying(boolean bool)
    {
        m_miniGamePlaying = bool;
    }
    public boolean getBoxOpen()
    {
        return m_boxOpen;
    }
    public void setBoxOpen(boolean bool)
    {
        m_boxOpen = bool;
    }
    public boolean getSoundKillerPlaying()
    {
        return m_soundKillerPlaying;
    }
    public void setSoundKillerPlaying(boolean bool)
    {
        m_soundKillerPlaying = bool;
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


    public void setLife(int number)
    {
        m_life = number;
    }
    public int getLife()
    {
        return m_life;
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

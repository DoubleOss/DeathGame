package doubleos.deathgame.variable;

import doubleos.deathgame.Main;
import doubleos.deathgame.ablilty.Hidden;
import doubleos.deathgame.ablilty.KillerCommon;
import doubleos.deathgame.ablilty.KillerHidden2;
import doubleos.deathgame.gui.CellularGame;
import doubleos.deathgame.gui.DefectiveGame;
import javafx.scene.control.Cell;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

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

    ArrayList<Player> m_GamePlayerList = new ArrayList<>();
    ArrayList<Player> m_killerPlayerList = new ArrayList<>();

    HashMap<Player, CellularGame> m_cellGameClassPlayer = new HashMap<>();
    HashMap<Player, DefectiveGame> m_defectiveGameClassPlayer = new HashMap<>();
    HashMap<Player, Hidden> m_killerHiddenClass = new HashMap<>();


    GameStage m_GameStage = GameStage.LAB;

    GameState m_GameState = GameState.END;


    Player m_orignalKillerPlayer;


    KillerHidden2 m_KillerHidden2;



    int m_GameTime_Min = 20;
    int m_GameTime_Sec = 0;

    int m_MissionRotateNumber = 1;

    int m_repairBoxCount = 0;


    boolean m_isKillerCheckTras = false;
    boolean m_TimeStart = false;

    boolean m_Checkkiller = false;





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


    public void addKillerListName(Player killer)
    {
        m_killerPlayerList.add(killer);

    }
    public Player getKillerListName(Player killer)
    {
        for(int i = 0; i<m_killerPlayerList.size(); i++)
        {
            if(m_killerPlayerList.get(i).equals(killer))
            {
                return m_killerPlayerList.get(i);
            }
        }
        return null;
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


    public ArrayList<Player> getGamePlayerList()
    {
        return m_GamePlayerList;
    }

    public void addGamePlayerList(Player player)
    {
        m_GamePlayerList.add(player);
    }

    public void setKillerHidden2(KillerHidden2 hidden2)
    {
        m_KillerHidden2 = hidden2;
    }
    public KillerHidden2 getKillerHidden2()
    {
        return m_KillerHidden2;
    }

    public boolean getIsKillerCheckTras()
    {
        return m_isKillerCheckTras;
    }
    public void setIsKillerCheckTras(boolean bool)
    {
        m_isKillerCheckTras = bool;
    }

    public Player getOrignalKillerPlayer()
    {
        return m_orignalKillerPlayer;
    }
    public void setOrignalKillerPlayer(Player killer)
    {
        m_orignalKillerPlayer = killer;
    }



    public int getMissionRotateNumber()
    {
        return m_MissionRotateNumber;
    }

    public void setMissionRotateNumber(int number)
    {
        m_MissionRotateNumber = number;
    }

    public void setRepairBoxCount(int number)
    {
        m_repairBoxCount = number;
    }
    public int getRepairBoxCount()
    {
        return m_repairBoxCount;
    }

    public CellularGame getCellGameClassPlayer(Player player)
    {
        return m_cellGameClassPlayer.get(player);
    }
    public void addCellGameClassGetPlayer(CellularGame cellclass, Player player)
    {
        m_cellGameClassPlayer.put(player, cellclass);
    }
    public HashMap<Player, CellularGame> getcellClassHash()
    {
        return m_cellGameClassPlayer;
    }


    public DefectiveGame getDefectiveGameClassPlayer(Player player)
    {
        return m_defectiveGameClassPlayer.get(player);
    }

    public void addDefectiveClassGetPlayer(DefectiveGame defclass, Player player)
    {
        m_defectiveGameClassPlayer.put(player, defclass);
    }
    public HashMap<Player, DefectiveGame> getdefectiveClassHash()
    {
        return m_defectiveGameClassPlayer;
    }
    public HashMap<Player, Hidden> getKillerHiddenClass()
    {
        return m_killerHiddenClass;
    }
    public void addKillerHiddenClass(Player player, Hidden hiddenclass)
    {
        m_killerHiddenClass.put(player, hiddenclass);
    }

    public void setMissionRotate()
    {
        if(m_MissionRotateNumber == 2)
        {
            m_MissionRotateNumber = 2;
            MissionManager.Instance().setActiveMission(MissionManager.ActiveMission.MISSION2);

        }
        else if (m_MissionRotateNumber ==3)
        {
            m_MissionRotateNumber = 3;
            MissionManager.Instance().setActiveMission(MissionManager.ActiveMission.MISSION3);
        }
        else if (m_MissionRotateNumber == 4)
        {
            m_MissionRotateNumber = 4;
            MissionManager.Instance().setActiveMission(MissionManager.ActiveMission.MISSION4);
        }
        else
        {
            m_MissionRotateNumber = 1;
            MissionManager.Instance().setActiveMission(MissionManager.ActiveMission.MISSION1);
        }
        MissionManager.Instance().setMission();

    }

    public void GameReset()
    {
        m_GameTime_Min = 20;
        m_GameTime_Sec = 0;

        m_killerPlayerList.clear();
        m_GamePlayerList.clear();

        m_orignalKillerPlayer = null;

        MissionManager.Instance().setActiveMission(MissionManager.ActiveMission.MISSION1);

        setTimeStart(false);
    }






}

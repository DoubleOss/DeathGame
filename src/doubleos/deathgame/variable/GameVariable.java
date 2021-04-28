package doubleos.deathgame.variable;

import doubleos.deathgame.Main;
import doubleos.deathgame.ablilty.Hidden;
import doubleos.deathgame.ablilty.KillerCommon;
import doubleos.deathgame.ablilty.KillerHidden2;
import doubleos.deathgame.gui.CellularGame;
import doubleos.deathgame.gui.DefectiveGame;
import javafx.scene.control.Cell;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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

    public ArrayList<String> adminList = new ArrayList<>();

    ArrayList<String> m_GamePlayerList = new ArrayList<>();
    ArrayList<String> m_killerPlayerList = new ArrayList<>();

    HashMap<String, CellularGame> m_cellGameClassPlayer = new HashMap<>();
    HashMap<String, DefectiveGame> m_defectiveGameClassPlayer = new HashMap<>();
    HashMap<String, Hidden> m_killerHiddenClass = new HashMap<>();

    HashMap<String, PlayerVariable> m_playerVariableMap = new HashMap<>();

    HashMap<String, PlayerVariable> m_playerVariable = new HashMap<>();


    GameStage m_GameStage = GameStage.LAB;

    GameState m_GameState = GameState.END;


    String m_orignalKillerPlayer;


    KillerHidden2 m_KillerHidden2;

    String m_hidden2Target;



    int m_GameTime_Min = 20;
    int m_GameTime_Sec = 0;

    int m_MissionRotateNumber = 1;

    int m_repairBoxCount = 0;


    boolean m_isKillerCheckTras = false;
    boolean m_TimeStart = false;

    boolean m_Checkkiller = false;

    boolean m_teleporting = false;





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
        m_killerPlayerList.add(killer.getName());

    }
    public String getKillerListName(Player killer)
    {
        for(int i = 0; i<m_killerPlayerList.size(); i++)
        {
            if(m_killerPlayerList.get(i).equalsIgnoreCase(killer.getName()))
            {
                return m_killerPlayerList.get(i);
            }
        }
        return null;
    }
    public ArrayList<String> getKillerPlayerList()
    {
        return m_killerPlayerList;
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


    public ArrayList<String> getGamePlayerList()
    {
        return m_GamePlayerList;
    }

    public void addGamePlayerList(Player player)
    {
        m_GamePlayerList.add(player.getName());
    }

    public void setKillerHidden2(KillerHidden2 hidden2)
    {
        m_KillerHidden2 = hidden2;
    }
    public KillerHidden2 getKillerHidden2()
    {
        return m_KillerHidden2;
    }

    public void setHidden2Targer(Player player)
    {
        if(player == null)
        {
            m_hidden2Target = null;
        }
        else
        {
            m_hidden2Target = player.getName();
        }

    }

    public String getHidden2Target()
    {
        return m_hidden2Target;
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
        Player player = Bukkit.getPlayer(m_orignalKillerPlayer);
        return player;
    }
    public void setOrignalKillerPlayer(Player killer)
    {
        m_orignalKillerPlayer = killer.getName();
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
        m_cellGameClassPlayer.put(player.getName(), cellclass);
    }
    public HashMap<String, CellularGame> getcellClassHash()
    {
        return m_cellGameClassPlayer;
    }


    public DefectiveGame getDefectiveGameClassPlayer(Player player)
    {
        return m_defectiveGameClassPlayer.get(player);
    }

    public void addDefectiveClassGetPlayer(DefectiveGame defclass, Player player)
    {
        m_defectiveGameClassPlayer.put(player.getName(), defclass);
    }
    public HashMap<String, DefectiveGame> getdefectiveClassHash()
    {
        return m_defectiveGameClassPlayer;
    }
    public HashMap<String, Hidden> getKillerHiddenClass()
    {
        return m_killerHiddenClass;
    }
    public void addKillerHiddenClass(Player player, Hidden hiddenclass)
    {
        m_killerHiddenClass.put(player.getName(), hiddenclass);
    }
    public void addPlayerVarible(Player player, PlayerVariable variable)
    {
        m_playerVariable.put(player.getName(), variable);
    }
    public HashMap<String, PlayerVariable> getPlayerVariable()
    {
        return m_playerVariable;
    }

    public void setTeleporting(boolean bool)
    {
        m_teleporting = bool;
    }
    public boolean getTeleporting()
    {
        return m_teleporting;
    }

    public HashMap<String, PlayerVariable> getPlayerVariableMap()
    {
        return m_playerVariableMap;
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

        for(String stringPlayer : getGamePlayerList())
        {
            Player p = Bukkit.getPlayer(stringPlayer);
            p.setGameMode(GameMode.SURVIVAL);
            getPlayerVariableMap().get(p.getName()).resetPlayerVariable();
            p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false").getBytes());
        }

        m_GameTime_Min = 20;
        m_GameTime_Sec = 0;


        m_killerPlayerList.clear();
        m_GamePlayerList.clear();
        m_playerVariable.clear();

        m_cellGameClassPlayer.clear();
        m_defectiveGameClassPlayer.clear();
        m_killerHiddenClass.clear();

        m_teleporting = false;


        m_orignalKillerPlayer = null;

        m_Checkkiller = false;
        m_isKillerCheckTras = false;

        m_GameState = GameState.END;

        MissionManager.Instance().resetMission();

        setTimeStart(false);
    }






}

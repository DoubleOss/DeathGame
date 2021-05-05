package doubleos.deathgame.variable;

import catserver.server.CatServer;
import doubleos.deathgame.Main;
import doubleos.deathgame.ablilty.*;
import doubleos.deathgame.gui.CellularGame;
import doubleos.deathgame.gui.DefectiveGame;
import doubleos.deathgame.util.Utils;
import javafx.scene.control.Cell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
    HashMap<String, PlayerVariable> m_playerListVariable = new HashMap<>();




    GameStage m_GameStage = GameStage.LAB;

    GameState m_GameState = GameState.END;


    String m_orignalKillerPlayer;


    KillerHidden2 m_KillerHidden2;

    String m_hidden2Target;



    int m_GameTime_Min = 20;
    int m_GameTime_Sec = 0;

    int m_MissionRotateNumber = 1;

    int m_repairBoxCount = 0;

    int m_escapePlayerCount = 0;

    int m_killCoolTimeTimer = 0;


    boolean m_isKillerCheckTras = false;
    boolean m_TimeStart = false;

    boolean m_Checkkiller = false;

    boolean m_teleporting = false;

    boolean m_killCoolTime = false;





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
                    if(m_GameTime_Min == 1)
                    {
                        if(m_GameTime_Sec == 59)
                        {
                            for(String s : m_killerPlayerList)
                            {
                                Player p = Bukkit.getPlayer(s);
                                setBerserker(p);
                            }
                            for(Player loopPlayer :Bukkit.getOnlinePlayers())
                            {
                                if(loopPlayer.isOp())
                                {
                                    loopPlayer.sendMessage(ChatColor.GOLD + "[알림] "+  ChatColor.WHITE +" 살인마들이 폭주하였습니다.");
                                }
                            }
                        }
                    }
                    if(m_GameTime_Min == 0)
                    {
                        if(m_GameTime_Sec == 0)
                        {
                            Bukkit.broadcastMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE +" 제한시간 20분이 종료되어 탈출구가 열립니다");
                            if(GameVariable.Instance().getGameStage().equals(GameStage.LAB))
                                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "탈출구활성화 연구소");
                            else if(GameVariable.Instance().getGameStage().equals(GameStage.CATHEDRAL))
                                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "탈출구활성화 성당");
                            else if(GameVariable.Instance().getGameStage().equals(GameStage.FACTORY))
                                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "탈출구활성화 인형공장");
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
    void setBerserker(Player player)
    {
        GameVariable.Instance().getPlayerVariableMap().get(player.getName()).setKillerType(PlayerVariable.KillerType.BERSERKER);
        PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, 2400, 0);
        player.addPotionEffect(effect, true);
        GameVariable.Instance().setIsKillerCheckTras(true);
        switch(GameVariable.Instance().getGameStage())
        {
            case LAB:
                KillerHidden1 hidden1 = new KillerHidden1();
                hidden1.initKillerHidden1();
                break;
            case CATHEDRAL:
                KillerHidden2 hidden2 = new KillerHidden2();
                hidden2.initKillerHidden2();
                break;
            case FACTORY:
                KillerHidden3 hidden3 = new KillerHidden3();
                hidden3.initKillerHidden3();
                break;
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


    public void setKillCoolTime(boolean bool)
    {
        m_killCoolTime = bool;
    }

    public boolean getKillCoolTime()
    {
        return m_killCoolTime;
    }

    public int getKillCoolTimeTimer()
    {
        return m_killCoolTimeTimer;
    }

    public void setKillCoolTimeTimer(int time)
    {
        m_killCoolTimeTimer = time;
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
        MissionManager.Instance().setBoxRepair(number);
    }
    public int getRepairBoxCount()
    {
        return MissionManager.Instance().getBoxRepair();
    }

    public CellularGame getCellGameClassPlayer(String player)
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


    public DefectiveGame getDefectiveGameClassPlayer(String player)
    {
        return m_defectiveGameClassPlayer.get(player);
    }

    public void addDefectiveClassGetPlayer(DefectiveGame defclass, String player)
    {
        m_defectiveGameClassPlayer.put(player, defclass);
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
    public void addPlayerVarible()
    {
        for(Player p : Bukkit.getOnlinePlayers())
        {
            if(!m_playerListVariable.get(p.getName()).getObserver())
            {
                m_playerVariableMap.put(p.getName(), m_playerListVariable.get(p.getName()));
                addGamePlayerList(p);
            }
        }
    }
    public int getEscapePlayerCount()
    {
        return  m_escapePlayerCount;
    }
    public void setEscapePlayerCount(int number)
    {
        m_escapePlayerCount = number;
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

    public HashMap<String, PlayerVariable> getPlayerListVariableMap()
    {
        return  m_playerListVariable;
    }

    public void setMissionRotate()
    {
        if(m_MissionRotateNumber == 2)
        {
            MissionManager.Instance().setActiveMission(MissionManager.ActiveMission.MISSION2);

        }
        else if (m_MissionRotateNumber ==3)
        {
            MissionManager.Instance().setActiveMission(MissionManager.ActiveMission.MISSION3);
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
            if(p!= null)
            {
                if(getPlayerListVariableMap().get(stringPlayer) == null)
                {
                    PlayerVariable playerVariable = new PlayerVariable(p);
                    getPlayerListVariableMap().put(stringPlayer, playerVariable);
                }

                p.setGameMode(GameMode.SURVIVAL);
                getPlayerListVariableMap().get(p.getName()).resetPlayerVariable();
                p.sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false").getBytes());
                Utils.Instance().inventoryClear(p);
            }

        }

        m_GameTime_Min = 20;
        m_GameTime_Sec = 0;
        m_escapePlayerCount=0;


        GameItem.Instance().initGameItem();
        m_killerPlayerList.clear();
        m_GamePlayerList.clear();
        m_playerVariableMap.clear();

        m_cellGameClassPlayer.clear();
        m_defectiveGameClassPlayer.clear();
        m_killerHiddenClass.clear();

        m_teleporting = false;


        m_orignalKillerPlayer = null;

        m_Checkkiller = false;
        m_isKillerCheckTras = false;
        m_killCoolTime = false;
        m_killCoolTimeTimer = 0;

        m_GameState = GameState.END;

        MissionManager.Instance().resetMission();
        


        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "탈출구활성화 초기화");

        setTimeStart(false);
    }






}

package doubleos.deathgame.variable;

import doubleos.deathgame.ablilty.KillerHidden1;
import doubleos.deathgame.ablilty.KillerHidden2;
import doubleos.deathgame.ablilty.KillerHidden3;
import doubleos.deathgame.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class MissionManager
{

    public enum ActiveMission
    {
        MISSION1,
        MISSION2,
        MISSION3,
        MISSION4;
    }

    //싱글톤
    static private MissionManager _instance = null;

    static public MissionManager Instance()
    {
        if(_instance == null)
        {
            _instance = new MissionManager();
        }
        return _instance;
    }
    HashMap<Location, RepairBox> m_repairBoxClassMap = new HashMap<>();

    ArrayList<Location> m_repairBlock = new ArrayList<>();

    ActiveMission m_activeMissoion = ActiveMission.MISSION1;

    String m_missoin1_Title;
    String m_missoin2_Title;

    boolean m_mission1_Success = false;
    boolean m_mission2_Success = false;

    int m_Mission1_PotionCount = 0;

    int m_BoxRepair = 0;


    public void setMission()
    {
        m_mission1_Success = false;
        m_mission2_Success = false;
        switch (m_activeMissoion)
        {
            case MISSION1:
            {
                m_missoin1_Title = "물약제조";
                m_missoin2_Title = "해킹";

                break;
            }
            case MISSION2:
            {
                m_missoin1_Title = "소독";
                m_missoin2_Title = "위치 조사";
                break;
            }
            case MISSION3:
            {
                m_missoin1_Title = "기계 수리";
                m_missoin2_Title = "세포 찾기";
                break;
            }
            case MISSION4:
            {
                m_missoin1_Title = "불량품 찾기";
                m_missoin2_Title = "리스트 분류";
                break;
            }
        }
    }


    public void resetMission()
    {

        m_activeMissoion = ActiveMission.MISSION1;

        m_mission1_Success = false;
        m_mission2_Success = false;

        m_Mission1_PotionCount = 0;
        m_BoxRepair = 0;

        m_repairBoxClassMap.clear();
        m_repairBlock.clear();

    }

    void killerHiddenChange()
    {
        if(m_mission1_Success && m_mission2_Success)
        {
            if(!GameVariable.Instance().getIsKillerCheckTras())
            {
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

        }
    }

    public void initRepairBoxList()
    {
        if(GameVariable.Instance().getGameState().equals(GameVariable.GameStage.LAB))
        {
            Location location1 = new Location(Bukkit.getWorld("World"), 1, 1, 1);
            Block block1 = Bukkit.getWorld("world").getBlockAt(location1);
            m_repairBlock.add(location1);
            m_repairBoxClassMap.put(location1, new RepairBox());
        }

    }

    public HashMap<Location, RepairBox> getRepairBoxClassMap()
    {
        return m_repairBoxClassMap;
    }

    public ArrayList<Location> getRepairBoxList()
    {
        return m_repairBlock;
    }


    public void setActiveMission(ActiveMission mission)
    {
        m_activeMissoion = mission;
    }
    public ActiveMission getActiveMission()
    {

        return m_activeMissoion;
    }

    public String getMission1Title()
    {
        return m_missoin1_Title;
    }

    public String getMission2Title()
    {
        return m_missoin2_Title;
    }

    public boolean getMission1Success()
    {
        return m_mission1_Success;
    }
    public boolean getMission2Success()
    {
        return m_mission2_Success;
    }

    public void setMission1Success(boolean bool)
    {
        m_mission1_Success = bool;
        killerHiddenChange();
    }

    public void setMission2Success(boolean bool)
    {
        m_mission2_Success = bool;
        killerHiddenChange();
    }

    public int getMission1PotionCount()
    {
        return m_Mission1_PotionCount;
    }

    public int getBoxRepair()
    {

        return m_BoxRepair;
    }
    public void setBoxRepair(int number)
    {
        m_BoxRepair = number;
        if(m_BoxRepair == 8)
        {
            for(String stringPlayer : GameVariable.Instance().getGamePlayerList())
            {
                Player p  = Bukkit.getPlayer(stringPlayer);
                p.sendTitle("[!]", ChatColor.GREEN+ "배전박스가 전부 수리되어 탈출구가 열렸습니다.", 1, 30, 1);
            }
        }
    }

    public void setMissionPotionCount(int number)
    {
        m_Mission1_PotionCount = number;
        if(m_Mission1_PotionCount ==1)
        {
            m_mission1_Success = true;

        }
    }

}

package doubleos.deathgame.variable;

import doubleos.deathgame.ablilty.KillerHidden1;
import doubleos.deathgame.ablilty.KillerHidden2;
import doubleos.deathgame.ablilty.KillerHidden3;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MissionManager
{

    public enum ActiveMission
    {
        MISSION1,
        MISSION2,
        MISSION3;
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

    ArrayList<Location> m_missionBoxList = new ArrayList<>();
    HashMap<Location, MissionBox> m_missionBoxMap = new HashMap<>();

    ArrayList<Location> m_FactoryLoc = new ArrayList<>();

    ActiveMission m_activeMissoion = ActiveMission.MISSION1;

    String m_missoin1_Title;
    String m_missoin2_Title;

    boolean m_mission1_Success = false;
    boolean m_mission2_Success = false;

    int m_Mission1_PotionCount = 0;

    int m_BoxRepair = 0;

    int m_FactoryHiddenCount = 0;

    public Location m_MissionBoxUseLocation;


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
                m_missoin2_Title = "기계 수리";
                break;
            }
            case MISSION3:
            {
                m_missoin1_Title = "세포 찾기";
                m_missoin2_Title = "불량품 찾기";
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
        m_missionBoxMap.clear();
        m_missionBoxList.clear();


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
        if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.LAB))
        {

            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -314, 63, 19));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -311, 63, -48));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -368, 63, -11));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -315, 72, -6));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -357, 72, -39));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -349, 72, 45));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -349, 80, -29));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -334, 80, 26));

        }
        else if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.CATHEDRAL))
        {
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -559, 63, 77));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -543, 63, 54));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -501, 63, 70));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -507, 64, 85));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -488, 71, 84));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -555, 63, 46));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -532, 70, 61));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -537, 63, 59));

        }
        else if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.FACTORY))
        {
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -418, 63, 98));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -397, 63, 77));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -384, 63, 99));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -408, 63, 92));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -360, 63, 88));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -420, 63, 144));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -371, 63, 146));
            m_repairBlock.add(new Location(Bukkit.getWorld("world"), -423, 75, 156));

        }
        for(int i = 0; i<m_repairBlock.size(); i++)
        {
            m_repairBoxClassMap.put(m_repairBlock.get(i), new RepairBox());

        }

    }

    public void initMissionBox()
    {
        if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.LAB))
        {
            m_missionBoxList.add(new Location(Bukkit.getWorld("world"), -367, 71, -50));
            m_missionBoxList.add(new Location(Bukkit.getWorld("world"), -334, 71, 45));
            m_missionBoxList.add(new Location(Bukkit.getWorld("world"), -319, 79, -50));
            m_missionBoxList.add(new Location(Bukkit.getWorld("world"), -319, 62, -48));

        }
        else if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.CATHEDRAL))
        {
            m_missionBoxList.add(new Location(Bukkit.getWorld("world"), -512, 62, 41));
            m_missionBoxList.add(new Location(Bukkit.getWorld("world"), -568, 70, 56));
            m_missionBoxList.add(new Location(Bukkit.getWorld("world"), -498, 63, 69));
            m_missionBoxList.add(new Location(Bukkit.getWorld("world"), -523, 62, 75));

        }
        else if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.FACTORY))
        {
            m_missionBoxList.add(new Location(Bukkit.getWorld("world"), -374, 69, 144));
            m_missionBoxList.add(new Location(Bukkit.getWorld("world"), -441, 62, 132));
            m_missionBoxList.add(new Location(Bukkit.getWorld("world"), -398, 62, 163));
            m_missionBoxList.add(new Location(Bukkit.getWorld("world"), -393, 62, 75));
            m_missionBoxList.add(new Location(Bukkit.getWorld("world"), -391, 62, 92));

        }
        for(int i = 0; i<m_missionBoxList.size(); i++)
        {
            m_missionBoxMap.put(m_missionBoxList.get(i), new MissionBox());
        }
    }


    public void initFactoryHiddenLoc()
    {
        m_FactoryLoc.add(new Location(Bukkit.getWorld("world"), -415, 62, 72));
        m_FactoryLoc.add(new Location(Bukkit.getWorld("world"), -417, 62, 72));
        m_FactoryLoc.add(new Location(Bukkit.getWorld("world"), -419, 62, 72));
        m_FactoryLoc.add(new Location(Bukkit.getWorld("world"), -415, 62, 75));
        m_FactoryLoc.add(new Location(Bukkit.getWorld("world"), -417, 62, 75));
        m_FactoryLoc.add(new Location(Bukkit.getWorld("world"), -419, 62, 75));

    }

    public HashMap<Location, MissionBox> getMissionBoxMap()
    {
        return m_missionBoxMap;
    }
    public ArrayList<Location> getMissionBoxList()
    {
        return m_missionBoxList;
    }

    public ArrayList<Location> getFactoryLoc()
    {
        return m_FactoryLoc;
    }

    public void resetMissionBox()
    {
        for(int i = 0; i<m_missionBoxList.size(); i++)
        {
            m_missionBoxMap.get(m_missionBoxList.get(i)).setBoxUse(false);
        }
    }
    public void successMissionbox()
    {
        m_missionBoxMap.get(m_MissionBoxUseLocation).setBoxUse(true);
        m_MissionBoxUseLocation = null;
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
        if(m_BoxRepair >= 8)
        {
            for(String stringPlayer : GameVariable.Instance().getGamePlayerList())
            {
                Player p  = Bukkit.getPlayer(stringPlayer);
                p.sendTitle("[!]", ChatColor.GREEN+ "배전박스가 전부 수리되어 탈출구가 열렸습니다.", 1, 30, 1);
            }
            Bukkit.broadcastMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE +" 배전박스가 전부 수리되어 탈출구가 열렸습니다.");
        }
    }

    public void setMissionPotionCount(int number)
    {
        m_Mission1_PotionCount = number;
        if(m_Mission1_PotionCount ==1)
        {
            setMission1Success(true);

        }
    }

    public int getFactoryHiddenCount()
    {
        return m_FactoryHiddenCount;
    }
    public void setFactoryHiddenCount(int count)
    {
        m_FactoryHiddenCount = count;
    }

}

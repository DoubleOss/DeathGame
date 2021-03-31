package doubleos.deathgame.variable;

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

    ActiveMission m_activeMissoion = ActiveMission.MISSION1;

    String m_missoin1_Title;
    String m_missoin2_Title;

    boolean m_mission1_Success = false;
    boolean m_mission2_Success = false;


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



}

package doubleos.deathgame.command;



import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import sun.invoke.empty.Empty;

import java.util.ArrayList;
import java.util.Collections;


public class GameCommand implements CommandExecutor
{

    ArrayList<Player> m_GamePlayerList = new ArrayList<>();

    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings)
    {

        if(sender instanceof Player)
        {
            Player player = (Player)sender;
            if(s.equalsIgnoreCase("죽술"))
            {
                if (strings[0].length()==0 || strings[0].equalsIgnoreCase("도움말"))
                {
                    help(player);
                    return true;
                }
                if (strings[0].equalsIgnoreCase("시작"))
                {

                    setPlayerVariable();
                    GameVariable.Instance().setGameState(GameVariable.GameState.PLAY);

                    Bukkit.broadcastMessage("잠시후 랜덤으로 킬러가 설정됩니다.");

                    BukkitTask task = new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            Collections.shuffle(m_GamePlayerList);

                            if(GameVariable.Instance().getCheckKiller() == false)
                            {
                                Main.instance.variablePlayer.get(m_GamePlayerList.get(0)).setHumanType(PlayerVariable.HumanType.KILLER);
                                GameVariable.Instance().setCheckKiller(true);
                                GameVariable.Instance().setKillerName(m_GamePlayerList.get(0));
                                m_GamePlayerList.get(0).sendMessage("당신은 킬러가 되셨습니다.");
                            }
                            else
                            {
                                player.sendMessage("킬러가 이미 존재함으로 킬러 뽑기는 스킵됩니다.");
                            }

                            player.sendMessage("테스트 " + m_GamePlayerList);
                            player.sendMessage("테스트2 " + Main.instance.variablePlayer.get(m_GamePlayerList.get(0)).getHumanType());

                            this.cancel();

                        }

                    }.runTaskTimer(Main.instance, 20l, 1l);

                    return true;

                }
                if (strings[0].equalsIgnoreCase("중지"))
                {
                    GameVariable.Instance().setGameState(GameVariable.GameState.PAUSE);
                    return true;
                }
                if (strings[0].equalsIgnoreCase("진행"))
                {
                    GameVariable.Instance().setGameState(GameVariable.GameState.PLAY);
                    return true;
                }
                if (strings[0].equalsIgnoreCase("초기화"))
                {
                    GameVariable.Instance().setGameState(GameVariable.GameState.END);
                    resetGame();
                    return true;
                }
                if (strings[0].equalsIgnoreCase("플레이어"))
                {
                    player.sendMessage("참가중인 플레이어: " + m_GamePlayerList);
                    return true;
                }
                if (strings[0].equalsIgnoreCase("살인마지정"))
                {
                    if((strings[1].isEmpty())==false)
                    {
                        player.sendMessage("지정한 플레이어" + strings[1] + "으로 살인마 설정이 완료 되었습니다.");
                        GameVariable.Instance().setCheckKiller(true);
                        GameVariable.Instance().setKillerName(Bukkit.getPlayer(strings[1]));

                        Bukkit.getPlayer(strings[1]).sendMessage("당신이 살인마로 지정되었습니다.");
                        return true;
                    }

                }


                return true;
            }
            return true;
        }
        return true;
    }

    void setPlayerVariable()
    {
        for(Player p : Bukkit.getOnlinePlayers())
        {

            if(Main.instance.adminList.isEmpty())
            {
                p.sendMessage("통과");
                m_GamePlayerList.add(p);
                PlayerVariable playerVariable = new PlayerVariable(p);
            }
            else
            {
                for(int i = 0; i<Main.instance.adminList.size(); i++)
                {
                    if(!Main.instance.adminList.get(i).equals(p))
                    {
                        p.sendMessage("통과");
                        m_GamePlayerList.add(p);
                        PlayerVariable playerVariable = new PlayerVariable(p);
                    }
                }

            }
            if (p == GameVariable.Instance().getKillerName())
            {
                Main.instance.variablePlayer.get(m_GamePlayerList.get(0)).setHumanType(PlayerVariable.HumanType.KILLER);
                GameVariable.Instance().setCheckKiller(true);
            }

        }
    }

    void resetGame()
    {
        m_GamePlayerList.clear();
        Main.instance.variablePlayer.clear();
        GameVariable.Instance().setCheckKiller(false);



    }
    void help(Player p)
    {
        p.sendMessage("      죽음의 술래잡기");
        p.sendMessage("/죽술 시작 - 게임을 시작합니다.");
        p.sendMessage("/죽술 중지 - 게임을 일시 중단 합니다. ");
        p.sendMessage("/죽술 진행 - 일시 중단된 게임을 재개 합니다.");
        p.sendMessage("/죽술 종료 - 게임을 종료 합니다.");
        p.sendMessage("/죽술 초기화 - 게임을 리셋 시킵니다.");
        p.sendMessage("/죽술 플레이어 - 참가하는 플레이어 목록을 확인합니다.");
        p.sendMessage("/죽술 살인마지정 닉네임 - 살인마를 지정합니다.");

        

    }

}

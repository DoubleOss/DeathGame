package doubleos.deathgame.command;



import doubleos.deathgame.Main;
import doubleos.deathgame.ablilty.Hidden2Gui;
import doubleos.deathgame.ablilty.KillerHidden1;
import doubleos.deathgame.ablilty.KillerHidden2;
import doubleos.deathgame.scoreboard.Scoreboard;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
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



    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings)
    {

        if(sender instanceof Player)
        {
            GameVariable gamevariable = GameVariable.Instance();
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

                    gamevariable.setGameState(GameVariable.GameState.PLAY);
                    Bukkit.broadcastMessage("잠시후 랜덤으로 킬러가 설정됩니다.");

                    BukkitTask task = new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            setPlayerVariable();
                            Collections.shuffle(gamevariable.getGamePlayerList());

                            if(gamevariable.getCheckKiller() == false)
                            {
                                Main.instance.variablePlayer.get(gamevariable.getGamePlayerList().get(0)).setHumanType(PlayerVariable.HumanType.KILLER);
                                GameVariable.Instance().setCheckKiller(true);
                                GameVariable.Instance().setKillerName(gamevariable.getGamePlayerList().get(0));
                                gamevariable.getGamePlayerList().get(0).sendMessage("당신은 킬러가 되셨습니다.");
                            }
                            else
                            {
                                player.sendMessage("킬러가 이미 존재함으로 킬러 뽑기는 스킵됩니다.");
                            }

                            player.sendMessage("테스트 " + gamevariable.getGamePlayerList());
                            player.sendMessage("테스트2 " + Main.instance.variablePlayer.get(gamevariable.getGamePlayerList().get(0)).getHumanType());

                            this.cancel();

                        }

                    }.runTaskTimer(Main.instance, 20l, 1l);

                    GameVariable.Instance().setTimeStart(true);
                    return true;

                }
                if (strings[0].equalsIgnoreCase("중지"))
                {
                    GameVariable.Instance().setGameState(GameVariable.GameState.PAUSE);
                    return true;
                }
                if (strings[0].equalsIgnoreCase("진행"))
                {
                    //GameVariable.Instance().setGameState(GameVariable.GameState.PLAY);
                    KillerHidden2 KillerHidden2 = new KillerHidden2();

                    MissionManager.Instance().setMission1Success(true);
                    MissionManager.Instance().setMission2Success(true);

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
                    player.sendMessage("참가중인 플레이어: " + gamevariable.getGamePlayerList());
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
                if(strings[0].equalsIgnoreCase("전도"))
                {
                    Hidden2Gui hidden2Gui = new Hidden2Gui();
                    hidden2Gui.initGuiItem();
                    hidden2Gui.openInventory(player);

                }


                return true;
            }
            return true;
        }
        return true;
    }

    void setPlayerVariable()
    {
        MissionManager.Instance().setMission();
        for(Player p : Bukkit.getOnlinePlayers())
        {

            if(Main.instance.adminList.isEmpty())
            {
                GameVariable.Instance().addGamePlayerList(p);
                PlayerVariable playerVariable = new PlayerVariable(p);
                Scoreboard score = new Scoreboard(p);
            }
            else
            {
                for(int i = 0; i<Main.instance.adminList.size(); i++)
                {
                    if(!Main.instance.adminList.get(i).equals(p))
                    {
                        GameVariable.Instance().addGamePlayerList(p);
                        Scoreboard score = new Scoreboard(p);
                        PlayerVariable playerVariable = new PlayerVariable(p);
                    }
                }

            }
            if (p == GameVariable.Instance().getKillerName())
            {
                Main.instance.variablePlayer.get(GameVariable.Instance().getGamePlayerList().get(0)).setHumanType(PlayerVariable.HumanType.KILLER);
                GameVariable.Instance().setCheckKiller(true);
            }



        }
    }

    void resetGame()
    {
        GameVariable.Instance().getGamePlayerList().clear();
        Main.instance.variablePlayer.clear();
        GameVariable.Instance().setCheckKiller(false);
        GameVariable.Instance().GameReset();
        MissionManager.Instance().setMission();


    }
    void help(Player p)
    {
        p.sendMessage("      죽음의 술래잡기");
        p.sendMessage("/죽술 시작 [스테이지] - 게임을 시작합니다.");
        p.sendMessage("[스테이지] - 1 : 연구소, 2 : 성당, 3: 공장");
        p.sendMessage("/죽술 중지 - 게임을 일시 중단 합니다. ");
        p.sendMessage("/죽술 진행 - 일시 중단된 게임을 재개 합니다.");
        p.sendMessage("/죽술 종료 - 게임을 종료 합니다.");
        p.sendMessage("/죽술 초기화 - 게임을 리셋 시킵니다.");
        p.sendMessage("/죽술 플레이어 - 참가하는 플레이어 목록을 확인합니다.");
        p.sendMessage("/죽술 살인마지정 닉네임 - 살인마를 지정합니다.");

        

    }

}

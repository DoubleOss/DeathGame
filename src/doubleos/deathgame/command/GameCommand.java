package doubleos.deathgame.command;



import doubleos.deathgame.Main;
import doubleos.deathgame.ablilty.*;
import doubleos.deathgame.gui.CellularGame;
import doubleos.deathgame.gui.DefectiveGame;
import doubleos.deathgame.gui.MechanicalRepair;
import doubleos.deathgame.gui.PotionMakeGui;
import doubleos.deathgame.scoreboard.Scoreboard;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                if(strings.length <1)
                {
                    help(player);
                    return true;
                }
                switch (strings[0])
                {
                    case "시작":
                        gamevariable.setGameState(GameVariable.GameState.PLAY);
                        Bukkit.broadcastMessage("잠시후 랜덤으로 킬러가 설정됩니다.");
                        setPlayerVariable();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Collections.shuffle(gamevariable.getGamePlayerList());
                                sender.sendMessage(Boolean.toString(gamevariable.getCheckKiller()));
                                if(gamevariable.getCheckKiller() == false)
                                {
                                    Main.instance.variablePlayer.get(gamevariable.getGamePlayerList().get(0)).setHumanType(PlayerVariable.HumanType.KILLER);
                                    sender.sendMessage(gamevariable.getGamePlayerList().get(0).getName());
                                    GameVariable.Instance().addKillerListName(gamevariable.getGamePlayerList().get(0));
                                    GameVariable.Instance().setOrignalKillerPlayer(gamevariable.getGamePlayerList().get(0));
                                    GameVariable.Instance().setCheckKiller(true);
                                    KillerSound sound = new KillerSound();
                                    sound.initSound((Player)sender);


                                    gamevariable.getGamePlayerList().get(0).sendMessage("당신은 킬러가 되셨습니다.");
                                }
                                else
                                {
                                    player.sendMessage("킬러가 이미 존재함으로 킬러 뽑기는 스킵됩니다.");
                                }

                                player.sendMessage("테스트 " + gamevariable.getGamePlayerList());
                                player.sendMessage("테스트2 " + Main.instance.variablePlayer.get(gamevariable.getGamePlayerList().get(0)).getHumanType());

                                GameVariable.Instance().setTimeStart(true);

                            }
                        }, 40l);
                        return true;
                    case "중지":
                        GameVariable.Instance().setGameState(GameVariable.GameState.PAUSE);
                        return true;
                    case "진행":
                        GameVariable.Instance().setGameState(GameVariable.GameState.PLAY);
                        return true;
                    case "초기화":
                        GameVariable.Instance().setGameState(GameVariable.GameState.END);
                        resetGame();
                        sender.sendMessage("초기화 되었습니다");
                        return true;
                    case "플레이어":
                        player.sendMessage("참가중인 플레이어: " + gamevariable.getGamePlayerList());
                        return true;
                    case "살인마지정":
                        if((strings[1].isEmpty())==false)
                        {
                            player.sendMessage("지정한 플레이어" + strings[1] + "으로 살인마 설정이 완료 되었습니다.");
                            GameVariable.Instance().setCheckKiller(true);
                            GameVariable.Instance().addKillerListName(Bukkit.getPlayer(strings[1]));
                            GameVariable.Instance().setOrignalKillerPlayer(Bukkit.getPlayer(strings[1]));

                            Bukkit.getPlayer(strings[1]).sendMessage("당신이 살인마로 지정되었습니다.");
                            return true;
                        }
                    case "전도":
                        Hidden2Gui hidden2Gui = new Hidden2Gui();
                        hidden2Gui.initGuiItem();
                        hidden2Gui.openInventory(player);
                        return true;
                    case"포션제작":
                        if(MissionManager.Instance().getMission1PotionCount() != 3)
                        {
                            PotionMakeGui potiongui = new PotionMakeGui();
                            potiongui.initGuiItem();
                            potiongui.openInventory(player);
                            return true;
                        }
                    case"세포게임":
                        CellularGame game = new CellularGame();
                        game.initGuiItem(((Player) sender).getPlayer());
                        game.openInventory(player);
                        return true;
                    case"기계수리":
                        MechanicalRepair repair = new MechanicalRepair();
                        repair.initGuiItem(((Player) sender).getPlayer());
                        repair.openInventory(player);
                        return true;
                    case"불량품게임":
                        DefectiveGame defgame = new DefectiveGame();
                        defgame.initGuiItem(((Player) sender).getPlayer());
                        defgame.openInventory(player);
                        return true;
                    case"미션완료":
                        if(strings[1].isEmpty() == false)
                        {
                            if(strings[1].equalsIgnoreCase("1"))
                            {
                                MissionManager.Instance().setMission1Success(true);
                            }
                            else if (strings[1].equalsIgnoreCase("2"))
                            {
                                MissionManager.Instance().setMission2Success(true);
                            }
                        }
                    case "살인마보기":
                        sender.sendMessage(GameVariable.Instance().getOrignalKillerPlayer().getName());
                    case "변신":
                        if((strings[1].isEmpty())==false)
                        {
                            MissionManager.Instance().setMission1Success(true);
                            MissionManager.Instance().setMission2Success(true);
                            if(strings[1].equalsIgnoreCase("1"))
                            {
                                KillerHidden1 killerhidden1 = new KillerHidden1();
                                killerhidden1.initKillerHidden1();
                                GameVariable.Instance().setIsKillerCheckTras(true);
                            }
                            if(strings[1].equalsIgnoreCase("2"))
                            {
                                KillerHidden2 killerhidden2 = new KillerHidden2();
                                killerhidden2.initKillerHidden2();
                                GameVariable.Instance().setIsKillerCheckTras(true);
                            }
                            if(strings[1].equalsIgnoreCase("3"))
                            {
                                KillerHidden3 killerhidden3 = new KillerHidden3();
                                killerhidden3.initKillerHidden3();
                                GameVariable.Instance().setIsKillerCheckTras(true);
                            }

                        }
                        return true;
                    case "도움말":
                        help(player);
                        return true;
                    default:
                        help(player);
                }
                return false;
            }
        }
        return false;
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

        }
    }

    void resetGame()
    {
        GameVariable.Instance().getGamePlayerList().clear();
        Main.instance.variablePlayer.clear();
        GameVariable.Instance().setCheckKiller(false);
        GameVariable.Instance().GameReset();
        MissionManager.Instance().setMission();
        GameVariable.Instance().setIsKillerCheckTras(false);


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
        p.sendMessage("/죽술 변신 [번호] - 1번 연구소 2번 성당 3번 인형공장");
        p.sendMessage("/죽술 포션제작 - 살인마 변신 미션 기능");
        p.sendMessage("/죽술 미션완료 [번호] - 1번 = 1번미션 강제완료 2번 = 2번 강제완료");


    }

}

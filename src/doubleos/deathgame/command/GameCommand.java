package doubleos.deathgame.command;



import doubleos.deathgame.Main;
import doubleos.deathgame.ablilty.*;
import doubleos.deathgame.gui.CellularGame;
import doubleos.deathgame.gui.DefectiveGame;
import doubleos.deathgame.gui.MechanicalRepair;
import doubleos.deathgame.gui.PotionMakeGui;
import doubleos.deathgame.scoreboard.Scoreboard;
import doubleos.deathgame.sound.KillerSound;
import doubleos.deathgame.util.Utils;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.inventivetalent.glow.GlowAPI;

import java.util.Collections;


public class GameCommand implements CommandExecutor
{



    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings)
    {

        if(sender instanceof Player)
        {
            GameVariable gamevariable = GameVariable.Instance();
            Player player = (Player)sender;
            if(s.equalsIgnoreCase("공지"))
            {
                if(!s.isEmpty())
                {
                    Utils.Instance().broadcastTitle(" [!]", strings.toString(), 1, 20, 1, ChatColor.WHITE);
                    return true;
                }
            }
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
                        if(!strings[1].isEmpty() || strings[1] != null)
                        {
                            gamevariable.setGameState(GameVariable.GameState.PLAY);
                            numberToSetStage(Integer.parseInt(strings[1]));

                            Utils.Instance().broadcastTitle("[!]", String.format(ChatColor.GREEN+"3초후 랜덤으로 살인마가 설정됩니다. "), 1, 60, 1, ChatColor.WHITE);
                            //Bukkit.broadcastMessage(ChatColor.RED + "[죽음의 술래잡기]" + ChatColor.WHITE +"잠시후 랜덤으로 킬러가 설정됩니다.");
                            addGamePlayerVariable();
                            Utils.Instance().randomAllTeleport();
                            gamevariable.setTeleporting(true);
                            MissionManager.Instance().initRepairBoxList();
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Collections.shuffle(gamevariable.getGamePlayerList());
                                    sender.sendMessage(Boolean.toString(gamevariable.getCheckKiller()));
                                    if(gamevariable.getCheckKiller() == false)
                                    {

                                        KillerCommon common = new KillerCommon();
                                        Player killer = Bukkit.getPlayer(gamevariable.getGamePlayerList().get(0));
                                        gamevariable.getPlayerVariableMap().get(killer.getName()).setHumanType(PlayerVariable.HumanType.KILLER);
                                        sender.sendMessage(killer.getName());
                                        common.initCommon(killer);
                                        gamevariable.addKillerListName(killer);
                                        gamevariable.setOrignalKillerPlayer(killer);
                                        gamevariable.setCheckKiller(true);
                                        playSound();

                                        //타이틀로
                                        killer.sendTitle("[!]", "당신은 살인마로 선정되셨습니다.", 0, 40, 0);
                                    }
                                    else
                                    {
                                        player.sendMessage("킬러가 이미 존재함으로 킬러 뽑기는 스킵됩니다.");
                                        playSound();
                                    }

                                    player.sendMessage("테스트 " + gamevariable.getGamePlayerList());
                                    player.sendMessage("테스트2 " + gamevariable.getPlayerVariableMap().get(gamevariable.getGamePlayerList().get(0)).getHumanType());

                                    GameVariable.Instance().setTimeStart(true);
                                    GameVariable.Instance().setTeleporting(false);

                                }
                            }, 40l);
                            allPlayerScoreBoard();
                        }
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
                    case "관전":
                        if((strings[1].isEmpty())==false || strings[1] != null)
                        {
                            Player observer = Bukkit.getPlayer(strings[1]);
                            if(GameVariable.Instance().getPlayerVariable().get(observer).getObserver()==false)
                            {
                                GameVariable.Instance().getPlayerVariable().get(observer).setObserver(true);
                                observer.sendMessage("관전 설정 되었습니다.");
                                observer.setGameMode(GameMode.SPECTATOR);
                            }
                            else
                            {
                                GameVariable.Instance().getPlayerVariable().get(observer).setObserver(false);
                                observer.sendMessage("관전 설정이 해제되었습니다.");
                                observer.setGameMode(GameMode.SURVIVAL);
                            }

                            return true;
                        }

                    case "플레이어":
                        player.sendMessage("참가중인 플레이어: " + gamevariable.getGamePlayerList());
                        return true;
                    case "살인마지정":
                        if((strings[1].isEmpty())==false || strings[1] != null)
                        {
                            player.sendMessage("지정한 플레이어" + strings[1] + "으로 살인마 설정이 완료 되었습니다.");
                            GameVariable.Instance().setCheckKiller(true);
                            GameVariable.Instance().addKillerListName(Bukkit.getPlayer(strings[1]));
                            GameVariable.Instance().setOrignalKillerPlayer(Bukkit.getPlayer(strings[1]));

                            //액션바
                            Bukkit.getPlayer(strings[1]).sendMessage("당신이 살인마로 지정되었습니다.");
                            return true;
                        }
                    case "전도":
                        Hidden2Gui hidden2Gui = new Hidden2Gui();
                        hidden2Gui.initGuiItem();
                        hidden2Gui.openInventory(player);
                        return true;
                    case "포션제작":
                        if(MissionManager.Instance().getMission1PotionCount() != 3)
                        {
                            PotionMakeGui potiongui = new PotionMakeGui();
                            potiongui.initGuiItem();
                            potiongui.openInventory(player);
                            return true;
                        }
                    case "세포게임":
                        CellularGame game = new CellularGame();
                        game.initGuiItem(((Player) sender).getPlayer());
                        game.openInventory(player);
                        return true;
                    case "기계수리":
                        MechanicalRepair repair = new MechanicalRepair();
                        repair.initGuiItem(((Player) sender).getPlayer());
                        repair.openInventory(player);
                        return true;
                    case "불량품게임":
                        DefectiveGame defgame = new DefectiveGame();
                        defgame.initGuiItem(((Player) sender).getPlayer());
                        defgame.openInventory(player);
                        return true;
                    case "미션완료":
                        if(strings[1].isEmpty() == false || strings[1] != null)
                        {
                            if(strings[1].equalsIgnoreCase("1"))
                            {
                                MissionManager.Instance().setMission1Success(true);
                            }
                            else if (strings[1].equalsIgnoreCase("2"))
                            {
                                MissionManager.Instance().setMission2Success(true);
                            }
                            return true;
                        }
                    case "살인마보기":
                        sender.sendMessage(GameVariable.Instance().getOrignalKillerPlayer().getName());
                        return true;
                    case "실패효과":
                        for(String stringName : GameVariable.Instance().getKillerPlayerList())
                        {
                            Player p = Bukkit.getPlayer(stringName);
                            GlowAPI.setGlowing((Player)sender, GlowAPI.Color.WHITE, p);
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, () ->
                        {
                            for(String stringName: GameVariable.Instance().getKillerPlayerList())
                            {
                                Player p = Bukkit.getPlayer(stringName);
                                GlowAPI.setGlowing((Player)sender, false, p);
                            }
                        }, 100l);
                        return true;
                    case "미니게임온":
                        if((strings[1].isEmpty())==false || strings[1] != null)
                        {
                            //죽슬 미니게임온 닉네임
                            Player p = Bukkit.getPlayer(strings[1]);
                            p.sendPluginMessage(Main.instance, "DeathGame", String.format("MiniGame" + "_" + "true").getBytes());
                            return true;
                        }
                        return true;
                    case "미니게임오프":
                        if((strings[1].isEmpty())==false || strings[1] != null)
                        {
                            Player p = Bukkit.getPlayer(strings[1]);;
                            p.sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "false").getBytes());
                            return true;
                        }
                        return true;
                    case "로딩활성화":
                        if((strings[1].isEmpty())==false || strings[1] != null)
                        {
                            Player p = Bukkit.getPlayer(strings[1]);
                            //죽슬 로딩활성화 닉네임 10 = 10초짜리 로딩바 활성화
                            p.sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "true" + "_" + "%s" + "_" +"%s", strings[2], strings[3]).getBytes());
                            return true;
                        }
                    case "로딩비활성화":
                        if((strings[1].isEmpty())==false || strings[1] != null)
                        {
                            Player p = Bukkit.getPlayer(strings[1]);;
                            p.sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "false").getBytes());
                            return true;
                        }

                    case "소리테스트":
                        if((strings[1].isEmpty())==false || strings[1] != null)
                        {
                            if (strings[1].equalsIgnoreCase("true"))
                            {
                                ((Player) sender).sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "true" + "_" + "%s",strings[2]).getBytes());
                                return true;
                            }
                            else
                            {
                                ((Player) sender).sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false").getBytes());
                                return true;
                            }

                        }
                    case "변신":
                        if((strings[1].isEmpty())==false || strings[1] != null)
                        {
                            numberToSetStage(Integer.parseInt(strings[1]));
                            MissionManager.Instance().setMission1Success(true);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, () ->
                            {
                                MissionManager.Instance().setMission2Success(true);
                            }, 5l);

                            /*
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

                             */

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
    void allPlayerScoreBoard()
    {
        for(String stringPlayer : GameVariable.Instance().getGamePlayerList())
        {
            Player p = Bukkit.getPlayer(stringPlayer);
            Scoreboard scoreboard = new Scoreboard(p);
        }
    }

    void addGamePlayerVariable()
    {
        MissionManager.Instance().setMission();
        GameVariable gameVariable = GameVariable.Instance();
        for(Player p : Bukkit.getOnlinePlayers())
        {
            if(gameVariable.adminList.isEmpty())
            {
                gameVariable.addGamePlayerList(p);
                gameVariable.addPlayerVarible(p, gameVariable.getPlayerVariable().get(p.getName()));
                //PlayerVariable playerVariable = new PlayerVariable(p);
            }
            else
            {
                for(int i = 0; i<gameVariable.adminList.size(); i++)
                {
                    if(!gameVariable.adminList.get(i).equals(p))
                    {
                        gameVariable.addGamePlayerList(p);
                        gameVariable.addPlayerVarible(p, gameVariable.getPlayerVariable().get(p.getName()));

                        //PlayerVariable playerVariable = new PlayerVariable(p);
                    }
                }

            }

        }
    }

    void numberToSetStage(int number)
    {
        if(number == 1)
        {
            GameVariable.Instance().setGameStage(GameVariable.GameStage.LAB);
        }
        else if(number == 2)
        {
            GameVariable.Instance().setGameStage(GameVariable.GameStage.CATHEDRAL);
        }
        else if (number == 3)
        {
            GameVariable.Instance().setGameStage(GameVariable.GameStage.FACTORY);
        }
    }

    void playSound()
    {
        KillerSound sound = new KillerSound();
        Player player = Bukkit.getPlayer(GameVariable.Instance().getGamePlayerList().get(0));
        sound.initSound(player);
    }
    void resetGame()
    {
        GameVariable.Instance().GameReset();
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
        p.sendMessage("/죽술 관전 - 관전모드로 전환, 해제 기능");
        


    }

}

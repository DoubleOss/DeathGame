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
                            gamevariable.GameReset();
                            gamevariable.setGameState(GameVariable.GameState.PLAY);
                            numberToSetStage(Integer.parseInt(strings[1]));

                            Utils.Instance().broadcastTitle("[!]", String.format(ChatColor.GREEN+"잠시후 랜덤으로 살인마가 설정됩니다. "), 1, 60, 1, ChatColor.WHITE);
                            //Bukkit.broadcastMessage(ChatColor.RED + "[죽음의 술래잡기]" + ChatColor.WHITE +"잠시후 랜덤으로 킬러가 설정됩니다.");
                            addGamePlayerVariable();
                            Utils.Instance().randomAllTeleport();
                            gamevariable.setTeleporting(true);
                            for(String stringName : gamevariable.getGamePlayerList())
                            {
                                Bukkit.getPlayer(stringName).setWalkSpeed(0);
                            }

                            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Collections.shuffle(gamevariable.getGamePlayerList());
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
                                        giveItem();

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
                            MissionManager.Instance().initRepairBoxList();
                            MissionManager.Instance().initMissionBox();
                            //playSound();
                            for(String stringName : gamevariable.getGamePlayerList())
                            {
                                Bukkit.getPlayer(stringName).setWalkSpeed(0.2f);
                            }
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
                            if(GameVariable.Instance().getPlayerListVariableMap().get(observer.getName()).getObserver()==false)
                            {
                                GameVariable.Instance().getPlayerListVariableMap().get(observer.getName()).setObserver(true);
                                GameVariable.Instance().adminList.add(observer.getName());
                                observer.sendMessage("관전 설정 되었습니다.");
                                //observer.setGameMode(GameMode.SPECTATOR);
                                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "Gamemode 3 "+observer.getPlayer().getName());
                            }
                            else
                            {
                                GameVariable.Instance().getPlayerListVariableMap().get(observer.getName()).setObserver(false);
                                GameVariable.Instance().adminList.remove(observer.getName());
                                observer.sendMessage("관전 설정이 해제되었습니다.");
                                observer.setGameMode(GameMode.SURVIVAL);
                            }
                            return true;
                        }

                    case "플레이어":
                        player.sendMessage("참가중인 플레이어: " + gamevariable.getGamePlayerList());
                        return true;
                    case "관전플레이어":
                        ArrayList<String> playerName = new ArrayList<>();
                        for(Player p : Bukkit.getOnlinePlayers())
                        {
                            if(GameVariable.Instance().getPlayerListVariableMap().get(p.getName()).getObserver())
                            {
                                playerName.add(p.getName());
                            }
                        }
                        sender.sendMessage("관전 플레이어: " + playerName.toString());
                        return true;
                    case "살인마지정":
                        if((strings[1].isEmpty())==false || strings[1] != null)
                        {
                            player.sendMessage("지정한 플레이어" + strings[1] + "으로 살인마 설정이 완료 되었습니다.");
                            KillerCommon common = new KillerCommon();
                            Player killer = Bukkit.getPlayer(gamevariable.getGamePlayerList().get(0));
                            killer.performCommand("살인자연구소시작아이템 설정");
                            gamevariable.getPlayerVariableMap().get(killer.getName()).setHumanType(PlayerVariable.HumanType.KILLER);
                            sender.sendMessage(killer.getName());
                            common.initCommon(killer);
                            gamevariable.addKillerListName(killer);
                            gamevariable.setOrignalKillerPlayer(killer);
                            gamevariable.setCheckKiller(true);

                            //액션바
                            //Bukkit.getPlayer(strings[1]).sendMessage("당신이 살인마로 지정되었습니다.");
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
                    case "플레이어정보":
                        if(!strings[1].isEmpty() || strings[1] != null)
                        {
                            Player p = Bukkit.getPlayer(strings[1]);
                            sender.sendMessage("플레이어명: " + p.getName());
                            sender.sendMessage("휴먼 타입: " + gamevariable.getPlayerVariableMap().get(p.getName()).getHumanType());
                            sender.sendMessage("킬러 타입: " + gamevariable.getPlayerVariableMap().get(p.getName()).getKillerType());
                            sender.sendMessage("게임 탈락 여부: " + gamevariable.getPlayerVariableMap().get(p.getName()).getObserver());
                            sender.sendMessage("게임 옵저버여부: " + gamevariable.getPlayerListVariableMap().get(p.getName()).getObserver());
                            return true;
                        }
                    case "탈출완료":
                        if(!gamevariable.getPlayerVariableMap().get(sender.getName()).getEscape())
                        {
                            gamevariable.getPlayerVariableMap().get(sender.getName()).setEscape(true);
                            int count = gamevariable.getGamePlayerList().size() - gamevariable.getKillerPlayerList().size();
                            for(Player p : Bukkit.getOnlinePlayers())
                            {
                                if(p.isOp())
                                {
                                    p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.RED+ sender.getName() +  ChatColor.WHITE +" 님이 탈출 하셨습니다. 남은인원 : " + gamevariable.getEscapePlayerCount() + " |" + count);
                                }
                            }
                        }
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
                    case "인벤초기화":
                        for(Player p : Bukkit.getOnlinePlayers())
                        {
                            if(!p.isOp())
                            {
                                Utils.Instance().inventoryClear(p);
                            }
                        }
                    case "실패효과":
                        for(Player p : Bukkit.getOnlinePlayers())
                        {
                            if(p.isOp())
                                p.sendMessage(ChatColor.GOLD + "[알림] "+ChatColor.RED+ sender.getName() +ChatColor.WHITE + " 님이 미니게임에 실패하였습니다.");
                        }
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
                            p.sendPluginMessage(Main.instance, "DeathGame", String.format("MiniGame" + "_" + "false").getBytes());
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
        for(Player player : Bukkit.getOnlinePlayers())
        {
            Scoreboard scoreboard = new Scoreboard(player);
        }
    }
    void addGamePlayerVariable()
    {
        MissionManager.Instance().setMission();
        GameVariable gameVariable = GameVariable.Instance();
        gameVariable.addPlayerVarible();

    }
    void giveItem()
    {
        GameVariable gameVariable = GameVariable.Instance();

        for(Player p : Bukkit.getOnlinePlayers())
        {
            if(!gameVariable.getPlayerListVariableMap().get(p.getName()).getObserver())
            {
                if(gameVariable.getPlayerVariableMap().get(p.getName()).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
                {
                    if(!gameVariable.getPlayerListVariableMap().get(p.getName()).getObserver())
                    {
                        if(gameVariable.getGameStage().equals(GameVariable.GameStage.LAB))
                            p.performCommand("연구소시작아이템 지급");
                        else if(gameVariable.getGameStage().equals(GameVariable.GameStage.CATHEDRAL))
                            p.performCommand("성당시작아이템 지급");
                        else if(gameVariable.getGameStage().equals(GameVariable.GameStage.FACTORY))
                            p.performCommand("인형공장시작아이템 지급");
                    }
                }
                else if(gameVariable.getPlayerVariableMap().get(p.getName()).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                {
                    if(gameVariable.getGameStage().equals(GameVariable.GameStage.LAB))
                        p.performCommand("살인자연구소시작아이템 지급");
                    else if(gameVariable.getGameStage().equals(GameVariable.GameStage.CATHEDRAL))
                        p.performCommand("살인마성당시작아이템 지급");
                    else if(gameVariable.getGameStage().equals(GameVariable.GameStage.FACTORY))
                        p.performCommand("살인마인형공장시작아이템 지급");
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
        p.sendMessage("/죽술 초기화 - 게임을 리셋 시킵니다.");
        p.sendMessage("/죽술 플레이어 - 참가하는 플레이어 목록을 확인합니다.");
        p.sendMessage("/죽술 관전플레이어 - 관전하는 플레이어 목록을 확인합니다.");
        p.sendMessage("/죽술 살인마지정 닉네임 - 살인마를 지정합니다.");
        p.sendMessage("/죽술 변신 [번호] - 1번 연구소 2번 성당 3번 인형공장");
        p.sendMessage("/죽술 미션완료 [번호] - 1번 = 1번미션 강제완료 2번 = 2번 강제완료");
        p.sendMessage("/죽술 관전 닉네임 - 관전모드로 전환, 해제 기능");
        p.sendMessage("/죽술 플레이어정보 닉네임 - 게임 관련 변수를 띄워줍니다.");
        p.sendMessage("/죽술 인벤초기화 - 오피를 제외한 모든 플레이어들의 인벤토리를 초기화 합니다.");

    }

}

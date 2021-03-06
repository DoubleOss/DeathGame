package doubleos.deathgame.command;



import doubleos.deathgame.Main;
import doubleos.deathgame.ablilty.*;
import doubleos.deathgame.gui.CellularGame;
import doubleos.deathgame.gui.DefectiveGame;
import doubleos.deathgame.gui.MechanicalRepair;
import doubleos.deathgame.gui.PotionMakeGui;
import doubleos.deathgame.scoreboard.Scoreboard;
import doubleos.deathgame.sound.HeartSound;
import doubleos.deathgame.sound.KillerSound;
import doubleos.deathgame.util.Utils;
import doubleos.deathgame.variable.GameItem;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.MissionManager;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
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
            MissionManager missionManager = MissionManager.Instance();
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
                        if(!strings[1].isEmpty() || strings[1] != null)
                        {
                            //gamevariable.GameReset();
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
                                        gamevariable.getPlayerVariableMap().get(killer.getName()).setKillerType(PlayerVariable.KillerType.COMMON);
                                        sender.sendMessage(killer.getName());
                                        giveItem();
                                        common.initCommon(killer);
                                        gamevariable.addKillerListName(killer);
                                        gamevariable.setOrignalKillerPlayer(killer);
                                        gamevariable.setCheckKiller(true);
                                        numberToSetStageAbility(Integer.parseInt(strings[1]));

                                        //타이틀로
                                        killer.sendTitle("[!]", "당신은 살인마로 선정되셨습니다.", 0, 40, 0);
                                    }
                                    else
                                    {
                                        player.sendMessage("킬러가 이미 존재함으로 킬러 뽑기는 스킵됩니다.");
                                        giveItem();
                                        KillerCommon common = new KillerCommon();
                                        common.initCommon(gamevariable.getOrignalKillerPlayer());
                                        numberToSetStageAbility(Integer.parseInt(strings[1]));

                                    }

                                    player.sendMessage("테스트 " + gamevariable.getGamePlayerList());
                                    player.sendMessage("테스트2 " + gamevariable.getPlayerVariableMap().get(gamevariable.getGamePlayerList().get(0)).getHumanType());

                                }
                            }, 40l);
                            BukkitTask tasks = new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    allPlayerScoreBoard();
                                    missionManager.initRepairBoxList();
                                    missionManager.initMissionBox();
                                    for(Player p1 : Bukkit.getOnlinePlayers())
                                    {
                                        p1.sendTitle("[!]", "30초 동안 스토리북을 읽을 시간이 주어집니다.", 0, 60, 0);
                                    }
                                    this.cancel();
                                }
                            }.runTaskTimer(Main.instance, 80l, 80l);

                            BukkitTask task = new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    for(String stringName : gamevariable.getGamePlayerList())
                                    {
                                        Bukkit.getPlayer(stringName).setWalkSpeed(0.2f);
                                        Bukkit.getPlayer(stringName).sendTitle("[!]", "게임이 시작되었습니다.", 0, 60, 0);
                                    }
                                    GameVariable.Instance().setTimeStart(true);
                                    GameVariable.Instance().setTeleporting(false);
                                    MissionManager.Instance().loopingRepairBox();
                                    KillerSound killersound = new KillerSound();
                                    killersound.initSound(gamevariable.getOrignalKillerPlayer());
                                    Bukkit.broadcastMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE +" 게임이 시작되었습니다!");
                                    this.cancel();

                                }
                            }.runTaskTimer(Main.instance, 600l, 600l);
                            //playSound();

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
                            if(gamevariable.getPlayerListVariableMap().get(observer.getName()).getObserver()==false)
                            {
                                gamevariable.getPlayerListVariableMap().get(observer.getName()).setObserver(true);
                                gamevariable.adminList.add(observer.getName());
                                observer.sendMessage("관전 설정 되었습니다.");
                                //observer.setGameMode(GameMode.SPECTATOR);
                                observer.getPlayer().performCommand("관전");
                            }
                            else
                            {
                                gamevariable.getPlayerListVariableMap().get(observer.getName()).setObserver(false);
                                gamevariable.adminList.remove(observer.getName());
                                observer.sendMessage("관전 설정이 해제되었습니다.");
                                observer.setGameMode(GameMode.SURVIVAL);
                            }
                            return true;
                        }
                        return true;

                    case "플레이어":
                        player.sendMessage("참가중인 플레이어: " + gamevariable.getGamePlayerList());
                        return true;
                    case "관전플레이어":
                        ArrayList<String> playerName = new ArrayList<>();
                        for(Player p : Bukkit.getOnlinePlayers())
                        {
                            if(gamevariable.getPlayerListVariableMap().get(p.getName()).getObserver())
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
                            Player killer = Bukkit.getPlayer(strings[1]);
                            gamevariable.getPlayerVariableMap().put(killer.getName(), gamevariable.getPlayerListVariableMap().get(killer.getName()));
                            gamevariable.getPlayerVariableMap().get(killer.getName()).setHumanType(PlayerVariable.HumanType.KILLER);
                            gamevariable.getPlayerVariableMap().get(killer.getName()).setKillerType(PlayerVariable.KillerType.COMMON);
                            gamevariable.getGamePlayerList().add(killer.getName());
                            sender.sendMessage(killer.getName());
                            //common.initCommon(killer);
                            gamevariable.addKillerListName(killer);
                            gamevariable.setOrignalKillerPlayer(killer);
                            gamevariable.setCheckKiller(true);
                            //액션바
                            //Bukkit.getPlayer(strings[1]).sendMessage("당신이 살인마로 지정되었습니다.");
                            return true;
                        }
                        return true;
                    case "구속해제":
                        if((strings[1].isEmpty())==false || strings[1] != null)
                        {
                            Player p3 = Bukkit.getPlayer(strings[1]);
                            p3.setWalkSpeed(0.2f);
                        }
                        return true;

                    case "전도":
                        Hidden2Gui hidden2Gui = new Hidden2Gui();
                        hidden2Gui.initGuiItem();
                        hidden2Gui.openInventory(player);
                        return true;
                    case "포션제작":
                        if(missionManager.getMission1PotionCount() != 3)
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
                            sender.sendMessage("킬러 소리 여부: " + gamevariable.getPlayerListVariableMap().get(p.getName()).getSoundKillerPlaying());
                            sender.sendMessage("체력 소리 여부: " + gamevariable.getPlayerListVariableMap().get(p.getName()).getSoundPlaying());
                            sender.sendMessage("아이템 미니게임 여부: " + gamevariable.getPlayerListVariableMap().get(p.getName()).getMiniGamePlaying());
                            sender.sendMessage("남은 체력 여부: " + gamevariable.getPlayerListVariableMap().get(p.getName()).getLife());
                            sender.sendMessage("상자 오픈 가능 여부: " + gamevariable.getPlayerListVariableMap().get(p.getName()).getBoxOpen());
                            sender.sendMessage("힐키트 사용중 여부: " + gamevariable.getPlayerListVariableMap().get(p.getName()).getHealKit());
                            return true;
                        }
                        return true;
                    case "게임정보":
                        sender.sendMessage("");
                        sender.sendMessage("     게임 정보");
                        sender.sendMessage("");
                        for (String stringPlayer : gamevariable.getGamePlayerList())
                        {
                            if(gamevariable.getPlayerVariableMap().get(stringPlayer).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
                            {
                                String survive = (gamevariable.getPlayerVariableMap().get(stringPlayer).getObserver() ? ChatColor.RED + "탈락" : ChatColor.GREEN + "생존" );
                                String escape = (gamevariable.getPlayerVariableMap().get(stringPlayer).getEscape() ? ChatColor.GREEN + "탈출" : ChatColor.RED + "미탈출");
                                sender.sendMessage(ChatColor.WHITE + "플레이어명 : "+stringPlayer + "  생존여부 : " + survive + ChatColor.WHITE  +"  탈출 여부 : " + escape);
                            }

                        }
                        sender.sendMessage("");
                        sender.sendMessage("");
                        int rapaircount = 0;
                        for(Location loc : missionManager.getRepairBoxList())
                        {
                            rapaircount++;
                            String stringRepair = (missionManager.getRepairBoxClassMap().get(loc).getRepair() ?  ChatColor.GREEN +  "O" : ChatColor.RED + "X" );
                            String stringHealth = String.format("%.2f",missionManager.getRepairBoxClassMap().get(loc).gethealth());
                            String stringRepiring = (missionManager.getRepairBoxClassMap().get(loc).getRepairing() ? ChatColor.GREEN + "O" :  ChatColor.RED +"X");

                            sender.sendMessage(ChatColor.WHITE + "배전박스 "+rapaircount+" - 수리 완료 : " + stringRepair + ChatColor.WHITE  +",  체력 : " + stringHealth + " | 120" + ",  수리 중 : " + stringRepiring);
                        }
                        sender.sendMessage("");
                        return true;
                    case "탈출완료":
                        if(strings[1] != null)
                        {
                            Player p4 = Bukkit.getPlayer(strings[1]);
                            if(!gamevariable.getPlayerListVariableMap().get(p4).getObserver())
                            {
                                if(gamevariable.getPlayerVariableMap().get(p4.getName()).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
                                {
                                    if(!gamevariable.getPlayerVariableMap().get(p4.getName()).getEscape())
                                    {
                                        gamevariable.getPlayerVariableMap().get(p4.getName()).setEscape(true);
                                        int deathCount = 0;
                                        for(Player p : Bukkit.getOnlinePlayers())
                                        {
                                            if(gamevariable.getPlayerVariableMap().get(p.getName()) != null)
                                            {
                                                if(gamevariable.getPlayerVariableMap().get(p.getName()).getObserver())
                                                {
                                                    deathCount++;
                                                }
                                            }

                                        }
                                        int count = gamevariable.getGamePlayerList().size() - gamevariable.getKillerPlayerList().size() - deathCount;
                                        gamevariable.setEscapePlayerCount(gamevariable.getEscapePlayerCount()+1);
                                        for(Player p : Bukkit.getOnlinePlayers())
                                        {
                                            if(p.isOp())
                                            {
                                                p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.RED+ p4.getName() +  ChatColor.WHITE +" 님이 탈출 하셨습니다. 남은인원 : " + gamevariable.getEscapePlayerCount() + " | " + count);
                                            }
                                        }
                                        if(count - gamevariable.getEscapePlayerCount() == 0 && gamevariable.getEscapePlayerCount() >= 3)
                                        {
                                            for(Player p2 : Bukkit.getOnlinePlayers())
                                            {
                                                if(p2.isOp())
                                                    p2.sendMessage(ChatColor.GOLD + "[알림] " + ChatColor.WHITE +" 게임이 종료되었습니다.");
                                            }
                                            //gamevariable.GameReset();
                                        }
                                        return true;
                                    }
                                }

                            }
                        }
                        return true;
                    case "게임템지급":
                        ((Player) sender).getInventory().addItem(GameItem.Instance().m_humanHeal_Ability1_Item);
                        ((Player) sender).getInventory().addItem(GameItem.Instance().m_humanCom_Ability1_Item);
                        return true;
                    case "살인마보기":
                        sender.sendMessage(gamevariable.getOrignalKillerPlayer().getName());
                        return true;
                    case "인벤초기화":
                        for(Player p : Bukkit.getOnlinePlayers())
                        {
                            if(!p.isOp())
                            {
                                Utils.Instance().inventoryClear(p);
                            }
                        }
                        return true;
                    case "성공효과":
                        if (strings[1].equalsIgnoreCase("box"))
                        {
                            gamevariable.getPlayerVariableMap().get(sender.getName()).setBoxOpen(true);
                            gamevariable.getPlayerVariableMap().get(sender.getName()).setMiniGamePlaying(false);
                            sender.sendMessage(ChatColor.RED + "[죽음의 술래잡기]" + ChatColor.WHITE +" 미니게임에 클리어 하여 상자를 1회 열 수 있습니다.");
                            BukkitTask task = new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    gamevariable.getPlayerVariableMap().get(sender.getName()).setBoxOpen(false);
                                    this.cancel();
                                }
                            }.runTaskTimer(Main.instance, 300l, 300l);
                        }
                        return true;
                    case "실패효과":
                        for(Player p : Bukkit.getOnlinePlayers())
                        {
                            if(p.isOp())
                                p.sendMessage(ChatColor.GOLD + "[알림] "+ChatColor.RED+ sender.getName() +ChatColor.WHITE + " 님이 미니게임에 실패하였습니다.");
                        }
                        if(strings[1].equalsIgnoreCase("repair"))
                        {
                            for(Location loc : MissionManager.Instance().getRepairBoxList())
                            {
                                if(((Player) sender).getLocation().distance(loc) < 3)
                                {
                                    //LoddingBar_modify_60_10 = 로딩바 60초 짜리 로딩바를 10초 깎음
                                    float health = missionManager.getRepairBoxClassMap().get(loc).gethealth();
                                    missionManager.getRepairBoxClassMap().get(loc).sethealth(health - 10);
                                    ((Player) sender).sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "modify" + "_" + "120" +"_"+ "10").getBytes());
                                    for(String stringName : gamevariable.getKillerPlayerList())
                                    {
                                        Player p = Bukkit.getPlayer(stringName);
                                        GlowAPI.setGlowing((Player)sender, GlowAPI.Color.WHITE, p);
                                    }
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, () ->
                                    {
                                        for(String stringName: gamevariable.getKillerPlayerList())
                                        {
                                            Player p = Bukkit.getPlayer(stringName);
                                            GlowAPI.setGlowing((Player)sender, false, p);
                                        }
                                    }, 100l);
                                    return true;
                                }
                            }
                        }
                        if(strings[1].equalsIgnoreCase("repair"))
                        {
                            gamevariable.getPlayerVariableMap().get(sender.getName()).setMiniGamePlaying(false);
                        }
                        return true;
                    case "미니게임온":
                        if((strings[1].isEmpty())==false || strings[1] != null)
                        {
                            //죽슬 미니게임온 닉네임
                            Player p = Bukkit.getPlayer(strings[1]);
                            p.sendPluginMessage(Main.instance, "DeathGame", String.format("MiniGame" + "_" + "true"+"_" + "none").getBytes());
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
                                ((Player) sender).sendPluginMessage(Main.instance, "DeathGame", String.format("HeartSound" + "_" + "false" + "_" + "%s", strings[2]).getBytes());
                                return true;
                            }

                        }
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
                p.getInventory().addItem(GameItem.Instance().m_glowStone_Item);
                if(gameVariable.getPlayerVariableMap().get(p.getName()).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
                {
                    if(!gameVariable.getPlayerListVariableMap().get(p.getName()).getObserver())
                    {
                        p.getInventory().addItem(GameItem.Instance().m_humanCom_Ability1_Item);
                        if(gameVariable.getGameStage().equals(GameVariable.GameStage.LAB))
                        {
                            p.performCommand("연구소시작아이템 지급");
                        }
                        else if(gameVariable.getGameStage().equals(GameVariable.GameStage.CATHEDRAL))
                        {
                            p.performCommand("성당시작아이템 지급");
                        }
                        else if(gameVariable.getGameStage().equals(GameVariable.GameStage.FACTORY))
                        {
                            p.performCommand("인형공장시작아이템 지급");
                        }

                    }
                }
                else if(gameVariable.getPlayerVariableMap().get(p.getName()).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                {
                    if(gameVariable.getGameStage().equals(GameVariable.GameStage.LAB))
                    {
                        p.performCommand("살인자연구소시작아이템 지급");
                    }
                    else if(gameVariable.getGameStage().equals(GameVariable.GameStage.CATHEDRAL))
                    {
                        p.performCommand("살인마성당시작아이템 지급");
                    }
                    else if(gameVariable.getGameStage().equals(GameVariable.GameStage.FACTORY))
                    {
                        p.performCommand("살인마인형공장시작아이템 지급");
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
    void numberToSetStageAbility(int number)
    {
        GameVariable gameVariable = GameVariable.Instance();
        if(number == 1)
        {
            KillerHidden1 killerHidden1 = new KillerHidden1();
            killerHidden1.initKillerHidden1();
        }
        else if(number == 2)
        {
            KillerHidden2 killerHidden2 = new KillerHidden2();
            killerHidden2.initKillerHidden2();
        }
        else if (number == 3)
        {
            KillerHidden3 killerHidden3 = new KillerHidden3();
            killerHidden3.initKillerHidden3();
        }
    }

    void playSound()
    {
        HeartSound sound = new HeartSound();
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
        p.sendMessage("/죽술 관전 닉네임 - 관전모드로 전환, 해제 기능");
        p.sendMessage("/죽술 플레이어정보 닉네임 - 게임 관련 변수를 띄워줍니다.");
        p.sendMessage("/죽술 게임정보 - 게임 탈락한 인원 및 생존자 탈출자등을 표시합니다.");

        p.sendMessage("/죽술 인벤초기화 - 오피를 제외한 모든 플레이어들의 인벤토리를 초기화 합니다.");


    }

}

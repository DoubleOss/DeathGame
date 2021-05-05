package doubleos.deathgame.event;

import doubleos.deathgame.Main;
import doubleos.deathgame.ablilty.KillerCommon;
import doubleos.deathgame.command.GameCommand;
import doubleos.deathgame.variable.GameVariable;
import doubleos.deathgame.variable.PlayerVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Kill implements Listener
{

    @EventHandler
    void onDeathEvent(PlayerDeathEvent event)
    {
        if(GameVariable.Instance().getKillerListName(event.getEntity().getKiller()) != null)
        {
            if(checkPlayingGamePlayer(event.getEntity().getPlayer()))
            {

                event.setDeathMessage("");
                for(Player p : Bukkit.getOnlinePlayers())
                {
                    if(p.isOp())
                    {
                        p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.WHITE + "살인마 " + ChatColor.RED + event.getEntity().getKiller().getName() + ChatColor.WHITE + "님이 "
                                +ChatColor.GOLD+ event.getEntity().getPlayer().getName() +ChatColor.WHITE + " 님을 살해 하셨습니다." );
                    }
                }
                if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.CATHEDRAL))
                {
                    if(GameVariable.Instance().getHidden2Target().equals(event.getEntity().getPlayer().getName()))
                    {

                        KillerCommon common = new KillerCommon();
                        common.initCommon(event.getEntity());
                        //event.getEntity().performCommand("살인자연구소시작아이템 설정");
                        event.getEntity().getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + " 당신은 전도를 받아 살인마로 다시 부활합니다.");
                        GameVariable.Instance().getPlayerVariableMap().get(event.getEntity().getName()).setHumanType(PlayerVariable.HumanType.KILLER);
                        GameVariable.Instance().addKillerListName(event.getEntity());
                        GameVariable.Instance().setHidden2Targer(null);
                        setKillColltime(event.getEntity().getKiller());

                        for(Player p1 : Bukkit.getOnlinePlayers())
                        {
                            if(p1.isOp())
                            {
                                p1.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.WHITE + "살인마의 전도가 성공하여 " + ChatColor.RED + event.getEntity().getPlayer().getName() + ChatColor.WHITE + " 님이 살인마로 다시 부활 하셨습니다.");
                            }
                        }
                        //if(GameVariable.Instance().getGamePlayerList().size() == GameVariable.Instance().getKillerPlayerList().size())
                        int deathCount = getGameDeathCount();
                        if(GameVariable.Instance().getGamePlayerList().size() - GameVariable.Instance().getKillerPlayerList().size() - deathCount == 0)
                        {
                            for(Player p2 : Bukkit.getOnlinePlayers())
                            {
                                p2.sendTitle("[!]", ChatColor.GREEN+ "남은 기자들이 살인마로 변하여 게임이 종료됩니다.", 1, 30, 1);
                                p2.sendMessage( ChatColor.GREEN+ "남은 기자들이 살인마로 변하여 게임이 종료됩니다.");
                                p2.performCommand("spawn");
                                GameVariable.Instance().GameReset();
                            }

                        }

                    }
                    else if(GameVariable.Instance().getHidden2Target() == null)
                    {
                        event.getEntity().getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + ": 당신은 살인마에 의해 살해 당하였습니다.");
                        GameVariable.Instance().getPlayerVariableMap().get(event.getEntity().getPlayer().getName()).setObserver(true);
                        setKillColltime(event.getEntity().getKiller());
                        if(GameVariable.Instance().getGamePlayerList().size() - GameVariable.Instance().getKillerPlayerList().size() - getGameDeathCount() == 0)
                        {
                            for(Player p2 : Bukkit.getOnlinePlayers())
                            {
                                p2.sendTitle("[!]", ChatColor.GREEN+ " 모든 생존자들이 죽어 게임이 종료됩니다.", 1, 30, 1);
                                p2.sendMessage( ChatColor.GREEN+ " 모든 생존자들이 죽어 게임이 종료됩니다.");
                                p2.performCommand("spawn");
                                GameVariable.Instance().GameReset();
                            }
                            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"영상 전체재생 death.mp4");
                        }
                    }
                    else
                    {
                        event.getEntity().getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + ": 당신은 살인마에 의해 살해 당하였습니다.");
                        GameVariable.Instance().getPlayerVariableMap().get(event.getEntity().getPlayer().getName()).setObserver(true);
                        GameVariable.Instance().setHidden2Targer(null);
                        GameVariable.Instance().getOrignalKillerPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + ": 전도에 실패 하셨습니다.");
                        setKillColltime(event.getEntity().getKiller());
                        for(Player p : Bukkit.getOnlinePlayers())
                        {
                            if(p.isOp())
                            {
                                p.sendMessage(ChatColor.GOLD + "[알림] "+ ChatColor.WHITE + "살인마의 전도에 실패 하였습니다");
                            }
                        }
                        if(GameVariable.Instance().getGamePlayerList().size() - GameVariable.Instance().getKillerPlayerList().size() - getGameDeathCount() == 0)
                        {
                            for(Player p2 : Bukkit.getOnlinePlayers())
                            {
                                p2.sendTitle("[!]", ChatColor.GREEN+ " 모든 생존자들이 죽어 게임이 종료됩니다.", 1, 30, 1);
                                p2.sendMessage( ChatColor.GREEN+ " 모든 생존자들이 죽어 게임이 종료됩니다.");
                                p2.performCommand("spawn");
                                GameVariable.Instance().GameReset();

                            }
                            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"영상 전체재생 death.mp4");

                        }
                    }

                }
                else
                {
                    if(GameVariable.Instance().getHidden2Target() == null)
                    {
                        event.getEntity().getPlayer().sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + ": 당신은 살인마에 의해 살해 당하였습니다.");
                        GameVariable.Instance().getPlayerVariableMap().get(event.getEntity().getPlayer().getName()).setObserver(true);
                        setKillColltime(event.getEntity().getKiller());
                        if(GameVariable.Instance().getGamePlayerList().size() - GameVariable.Instance().getKillerPlayerList().size() - getGameDeathCount() == 0)
                        {
                            for(Player p2 : Bukkit.getOnlinePlayers())
                            {
                                p2.sendTitle("[!]", ChatColor.GREEN+ " 모든 생존자들이 죽어 게임이 종료됩니다.", 1, 30, 1);
                                p2.sendMessage( ChatColor.GREEN+ " 모든 생존자들이 죽어 게임이 종료됩니다.");
                                p2.performCommand("spawn");
                                GameVariable.Instance().GameReset();
                            }
                            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"영상 전체재생 death.mp4");

                        }

                    }

                }

            }

        }
    }

    @EventHandler
    void onRespawn(PlayerRespawnEvent event)
    {
        if(checkPlayingGamePlayer(event.getPlayer()))
        {
            if(GameVariable.Instance().getPlayerVariableMap().get(event.getPlayer().getName()).getObserver())
            {
                //event.getPlayer().setGameMode(GameMode.SPECTATOR);
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "Gamemode 3 "+event.getPlayer().getName());
            }
            else if(GameVariable.Instance().getGameStage().equals(GameVariable.GameStage.CATHEDRAL))
            {
                if(GameVariable.Instance().getPlayerVariableMap().get(event.getPlayer().getName()).getHumanType().equals(PlayerVariable.HumanType.KILLER))
                {
                    Location loc = new Location(Bukkit.getWorld("world"), -532, 62, 67);
                    event.getPlayer().teleport(loc);

                }
            }
        }

    }


    void setKillColltime(Player p)
    {
        GameVariable gameVariable = GameVariable.Instance();
        if(!gameVariable.getKillCoolTime())
        {
            gameVariable.setKillCoolTimeTimer(30);
            p.sendMessage(ChatColor.RED + "[죽음의 술래잡기]" +ChatColor.WHITE + " 상대방을 죽여 다음 킬은 " +gameVariable.getKillCoolTimeTimer()+ " 초 이후에 가능합니다.");
            gameVariable.setKillCoolTime(true);
            BukkitTask task = new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if(gameVariable.getKillCoolTimeTimer() <= 0)
                    {
                        gameVariable.setKillCoolTime(false);
                    }
                    else if(gameVariable.getGameState().equals(GameVariable.GameState.END))
                    {
                        gameVariable.setKillCoolTime(false);
                        this.cancel();
                    }
                    else
                    {
                        gameVariable.setKillCoolTimeTimer(gameVariable.getKillCoolTimeTimer()-1);
                    }

                }
            }.runTaskTimer(Main.instance, 20l, 20l);
        }
    }



    boolean checkPlayingGamePlayer(Player player)
    {
        for(String stringPlayer : GameVariable.Instance().getGamePlayerList())
        {
            if(stringPlayer.equals(player.getName()))
            {
                return true;
            }
        }
        return false;
    }


    int getGameDeathCount()
    {
        int deathCount = 0;
        for(String s : GameVariable.Instance().getGamePlayerList())
        {
            if(GameVariable.Instance().getPlayerVariableMap().get(s).getHumanType().equals(PlayerVariable.HumanType.HUMAN))
            {
                if(GameVariable.Instance().getPlayerVariableMap().get(s).getObserver())
                {
                    deathCount++;
                }
            }
        }
        return deathCount;
    }
}

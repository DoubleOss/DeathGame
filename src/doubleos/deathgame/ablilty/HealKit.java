package doubleos.deathgame.ablilty;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import doubleos.deathgame.Main;
import doubleos.deathgame.variable.GameItem;
import doubleos.deathgame.variable.GameVariable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class HealKit
{

    public void initHealKit(Player player)
    {
        GameVariable gameVariable = GameVariable.Instance();
        String stringPlayer = player.getName();
        player.setWalkSpeed(0f);
        player.sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "true" + "_" + "7" + "_" +"0").getBytes());
        ActionBarAPI.sendActionBar(player, "회복중 입니다.",  50);
        BukkitTask task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(!gameVariable.getPlayerVariableMap().get(stringPlayer).getHealKit())
                {
                    player.setWalkSpeed(0.2f);
                    player.sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "false").getBytes());
                    this.cancel();
                }
                else
                {
                    if((player.getHealth()+10) >= 20)
                        player.setHealth(player.getMaxHealth());
                    else
                        player.setHealth(player.getHealth()+10);
                    player.sendMessage(ChatColor.RED + "[죽음의 술래잡기]" + ChatColor.WHITE +" 회복 되셨습니다.");
                    GameItem.Instance().m_humanHeal_Ability1_Item.setAmount(1);
                    player.getInventory().removeItem(GameItem.Instance().m_humanHeal_Ability1_Item);
                    player.setWalkSpeed(0.2f);
                    player.sendPluginMessage(Main.instance, "DeathGame", String.format("LoadingBar" + "_" + "false").getBytes());
                    gameVariable.getPlayerVariableMap().get(stringPlayer).setHealKit(false);
                    this.cancel();
                }

            }
        }.runTaskTimer(Main.instance, 140l, 140l);

    }


}

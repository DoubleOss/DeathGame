package doubleos.deathgame;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class Death implements Listener
{


	   @EventHandler
	   public void deathEvent(EntityDeathEvent e)
	   {

		   
		   //Bukkit.broadcastMessage("몬스터 죽었다 빼액 " + e.getEntity().toString());
	   }
}

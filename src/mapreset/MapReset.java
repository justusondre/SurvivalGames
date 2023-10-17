package mapreset;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import survivalgames.Main;

public class MapReset {
	
	public static void unloadMap(String mapName) {
	    World w = Bukkit.getServer().getWorld(mapName);
	    if (w == null) {
	    	for (Player p : Bukkit.getOnlinePlayers()) {
	            p.kickPlayer("The map you were in no longer exists.");
	        }
	        return;
	    }

	    for (Entity en : w.getEntities()) {
	        if (en instanceof Player) {
	            Player p = (Player) en;
	            p.teleport(Bukkit.getWorld("world").getSpawnLocation());
	        }
	    }

	    Bukkit.getServer().unloadWorld(w, false);
	}

	public static void loadMap(String mapName) {
	    World w;
	    if (Bukkit.getWorld(mapName) == null) {
	        w = Bukkit.getServer().createWorld(new WorldCreator(mapName));
	    } else {
	        w = Bukkit.getWorld(mapName);
	    }
	    
	    w.setGameRuleValue("doMobSpawning", "false");	    
	    w.setGameRuleValue("doFireTick", "false");
	    w.setGameRuleValue("doDaylightCycle", "false");
	    w.setGameRuleValue("doWeatherCycle", "false");
	        
	    for (Entity entity : w.getEntities()) {
	        if (entity instanceof LivingEntity && !(entity instanceof Player)) {
	            entity.remove();
	        }
	    }
	    
	    for (Entity drop : w.getEntitiesByClasses(Item.class)) {
	        drop.remove();
	    }
	    
	    w.setAutoSave(false);
	}
    
    public static void resetMap(String mapName) {
        unloadMap(mapName);
        
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            loadMap(mapName);
        }, 200L);
    }
}

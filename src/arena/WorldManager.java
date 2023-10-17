package arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import survivalgames.Main;

public class WorldManager {
	public static File f = new File("plugins/HungerGames", "worldreset.yml");
	public static FileConfiguration cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
	public static List<String> worlds = new ArrayList<>();
	
		  
	  public static void load() {
	    worlds.add("world");
	    worlds = setObject("worlds", worlds);
	  }
	  
	  public static void save() {
	    try {
	      cfg.save(f);
	    } catch (IOException e) {
	      e.printStackTrace();
	    } 
	  }
	  
	  public static List<String> setObject(String path, List<String> value) {
	    if (cfg.contains(path))
	      return cfg.getStringList(path); 
	    cfg.set(path, value);
	    save();
	    return value;
	    
	  }
	  
	  public void setWorldBorderSize(int size) {
		  Bukkit.getServer().getWorld("world").getWorldBorder().setSize(size);
	  }
	  
	  public void shrinkBorder(int size, int seconds) {
		  Bukkit.getServer().getWorld("world").getWorldBorder().setSize(size, seconds);
	  }
	  
	  public void setStaticTime(int time) {
		  Bukkit.getWorld("world").setTime(time);
	  }
	  
	  public void setTime(int time) {
 		 new BukkitRunnable() {
              public void run() {
                                                             
             	 Bukkit.getWorld("world").setTime(Bukkit.getWorld("world").getTime() + 500);
		                    
                              
                      if(Bukkit.getWorld("world").getTime() >= 24000) {
                     	 Bukkit.getWorld("world").setTime(0);
                      		}
                      
                      if(Bukkit.getWorld("world").getTime() == time) {
                     	 cancel();
                      }
                      
                  	for(Player player : Bukkit.getOnlinePlayers()) {    		
                     	player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 1F);

                  	}
                      
              			}
          		}.runTaskTimer(Main.getInstance(), 20 * 1, 20 * 1/8); //first number is the delay, second number is in between waves.
  			}
	  
	  public boolean isOutsideOfBorder(Player p) {
	        Location loc = p.getLocation();
	        WorldBorder border = p.getWorld().getWorldBorder();
	        double x = loc.getX();
	        double z = loc.getZ();
	        double size = border.getSize();
	        p.sendMessage("outside of the border");
	        return ((x > size || (-x) > size) || (z > size || (-z) > size));
	    }
}
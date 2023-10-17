package utils;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import utils.StringUtils.DefaultFontInfo;

public class MiscUtils {
  private static final Random random = new Random();

  public static void spawnRandomFirework(Location location) {
    FireworkEffect.Type type;
    Firework fw = (Firework)location.getWorld().spawnEntity(location, EntityType.FIREWORK);
    FireworkMeta fwm = fw.getFireworkMeta();
    switch (random.nextInt(4)) {
      case 0:
        type = FireworkEffect.Type.BALL;
        break;
      case 1:
        type = FireworkEffect.Type.BALL_LARGE;
        break;
      case 2:
        type = FireworkEffect.Type.BURST;
        break;
      case 3:
        type = FireworkEffect.Type.CREEPER;
        break;
      case 4:
        type = FireworkEffect.Type.STAR;
        break;
      default:
        type = FireworkEffect.Type.BALL;
        break;
    }
    FireworkEffect effect = FireworkEffect.builder().flicker(random.nextBoolean()).withColor(Color.fromBGR(random.nextInt(250) + 1)).withFade(Color.fromBGR(random.nextInt(250) + 1)).with(type).trail(random.nextBoolean()).build();
    fwm.addEffect(effect);
    fwm.setPower(random.nextInt(2) + 1);
    fw.setFireworkMeta(fwm);
  }

  public static void sendCenteredMessage(Player player, String message) {
    String[] lines = ChatColor.translateAlternateColorCodes('&', message).split("\n", 40);
    StringBuilder returnMessage = new StringBuilder();
    for (String line : lines) {
      int messagePxSize = 0;
      boolean previousCode = false, isBold = false;
      for (char c : line.toCharArray()) {
        if (c == '§') {
          previousCode = true;
        } else if (previousCode) {
          previousCode = false;
          isBold = (c == 'l');
        } else {
          DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
          messagePxSize = isBold ? (messagePxSize + dFI.getBoldLength()) : (messagePxSize + dFI.getLength());
          messagePxSize++;
        }
      }
      int toCompensate = 165 - messagePxSize / 2, spaceLength = DefaultFontInfo.SPACE.getLength() + 1, compensated = 0;
      StringBuilder sb = new StringBuilder();
      while (compensated < toCompensate) {
        sb.append(" ");
        compensated += spaceLength;
      }
      returnMessage.append(sb).append(line).append("\n");
    }
    player.sendMessage(returnMessage.toString());
  }
}

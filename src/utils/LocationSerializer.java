package utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializer {

	public static final Location DEFAULT_LOCATION;

	public static final String SERIALIZED_LOCATION;

	private static final DecimalFormat decimalFormat = new DecimalFormat("0.000");

	static {
		DecimalFormatSymbols formatSymbols = decimalFormat.getDecimalFormatSymbols();
		formatSymbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(formatSymbols);
		DEFAULT_LOCATION = Bukkit.getWorlds().get(0).getSpawnLocation();
		SERIALIZED_LOCATION = toString(DEFAULT_LOCATION);
	}

	public static Location fromString(String input) {
		if (input == null)
			return null;
		String[] parts = input.split(", ");
		if (parts.length != 6)
			return null;
		try {
			World world = Bukkit.getWorld(parts[0]);
			if (world == null)
				return null;
			double x = NumberUtils.getDouble(parts[1]);
			double y = NumberUtils.getDouble(parts[2]);
			double z = NumberUtils.getDouble(parts[3]);
			float yaw = NumberUtils.getFloat(parts[4]);
			float pitch = NumberUtils.getFloat(parts[5]);
			return new Location(world, x, y, z, yaw, pitch);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String toString(Location loc) {
		if (loc == null)
			return "";
		return loc.getWorld().getName() + ", " + decimalFormat.format(loc.getX()) + ", "
				+ decimalFormat.format(loc.getY()) + ", " + decimalFormat.format(loc.getZ()) + ", "
				+ decimalFormat.format(loc.getYaw()) + ", " + decimalFormat.format(loc.getPitch());
	}

	public static boolean isDefaultLocation(String serializedLocation) {
		return (serializedLocation != null && serializedLocation.equals(SERIALIZED_LOCATION));
	}

	public static boolean isDefaultLocation(Location location) {
		return (location != null && location.equals(DEFAULT_LOCATION));
	}
}

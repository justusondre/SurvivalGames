package command;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class SpawnInfo {
    private Location location;
    private Vector rotation;

    public SpawnInfo(Location location, Vector rotation) {
        this.location = location;
        this.rotation = rotation;
    }

    public Location getLocation() {
        return location;
    }

    public Vector getRotation() {
        return rotation;
    }
}
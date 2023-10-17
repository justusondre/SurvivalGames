package utils;

import java.util.Objects;
import java.util.function.Function;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public final class Title {

    private String title;
    private String subtitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public Title clone() {
        return new Title(this.title, this.subtitle, this.fadeIn, this.stay, this.fadeOut);
    }

    public void send(Player player) {
        sendTitle(player, this.title, this.subtitle, this.fadeIn, this.stay, this.fadeOut);
    }

    @SuppressWarnings("deprecation")
	public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Objects.requireNonNull(player, "Cannot send title to null player");
        if (title == null && subtitle == null)
            return;

        // Send title and subtitle using 1.8-compatible methods
        player.sendTitle(title, subtitle);
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 10, 20, 10);
    }

    public static Title parseTitle(ConfigurationSection config) {
        return parseTitle(config, null);
    }

    public static Title parseTitle(ConfigurationSection config, Function<String, String> transformers) {
        String title = config.getString("title");
        String subtitle = config.getString("subtitle");
        if (transformers != null) {
            title = transformers.apply(title);
            subtitle = transformers.apply(subtitle);
        }
        int fadeIn = config.getInt("fade-in");
        int stay = config.getInt("stay");
        int fadeOut = config.getInt("fade-out");
        if (fadeIn < 1)
            fadeIn = 10;
        if (stay < 1)
            stay = 20;
        if (fadeOut < 1)
            fadeOut = 10;
        return new Title(title, subtitle, fadeIn, stay, fadeOut);
    }

    public String getTitle() {
        return this.title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public static void clearTitle(Player player) {
        Objects.requireNonNull(player, "Cannot clear title from null player");

        // Clear the title using 1.8-compatible methods
        player.resetTitle();
    }

    public static void sendTabList(String header, String footer, Player... players) {
        // 1.8 does not support tab list title modifications
        // You may want to handle this differently or provide a fallback
    }
}
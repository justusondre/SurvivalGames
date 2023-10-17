package object;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;

public enum TeamDistrict {
	
	DISTRICT_ONE("District 1",14,Color.FUCHSIA,ChatColor.LIGHT_PURPLE,"1",DyeColor.PINK, "Luxury"),
	DISTRICT_TWO("District 2",11,Color.YELLOW,ChatColor.YELLOW,"2",DyeColor.YELLOW, "Masonary"),
	DISTRICT_THREE("District 3",4,Color.AQUA,ChatColor.AQUA,"3",DyeColor.CYAN, "Technology"),
	DISTRICT_FOUR("District 4",0,Color.GRAY,ChatColor.GRAY,"4",DyeColor.GRAY, "Fishing"),
	DISTRICT_FIVE("District 5",9,Color.RED,ChatColor.RED,"5",DyeColor.RED, "Power"),
	DISTRICT_SIX("District 6",7,Color.LIME,ChatColor.GREEN,"6",DyeColor.LIME, "Transportation"),
	DISTRICT_SEVEN("District 7",6,Color.BLUE,ChatColor.BLUE,"7",DyeColor.BLUE, "Lumber"),
	DISTRICT_EIGHT("District 8",5,Color.WHITE,ChatColor.WHITE,"8",DyeColor.WHITE, "Textiles"),
	DISTRICT_NINE("District 9",9,Color.BLACK,ChatColor.BLACK,"9",DyeColor.BLACK, "Grains"),
	DISTRICT_TEN("District 10",7,Color.TEAL,ChatColor.GRAY,"10",DyeColor.GRAY, "Livestock"),
	DISTRICT_ELEVEN("District 11",6,Color.FUCHSIA,ChatColor.LIGHT_PURPLE,"11",DyeColor.PINK, "Agriculture"),
	DISTRICT_TWELVE("District 12",5,Color.GRAY,ChatColor.DARK_GRAY,"12",DyeColor.GRAY, "Coal Mining");
	
    private final String displayName;
    private final int value;
    private final Color color;
    private final ChatColor chatColor;
    private final String symbol;
    private final DyeColor dye;
    private final String description;

    TeamDistrict(String displayName, int value, Color color, ChatColor chatColor, String symbol, DyeColor dye, String description) {
        this.displayName = displayName;
        this.value = value;
        this.color = color;
        this.chatColor = chatColor;
        this.symbol = symbol;
        this.dye = dye;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    public int getValue()
    {
        return value;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public Color getColor()
    {
        return color;
    }

    public ChatColor getChatColor()
    {
        return chatColor;
    }

    public DyeColor getDye() {
        return dye;
    }

    public String format(){
        return getChatColor()+getDisplayName();
    }

	public String getDescription() {
		return description;
	}
}
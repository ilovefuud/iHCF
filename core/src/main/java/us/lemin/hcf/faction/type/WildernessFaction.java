package us.lemin.hcf.faction.type;

import org.bukkit.command.CommandSender;
import us.lemin.hcf.HCF;

import java.util.Map;

/**
 * Represents the {@link WildernessFaction}.
 */
public class WildernessFaction extends Faction {

    public WildernessFaction() {
        super("Wilderness");
    }

    public WildernessFaction(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getDisplayName(CommandSender sender) {
        return HCF.getPlugin().getConfiguration().getRelationColourWilderness() + getName();
    }
}

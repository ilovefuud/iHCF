package us.lemin.hcf.faction.type;

import org.bukkit.command.CommandSender;
import us.lemin.hcf.HCF;

import java.util.Map;

/**
 * Represents the {@link WarzoneFaction}.
 */
public class WarzoneFaction extends Faction {

    public WarzoneFaction() {
        super("Warzone");
    }

    public WarzoneFaction(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getDisplayName(CommandSender sender) {
        return HCF.getPlugin().getConfiguration().getRelationColourWarzone() + getName();
    }
}

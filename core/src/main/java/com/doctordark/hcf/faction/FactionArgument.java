package com.doctordark.hcf.faction;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.player.rank.Rank;

@Setter
@Getter
public abstract class FactionArgument implements PlayerSubCommand {

    private String name;
    private String description;
    private Rank requiredRank;
    protected String permission;


    public FactionArgument(String name, String description) {
        this(name, description, Rank.MEMBER);
    }

    public FactionArgument(String name, String description, Rank requiredRank) {
        this.name = name;
        this.description = description;
        this.requiredRank = requiredRank;
    }

    public boolean validate(Player player) {
        return CorePlugin.getInstance().getProfileManager().getProfile(player).hasRank(requiredRank);
    }
}

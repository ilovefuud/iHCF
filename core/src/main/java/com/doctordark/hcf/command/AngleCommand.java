package com.doctordark.hcf.command;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.utils.message.CC;


/**
 * Command used to check the angle and yaw positions of {@link Player}s.
 */
public class AngleCommand extends PlayerCommand {


    public AngleCommand() {
        super("angle");
    }

    @Override
    public void execute(Player player, String[] strings) {
        Location location = ((Player) player).getLocation();
        player.sendMessage(CC.GOLD + location.getYaw() + " yaw" + CC.WHITE + ", " + CC.GOLD
                + location.getPitch() + " pitch");
    }
}

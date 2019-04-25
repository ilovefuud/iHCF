package com.doctordark.hcf.command;

import com.doctordark.hcf.HCF;
import org.apache.commons.lang3.time.FastDateFormat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import us.lemin.core.commands.BaseCommand;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Command used to check the current time for the server.
 */
public class ServerTimeCommand extends BaseCommand {

    private final FastDateFormat format;

    public ServerTimeCommand(HCF plugin) {
        super("servertime");
        format = FastDateFormat.getInstance("E MMM dd h:mm:ssa z yyyy", plugin.getConfiguration().getServerTimeZone(), Locale.ENGLISH);
    }

    @Override
    protected void execute(CommandSender commandSender, String[] strings) {
        commandSender.sendMessage(ChatColor.GREEN + "The server time is " + ChatColor.LIGHT_PURPLE + format.format(System.currentTimeMillis()) + ChatColor.GREEN + '.');

    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }

}

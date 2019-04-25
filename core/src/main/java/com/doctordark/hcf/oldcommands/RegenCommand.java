package com.doctordark.hcf.oldcommands;

import com.doctordark.hcf.Configuration;
import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.struct.RegenStatus;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;

import java.util.Collections;
import java.util.List;

public class RegenCommand extends PlayerCommand {

    private final HCF plugin;

    public RegenCommand(HCF plugin) {
        super("regen");
        this.plugin = plugin;
    }

    public long getRemainingRegenMillis(PlayerFaction faction) {
        long millisPassedSinceLastUpdate = System.currentTimeMillis() - faction.getLastDtrUpdateTimestamp();
        double dtrRequired = faction.getMaximumDeathsUntilRaidable() - faction.getDeathsUntilRaidable();
        Configuration configuration = HCF.getPlugin().getConfiguration();
        return (long) ((configuration.getFactionDtrUpdateMillis() / configuration.getFactionDtrUpdateIncrement()) * dtrRequired) - millisPassedSinceLastUpdate;
    }

    @Override
    public void execute(Player player, String[] strings) {
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        RegenStatus regenStatus = playerFaction.getRegenStatus();
        switch (regenStatus) {
            case FULL:
                player.sendMessage(ChatColor.RED + "Your faction currently has full DTR.");
                return;
            case PAUSED:
                player.sendMessage(ChatColor.BLUE + "Your faction is currently on DTR freeze for another " + ChatColor.WHITE +
                        DurationFormatUtils.formatDurationWords(playerFaction.getRemainingRegenerationTime(), true, true) + ChatColor.BLUE + '.');

                return;
            case REGENERATING:
                player.sendMessage(ChatColor.BLUE + "Your faction currently has " + ChatColor.YELLOW + regenStatus.getSymbol() + ' ' +
                        playerFaction.getDeathsUntilRaidable() + ChatColor.BLUE + " DTR and is regenerating at a rate of " + ChatColor.GOLD +
                        plugin.getConfiguration().getFactionDtrUpdateIncrement() + ChatColor.BLUE + " every " + ChatColor.GOLD +
                        plugin.getConfiguration().getFactionDtrUpdateTimeWords() + ChatColor.BLUE + ". Your ETA for maximum DTR is " + ChatColor.LIGHT_PURPLE +
                        DurationFormatUtils.formatDurationWords(getRemainingRegenMillis(playerFaction), true, true) + ChatColor.BLUE + '.');

                return;
        }

        player.sendMessage(ChatColor.RED + "Unrecognised regen status, please inform an Administrator.");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }
}

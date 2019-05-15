package com.doctordark.hcf.manager;

import com.doctordark.hcf.HCF;
import org.bukkit.entity.Player;

public class CommandReward extends Reward {

    public CommandReward(String name, String value, String command) {
        super(name);
        this.setReward(command);
        this.setRewardValue(value);
    }

    @Override
    public void execute(Player player) {
        HCF plugin = HCF.getPlugin();
        String value = (String) this.getRewardValue();
        if (value == null) {
            value = "";
        }
        String executedCommand = ((String) this.getRewardValue())
                .replace("%player%", player.getName())
                .replace("%value%", value);
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), executedCommand);
    }
}

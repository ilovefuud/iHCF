package us.lemin.hcf.manager;

import org.bukkit.entity.Player;
import us.lemin.hcf.HCF;

import java.util.LinkedHashMap;
import java.util.Map;

public class RewardManager {

    private final HCF plugin;

    private final Map<String, CommandReward> commandRewards = new LinkedHashMap<>();


    public RewardManager(HCF plugin) {
        this.plugin = plugin;
        registerEntries(
                new CommandReward("Lives Reward", String.valueOf(1), "/lives give %player% %value%"),
                new CommandReward("Lives Reward", String.valueOf(5), "/lives give %player% %value%"),
                new CommandReward("Lives Reward", String.valueOf(10), "/lives give %player% %value%"),
                new CommandReward("Lives Reward", String.valueOf(15), "/lives give %player% %value%")
        );
    }


    private void registerEntries(Reward... rewards) {
        for (Reward reward : rewards) {
            if (reward.getClass().isAssignableFrom(CommandReward.class)) {
                CommandReward commandReward = (CommandReward) reward;
                commandRewards.put(commandReward.getName(), commandReward);
            }
        }
    }

    public Map<String, CommandReward> getCommandRewards() {
        return commandRewards;
    }

    public CommandReward getCommandRewardByName(String string) {
        return commandRewards.get(string);
    }

    public void executeReward(String rewardName, Player player) {
        getCommandRewardByName(rewardName).execute(player);
    }
}

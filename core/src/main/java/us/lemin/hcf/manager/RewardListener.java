package us.lemin.hcf.manager;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import us.lemin.hcf.HCF;

@RequiredArgsConstructor
public class RewardListener implements Listener {

    private final HCF plugin;

    public void onInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_AIR || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (event.getItem().getType() != Material.PAPER) return;

        ItemStack item = event.getItem();

        if (!item.hasItemMeta()) return;


        if (!item.getItemMeta().hasLore()) return;

        /*boolean rewardItem = item.getItemMeta().getLore().stream().anyMatch(lore -> lore.contains("Reward Item"));*/
        boolean rewardItem = item.getItemMeta().getLore().get(0).contains("Reward Item");

        if (!rewardItem) return;

        String type = item.getItemMeta().getLore().get(1).replace("Type: ", "");

        // just a test, will be retrieved from plugin when implemented
        RewardManager rewardManager = new RewardManager(plugin);
        rewardManager.getCommandRewardByName("ass").execute(event.getPlayer());

        event.getPlayer().setItemInHand(null);
    }

}

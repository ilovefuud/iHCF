package land.pvp.testplugin;

import land.pvp.sokudotab.api.TabLines;
import land.pvp.sokudotab.api.TabTemplate;
import org.bukkit.entity.Player;
import us.lemin.core.utils.player.PlayerList;

public class LobbyTemplate implements TabTemplate {

    @Override
    public TabLines getLines(Player player) {
        TabLines lines = new TabLines(player);
        PlayerList list = PlayerList.newList().sortedByRank();

        lines.middle(1, "&d&lPvP Land");
        lines.left(2, "&c&nStaff");
        lines.middle(2, "&b&nRanked Members");
        lines.right(2, "&a&nMembers");

        int staffCount = 4;
        int rankedMembersCount = 4;
        int membersCount = 4;

        for (Player sortedPlayer : list.getOnlinePlayers()) {
            CoreProfile profile = CorePlugin.getInstance().getProfileManager().getProfile(sortedPlayer.getUniqueId());
            String name = profile.getRank().getColor() + profile.getName();

            if (profile.isStaff()) {
                lines.left(staffCount++, name);
            } else if (profile.hasRank(Rank.EXCLUSIVE)) {
                lines.middle(rankedMembersCount++, name);
            } else {
                lines.right(membersCount++, name);
            }
        }

        return lines;
    }
}

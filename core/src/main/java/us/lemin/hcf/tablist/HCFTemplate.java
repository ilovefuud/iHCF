package us.lemin.hcf.tablist;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.api.tablistapi.api.TabLines;
import us.lemin.core.api.tablistapi.api.TabTemplate;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.player.PlayerList;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.struct.Role;

import java.util.Comparator;

@RequiredArgsConstructor
public class HCFTemplate implements TabTemplate {

    private final HCF plugin;

    private final Comparator<FactionMember> factionRankOrder = (a, b) -> {
        Role roleA = a.getRole();
        Role roleB = b.getRole();
        return roleA.compareTo(roleB);
    };

    @Override
    public TabLines getLines(Player player) {
        TabLines lines = new TabLines(player);
        PlayerList list = PlayerList.newList().sortedByRank();

        lines.middle(1, "&e&lLemin &6&lKitPvP");
        lines.left(3, "&6&nStaff");
        lines.middle(3, "&a&nPremium");
        lines.right(3, "&2&nVoters");

        int staffCount = 5;
        int rankedMembersCount = 5;
        int votersCount = 5;

        int regularPlayersRow = 14;
        int regularPlayersCount = 1;

        for (Player sortedPlayer : list.getOnlinePlayers()) {
            CoreProfile profile = CorePlugin.getInstance().getProfileManager().getProfile(sortedPlayer.getUniqueId());
            String name = profile.getRank().getColor() + profile.getName();

            if (profile.hasStaff()) {
                lines.left(staffCount++, name);
            } else if (profile.hasRank(Rank.PREMIUM)) {
                lines.middle(rankedMembersCount++, name);
            } else if (profile.hasRank(Rank.VOTER)){
                lines.right(votersCount++, name);
            } else {
                lines.addAfter(regularPlayersCount, regularPlayersRow, name);
            }
        }

        return lines;
    }
}

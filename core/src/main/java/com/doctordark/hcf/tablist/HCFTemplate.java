package com.doctordark.hcf.tablist;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.FactionMember;
import com.doctordark.hcf.faction.struct.Role;
import com.doctordark.hcf.faction.type.PlayerFaction;
import land.pvp.sokudotab.api.TabLines;
import land.pvp.sokudotab.api.TabTemplate;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import us.lemin.core.utils.player.PlayerList;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        PlayerFaction faction = plugin.getFactionManager().getPlayerFaction(player.getUniqueId());
        List<FactionMember> sortedList = faction.getMembers().values().stream().sorted(factionRankOrder).collect(Collectors.toList());

        lines.middle(1, plugin.getConfiguration().getScoreboardTablistTitle());



        lines.left(2, "Faction");
        lines.left(3, faction.getName());

        lines.middle(2, "Members");

        int memberCount = 3;
        for (FactionMember member : sortedList) {
            while (memberCount < 19) {
                lines.middle(memberCount++, member.getRole().getAstrix() + member.getName());
            }
        }



        return lines;
    }
}

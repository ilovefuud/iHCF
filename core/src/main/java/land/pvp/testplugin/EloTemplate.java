package land.pvp.testplugin;

import land.pvp.sokudotab.api.TabLines;
import land.pvp.sokudotab.api.TabTemplate;
import org.bukkit.entity.Player;

public class EloTemplate implements TabTemplate {

    @Override
    public TabLines getLines(Player player) {
        TabLines lines = new TabLines(player);

        lines.middle(1, "&d&lPvP Land");

        lines.left(2, "&b&nELO Ratings");
        lines.left(4, "&bNo Debuff&7: &31,000");
        lines.left(5, "&bDebuff&7: &31,000");
        lines.left(6, "&bGapple&7: &31,000");
        lines.left(7, "&bSumo&7: &31,000");
        lines.left(8, "&bCombo&7: &31,000");
        lines.left(9, "&bArcher&7: &31,000");
        lines.left(10, "&bAxe&7: &31,000");


        lines.right(2, "&b&nOther Information");
        lines.right(4, "&bPing&7: &3" + player.spigot().getPing());

        return lines;
    }
}

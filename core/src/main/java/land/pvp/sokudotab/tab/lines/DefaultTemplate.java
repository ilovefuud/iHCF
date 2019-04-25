package land.pvp.sokudotab.tab.lines;


import org.bukkit.entity.Player;
import land.pvp.sokudotab.api.TabLines;
import land.pvp.sokudotab.api.TabTemplate;

public class DefaultTemplate implements TabTemplate {

    @Override
    public TabLines getLines(Player player) {
        return new TabLines(player);
    }
}

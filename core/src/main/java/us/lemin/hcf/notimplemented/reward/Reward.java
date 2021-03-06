package us.lemin.hcf.notimplemented.reward;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class Reward {

    private final String name;

    private Object reward;
    private Object rewardValue;

    public abstract void execute(Player player);

}

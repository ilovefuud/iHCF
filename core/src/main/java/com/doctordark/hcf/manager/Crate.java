package com.doctordark.hcf.manager;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class Crate {

    private final String name;

    private ItemStack key;
    private int winningsAmount = 1;
    private LinkedHashMap<Integer, ItemStack> winnings;

    public Crate(String name) {
        this.name = name;
    }



    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("key", key.serialize());
        map.put("winnings_amount", winningsAmount);
        map.put("winnings", winnings);
        return map;
    }
}

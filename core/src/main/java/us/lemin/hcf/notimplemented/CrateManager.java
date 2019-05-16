package us.lemin.hcf.notimplemented;

import us.lemin.core.storage.flatfile.Config;
import us.lemin.hcf.HCF;

import java.util.LinkedHashMap;
import java.util.Map;

public class CrateManager {

    private final HCF plugin;
    private final Config config;

    private final Map<String, Crate> crates = new LinkedHashMap<>();


    public CrateManager(HCF plugin) {
        this.plugin = plugin;
        config = new Config(plugin, "crates");
    }


    public Crate registerCrate(String string) {
        return crates.put(string, new Crate(string));
    }

    public Crate getCrateByName(String string) {
        return crates.get(string);
    }

    public void saveCrates() {
        crates.values().forEach(crate -> {
            String crateName = crate.getName();
            config.set("Crate." + crateName, crate.serialize());
        });
    }
}

package org.ipvp.hcfextra;

import lombok.Getter;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.ipvp.hcfextra.command.CoordsCommand;
import org.ipvp.hcfextra.command.EndportalHandler;
import org.ipvp.hcfextra.command.HCFExtraCommand;
import org.ipvp.hcfextra.command.HelpCommand;
import org.ipvp.hcfextra.inventoryrestore.InventoryRestoreHandler;

public class HCFExtra extends JavaPlugin {

    @Getter
    private static HCFExtra plugin;

    @Getter
    private Configuration configuration;

    @Override
    public void onEnable() {
        HCFExtra.plugin = this;
        PluginCommand temp;

        (this.configuration = new Configuration(this)).reload();

        InventoryRestoreHandler inventoryRestoreHandler = new InventoryRestoreHandler();
        this.getServer().getPluginManager().registerEvents(inventoryRestoreHandler, this);
        (temp = this.getCommand("inv")).setExecutor(inventoryRestoreHandler);
        temp.setPermission("ihcfextra.oldcommands.inv");

        EndportalHandler endportalHandler = new EndportalHandler();
        this.getServer().getPluginManager().registerEvents(endportalHandler, this);
        (temp = this.getCommand("endportal")).setExecutor(endportalHandler);
        temp.setPermission("ihcfextra.oldcommands.endportal");

        (temp = this.getCommand("hcfextra")).setExecutor(new HCFExtraCommand(this));
        temp.setPermission("ihcfextra.oldcommands.hcfextra");

        this.getCommand("coords").setExecutor(new CoordsCommand(this));
        this.getCommand("help").setExecutor(new HelpCommand(this));
    }

    @Override
    public void onDisable() {
        HCFExtra.plugin = null;
    }
}

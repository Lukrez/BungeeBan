package me.markus.bungeeban;

import me.markus.bungeeban.commands.BanCommand;
import me.markus.bungeeban.commands.CheckBanCommand;
import me.markus.bungeeban.commands.CheckIPCommand;
import me.markus.bungeeban.commands.IPBanCommand;
import me.markus.bungeeban.commands.KickCommand;
import me.markus.bungeeban.commands.UnbanCommand;
import net.md_5.bungee.api.plugin.Plugin;


public class BungeeBan extends Plugin  {
	
	public static BungeeBan instance;
	public MySQLDataSource database;
	
    @Override
    public void onEnable() {
    	instance = this;
    	// setup pluginfolder
    	if (!this.getDataFolder().exists())
			this.getDataFolder().mkdir();
    	
    	// Load setting
    	Settings.loadSettings();

    	// link commands
    	this.getProxy().getPluginManager().registerCommand(this, new BanCommand());
    	this.getProxy().getPluginManager().registerCommand(this, new IPBanCommand());
    	this.getProxy().getPluginManager().registerCommand(this, new UnbanCommand());
    	this.getProxy().getPluginManager().registerCommand(this, new CheckBanCommand());
    	this.getProxy().getPluginManager().registerCommand(this, new KickCommand());
    	this.getProxy().getPluginManager().registerCommand(this, new CheckIPCommand());
    	
    	// link listeners
    	this.getProxy().getPluginManager().registerListener(this, new EventListeners());
    	
    	
    	try {
    		database = new MySQLDataSource();
		} catch (Exception ex) {
			this.getLogger().severe(ex.getMessage());
			this.getLogger().severe("Can't use MySQL... Please input correct MySQL information! SHUTDOWN...");
			this.getProxy().stop();
		}
    	
        getLogger().info("Finished setup!");
    	
    }
    
    @Override
    public void onDisable() {
    	database.close();
    }
    
    public void shutdown(){
    	database.close();
    	this.getProxy().stop();
    }
}
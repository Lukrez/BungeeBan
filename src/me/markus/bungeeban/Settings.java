package me.markus.bungeeban;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;


public class Settings {

	public static String getMySQLHost;
	public static String getMySQLPort;
	public static String getMySQLUsername;
	public static String getMySQLDatabase;
	public static String getMySQLPassword;
	public static boolean isStopEnabled;
	
	

	public static void loadSettings() {

		// Default settings
		getMySQLHost = "foo.server.com";
		getMySQLPort = "1234";
		getMySQLUsername = "sqlAdmin";
		getMySQLDatabase = "forumDB";
		getMySQLPassword = "foobar";
		isStopEnabled = true;

		File file = new File(BungeeBan.instance.getDataFolder(), "config.yml");
		if (!file.exists())
			saveSettings();
		
		try {
			Configuration yaml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			
			getMySQLHost = yaml.getString("Datasource.mySQLHost");
			getMySQLPort = yaml.getString("Datasource.mySQLPort");
			getMySQLUsername = yaml.getString("Datasource.mySQLUsername");
			getMySQLDatabase = yaml.getString("Datasource.mySQLDatabase");
			getMySQLPassword = yaml.getString("Datasource.mySQLPassword");


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		saveSettings();
	}

	public static void saveSettings() {
		try {
			File file = new File(BungeeBan.instance.getDataFolder(), "config.yml");
			if (!file.exists()){
				file.createNewFile();
			}
			Configuration yaml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
	
			yaml.set("Datasource.mySQLHost", getMySQLHost);
			yaml.set("Datasource.mySQLPort", getMySQLPort);
			yaml.set("Datasource.mySQLUsername", getMySQLUsername);
			yaml.set("Datasource.mySQLPassword", getMySQLPassword);
			yaml.set("Datasource.mySQLDatabase", getMySQLDatabase);
			
			yaml.set("Security.SQLProblem.stopServer", isStopEnabled);

			ConfigurationProvider.getProvider(YamlConfiguration.class).save(yaml, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

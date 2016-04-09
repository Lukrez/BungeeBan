package me.markus.bungeeban.commands;

import java.util.ArrayList;

import me.markus.bungeeban.BungeeBan;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class CheckIPCommand extends TabCompleteCommand {

	public CheckIPCommand() {
		super("checkip");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission("bungeeban.ban")){
			return;
		}
		
		if ((args.length < 1) || (args[0].toLowerCase().equals("help"))){
			sender.sendMessage(new TextComponent("/checkip <spielername>"));
			return;
		}
		String playername = args[0];
		String ip = BungeeBan.instance.database.getPlayerIP(playername);
		if (ip.equals("")) {
			sender.sendMessage(new TextComponent("Keine IP für den Spieler verfügabr!"));
			return;
		}
		sender.sendMessage(new TextComponent("Spieler " + playername + " hat die IP: " + ip));	
		ArrayList<String> players = BungeeBan.instance.database.getPlayersByIP(ip);
		for (String player : players)  {
			sender.sendMessage(new TextComponent("Spieler " + player + " hat die selbe IP!"));
		}
	}

	@Override
	public boolean canUseTabCompletion(int argsposition) {
		if (argsposition == 1)
			return true;
		return false;
	}

}

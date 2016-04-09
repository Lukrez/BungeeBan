package me.markus.bungeeban.commands;

import java.sql.Timestamp;

import me.markus.bungeeban.BungeeBan;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class UnbanCommand extends Command{

	public UnbanCommand() {
		super("unban");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		if (!sender.hasPermission("bungeeban.ban")){
			return;
		}
		
		if ((args.length < 1) || (args[0].toLowerCase().equals("help"))){
			sender.sendMessage(new TextComponent("/unban <spielername>"));
			return;
		}
		String playername = args[0];
		// get player ip
		String ip = BungeeBan.instance.database.getPlayerIP(playername);
		Timestamp nameban = BungeeBan.instance.database.getPlayerBansByName(playername);
		Timestamp ipban = BungeeBan.instance.database.getPlayerBansByIp(ip);
		if (nameban == null && ipban == null){
			sender.sendMessage(new TextComponent("Keine Bans für den Spieler verfügbar!"));
			return;
		}
		if (nameban != null){
			BungeeBan.instance.database.unbanPlayerByName(playername);
			sender.sendMessage(new TextComponent("Alle Namen-Bans gelöscht!"));
		}
		if (ipban != null){
			BungeeBan.instance.database.unbanPlayerByIP(ip);
			sender.sendMessage(new TextComponent("Alle IP-Bans gelöscht!"));
		}
		
	}

}

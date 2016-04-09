package me.markus.bungeeban.commands;

import java.sql.Timestamp;

import me.markus.bungeeban.BungeeBan;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;


public class CheckBanCommand extends TabCompleteCommand {

	public CheckBanCommand() {
		super("checkban");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		if (!sender.hasPermission("bungeeban.ban")){
			return;
		}
		
		if ((args.length < 1) || (args[0].toLowerCase().equals("help"))){
			sender.sendMessage(new TextComponent("/checkban <spielername>"));
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
			sender.sendMessage(new TextComponent("Spieler ist Namens-gebannt bis " + nameban.toString()));
		}
		if (ipban != null){
			sender.sendMessage(new TextComponent("Spieler ist IP-gebannt bis " + ipban.toString()));
		}
		
	}

	@Override
	public boolean canUseTabCompletion(int argsposition) {
		if (argsposition == 1)
			return true;
		return false;
	}

}
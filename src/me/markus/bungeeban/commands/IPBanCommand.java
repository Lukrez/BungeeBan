package me.markus.bungeeban.commands;

import java.sql.Timestamp;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import me.markus.bungeeban.BungeeBan;

public class IPBanCommand extends AbstractBan{

	public IPBanCommand() {
		super("ipban");
	}

	@Override
	public String help() {
		return "/ipban <playername> <time> <m,h,d,w,M> <reason>";
	}

	@Override
	public boolean ban(String playername, String reason, String banner,
			Timestamp expires) {
		ProxiedPlayer player = BungeeBan.instance.getProxy().getPlayer(playername);
		String ip;
		if (player != null){
			ip = player.getAddress().getAddress().getHostAddress();
			player.disconnect(new TextComponent("Du bist bis " + expires.toString() + " gebannt!"));
		} else {
			ip = BungeeBan.instance.database.getPlayerIP(playername);
		}
		if (ip.equals(""))
			return false;
		
		
		BungeeBan.instance.database.banPlayerByIP(ip, reason, banner, expires);
		
		return true;
		
	}

}

package me.markus.bungeeban.commands;

import java.sql.Timestamp;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import me.markus.bungeeban.BungeeBan;


public class BanCommand extends AbstractBan{

	public BanCommand(){
		super("ban");
	}
	
	@Override
	public String help() {
		return "/ban <playername> <time> <m,h,d,w,M> <reason>";
	}

	@Override
	public boolean ban(String playername, String reason, String banner,
			Timestamp expires) {
		ProxiedPlayer player = BungeeBan.instance.getProxy().getPlayer(playername);
		if (player != null){
			player.disconnect(new TextComponent("Du bist bis " + expires.toString() + " gebannt!"));
		}
		BungeeBan.instance.database.banPlayerByName(playername, reason, banner, expires);
		return true;
		
	}
}

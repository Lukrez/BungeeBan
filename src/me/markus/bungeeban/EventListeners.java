package me.markus.bungeeban;

import java.sql.Timestamp;

import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/** Listeners:
- chat: mute
- join: ban
**/

public class EventListeners implements Listener{

	@EventHandler
	public void onPlayerPreLogin(PreLoginEvent e) {
		String ip = e.getConnection().getAddress().getAddress().getHostAddress();
		String playername = e.getConnection().getName();
		// update player ip
		BungeeBan.instance.database.updatePlayerIP(playername, ip);
		Timestamp nameban = BungeeBan.instance.database.getPlayerBansByName(playername);
		if (nameban != null){
			e.setCancelReason("Du bist gebannt bis zum " + nameban.toString());
			e.setCancelled(true);
			return;
		}
		Timestamp ipban = BungeeBan.instance.database.getPlayerBansByIp(ip);
		if (ipban != null){
			e.setCancelReason("Du bist gebannt bis zum " + ipban.toString());
			e.setCancelled(true);
		}
	}
	
}


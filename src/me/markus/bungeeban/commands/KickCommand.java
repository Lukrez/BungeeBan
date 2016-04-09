package me.markus.bungeeban.commands;

import me.markus.bungeeban.BungeeBan;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class KickCommand extends TabCompleteCommand {
	
	public KickCommand() {
		super("kick");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission("bungeeban.kick")){
			return;
		}
		
		if (args.length == 0){
			sender.sendMessage(new TextComponent("/kick <spielername>"));
			return;
		}
		
		String playername = args[0];
		ProxiedPlayer player = BungeeBan.instance.getProxy().getPlayer(playername);
		if (player == null) {
			sender.sendMessage(new TextComponent("Kein Spieler mit diesem Namen online!"));
			return;
		}
		player.disconnect(new TextComponent("Du wurdest gekickt!"));
		sender.sendMessage(new TextComponent("Du hast den Spieler '"+playername+"' gekickt!"));
	}

	@Override
	public boolean canUseTabCompletion(int argsposition) {
		if (argsposition == 1)
			return true;
		return false;
	}
}

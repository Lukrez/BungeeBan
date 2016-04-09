package me.markus.bungeeban.commands;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class AbstractBan extends TabCompleteCommand {

	public AbstractBan(String name) {
		super(name);
	}
	
	public abstract String help();
	
	public abstract boolean ban(String playername, String reason, String banner, Timestamp expires);
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (!sender.hasPermission("bungeeban.ban")){
			return;
		}
		String banner = sender.getName();
		if ((args.length < 4) || (args[0].toLowerCase().equals("help"))){
			sender.sendMessage(new TextComponent(this.help()));
			return;
		}
		String playername = args[0];
		int timeValue;
		try {
			timeValue = Integer.parseInt(args[1]);
		} catch (NumberFormatException e){
			sender.sendMessage(new TextComponent("Bitte eine Zahl als time angeben!"));
			return;
		}
		if (timeValue <= 0){
			sender.sendMessage(new TextComponent("Bitte eine Banzeit größer Null angeben!"));
			return;
		}
		// calculate bantime
		Calendar c = new GregorianCalendar();
		c.setTime(new Date());
		
		String timeType = args[2];
		if (timeType.equals("m")){
			c.add(Calendar.MINUTE, timeValue);
		} else if (timeType.equals("h")){
			c.add(Calendar.HOUR_OF_DAY, timeValue);
		} else if (timeType.equals("d")){
			c.add(Calendar.DAY_OF_MONTH, timeValue);
		} else if (timeType.equals("w")){
			c.add(Calendar.WEEK_OF_YEAR, timeValue);
		} else if (timeType.equals("M")){
			c.add(Calendar.MONTH, timeValue);
		} else {
			sender.sendMessage(new TextComponent("Bitte eine Zeiteinheit eingeben (m - Minute, h - Stunde, d - Tag, w - Woche, M - Monat"));
			return;
		}
		
		// check if ban is maximum 3 Months
		Calendar cMax = new GregorianCalendar();
		cMax.setTime(new Date());
		cMax.add(Calendar.MONTH, 3);
		if (c.after(cMax)){
			sender.sendMessage(new TextComponent("Bitte Spieler nur für maximal 3 Monate bannen!"));
			return;
		}
		Timestamp expires = new Timestamp(c.getTimeInMillis());
		
		
		String reason = "";
		
		for (int i=3;i<args.length;i++){
			if (i > 3){
				reason += " ";
			}
			reason += args[i];
		}
		
		if (this.ban(playername, reason, banner, expires) == true){
			sender.sendMessage(new TextComponent("Spieler wurde gebannt!"));
		} else {
			sender.sendMessage(new TextComponent("Spieler konnte nicht gebannt werden!"));
		}
		
	}
	
	@Override
	public boolean canUseTabCompletion(int argsposition) {
		if (argsposition == 1)
			return true;
		return false;
	}
	
}

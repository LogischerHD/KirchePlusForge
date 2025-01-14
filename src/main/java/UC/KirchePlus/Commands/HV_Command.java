package UC.KirchePlus.Commands;

import UC.KirchePlus.Events.Displayname;
import UC.KirchePlus.Utils.HV_User;
import UC.KirchePlus.Utils.PlayerCheck;
import UC.KirchePlus.Utils.TabellenMethoden;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class HV_Command extends CommandBase implements IClientCommand {																																																																				

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "hv";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/hv";
	}

	@Override
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("hausverbot");
		return aliases;
	}
	

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GRAY + " Die Hausverbote werden synchronisiert! Dies könnte paar Sekunden dauern!"));
			Thread thread = new Thread(){
				@Override
				public void run() {
					try {
						TabellenMethoden.getHVList();
					} catch (IOException | GeneralSecurityException e1) {}
					for(EntityPlayer p : Displayname.players) {
						p.refreshDisplayName();
					}
					for(Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
						if(e instanceof EntityPlayer) {
							EntityPlayer p = (EntityPlayer) e;
							((EntityPlayer) e).refreshDisplayName();
						}
					}
					Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.AQUA + "Die Hausverbote wurden synchronisiert!"));
				}
			};
			thread.start();
		}
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("help")) {
				displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv " + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "synchronisiere die Hausverbote mit dem Client."));
				displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv list" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Gibt eine Liste mit allen Spielern aus, die Hausverbot haben."));
				displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv namecheck" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Überprüft ob es Fehler bei den eingetragenen Spielernamen gibt."));
				displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv info <User>" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Zeigt die Hausverbot-Infos zum Spieler."));
			}else
			if(args[0].equalsIgnoreCase("list")) {
				displayMessage(new TextComponentString(
						TextFormatting.DARK_BLUE + "[]"+
						TextFormatting.DARK_AQUA + "========"+
						TextFormatting.GRAY + "["+
						TextFormatting.AQUA + "Hausverbot"+
						TextFormatting.GRAY + "]"+
						TextFormatting.DARK_AQUA + "========"+
						TextFormatting.DARK_BLUE + "[]"));
				ArrayList<String> online = new ArrayList<String>();
				for(String name : Displayname.HVs.keySet()) {
					if(!isOnline(name)) {
						if(!TabellenMethoden.isDayOver(Displayname.HVs.get(name).getBis())) {
							Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - "+name ));
						}else {
							Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - "+ TextFormatting.RED +name));
						}
					}else online.add(name);
				}
				for(String name : online) {
					if(!TabellenMethoden.isDayOver(Displayname.HVs.get(name).getBis())) {
						Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GREEN + " - " +name));
					}else {
						Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GREEN + " - " +TextFormatting.RED +name));
					}
				}
				return;
			}else
				if(args[0].equalsIgnoreCase("namecheck")){
					Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GRAY + " NameCheck wird ausgeführt! Dies könnte paar Sekunden dauern!"));
					Thread thread = new Thread(){
						@Override
						public void run() {
							ArrayList<String> nameError = new ArrayList<>();
							for(String name : Displayname.HVs.keySet()) {
								if(!isOnline(name)) {
									if(PlayerCheck.checkName(name) == false) nameError.add(name);
								}
							}
							if(nameError.size() == 0) {
								Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.BLUE + " - Es wurden keine Spieler mit fehlerhaften Namen gefunden!"));
							}else Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - Spieler mit Fehler im Namen:"));
							for(String player : nameError){
								Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - "+ TextFormatting.RED +player));
							}
						}
					};
					thread.start();
			}else
			if(args[0].equalsIgnoreCase("info")) {
				displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv info <User>" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Zeigt die Hausverbot-Infos zum Spieler."));
				return;
			}else {
				displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv " + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "synchronisiere die Hausverbote mit dem Client."));
				displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv list" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Gibt eine Liste mit allen Spielern aus, die Hausverbot haben."));
				displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " - " + TextFormatting.AQUA + "/hv info <User>" + TextFormatting.DARK_GRAY + "-> " + TextFormatting.GRAY + "Zeigt die Hausverbot-Infos zum Spieler."));
			}
		}
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("info")) {
				for(HV_User users : Displayname.HVs.values()) {
					if(args[1].equalsIgnoreCase(users.getName())) {
						displayMessage(new TextComponentString(
								TextFormatting.DARK_BLUE + "[]"+
								TextFormatting.DARK_AQUA + "========"+
								TextFormatting.GRAY + "["+
								TextFormatting.AQUA + "Hausverbot"+
								TextFormatting.GRAY + "]"+
								TextFormatting.DARK_AQUA + "========"+
								TextFormatting.DARK_BLUE + "[]"));
						displayMessage(new TextComponentString(""));
						displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " -" + TextFormatting.GRAY +" Wer: " + TextFormatting.RED + users.getName()));
						displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " -" + TextFormatting.GRAY +" Von: " + TextFormatting.RED + users.getVon()));
						displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " -" + TextFormatting.GRAY +" Grund: " + TextFormatting.RED + users.getGrund()));
						displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " -" + TextFormatting.GRAY +" Wann: " + TextFormatting.RED + users.getWann()));
						displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " -" + TextFormatting.GRAY +" Bis: " + TextFormatting.RED + users.getBis()));
						String str = ""; if(!users.getDauer().equals("Permanent")) str = " Wochen";
						displayMessage(new TextComponentString(TextFormatting.DARK_GRAY + " -" + TextFormatting.GRAY +" Dauer: " + TextFormatting.RED + users.getDauer() + str));
						displayMessage(new TextComponentString(""));
						return;
					}
				}	
			}
		}
		
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		List<String> tabs = new ArrayList<String>();
		if(args.length == 1) {
			tabs.add("list");
			tabs.add("help");
			tabs.add("info");
			tabs.add("namecheck");
		}	
		if(args[0].equalsIgnoreCase("info")) {
			tabs.clear();
			
			if(args.length == 2) {
				String start = args[1].toLowerCase();
				for(String names : Displayname.HVs.keySet()) {
					if(names.toLowerCase().startsWith(start)) {
						tabs.add(names);	
					}
				}
				return tabs;
			}
			
			
			for(String names : Displayname.HVs.keySet()) {
				tabs.add(names);
			}
			
		}
		return tabs;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
	


	private void displayMessage(TextComponentString text) {
		Minecraft.getMinecraft().player.sendMessage(text);
	}
	private boolean isOnline(String Playername) {
		if(Minecraft.getMinecraft().getConnection().getPlayerInfo(Playername) != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
		// TODO Auto-generated method stub
		return false;
	}

}

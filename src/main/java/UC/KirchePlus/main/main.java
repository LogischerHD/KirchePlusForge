package UC.KirchePlus.main;

import UC.KirchePlus.AutomaticActivity.SaveActivity_Command;
import UC.KirchePlus.Commands.HV_Command;
import UC.KirchePlus.Commands.MarryRP_Command;
import UC.KirchePlus.Commands.aEquip;
import UC.KirchePlus.Commands.checkDonations;
import UC.KirchePlus.Utils.MarryFile;
import UC.KirchePlus.Utils.PlayerCheck;
import UC.KirchePlus.Utils.SpenderInfo;
import UC.KirchePlus.Utils.TabellenMethoden;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.IOException;
import java.util.ArrayList;

@Mod(modid = main.MODID, version = main.VERSION)
public class main {
	
    public static final String MODID = "kirche+";
    public static final String VERSION = "2.0";
    public static ArrayList<SpenderInfo> spender = new ArrayList<>();

    @EventHandler
    public void init(FMLInitializationEvent event)  {
        NetworkRegistry.INSTANCE.newEventDrivenChannel("KP:Channel");
    	TabellenMethoden.init();
    	MarryFile.load();
		try {
			TabellenMethoden.getHVList();
			TabellenMethoden.getAllMemberSheets();
		} catch (Exception e) {e.printStackTrace();}

    }
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	ClientCommandHandler.instance.registerCommand(new HV_Command());
    	ClientCommandHandler.instance.registerCommand(new MarryRP_Command());
        ClientCommandHandler.instance.registerCommand(new checkDonations());
        ClientCommandHandler.instance.registerCommand(new aEquip());
        ClientCommandHandler.instance.registerCommand(new SaveActivity_Command());
    }










}

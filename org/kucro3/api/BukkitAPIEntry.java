package org.kucro3.api;

import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.kucro3.lib.gui.*;

public class BukkitAPIEntry extends JavaPlugin implements CommandExecutor {
	public void onEnable()
	{
		this.getCommand("rg").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		try {
			GUIInventory inv = new GUIInventory(1);
			inv.addComponent("1", new GUIUndragableItem(new ItemStack(Material.IRON_BLOCK)), 0);
			inv.addComponent("2", new GUIDragableItem(new ItemStack(Material.DIAMOND_BLOCK)), 1);
			inv.openInventory((Player)sender);
		} catch (GUIException e) {
			e.printStackTrace();
		}
		return true;
	}
}

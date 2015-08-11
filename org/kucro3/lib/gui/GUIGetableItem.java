package org.kucro3.lib.gui;

import org.bukkit.inventory.ItemStack;

public class GUIGetableItem extends GUIDragableItem {
	public GUIGetableItem(ItemStack item) 
	{
		super(item);
	}
	
	public boolean allowedGet(int x, int y)
	{
		return true;
	}
}

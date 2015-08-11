package org.kucro3.lib.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GUIDragableItem extends GUIComponent {
	public GUIDragableItem(ItemStack item)
	{
		super(1, 1);
		this.item = item;
	}

	@Override
	public ItemStack getContent(int x, int y)
	{
		if(x == 0 && y == 0)
			return item;
		return null;
	}

	@Override
	public void setContent(int x, int y, ItemStack item) 
	{
		if(x == 0 && y == 0)
			this.item = item;
	}
	
	public boolean allowedDrag(int x, int y)
	{
		return true;
	}

	@Override
	public void onClick(int x, int y, Player player, ItemStack carried, GUIInventory inv) 
	{
	}
	
	protected ItemStack item;
}

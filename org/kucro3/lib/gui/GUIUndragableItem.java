package org.kucro3.lib.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GUIUndragableItem extends GUIComponent {
	public GUIUndragableItem(ItemStack item) 
	{
		super(1, 1);
		this.content = item;
	}

	@Override
	public ItemStack getContent(int x, int y)
	{
		if(x == 0 && y == 0)
			return content;
		return null;
	}

	@Override
	public void setContent(int x, int y, ItemStack item) 
	{
		if(x == 0 && y == 0)
			this.content = item;
	}

	@Override
	public void onClick(int x, int y, Player player, ItemStack item, GUIInventory inv) 
	{
	}

	protected ItemStack content;
}

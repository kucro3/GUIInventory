package org.kucro3.lib.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class GUIComponent {
	protected GUIComponent(int xsize, int ysize)
	{
		this.xsize = xsize;
		this.ysize = ysize;
	}
	
	public boolean allowedGet(int x, int y) {return false;}
	
	public boolean allowedGet(int x, int y, Player player) {return allowedGet(x, y);}
	
	public boolean allowedPut(int x, int y) {return false;}
	
	public boolean allowedPut(int x, int y, ItemStack item) {return allowedPut(x, y);}
	
	public boolean allowedDrag(int x, int y) {return false;}
	
	public boolean allowedDrag(int x, int y, Player player) {return allowedDrag(x, y);}
	
	public abstract ItemStack getContent(int x, int y);
	
	public ItemStack getContent(int slot)
	{
		int[] l =  GUIInventory.offset(slot);
		return getContent(l[0], l[1]);
	}
	
	public void setNullWhenDrag(int x, int y)
	{
		setContent(x, y, null);
	}
	
	public void setItemWhenDragCancelled(int x, int y, ItemStack item)
	{
		setContent(x, y, item);
	}
	
	public abstract void setContent(int x, int y, ItemStack item);
	
	public abstract void onClick(int x, int y, Player player, ItemStack carried, GUIInventory inv);
	
	public final int getXSize()
	{
		return xsize;
	}
	
	public final int getYSize()
	{
		return ysize;
	}
	
	private final int xsize;
	
	private final int ysize;
}

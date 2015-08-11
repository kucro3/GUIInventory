package org.kucro3.lib.gui;

import java.util.*;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractInventory {	
	public List<HumanEntity> getViewers()
	{
		return viewers;
	}

	@Deprecated
	public HumanEntity getViewer()
	{
		return viewers.get(0);
	}
	
	public boolean reachable(HumanEntity entity)
	{
		return true;
	}
	
	public boolean putable(int slot, ItemStack item)
	{
		return true;
	}
	
	public abstract org.bukkit.inventory.ItemStack[] getContentsMethod();

	public abstract String getInventoryName();
	
	public abstract int getMaxStackSize();

	public abstract InventoryHolder getOwner();

	public abstract int getSize();

	public boolean nameable()
	{
		return true;
	}
	
	public abstract void onClosedMethod();

	final void onCloseMethod0(HumanEntity entity)
	{
		viewers.remove(entity);
		onCloseMethod(entity);
	}
	
	public abstract void onCloseMethod(HumanEntity entity);
	
	final void onOpenMethod0(HumanEntity entity)
	{
		viewers.add(entity);
		onOpenMethod(entity);
	}
	
	public abstract void onOpenMethod(HumanEntity entity);

	public abstract void setItemMethod(int slot, org.bukkit.inventory.ItemStack item);	
	
	public abstract void setMaxStackSizeMethod(int size);
	
	public abstract org.bukkit.inventory.ItemStack splitStackMethod(int slot, int amount);
	
	public abstract org.bukkit.inventory.ItemStack splitWithoutUpdateMethod(int slot);

	public abstract void startOpenMethod();
	
	public abstract void updateMethod();
	
	public abstract org.bukkit.inventory.ItemStack getItemMethod(int slot);
	
	public void setListener(InventoryListener listener)
	{
		this.listener = listener;
	}
	
	public InventoryListener getListener()
	{
		return listener;
	}
	
	protected boolean supportedCraftInventory()
	{
		return true;
	}
	
	public final Inventory getBukkitInventory()
	{
		if(this.supportedCraftInventory())
			return new FactoryInventory(this).getInventory();
		else
			throw new UnsupportedOperationException();
	}
	
	InventoryListener listener = null;
	
	protected final List<HumanEntity> viewers = new ArrayList<>();
}

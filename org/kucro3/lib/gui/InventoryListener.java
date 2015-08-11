package org.kucro3.lib.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

public interface InventoryListener {
	public abstract void onClose(HumanEntity player, AbstractInventory inv);
	
	public abstract void onClosed(AbstractInventory inv);
	
	public abstract void onOpen(HumanEntity player, AbstractInventory inv);
	
	public abstract boolean setItem(int slot, ItemStack item, AbstractInventory inv);
	
	public abstract boolean setMaxStackSize(int size, AbstractInventory inv);
	
	public abstract boolean splitStack(int slot, int amount, AbstractInventory inv);
	
	public abstract boolean splitWithoutUpdate(int slot, AbstractInventory inv);
	
	public abstract void startOpen(AbstractInventory inv);
	
	public abstract boolean update(AbstractInventory inv);
}

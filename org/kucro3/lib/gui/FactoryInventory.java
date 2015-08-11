package org.kucro3.lib.gui;

import java.util.List;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.IInventory;
import net.minecraft.server.v1_7_R2.ItemStack;

class FactoryInventory implements IInventory {
	public FactoryInventory(AbstractInventory inv)
	{
		this.inv = inv;
	}
	
	public boolean a(EntityHuman arg0) 
	{
		return inv.reachable(arg0.getBukkitEntity());
	}

	public boolean b(int arg0, ItemStack arg1)
	{
		return inv.putable(arg0, CraftItemStack.asBukkitCopy(arg1));
	}

	public ItemStack[] getContents()
	{
		return asNMSCopy(inv.getContentsMethod());
	}

	public List<HumanEntity> getViewers()
	{
		return inv.viewers;
	}

	public boolean k_() 
	{
		return inv.nameable();
	}

	public void l_()
	{
		if(inv.listener != null)
			inv.listener.onClosed(inv);
		inv.onClosedMethod();
	}
	
	public void onClose(CraftHumanEntity arg0) 
	{
		if(inv.listener != null)
			inv.listener.onClose(arg0, inv);
		inv.onCloseMethod0(arg0);
		inv.viewers.remove(arg0);
	}
	
	public void onOpen(CraftHumanEntity arg0) 
	{
		if(inv.listener != null)
			inv.listener.onOpen(arg0, inv);
		inv.onOpenMethod0(arg0);
		inv.viewers.add(arg0);
	}
	
	public void setItem(int arg0, ItemStack arg1) 
	{
		org.bukkit.inventory.ItemStack bukkititem = CraftItemStack.asBukkitCopy(arg1);
		if(inv.listener != null)
			if(!inv.listener.setItem(arg0, bukkititem, inv))
				inv.setItemMethod(arg0, bukkititem);
			else;
		else
			inv.setItemMethod(arg0, bukkititem);
				
	}
	
	public void setMaxStackSize(int arg0) 
	{
		if(inv.listener != null)
			if(!inv.listener.setMaxStackSize(arg0, inv))
				inv.setMaxStackSizeMethod(arg0);
			else;
		else
			inv.setMaxStackSizeMethod(arg0);
	}
	
	public ItemStack splitStack(int arg0, int arg1)
	{
		if(inv.listener != null)
			if(!inv.listener.splitStack(arg0, arg1, inv))
				return CraftItemStack.asNMSCopy(inv.splitStackMethod(arg0, arg1));
			else return null;
		else
			return CraftItemStack.asNMSCopy(inv.splitStackMethod(arg0, arg1));
	}
	
	public ItemStack splitWithoutUpdate(int arg0) 
	{
		if(inv.listener != null)
			if(!inv.listener.splitWithoutUpdate(arg0, inv))
				return CraftItemStack.asNMSCopy(inv.splitWithoutUpdateMethod(arg0));
			else return null;
		else
			return CraftItemStack.asNMSCopy(inv.splitWithoutUpdateMethod(arg0));
	}

	public void startOpen()
	{
		if(inv.listener != null)
			inv.listener.startOpen(inv);
		inv.startOpenMethod();
	}
	
	public void update() 
	{
		if(inv.listener != null)
			if(!inv.listener.update(inv))
				inv.updateMethod();
			else;
		else
			inv.updateMethod();
	}

	public ItemStack getItem(int slot)
	{
		return CraftItemStack.asNMSCopy(inv.getItemMethod(slot));
	}
	
	static org.bukkit.inventory.ItemStack[] asBukkitCopy(ItemStack[] stack)
	{
		org.bukkit.inventory.ItemStack[] stacks = new org.bukkit.inventory.ItemStack[stack.length];
		for(int i = 0; i < stacks.length; i++)
			stacks[i] = CraftItemStack.asBukkitCopy(stack[i]);
		return stacks;
	}
	
	static ItemStack[] asNMSCopy(org.bukkit.inventory.ItemStack[] stack)
	{
		ItemStack[] stacks = new ItemStack[stack.length];
		for(int i = 0; i < stacks.length; i++)
			stacks[i] = CraftItemStack.asNMSCopy(stack[i]);
		return stacks;
	}
	
	public String getInventoryName() 
	{
		return inv.getInventoryName();
	}

	public int getMaxStackSize()
	{
		return inv.getMaxStackSize();
	}

	public InventoryHolder getOwner() 
	{
		return inv.getOwner();
	}

	public int getSize()
	{
		return inv.getSize();
	}
	
	final Inventory getInventory()
	{
		return new CraftInventory(this);
	}
	
	final AbstractInventory inv;
}

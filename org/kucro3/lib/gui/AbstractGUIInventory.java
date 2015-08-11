package org.kucro3.lib.gui;

import java.lang.ref.*;
import java.util.*;

import org.bukkit.craftbukkit.v1_7_R2.entity.*;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.server.v1_7_R2.*;

public abstract class AbstractGUIInventory extends AbstractInventory {	
	public HumanEntity getViewer()
	{
		return viewers.get(0);
	}
	
	public abstract boolean dragable(int slot, Player player);
	
	public abstract boolean putable(int slot, org.bukkit.inventory.ItemStack item);
	
	public abstract boolean getable(int slot, Player player);
	
	public abstract org.bukkit.inventory.ItemStack[] getContentsMethod();

	public abstract String getInventoryName();
	
	public abstract int getMaxStackSize();

	public abstract InventoryHolder getOwner();

	public abstract int getSize();

	public boolean nameable()
	{
		return true;
	}
	
	public final void onClosedMethod()
	{
		viewing = null;
		windowId = -1;
		callOnClosed();
	}
	
	public void callOnClosed()
	{
		
	}

	public abstract void onCloseMethod(HumanEntity entity);
	
	public abstract void onOpenMethod(HumanEntity entity);

	public abstract void setItemMethod(int slot, org.bukkit.inventory.ItemStack item);	
	
	public abstract void setMaxStackSizeMethod(int size);
	
	public abstract org.bukkit.inventory.ItemStack splitStackMethod(int slot, int amount);
	
	public abstract org.bukkit.inventory.ItemStack splitWithoutUpdateMethod(int slot);

	public abstract void startOpenMethod();
	
	public abstract void updateMethod();
	
	public abstract org.bukkit.inventory.ItemStack getItemMethod(int slot);
	
	public final void updateFakeCursor(org.bukkit.inventory.ItemStack item)
	{
		viewing.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, CraftItemStack.asNMSCopy(item)));
	}
	
	public final void updateCursor(org.bukkit.inventory.ItemStack item)
	{
		viewing.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, CraftItemStack.asNMSCopy(item)));
		viewing.inventory.setCarried(CraftItemStack.asNMSCopy(item));
	}
	
	public final void updateSlot(int i)
	{
		org.bukkit.inventory.ItemStack item = getItemMethod(i);
		PlayerConnection pc = viewing.playerConnection;
		pc.sendPacket(new PacketPlayOutSetSlot(windowId, i, CraftItemStack.asNMSCopy(item)));
	}
	
	public final void updateAll()
	{
		List<ItemStack> list = new ArrayList<>(this.getSize());
		for(int i = 0; i < getSize(); i++)
			list.add(CraftItemStack.asNMSCopy(getItemMethod(i)));
		viewing.playerConnection.sendPacket(new PacketPlayOutWindowItems(windowId, list));
	}
	
	public final boolean isUsing()
	{
		return !(viewers.isEmpty() && (using == null || using.get() == null));
	}
	
	public final void openInventory(Player player) throws GUIException
	{
		EntityPlayer ep = ((CraftPlayer)player).getHandle();
		ep.openContainer(getInventory());
		windowId = ep.activeContainer.windowId;
		viewing = ep;
	}
	
	private final IInventory getInventory() throws GUIException
	{
		if(!isUsing())
		{
			IInventory inv = new FactoryInventory(this);
			using = new WeakReference<>(inv);
			return inv;
		}
		else
			throw new GUIExceptionUsing();
	}
	
	private int windowId;
	
	private EntityPlayer viewing;
	
	private Reference<IInventory> using;
}

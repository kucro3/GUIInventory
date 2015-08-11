package org.kucro3.lib.gui;

import java.util.*;

import org.bukkit.craftbukkit.v1_7_R2.entity.*;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GUIInventory extends AbstractGUIInventory {
	public GUIInventory(int ysize)
	{
		if(ysize < 0 || ysize > 6)
			throw new IllegalArgumentException("invaild size.");
		this.xsize = 9;
		this.size = ysize * xsize;
		this.nullptr = new GUIComponentPointerB();
		this.components = new GUIComponentPointer[size];
		Arrays.fill(components, nullptr);
		this.mappedComponents = new HashMap<>();
		this.ysize = ysize;
	}
	
	@Override
	public ItemStack[] getContentsMethod()
	{
		ItemStack[] stack = new ItemStack[size];
		for(int i = 0; i < stack.length; i++)
			stack[i] = getItemMethod(i);
		return stack;
	}

	@Override
	public String getInventoryName()
	{
		return name;
	}

	@Override
	public int getMaxStackSize() 
	{
		return 64;
	}

	@Override
	public InventoryHolder getOwner()
	{
		return null;
	}

	@Override
	public int getSize()
	{
		return size;
	}

	@Override
	public void onCloseMethod(HumanEntity entity)
	{
		
	}

	@Override
	public void onOpenMethod(HumanEntity entity)
	{
		
	}
	
	private final void checkSpace(GUIComponent component, int basicAddress) throws GUIException
	{
		int[] location = offset(basicAddress);
		int x = location[0], y = location[1];
		int xsize = component.getXSize(), ysize = component.getYSize();
		
		if(x + xsize > this.xsize || y + ysize > this.ysize)
			throw new GUIExceptionNotEnoughSpace();
	
		int i = basicAddress;
		for(int j = 0; j < ysize; j++, i += 9)
			for(int k = 0, l = i; k < xsize; k++, l++)
				if(components[l] != nullptr)
					throw new GUIExceptionSpaceDuplicated();
	}
	
	public final void addComponent(String name, GUIComponent component, int basicAddress) throws GUIException
	{
		updated = false;
		
		if(mappedComponents.containsKey(name))
			throw new GUIExceptionNameDuplicated();
		else if(component.getXSize() == 0 || component.getYSize() == 0)
			mappedComponents.put(name, new GUIComponentPointerA(name, component));
		else 
		{
			checkSpace(component, basicAddress);
			GUIComponentPointer pComponent = new GUIComponentPointerA(name, component);
			pComponent.basicAddress = basicAddress;
			pComponent.basicOffset = offset(basicAddress);
			pComponent.covered = new int[pComponent.getXSize() * pComponent.getYSize()];
			mappedComponents.put(name, pComponent);
			
			int address = basicAddress, count = 0;
			for(int i = 0; i < pComponent.getYSize(); i++, address+=9)
				for(int j = 0, k = address; j < pComponent.getXSize(); j++, k++, count++)
					(components[k] = pComponent).covered[count] = k;
		}
	}
	
	public final GUIComponent getComponent(int x, int y)
	{
		return getComponent(y * 9 + x);
	}
	
	public final GUIComponent getComponent(int slot)
	{
		return components[slot].getComponent();
	}
	
	public final boolean containsComponent(String name)
	{
		return mappedComponents.containsKey(name);
	}
	
	public final GUIComponent getComponent(String name)
	{
		GUIComponentPointer ptr;
		return (ptr = mappedComponents.get(name)) == null ? null : ptr.getComponent();
	}
	
	public final GUIComponent removeComponent(String name)
	{
		updated = false;
		
		GUIComponentPointer pComponent;
		if((pComponent = mappedComponents.get(name)) == null)
			return null;
		else
		{
			for(int address : pComponent.covered)
				components[address] = nullptr;
			
			return pComponent.getComponent();
		}
	}
	
	public final boolean updateComponent(String name)
	{
		GUIComponentPointer ptr;
		if((ptr = mappedComponents.get(name)) == null)
			return false;
		
		for(int slot : ptr.covered)
			updateSlot(slot);
		
		return true;
	}

	@Override
	public void setItemMethod(int slot, ItemStack item)
	{
		updated = false;
		
		Player player = (Player)super.getViewer();
		GUIComponentPointer component = components[slot];
		int[] location = relativeOffset(slot, component.basicOffset);
		int x = location[0], y = location[1];
		
		component.onClick(x, y, player, item, this);
		
		if(!component.allowedPut(x, y, item))
		{
			updateSlot(slot);
			this.carried = item;
			this.fakedCarrier = this.lastFake;
		}
		else
		{
			setItemMethod(slot, item);
			updateSlot(slot);
			if(component.allowedDrag(x, y, player))
			{
				ItemStack carried = getItemMethod(slot);
				this.carried = carried;
				this.fakedCarrier = !component.allowedGet(x, y, player);
			}
			else
				this.carried = null;
		}
	}

	@Override
	public void setMaxStackSizeMethod(int size)
	{
	}

	@Override
	public ItemStack splitStackMethod(int slot, int amount)
	{
		updated = false;
		
		GUIComponentPointer component = components[slot];
		int[] location = relativeOffset(slot, component.basicOffset);
		int x = location[0], y = location[1];
		
		ItemStack craftMirror = CraftItemStack.asCraftMirror(((CraftPlayer)super.getViewer()).getHandle().inventory.getCarried());
		
		component.onClick(x, y, (Player)super.getViewer(), craftMirror, this);
		
		if(!component.allowedDrag(x, y, (Player)super.getViewer()))
		{
			updateSlot(slot);
			if(!this.lastFake)
				carried = craftMirror;
			return null;
		}
		else
		{
			boolean flag = component.allowedGet(x, y, (Player)super.getViewer());
			ItemStack item = getItemMethod(slot)
					, result = new ItemStack(item);
			
			fakedCarrier = !flag;
			
			if(amount == -1)
				if(!flag)
				{
					updateSlot(slot);
					if(!this.lastFake)
						carried = craftMirror;
					return null;
				}
				else
					component.setContent(x, y, item);
			else
			{
				int realAmount = Math.min(item.getAmount(), amount);
				result.setAmount(realAmount);
				component.setContent(x, y, item);
				updateSlot(slot);
			}
			
//			fakedCarrier = !flag;
			carried = result;
			
			return null;
		}
	}

	@Override
	public ItemStack splitWithoutUpdateMethod(int slot)
	{
		return splitStackMethod(slot, -1);
	}

	@Override
	public void startOpenMethod() 
	{
	}

	@Override
	public void updateMethod() 
	{
		((Player)super.getViewer()).sendMessage("updated!" + carried + fakedCarrier + lastFake + updated);
		
		if(!updated)
		{
			lastFake = fakedCarrier;
			fakedCarrier = false;
			updated = true;
		}
		
		if(!lastFake)
			super.updateCursor(carried);
		else
			super.updateFakeCursor(carried);
	}
	
	@Override
	public boolean dragable(int slot, Player player)
	{
		GUIComponentPointer component = components[slot];
		int[] location = relativeOffset(slot, component.basicOffset);
		return component.allowedDrag(location[0], location[1], player);
	}
	
	@Override
	public boolean getable(int slot, Player player)
	{
		GUIComponentPointer component = components[slot];
		int[] location = relativeOffset(slot, component.basicOffset);
		return component.allowedGet(location[0], location[1], player);
	}
	
	@Override
	public boolean putable(int slot, ItemStack item)
	{
		GUIComponentPointer component = components[slot];
		int[] location = relativeOffset(slot, component.basicOffset);
		return component.allowedPut(location[0], location[1], item);
	}
	
	private static final int[] relativeOffset(int slot, int[] basicOffset)
	{
		if(basicOffset[0] < 0 || basicOffset[1] < 0)
			return nulladr;
		
		int[] location = offset(slot);
		int[] r = new int[2];
		r[0] = location[0] - basicOffset[0];
		r[1] = location[1] - basicOffset[1];
		
		assert ((r[0] & 0x80000000) | (r[1] & 0x80000000)) == 0;
		
		return r;
	}
	
	static final int[] offset(int slot)
	{
		if(fastIndex[slot] == null)
		{
			int x = slot % 9;
			int y = slot / 9;
			int[] i = (fastIndex[slot] = new int[] {x, y});
			return i;
		}
		else
			return fastIndex[slot];
	}

	@Override
	public ItemStack getItemMethod(int slot)
	{
		if(slot == -1)
			return carried;
		return components[slot].getContent(slot - components[slot].basicAddress);
	}
	
	public String name = "GUI";
	
	private final int size;
	
	private final int xsize;
	
	private final int ysize;
	
	private static final int[] nulladr = new int[2];
	
	private static final int[][] fastIndex = new int[54][];
	
	private ItemStack carried;
	
	private boolean lastFake;
	
	private boolean fakedCarrier;
	
	private boolean updated;
	
	private final GUIComponentPointer nullptr;
	
	private final GUIComponentPointer[] components;
	
	private final Map<String, GUIComponentPointer> mappedComponents;

	abstract class GUIComponentPointer extends GUIComponent
	{
		protected GUIComponentPointer(String name, int xsize, int ysize)
		{
			super(xsize, ysize);
			this.name = name;
		}
		
		final String getName()
		{
			return name;
		}
		
		public int[] covered;
		
		public int basicAddress;
		
		public int[] basicOffset;
		
		abstract GUIComponent getComponent();
		
		private final String name;
	}
	
	class GUIComponentPointerA extends GUIComponentPointer
	{
		GUIComponentPointerA(String name, GUIComponent component)
		{
			super(name, component.getXSize(), component.getYSize());
			Objects.requireNonNull(name);
			Objects.requireNonNull(component);
			this.component = component;
		}
		
		final GUIComponent getComponent()
		{
			return component;
		}
		
		@Override
		public final boolean allowedDrag(int x, int y)
		{
			return component.allowedDrag(x, y);
		}
		
		@Override
		public final boolean allowedGet(int x, int y)
		{
			return component.allowedGet(x, y);
		}
		
		@Override
		public final boolean allowedPut(int x, int y)
		{
			return component.allowedPut(x, y);
		}
		
		@Override
		public final org.bukkit.inventory.ItemStack getContent(int x, int y)
		{
			return component.getContent(x, y);
		}
		
		@Override
		public final void setContent(int x, int y, ItemStack item)
		{
			component.setContent(x, y, item);
		}
		
		@Override
		public final void onClick(int x, int y, Player player, ItemStack item, GUIInventory inv)
		{
			component.onClick(x, y, player, item, inv);
		}
		
		public int basicAddress;
		
		public int[] basicOffset;
		
		private final GUIComponent component;
	}
	
	class GUIComponentPointerB extends GUIComponentPointer
	{
		private GUIComponentPointerB()
		{
			super(null, 1, 1);
			super.basicAddress = -1;
			super.basicOffset = new int[] {-1, -1};
		}
		
		final GUIComponent getComponent()
		{
			return null;
		}

		@Override
		public ItemStack getContent(int x, int y)
		{
			return null;
		}

		@Override
		public void setContent(int x, int y, ItemStack item) 
		{
		}

		@Override
		public void onClick(int x, int y, Player player, ItemStack item, GUIInventory inv) 
		{
		}
	}
}

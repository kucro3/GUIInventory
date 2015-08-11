package org.kucro3.lib.gui;

import java.util.Arrays;

import org.bukkit.inventory.ItemStack;

public class GUIProgressBarVertical extends GUIProgressBar {
	public GUIProgressBarVertical(int xsize, int ysize, int topProgress, int graphicLength, ItemStack item) 
	{
		super(xsize, ysize, topProgress, graphicLength);
		this.item = item;
		this.contents = new ItemStack[ysize];
	}

	@Override
	public void setProgressContent(int progress)
	{
		if(progress < contents.length)
		{
			int i = 0;
			for(; i < progress; i++)
				contents[i] = item;
			for(; i < contents.length; i++)
				contents[i] = null;
		}
		else
			Arrays.fill(contents, item);
	}

	@Override
	public ItemStack getProgressContent(int x, int y) 
	{
		return contents[y];
	}

	private ItemStack[] contents;
	
	private final ItemStack item;
}

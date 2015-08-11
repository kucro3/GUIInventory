package org.kucro3.lib.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class GUIProgressBar extends GUIComponent {
	protected GUIProgressBar(int xsize, int ysize, int topProgress, int graphicLength)
	{
		super(xsize, ysize);
		this.topProgress = topProgress;
		this.graphicLength = graphicLength;
		this.ratio = topProgress / graphicLength;
	}
	
	@Override
	public final ItemStack getContent(int x, int y) 
	{
		return getProgressContent(x, y);
	}
	
	public abstract void setProgressContent(int progress);
	
	public abstract ItemStack getProgressContent(int x, int y);

	public final int getTopRealProgress()
	{
		return topProgress;
	}
	
	public final int getTopGrapchialProgress()
	{
		return graphicLength;
	}
	
	public final int getRealProgress()
	{
		return realProgress;
	}
	
	public final int getGraphicalProgress()
	{
		return graphicalProgress;
	}
	
	public final void setProgress(int progress)
	{
		this.realProgress = progress;
		
		if(progress > 0)
			if(progress < topProgress)
				this.graphicalProgress = progress / ratio;
			else
				this.graphicalProgress = this.graphicLength;
		else
			this.graphicalProgress = 0;
		
		setProgressContent(this.graphicalProgress);
	}
	
	@Override
	public void setContent(int x, int y, ItemStack item) 
	{
	}

	@Override
	public void onClick(int x, int y, Player player, ItemStack carried,
			GUIInventory inv) 
	{
	}
	
	private final int ratio;
	
	private final int graphicLength;
	
	private final int topProgress;
	
	private int graphicalProgress;
	
	private int realProgress;
}

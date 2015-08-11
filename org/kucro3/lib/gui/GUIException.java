package org.kucro3.lib.gui;

public class GUIException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -536501321561070252L;
	
	public GUIException()
	{
		super();
	}
	
	public GUIException(String msg)
	{
		super(msg);
	}
	
	public GUIException(Exception e)
	{
		super(e);
	}
}

package docubricks.gui;

import com.trolltech.qt.gui.QLabel;

/**
 * 
 * A horizontal header separator
 * 
 * @author Johan Henriksson
 *
 */
public class HeaderLabel extends QLabel
	{
	public HeaderLabel(String s)
		{
		super(s);
		//setObjectName("header");
		setStyleSheet("background-color:blue; color:white; font-size: 16pt;");
		}
	}

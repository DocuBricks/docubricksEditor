package docubricks.gui;

import java.util.LinkedList;

/**
 * 
 * List of suggested licenses
 * 
 * @author Johan Henriksson
 *
 */
public class LicensesUtil
	{
	public static LinkedList<String> licenses=new LinkedList<String>();
	
	public static void addLicense(String s)
		{
		licenses.add(s);
		}
	
	static
		{
		licenses.add("Apache License 2.0");
		licenses.add("BSD 3-Clause license");
		licenses.add("BSD 2-Clause license");
		licenses.add("CC-by-SA (Creative Commons Attribution-ShareAlike)");  //TODO more    
		licenses.add("CERN Open Hardware License"); //TODO add version num
		licenses.add("GPL 2 (GNU General Public License)");
		licenses.add("GPL 3 (GNU General Public License)");
		licenses.add("LGPL 2 (GNU Library/Lesser General Public License)");
		licenses.add("LGPL 3 (GNU Library/Lesser General Public License)");
		
		licenses.add("MIT license");
		licenses.add("Mozilla Public License 2.0");
		licenses.add("TAPR Open Hardware License");
/*		licenses.add("");
		licenses.add("");
		licenses.add("");
		
	*/	
		
		}
		
	
	
	
	
	
	
	
	}

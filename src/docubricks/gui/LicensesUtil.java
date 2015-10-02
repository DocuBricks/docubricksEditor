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
	public static LinkedList<String> otherLicenses=new LinkedList<String>();
	
	public static LinkedList<String> prefLicenses=new LinkedList<String>();
	
	public static void addLicense(String s)
		{
		otherLicenses.add(s);
		}
	
	static
		{
		prefLicenses.add("TAPR Open Hardware License");
		prefLicenses.add("CERN Open Hardware License"); //TODO add version num
		
		
		otherLicenses.add("Apache License 2.0");
		otherLicenses.add("BSD 3-Clause license");
		otherLicenses.add("BSD 2-Clause license");
		otherLicenses.add("CC-by-SA (Creative Commons Attribution-ShareAlike)");  //TODO more    
		otherLicenses.add("GPL 2 (GNU General Public License)");
		otherLicenses.add("GPL 3 (GNU General Public License)");
		otherLicenses.add("LGPL 2 (GNU Library/Lesser General Public License)");
		otherLicenses.add("LGPL 3 (GNU Library/Lesser General Public License)");
		
		otherLicenses.add("MIT license");
		otherLicenses.add("Mozilla Public License 2.0");
/*		licenses.add("");
		licenses.add("");
		licenses.add("");
		
	*/	
		
		}
		
	
	
	
	
	
	
	
	}

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
	public static LinkedList<String> otherSoftLicenses=new LinkedList<String>();
	public static LinkedList<String> otherHardLicenses=new LinkedList<String>();
	
	public static LinkedList<String> prefLicenses=new LinkedList<String>();
	
	public static String defLicense="CC-BY 3.0";
	
	
	public static void addSoftLicense(String s)
		{
		otherSoftLicenses.add(s);
		}
	public static void addHardLicense(String s)
		{
		otherHardLicenses.add(s);
		}
	
	static
		{
		addHardLicense("TAPR Open Hardware License");
		addHardLicense("CERN Open Hardware License"); //TODO add version num
		
		
		otherSoftLicenses.add("Apache License 2.0");
		otherSoftLicenses.add("BSD 3-Clause license");
		otherSoftLicenses.add("BSD 2-Clause license");
		otherSoftLicenses.add("CC-BY-SA 3.0 (Creative Commons Attribution-ShareAlike)");  //TODO more    
		otherSoftLicenses.add("GPL 2 (GNU General Public License)");
		otherSoftLicenses.add("GPL 3 (GNU General Public License)");
		otherSoftLicenses.add("LGPL 2 (GNU Library/Lesser General Public License)");
		otherSoftLicenses.add("LGPL 3 (GNU Library/Lesser General Public License)");
		
		otherSoftLicenses.add("MIT license");
		otherSoftLicenses.add("Mozilla Public License 2.0");
		
		prefLicenses.add(defLicense);
/*		licenses.add("");
		licenses.add("");
		licenses.add("");
		
	*/	
		
		}
		
	
	
	
	
	
	
	
	}

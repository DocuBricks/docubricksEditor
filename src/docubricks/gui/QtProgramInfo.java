package docubricks.gui;

/**
 * 
 * Information about the program
 * 
 * @author Johan Henriksson
 *
 */
public class QtProgramInfo
	{
	public static final String programName="Docubricks XML editor";
	public static final String programVersion="0.1.0";
	public static String licenseText=
			"If you use this software, please cite: ...\n"
			+ "\n"
			+ "Available under the 3-clause BSD license";
	
	
	public static String getVersionString()
		{
		return programVersion;
		}
	}

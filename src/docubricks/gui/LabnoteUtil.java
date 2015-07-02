package docubricks.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LabnoteUtil
	{

	 /**
   * Read file into string
   */
	public static String readFileToString(File src) throws IOException
		{
		InputStream is=new FileInputStream(src);
		String s=readStreamToString(is);
		is.close();
		return s;
		}

	/**
	 * Read UTF8 string from stream (assuming to read all of the stream)
	 */
	public static String readStreamToString(InputStream is) throws IOException
		{
		InputStreamReader reader=new InputStreamReader(is, "UTF-8");
		StringBuilder sbdis = new StringBuilder();
		char buf[]=new char[16386];
		for(;;)
			{
			int len=reader.read(buf);
			if(len==-1)
				break;
			sbdis.append(buf, 0, len);
			}
		reader.close();
		return sbdis.toString();
		}

	}

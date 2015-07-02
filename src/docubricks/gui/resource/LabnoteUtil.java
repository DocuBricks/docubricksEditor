package docubricks.gui.resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Utility functions
 * 
 * @author Johan Henriksson
 *
 */
public class LabnoteUtil
	{
	
	
  /**
   * Copy file from one location to another
   */
	public static void copy(File src, File dst) throws IOException
		{
		FileInputStream is = new FileInputStream(src);
		FileOutputStream os = new FileOutputStream(dst);
		FileChannel inChannel = is.getChannel();
		FileChannel outChannel = os.getChannel();
		inChannel.transferTo(0, inChannel.size(), outChannel);
		inChannel.close();
		outChannel.close();
		is.close();
		os.close();
		if(src.length()!=dst.length())
			throw new IOException("File copy length mismatch");
		}

  /**
   * Read file into memory
   */
	public static byte[] readFileToArray(File src) throws IOException
		{
		FileInputStream is = new FileInputStream(src);
		byte[] arr=readStreamToArray(is);
		is.close();
		return arr;
		}

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
	 * Read stream into an array
	 */
	public static byte[] readStreamToArray(InputStream is) throws IOException
		{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		byte[] buf = new byte[16384];
	  int len;
	  while ((len = is.read(buf)) > 0)
	  	os.write(buf, 0, len);
	  os.close();

	  return os.toByteArray();
		}


  
	/**
	 * Read the XML content from a file
	 */
  public static Document readXML(File filename) throws IOException, JDOMException
	  {
	  SAXBuilder saxBuilder = new SAXBuilder();
	  Document document = saxBuilder.build(filename);
	  return document;
	  }

	public static Document readXML(InputStream is) throws IOException, JDOMException
		{
	  SAXBuilder saxBuilder = new SAXBuilder();
	  Document document = saxBuilder.build(is);
	  return document;
		}

  
  /**
   * Write XML-document to disk
   */
  public static void writeXmlData(Document doc, File file) throws IOException
    {
    FileOutputStream writer=new FileOutputStream(file);
    writeXmlData(doc, writer);
    writer.close();
    }

  /**
   * Write XML-document to stream
   */
  public static void writeXmlData(Document doc, OutputStream os) throws IOException
    {
    Format format=Format.getPrettyFormat();
    XMLOutputter outputter = new XMLOutputter(format);
    outputter.output(doc, os);
    }

  /**
   * Delete directory or file recursively
   */
  public static void deleteRecursive(File f) throws IOException
    {
		if (f.isDirectory())
			for (File c : f.listFiles())
				deleteRecursive(c);
		f.delete();
    }



	/**
	 * E-mail regexp 
	 */
	private static final Pattern rfc2822 = Pattern.compile(
      "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
			);
	
	/**
	 * Check if an email address is valid
	 */
	public static boolean isEmailValid(String email)
		{
		return rfc2822.matcher(email).matches();
		}


	/**
	 * Figure out file extension. Return as .foo or as "" if none
	 */
	public static String getFileExt(File f)
		{
		String ext=f.getName();
		int index=ext.indexOf(".");
		if(index==-1)
			return "";
		else
			return ext.substring(index);
		}


	/**
	 * Put value within <name></name> and return XML element
	 */
	public static Element elementWithValue(String name, String value)
		{
		Element e=new Element(name);
		e.addContent(value);
		return e;
		}

	/**
	 * Store content of input stream to a file. The stream is closed afterwards
	 */
	public static void streamToFile(InputStream is, File file) throws IOException
		{
		FileOutputStream fos=new FileOutputStream(file);
		streamToStream(is, fos);
		is.close();
		fos.close();
		}

	/**
	 * Transfer the content from one stream to another
	 */
	public static void streamToStream(InputStream is, OutputStream os) throws IOException
		{
		byte[] buf = new byte[16384];
	  int len;
	  while ((len = is.read(buf)) > 0)
	  	os.write(buf, 0, len);
		}

	
	public static void streamToStream(InputStream is, OutputStream os, long tot) throws IOException
		{
		byte[] buf = new byte[16384];
	  int len;
	  while ((len = is.read(buf,0,(int)Math.min(tot,buf.length))) > 0)
	  	{
	  	os.write(buf, 0, len);
	  	tot-=len;
	  	}
		
		}

	/**
	 * Format the size of a document to make it human-readable
	 */
	public static String formatFileSize(long size)
		{
		NumberFormat nf=NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		
		if(size>1000000000)
			{
			return nf.format(size/1000000000.0)+" GB";
			}
		else if(size>1000000)
			{
			return nf.format(size/1000000.0)+" MB";
			}
		else if(size>1000)
			{
			return nf.format(size/1000.0)+" kB";
			}
		else 
			{
			return size+" B";
			}
			
		
		
		}


	/**
	 * Write string into file
	 */
	public static void writeStringToFile(File f, String s) throws IOException
		{
		FileWriter w=new FileWriter(f);
		w.write(s);
		w.close();
		}

	/**
	 * Convert array to UTF8 string
	 */
	public static String arrayToUTF8string(byte[] arr)
		{
		try
			{
			ByteArrayInputStream bis=new ByteArrayInputStream(arr);
			String s=readStreamToString(bis);
			bis.close();
			return s;
			}
		catch (Exception e)
			{
			e.printStackTrace();
			throw new RuntimeException("Conversion error");
			}
		}

	/**
	 * Primitive bubble sort, O(n^2)
	 */
	public static <E> void bubbleSort(ArrayList<E> list, Comparator<E> comp)
		{
		for(;;)
			{
			boolean sorted=false;
			//One round of bubble sort
			for(int i=0;i<list.size()-1;i++)
				{
				E a=list.get(i);
				E b=list.get(i+1);
				if(comp.compare(a,b)==-1)
					{
					list.set(i,b);
					list.set(i+1,a);
					sorted=true;
					}
				}
			if(!sorted)
				break;
			} 
		
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

	public static <E> String commaSeparateLowlevel(Collection<E> list)
		{
		boolean first=true;
		StringBuilder sb=new StringBuilder();
		for(E e:list)
			{
			if(!first)
				sb.append(", ");
			sb.append(e.toString());
			first=false;
			}
		return sb.toString();
		}
	
	public static <E> String commaSeparateOrNone(Collection<E> list)
		{
		boolean first=true;
		StringBuilder sb=new StringBuilder();
		for(E e:list)
			{
			if(!first)
				sb.append(", ");
			sb.append(e.toString());
			first=false;
			}
		String s=sb.toString();
		if(s.equals(""))
			return "(none)";
		else
			return sb.toString();
		}

	
	public static <E,F> String commaSeparate(Map<E,F> list)
		{
		boolean first=true;
		StringBuilder sb=new StringBuilder();
		for(E key:list.keySet())
			{
			if(!first)
				sb.append(", ");
			sb.append(key.toString()+"="+list.get(key));
			first=false;
			}
		String s=sb.toString();
		if(s.equals(""))
			return "(none)";
		else
			return sb.toString();
		}

	public static boolean equalsNull(Object value, Object value2)
		{
		if(value==null)
			return value2==null;
		else
			return value.equals(value2);
		}

	/**
	 * Format a duration, in ms
	 */
	public static String formatDuration(long duration)
		{
		long numDays=duration/(1000*60*60*24);
		duration-=numDays*1000*60*60*24;
		
		long numHours=duration/(1000*60*60);
		duration-=numHours*1000*60*60;
		
		long numMin=duration/(1000*60);
		duration-=numMin*1000*60;
		
		BigDecimal numSec=new BigDecimal(duration);
		numSec=numSec.divide(new BigDecimal(1000));
	
		String s="";
		if(numDays!=0)
			s+=numDays+"d";
		if(numHours!=0)
			s+=numHours+"h";
		if(numMin!=0)
			s+=numMin+"m";
		if(!numSec.equals(BigDecimal.ZERO) || s.isEmpty())
			s+=numSec+"s";
		return s;
		}

	/**
	 * Write a string. An integer followed by the string
	 */
	public static void writeString(DataOutputStream os, String s) throws IOException
		{
		os.writeInt(s.length());
		os.writeChars(s);
		}

	/**
	 * Write a string. An integer followed by the string. Check that the string is of length <=n
	 */
	public static void writeStringN(DataOutputStream os, String s, int n) throws IOException
		{
		if(s.length()<=n)
			{
			os.writeInt(s.length());
			os.writeChars(s);
			}
		else
			throw new IOException("Too long: "+s);
		}

	/**
	 * Read string from writers. Throw an error if the string is longer than n
	 */
	public static String readStringN(DataInputStream is, int n) throws IOException
		{
		StringBuilder sb=new StringBuilder();
		int len=is.readInt();
		if(len>n)
			{
			for(int i=0;i<n;i++)
				sb.append(is.readChar());
			System.out.println("So far: \""+sb.toString()+"\"");
			throw new IOException("String too long, max "+n);
			}
		for(int i=0;i<len;i++)
			sb.append(is.readChar());
		return sb.toString();
		}

	/**
	 * Read string from the writers
	 */
	public static String readString(DataInputStream is) throws IOException
		{
		StringBuilder sb=new StringBuilder();
		int len=is.readInt();
		for(int i=0;i<len;i++)
			sb.append(is.readChar());
		return sb.toString();
		}

	public static <E> void writeCollection(DataOutputStream dos, Collection<E> set) throws IOException
	{
	dos.writeInt(set.size());
	for(E bit:set)
		writeString(dos, bit.toString());
	}

	public static Collection<String> readCollection(DataInputStream is, int len) throws IOException
	{
	int num=is.readInt();
	LinkedList<String> list=new LinkedList<String>();
	for(int i=0;i<num;i++)
		list.add(readStringN(is, len));
	return list;
	}

	/**
	 * Check if two maps are equal to another, with equals() on both keys and values
	 */
	public static <E,F> boolean deepMapEquals(Map<E, F> map, Map<E, F> map2)
		{
		if(map.keySet().equals(map2.keySet()))
			{
			for(E key:map.keySet())
				if(!map.get(key).equals(map2.get(key)))
					return false;
			return true;
			}
		else
			return false;
		}

	public static boolean equalApprox(float x1, float x12)
		{
		String s1=""+x1;
		String s2=""+x12;
		s1=s1.substring(0,Math.min(s1.length(),4));
		s2=s2.substring(0,Math.min(s2.length(),4));
		
		if(!s1.equals(s2))	
			System.out.println("eek: "+x1+"  "+x12);
		
		return s1.equals(s2);
		}

	public static int compareInt(int a, int b)
		{
		if(a<b)
			return -1;
		else if(a>b)
			return 1;
		else
			return 0;
		}

	public static Long parseDuration(String text)
		{
		try
			{
			int i=0;
			long dur=0;
			for(;;)
				{
				//Parse numbers
				StringBuilder sbnum=new StringBuilder();
				while(i<text.length() && Character.isDigit(text.charAt(i)))
					{
					sbnum.append(text.charAt(i));
					i++;
					}
				if(sbnum.length()==0)
					return dur;
				int num=Integer.parseInt(sbnum.toString());
				

				//Parse unit
				StringBuilder sbunit=new StringBuilder();
				while(i<text.length() && Character.isAlphabetic(text.charAt(i)))
					{
					sbunit.append(text.charAt(i));
					i++;
					}
				String unit=sbunit.toString();

				long mul;
				if(unit.equals("d"))
					mul=24*3600L*1000L;
				else if(unit.equals("h"))
					mul=3600L*1000L;
				else if(unit.equals("m"))
					mul=60L*1000L;
				else if(unit.equals("s"))
					mul=1L*1000L;
				else if(unit.equals("ms"))
					mul=1L;
				else
					return null;
				
				dur+=mul*num;
				}
			}
		catch (NumberFormatException e)
			{
			e.printStackTrace();
			return null;
			}
		}

	}

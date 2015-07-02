package docubricks.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jdom2.Element;

/**
 * 
 * A file with media
 * 
 * @author Johan Henriksson
 *
 */
public class MediaFile
	{
//	public boolean toCopy=false;
	public File f=new File("/home/mahogny/Dropbox/m2/f/20150618_000109.jpg");

	
	//Note: no reason to distinguish image from video in tags! or? detect based on extension?
	//public String url;
	public String description;   //problematic if file is shared!
	
	
	public Element toXML(File basepath) throws IOException
		{
		//Save file if needed. i.e. if not in a subdirectory
		String currel=getRelativePath(basepath.getAbsolutePath(), f.getAbsolutePath());
		if(currel.startsWith("..") || currel.startsWith("./..") || currel.contains(":"))
			{
			//Find a suitable name - best is to just use the current name
			File subdir=new File(basepath,"usdata");
			subdir.mkdirs();
			File newfile=new File(subdir,f.getName());
			if(newfile.exists())
				{
				//Try to come up with an alternative name
				int i=0;
				File otherfile;
				do
					{
					otherfile=new File(newfile.getParentFile(), i+"-"+newfile.getName());
					i++;
					} while(otherfile.exists());
				newfile=otherfile;
				}
			
			//Copy the file
			copy(f, newfile);
			f=newfile;
			//toCopy=false;
			
			//idea: files already in a subdirectory should not need copying when attached?
			}
		else
			System.out.println("No need to save "+currel);
		
		Element root=new Element("file");
		root.setAttribute("url",getRelativePath(basepath.getAbsolutePath(), f.getAbsolutePath()));
		//desc?
		return root;
		}
	
	
	public static MediaFile fromXML(File basepath, Element root)
		{
		if(root.getName().equals("file"))
			{
			MediaFile mf=new MediaFile();
			mf.f=new File(basepath, root.getAttributeValue("url")); //need base path
			return mf;
			}
		else
			throw new RuntimeException("Not file: "+root);
		}
	
	
	/**
	 * Construct relative path. 
	 * From http://stackoverflow.com/questions/204784/how-to-construct-a-relative-path-in-java-from-two-absolute-paths-or-urls
	 */
	public static String getRelativePath (String baseDir, String targetPath) 
		{
		String[] base = baseDir.replace('\\', '/').split("\\/");
		targetPath = targetPath.replace('\\', '/');
		String[] target = targetPath.split("\\/");

		// Count common elements and their length.
		int commonCount = 0, commonLength = 0, maxCount = Math.min(target.length, base.length);
		while (commonCount < maxCount) 
			{
			String targetElement = target[commonCount];
			if (!targetElement.equals(base[commonCount])) 
				break;
			commonCount++;
			commonLength += targetElement.length() + 1; // Directory name length plus slash.
			}
		if (commonCount == 0)
			return targetPath; // No common path element.

		int targetLength = targetPath.length();
		int dirsUp = base.length - commonCount;
		StringBuilder relative = new StringBuilder(dirsUp * 3 + targetLength - commonLength + 1);
		for (int i = 0; i < dirsUp; i++)
			relative.append("../");
		if (commonLength < targetLength) 
			relative.append(targetPath.substring(commonLength));
		return "./"+relative.toString();
		}

	
  /**
   * Copy file from one location to another
   */
  public static void copy(File source, File destination) throws IOException 
    {
    InputStream in = new FileInputStream(source);
    OutputStream out = new FileOutputStream(destination);
    byte[] buffer = new byte[1024];
    int len;
    while ((len = in.read(buffer)) > 0) 
            out.write(buffer, 0, len);
    in.close();
    out.close();
    }

	}

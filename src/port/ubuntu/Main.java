package port.ubuntu;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;

import docubricks.gui.resource.LabnoteUtil;
import port.ubuntu.DebPackage.PkgType;



/**
 * Creates a deb-file
 * 
 * @author Johan Henriksson
 */
public class Main
	{

	public static void main(String[] args)
		{
		try
			{
			File releaseDir=new File("release");
			if(!releaseDir.exists())
				{
				System.err.println("No release dir!");
				System.exit(0);
				}
				
			File dPkg=new File("/tmp/docubricks");
			File dUsr=new File(dPkg,"usr");
			
			File dShare=new File(dUsr,"share");
			File dControl=new File(dPkg,"DEBIAN");
//			File dShareDoc=new File(dShare,"doc");
			File dUsrBin=new File(dUsr,"bin");
			File dRes=new File("ports/ubuntu");
			
			File fControlPostinst=new File(dControl,"postinst");
			File fControlPostrm=new File(dControl,"postrm");
			
			File fUsrBinLabstory=new File(dUsrBin,"docubricks");

			File dCompiledBin=new File("bin");
			File dCompiledLib=new File("lib");
//			File dCompiledLibLinux=new File(dCompiledLib,"linux");
			File dJarTarget=new File(dShare, "docubricks");
			File dTranslatonTarget=new File(new File(dShare, "docubricks"),"translations");

			//Clean dirs
			if(dPkg.exists())
				recursiveDelete(dPkg);

			
			//Make dirs
			dPkg.mkdirs();
			dControl.mkdirs();
			dJarTarget.mkdirs();
			dTranslatonTarget.mkdirs();
			/*
			dShare.mkdirs();
			dShareDoc.mkdirs();
			dUsrBin.mkdirs();
*/

			///////////////////////// Copy files
			
			//Copy debian file structure
			copyRecursive(new File(dRes,"root"), dPkg);

			//Copy library files
			for(File f:dCompiledLib.listFiles())
				if(f.getName().endsWith(".jar"))
					copyRecursive(f, new File(dJarTarget,f.getName()));
/*
			for(File f:dCompiledLibLinux.listFiles())
				if(f.getName().endsWith(".jar"))
					copyRecursive(f, new File(dJarTarget,f.getName()));
	*/		
			copyRecursive(new File(dCompiledBin.getParent(),"docubricks.jar"), new File(dJarTarget,"docubricks.jar"));
			//copyRecursive(new File(dCompiledBin.getParent(),"labstoryPostload.jar"), new File(dJarTarget,"labstoryPostload.jar"));
			
			//Copy translaton files
//			copyRecursive(new File(dCompiledBin, "translations"), dTranslatonTarget);
			
			
			setExec(fControlPostinst);
			setExec(fControlPostrm);
			setExec(fUsrBinLabstory);

			////////////////////// Set up dependencies
			System.out.println("Set up package dependencies");
			//Packages
			//maybe do pattern match instead?
			List<DebPackage> pkgs=new LinkedList<DebPackage>();
			pkgs.add(new DebPackage("default-jre | java6-runtime",null,new String[]{}));
			
			
			pkgs.add(new DebPackage("libqtjambi-snapshot",new String[]{"qtjambi.jar"},new String[]{}));

			pkgs.add(new DebPackage("libphonon4",new String[]{},new String[]{}));

			pkgs.add(new DebPackage("libdbus-java",new String[]{"dbus.jar"},new String[]{"dbus.jar"}));

			System.out.println("Extracting packages");
			deletePkgFiles(pkgs, dJarTarget);
			
			//////////// Write manifest file
			File manifestFile=File.createTempFile("MANIFEST", "");
			StringBuffer manifestContent=new StringBuffer();
			manifestContent.append("Manifest-Version: 1.0\n");
			manifestContent.append("Class-Path: \n");
			for(DebPackage pkg:pkgs)
				for(String jar:pkg.linkJars)
					{
					File jarfile=new File("/usr/share/java",jar);
					manifestContent.append(" "+jarfile.getAbsolutePath()+" \n");
					}
			manifestContent.append("\n");
			writeFile(manifestFile, manifestContent.toString());
			runUntilQuit(new String[]{"/usr/bin/jar","cmf",manifestFile.getAbsolutePath(),new File(dJarTarget,"externaljars.jar").getAbsolutePath()});

			System.out.println(manifestContent.toString());
			
//			System.exit(-1);
			
			/////////////// Read information about software
			Scanner scannerVersion = new Scanner(LabnoteUtil.readFileToString(new File(dCompiledBin,"docubricks/gui/version.txt")));
			Scanner scannerTimestamp = new Scanner(LabnoteUtil.readFileToString(new File(dCompiledBin,"docubricks/gui/timestamp.txt")));
			String version=scannerVersion.nextLine()+"."+scannerTimestamp.nextLine();
			scannerVersion.close();
			scannerTimestamp.close();
			
			int totalSize=(int)Math.ceil((recursiveSize(dUsr)+100000)/1024.0);

			/////////////// Write control file
			System.out.println("Writing control file");
			
			String controlFile=LabnoteUtil.readFileToString(new File(dRes,"debiancontrol-TEMPLATE")).
			replace("DEPENDENCIES", makeDeps(pkgs)).
			replace("RECOMMENDS", makeRecommends(pkgs)).
			replace("SUGGESTS", makeSuggests(pkgs)).
			replace("VERSION",version).
			replace("SIZE",""+totalSize);
			System.out.println("--------------------------------------");
			System.out.println(controlFile);
			System.out.println("--------------------------------------");
			writeFile(new File(dControl,"control"), controlFile);

			//////////// Make deb-file
			System.out.println("Debianizing");
			File outDeb=new File(releaseDir, "docubricks-"+version+".deb");
//			String tempDebfile="/tmp/labstory";
			if(outDeb.exists())
				outDeb.delete();
			runUntilQuit(new String[]{"/usr/bin/dpkg-deb","-b",dPkg.toString()});
			runUntilQuit(new String[]{"/bin/mv",dPkg.toString()+".deb",outDeb.toString()});
			System.out.println(outDeb);
			
			
			System.out.println("Done");
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}	
		
		}

	public static void setExec(File file)
		{
		System.out.println("set exec "+file);
		runUntilQuit(new String[]{"/bin/chmod","+x",file.getPath()});
		}
	
	public static void runUntilQuit(String[] arg)
		{
		StringBuffer sb=new StringBuffer();
		for(String s:arg)
			sb.append(" "+s);
		System.out.println("exec: "+sb);
		try
			{
			Process proc=Runtime.getRuntime().exec(arg);
			BufferedReader os=new BufferedReader(new InputStreamReader(proc.getInputStream()));
			proc.waitFor();
			String line;
			while((line=os.readLine())!=null)
				System.out.println("Got: "+line);
			System.out.println("Exit value: "+proc.exitValue());
			}
		catch (IOException e)
			{
			e.printStackTrace();
			}
		catch (InterruptedException e)
			{
			e.printStackTrace();
			}
		
		}
	
	
	public static String makeDeps(List<DebPackage> pkgs) throws Exception
		{
		StringBuffer sb=new StringBuffer();
		boolean first=true;
		for(DebPackage p:pkgs)
			if(p.name!=null && p.type==PkgType.Depends)
				{
				if(!first)
					sb.append(",");
				sb.append(p.name);
				first=false;
				}
		return sb.toString();
		}
	
	public static String makeSuggests(List<DebPackage> pkgs) throws Exception
		{
		StringBuffer sb=new StringBuffer();
		boolean first=true;
		for(DebPackage p:pkgs)
			if(p.name!=null && p.type==PkgType.Suggests)
				{
				if(!first)
					sb.append(",");
				sb.append(p.name);
				first=false;
				}
		return sb.toString();
		}
	
	
	public static String makeRecommends(List<DebPackage> pkgs) throws Exception
		{
		StringBuffer sb=new StringBuffer();
		boolean first=true;
		for(DebPackage p:pkgs)
			if(p.name!=null && p.type==PkgType.Recommends)
				{
				if(!first)
					sb.append(",");
				sb.append(p.name);
				first=false;
				}
		return sb.toString();
		}
	
	public static void deletePkgFiles(List<DebPackage> pkgs, File root)
		{
		boolean toDel=false;
		String fname=root.getName();
		for(DebPackage p:pkgs)
			if(p.providesFiles.contains(fname))
				{
				toDel=true;
				break;
				}
		if(toDel)
			recursiveDelete(root);
		else
			{
			if(root.isDirectory())
				for(File child:root.listFiles())
					deletePkgFiles(pkgs, child);
			}
		}

	public static void deleteExt(File root, String ext)
		{
		for(File child:root.listFiles())
			if(child.getName().endsWith(ext))
				recursiveDelete(child);
			else if(child.isDirectory())
				deleteExt(child, ext);
		}
	
	
	public static void deleteBinDirs(File root)
		{
		for(File child:root.listFiles())
			{
			if(child.isDirectory() && child.getName().startsWith("bin_"))
				{
				String osName=child.getName().substring(4);
				if(!osName.equals("linux"))
					recursiveDelete(child);
				}
			else if(child.isDirectory())
				deleteBinDirs(child);
			}
		}
	
	
	@SuppressWarnings("resource")
	public static void copyFile(File in, File out) throws IOException 
		{
		//limitation http://forums.sun.com/thread.jspa?threadID=439695&messageID=2917510
		out.getParentFile().mkdirs();
		FileChannel inChannel = new	FileInputStream(in).getChannel();
		FileChannel outChannel = new FileOutputStream(out).getChannel();
		try{inChannel.transferTo(0, inChannel.size(), outChannel);} 
		catch (IOException e){throw e;}
		finally 
			{
			if (inChannel != null) inChannel.close();
			if (outChannel != null) outChannel.close();
			}
		}
	
	
	public static void copyRecursive(File in, File out) throws IOException 
		{
		if(in.isDirectory())
			{
			for(File c:in.listFiles())
				if(!c.getName().equals(".") && !c.getName().equals(".."))
					{
					File outc=new File(out,c.getName());
					if(c.isDirectory())
						outc.mkdirs();
					copyRecursive(c, outc);
					}
			}
		else
			copyFile(in,out);
		}
	
	
	public static void recursiveDelete(File root)
		{
		//System.out.println("delete "+root);
		if(root.isDirectory())
			for(File child:root.listFiles())
				recursiveDelete(child);
		root.delete();
		}

	public static long recursiveSize(File root)
		{
		//System.out.println("delete "+root);
		if(root.isDirectory())
			{
			long size=0;
			for(File child:root.listFiles())
				size+=recursiveSize(child);
			return size;
			}
		else
			return root.length();
		}

	
  /**
   * Write string to file
   */
  public static void writeFile(File file,String out) throws IOException
    {
    FileWriter fw=new FileWriter(file);
    fw.write(out);
    fw.close();
    }

	
	}

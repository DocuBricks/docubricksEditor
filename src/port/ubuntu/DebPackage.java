package port.ubuntu;
import java.util.*;

/**
 * Debian package: name and what it provides
 * 
 * @author Johan Henriksson
 *
 */
public class DebPackage
	{
	public String name; //debian package file
	public Set<String> linkJars=new HashSet<String>();
	public Set<String> providesFiles=new HashSet<String>();
	private DebPackage(){}
	
	
	public enum PkgType{Depends, Suggests, Recommends}
	
	public PkgType type;
	
	/**
	 * Normal dependency
	 * @param name Name of required package
	 * @param linkjar JAR-files to link to in the system
	 * @param provides Files currently in libs/ that can be deleted
	 */
	public DebPackage(String name, String[] linkjar, String[] provides)
		{
		this.type=PkgType.Depends;
		this.name=name;
		
		if(linkjar!=null)
			for(String s:linkjar)
				linkJars.add(s);
		if(provides!=null)
			for(String s:provides)
				providesFiles.add(s);
		}
	
	
	public static DebPackage suggest(String name)
		{
		DebPackage pkg=new DebPackage();
		pkg.name=name;
		pkg.type=PkgType.Suggests;
		return pkg;
		}

	/**
	 * Recommended package dependency
	 */
	public static DebPackage recommends(String name, String[] linkjar, String[] provides)
		{
		DebPackage pkg=new DebPackage(name, linkjar, provides);
		pkg.type=PkgType.Recommends;
		return pkg;
		}
	
	
	}

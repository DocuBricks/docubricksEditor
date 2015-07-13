package docubricks.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;



/**
 * 
 * A project bundle of all data
 * 
 * @author Johan Henriksson
 *
 */
public class DocubricksProject
	{
	public ArrayList<Unit> units=new ArrayList<Unit>();
	public ArrayList<PhysicalPart> physicalParts=new ArrayList<PhysicalPart>();
	public ArrayList<Author> authors=new ArrayList<Author>();
	
	/**
	 * Find a free ID for a unit
	 */
	private int findFreeUnitID()
		{
		int id;
		for(;;)
			{
			id=(int)(Math.random()*Integer.MAX_VALUE);
			for(Unit p:units)
				if(p.id.equals(id))
					continue;
			break;
			}
		return id;
		}
	
	
	/**
	 * Find a free ID for a physical part
	 */
	private String findFreePhysPartID()
		{
		String id;
		for(;;)
			{
			id=""+(int)(Math.random()*Integer.MAX_VALUE);
			for(PhysicalPart p:physicalParts)
				if(p.id.equals(id))
					continue;
			break;
			}
		return id;
		}

	
	/**
	 * Find a free ID for a physical part
	 */
	private String findFreeAuthorID()
		{
		String id;
		for(;;)
			{
			id=""+(int)(Math.random()*Integer.MAX_VALUE);
			for(Author p:authors)
				if(p.id.equals(id))
					continue;
			break;
			}
		return id;
		}
	
	
	
	/**
	 * Serialize to XML
	 */
	public Element toXML(File basepath) throws IOException
		{
		Element eroot=new Element("project");
		for(PhysicalPart p:physicalParts)
			{
			Element ep=p.toXML(basepath);
			eroot.addContent(ep);
			}
		for(Unit u:units)
			{
			Element eu=u.toXML(basepath);
			eroot.addContent(eu);
			}
		for(Author a:authors)
			{
			Element eu=a.toXML(basepath);
			eroot.addContent(eu);
			}
		return eroot;
		}


	/**
	 * Read from XML
	 */
	public static DocubricksProject fromXML(File basepath, Element e)
		{
		DocubricksProject p=new DocubricksProject();
		for(Element c:e.getChildren())
			{
			if(c.getName().equals("author"))
				{
				Author a=Author.fromXML(basepath, c);
				p.authors.add(a);
				}
			}
		for(Element c:e.getChildren())
			{
			if(c.getName().equals("physical_part"))
				{
				PhysicalPart part=PhysicalPart.fromXML(basepath, c);
				p.physicalParts.add(part);
				}
			}
		for(Element c:e.getChildren())
			{
			if(c.getName().equals("unit"))
				{
				Unit u=Unit.fromXML(basepath, p, c);
				p.units.add(u);
				}
			}
		return p;
		}
	
	
	/**
	 * Store XML file (and blobs)
	 */
	public void storeXML(File f) throws IOException
		{
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    xmlOutputter.output(toXML(f.getParentFile()), new FileOutputStream(f));
		}

	
	/**
	 * Load XML file
	 */
	public static DocubricksProject loadXML(File f) throws IOException
		{
		try
			{
			FileInputStream is=new FileInputStream(f);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(is);
			DocubricksProject proj=fromXML(f.getParentFile(), doc.getRootElement());
			is.close();
			return proj;
			}
		catch (Exception e)
			{
			e.printStackTrace();
			throw new IOException(e.getMessage());
			}
		}

	
	/**
	 * Get physical part
	 */
	public PhysicalPart getPhysicalPart(String id)
		{
		for(PhysicalPart p:physicalParts)
			if(p.id.equals(id))
				return p;
		for(PhysicalPart p:physicalParts)
			System.out.println("ids: "+p.id+"   vs   "+id);
		throw new RuntimeException("Cannot find physical part "+id);
		}


	/**
	 * Get unit by ID
	 */
	public Unit getUnit(String id)
		{
		for(Unit p:units)
			if(p.id.equals(id))
				return p;
		throw new RuntimeException("Missing unit: "+id);
		}


	/**
	 * Create and attach a new unit
	 */
	public Unit createUnit()
		{
		Unit nu=new Unit();
		nu.id=""+findFreeUnitID();
		units.add(nu);
		return nu;
		}

	
	/**
	 * Create physical part
	 */
	public PhysicalPart createPhysicalPart()
		{
		PhysicalPart p=new PhysicalPart();
		p.id=findFreePhysPartID();
		physicalParts.add(p);
		return p;
		}


	public Author createAuthor()
		{
		System.out.println("creat "+authors);
		Author p=new Author();
		p.id=findFreeAuthorID();
		authors.add(p);
		return p;
		}


	public Author getAuthor(String id)
		{
		for(Author p:authors)
			if(p.id.equals(id))
				return p;
		throw new RuntimeException("Missing author: "+id);
		}


	/**
	 * Get root-level units
	 */
	public Collection<Unit> getRootUnits()
		{
		LinkedList<Unit> ret=new LinkedList<Unit>();
		HashSet<Unit> hasparent=new HashSet<Unit>();
		for(Unit u:units)
			for(LogicalPart lp:u.logicalParts)
				for(LogicalPartImplementation imp:lp.implementingPart)
					if(imp instanceof LogicalPartImplementationUnit)
						hasparent.add(((LogicalPartImplementationUnit)imp).get(this));
		for(Unit u:units)
			if(!hasparent.contains(u))
				ret.add(u);
		return ret;
		}



	
	}

package docubricks.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.ProcessingInstruction;
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
	public ArrayList<Brick> units=new ArrayList<Brick>();
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
			for(Brick p:units)
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
		for(Brick u:units)
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
				Brick u=Brick.fromXML(basepath, p, c);
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

		ProcessingInstruction pi=new ProcessingInstruction("xml-stylesheet");
		HashMap<String, String> pid=new HashMap<String, String>();
		pid.put("href", "docubricks.xsl");
		pid.put("type", "text/xml");
		pi.setData(pid);

		Document doc=new Document();
		doc.addContent(pi);
		doc.addContent(toXML(f.getParentFile()));
		
    xmlOutputter.output(doc, new FileOutputStream(f));
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
		throw new RuntimeException("Cannot find physical part "+id);
		}


	/**
	 * Get unit by ID
	 */
	public Brick getUnit(String id)
		{
		for(Brick p:units)
			if(p.id.equals(id))
				return p;
		throw new RuntimeException("Missing unit: "+id);
		}


	/**
	 * Create and attach a new unit
	 */
	public Brick createUnit()
		{
		Brick nu=new Brick();
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
	public Collection<Brick> getRootUnits()
		{
		LinkedList<Brick> ret=new LinkedList<Brick>();
		HashSet<Brick> hasparent=new HashSet<Brick>();
		for(Brick u:units)
			for(Function lp:u.logicalParts)
				for(FunctionImplementation imp:lp.implementingPart)
					if(imp instanceof FunctionImplementationBrick)
						hasparent.add(((FunctionImplementationBrick)imp).get(this));
		for(Brick u:units)
			if(!hasparent.contains(u))
				ret.add(u);
		return ret;
		}
	}

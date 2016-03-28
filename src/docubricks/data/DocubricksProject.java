package docubricks.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.ProcessingInstruction;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;



/**
 * 
 * A project bundle of all data
 * 
 * @author Johan Henriksson
 *
 */
public class DocubricksProject
	{
	public ArrayList<Brick> bricks=new ArrayList<Brick>();
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
			for(Brick p:bricks)
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
		Element eroot=new Element("docubricks");
		for(PhysicalPart p:physicalParts)
			{
			Element ep=p.toXML(basepath);
			eroot.addContent(ep);
			}
		for(Brick u:bricks)
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
			if(c.getName().equals("unit") || c.getName().equals("brick"))
				{
				Brick u=Brick.fromXML(basepath, p, c);
				p.bricks.add(u);
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
		FileInputStream is=new FileInputStream(f);
		DocubricksProject proj=loadXML(is, f.getParentFile());
		is.close();
		return proj;
		}

	public static DocubricksProject loadXML(InputStream is, File base) throws IOException
		{
		try
			{
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(is);
			DocubricksProject proj=fromXML(base, doc.getRootElement());
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
		for(Brick p:bricks)
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
		bricks.add(nu);
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
		for(Brick u:bricks)
			for(Function lp:u.functions)
				for(FunctionImplementation imp:lp.implementingPart)
					if(imp instanceof FunctionImplementationBrick)
						hasparent.add(((FunctionImplementationBrick)imp).get(this));
		for(Brick u:bricks)
			if(!hasparent.contains(u))
				ret.add(u);
		return ret;
		}


	public List<Brick> getBrickOrder()
		{
		/*
		Collections.sort(units, new Comparator<Brick>()
			{
			public int compare(Brick a, Brick b)
				{
				return Integer.compare(a.order, b.order);
				}
			});
		*/
		return bricks;
		}


	public void removeBrick(Brick unit)
		{
		bricks.remove(unit);
		for(Brick b:bricks)
			b.removeBrickRef(unit);
		}
	
	
	
	
	public JSONObject toJSON(File basepath) throws IOException
		{
		JSONObject eroot=new JSONObject();
		JSONArray arrpart=new JSONArray();
		JSONArray arrbricks=new JSONArray();
		JSONArray arrauthors=new JSONArray();
		eroot.put("parts", arrpart);
		eroot.put("bricks", arrbricks);
		eroot.put("authors", arrauthors);
		for(PhysicalPart p:physicalParts)
			{
			JSONObject ep=p.toJSON(basepath);
			arrpart.add(ep);
			}
		for(Brick u:bricks)
			{
			JSONObject eu=u.toJSON(basepath);
			arrbricks.add(eu);
			}
		for(Author a:authors)
			{
			JSONObject eu=a.toJSON(basepath);
			arrauthors.add(eu);
			}
		return eroot;
		}
	}

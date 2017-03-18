package docubricks.data;

import java.io.File;
import java.io.IOException;

import org.jdom2.Element;

import net.minidev.json.JSONObject;

/**
 * 
 * Information about one author
 * 
 * @author Johan Henriksson
 *
 */
public class Author
	{
	public String id;
	
	public String name="";
	public String email="";
	public String orcid="";
	public String affiliation="";
	
	
	public Element toXML(File basepath) throws IOException
		{
		Element eroot=new Element("author");
	
		eroot.setAttribute("id", id);
		eroot.addContent(elWithContent("name", name));
		
		eroot.addContent(elWithContent("email", email));
		eroot.addContent(elWithContent("orcid", orcid));
		eroot.addContent(elWithContent("affiliation", affiliation));
	
		return eroot;
		}


	private static Element elWithContent(String el, String content)
		{
		Element e=new Element(el);
		e.addContent(content);
		return e;
		}
	
	
	public static Author fromXML(File basepath, Element c)
		{
		Author part=new Author();
		part.id=c.getAttributeValue("id");
		
		part.name=c.getChildText("name");
		part.email=c.getChildText("email");
		part.orcid=c.getChildText("orcid");
		part.affiliation=c.getChildText("affiliation");
	
		return part;
		}


	public JSONObject toJSON(File basepath)
		{
		JSONObject eroot=new JSONObject();
		eroot.put("id", id);
		eroot.put("name", name);
		
		eroot.put("email", email);
		eroot.put("orcid", orcid);
		eroot.put("affiliation", affiliation);
		return eroot;
		}
	
	
	
	}

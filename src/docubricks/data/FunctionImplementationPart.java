package docubricks.data;

import org.jdom2.Element;

import net.minidev.json.JSONObject;

/**
 * 
 * Implementation for a logical part: physical part
 * 
 * @author Johan Henriksson
 *
 */
public class FunctionImplementationPart extends FunctionImplementation
	{
	Part part;
	
	public FunctionImplementationPart(Part p, int quantity)
		{
		part=p;
		this.quantity=quantity;
		}
	
	/**
	 * Serialize to XML
	 */
	public Element toXML()
		{
		Element e=new Element("implementation");
		e.setAttribute("type", "part");
		e.setAttribute("quantity", ""+quantity);
		e.setAttribute("id", part.id);
		return e;
		}
	
	public JSONObject toJSON()
		{
		JSONObject e=new JSONObject();
		e.put("type", "part");
		e.put("quantity", quantity);
		e.put("id", part.id);
		return e;
		}

	
	@Override
	public boolean equals(Object obj)
		{
		if(obj instanceof FunctionImplementationPart)
			{
			FunctionImplementationPart o=(FunctionImplementationPart)obj;
			return o.part==part;
			}
		else
			return false;
		}

	public String getRepresentativeName(DocubricksProject project)
		{
		return "Part: "+part.name;
		}
	}
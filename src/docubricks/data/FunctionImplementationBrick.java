package docubricks.data;

import org.jdom2.Element;

import net.minidev.json.JSONObject;

/**
 * 
 * Implementation for a logical part: unit
 * 
 * @author Johan Henriksson
 *
 */
public class FunctionImplementationBrick extends FunctionImplementation
	{
	
	String id;
	public FunctionImplementationBrick(String id, int quantity)
		{
		this.id=id;
		this.quantity=quantity;
		}
	
	/**
	 * Serialize to XML
	 */
	public Element toXML()
		{
		Element e=new Element("implementation");
		e.setAttribute("type", "brick");
		e.setAttribute("quantity", ""+quantity);
		e.setAttribute("id", id);
		return e;
		}
	

	public JSONObject toJSON()
		{
		JSONObject e=new JSONObject();
		e.put("type", "brick");
		e.put("quantity", quantity);
		e.put("id", id);
		return e;
		}

	
	@Override
	public boolean equals(Object obj)
		{
		if(obj instanceof FunctionImplementationBrick)
			{
			FunctionImplementationBrick o=(FunctionImplementationBrick)obj;
			return o.id.equals(id);//==unit;
			}
		else
			return false;
		}

	public Brick get(DocubricksProject project)
		{
		return project.getUnit(id);
		}

	public String getRepresentativeName(DocubricksProject project)
		{
		return "Brick: "+get(project).getName();
		}
	}
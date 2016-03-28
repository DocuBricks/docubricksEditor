package docubricks.data;

import org.jdom2.Element;

import net.minidev.json.JSONObject;

/**
 * 
 * 
 * @author Johan Henriksson
 *
 */
public class AssemblyStepComponent
	{
	public Function function;
	public int quantity=1;
	
	public Element toXML()
		{
		Element e=new Element("component");
		e.setAttribute("quantity",""+quantity);
		e.setAttribute("id",function.id);		
		return e;
		}

	public static AssemblyStepComponent fromXML(Brick brick, Element root)
		{
		AssemblyStepComponent comp=new AssemblyStepComponent();
		comp.quantity=Integer.parseInt(root.getAttributeValue("quantity"));
		comp.function=brick.getFunction(root.getAttributeValue("id"));
		return comp;
		}

	public Object toJSON()
		{
		JSONObject e=new JSONObject();
		e.put("quantity",""+quantity);
		e.put("id",function.id);		
		return e;
		}
	}

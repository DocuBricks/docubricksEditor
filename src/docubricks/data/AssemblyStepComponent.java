package docubricks.data;

import org.jdom2.Element;

/**
 * 
 * 
 * @author Johan Henriksson
 *
 */
public class AssemblyStepComponent
	{
	public Function function;
	public int count=1;
	
	public Element toXML()
		{
		Element e=new Element("component");
		e.setAttribute("count",""+count);
		e.setAttribute("id",function.id);		
		return e;
		}

	public static AssemblyStepComponent fromXML(Brick brick, Element root)
		{
		AssemblyStepComponent comp=new AssemblyStepComponent();
		comp.count=Integer.parseInt(root.getAttributeValue("count"));
		comp.function=brick.getFunction(root.getAttributeValue("id"));
		return comp;
		}
	}

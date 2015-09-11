package docubricks.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Element;

/**
 * 
 * An instruction (list of steps to do something)
 * 
 * @author Johan Henriksson
 *
 */
public class AssemblyInstruction
	{
	public ArrayList<AssemblyStep> steps=new ArrayList<AssemblyStep>();

	
	public Element toXML(File basepath) throws IOException
		{
		Element eroot=new Element("assembly_instruction");

		for(AssemblyStep s:steps)
			eroot.addContent(s.toXML(basepath));
			
		return eroot;
		}


	
	public static AssemblyInstruction fromXML(Brick brick, File basepath, Element root)
		{
		AssemblyInstruction inst=new AssemblyInstruction();
		for(Element child:root.getChildren())
			{
			AssemblyStep step=AssemblyStep.fromXML(brick, basepath, child);
			inst.steps.add(step);
			}
		
		return inst;
		}
	}

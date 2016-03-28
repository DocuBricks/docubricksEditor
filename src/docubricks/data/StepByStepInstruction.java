package docubricks.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Element;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * 
 * An instruction (list of steps to do something)
 * 
 * @author Johan Henriksson
 *
 */
public class StepByStepInstruction
	{
	public ArrayList<AssemblyStep> steps=new ArrayList<AssemblyStep>();
	public String name="";

	
	public Element toXML(File basepath) throws IOException
		{
		Element eroot=new Element("assembly_instruction");

		for(AssemblyStep s:steps)
			eroot.addContent(s.toXML(basepath));
		//eroot.setAttribute("name", name);

		return eroot;
		}


	
	public static StepByStepInstruction fromXML(Brick brick, File basepath, Element root)
		{
		StepByStepInstruction inst=new StepByStepInstruction();
		if(root==null)
			return inst;
		for(Element child:root.getChildren())
			{
			AssemblyStep step=AssemblyStep.fromXML(brick, basepath, child);
			inst.steps.add(step);
			}
		return inst;
		}



	public JSONObject toJSON(File basepath) throws IOException
		{
		JSONObject eroot=new JSONObject();
		
		JSONArray asteps=new JSONArray();
		eroot.put("steps",asteps);
		for(AssemblyStep s:steps)
			asteps.add(s.toJSON(basepath));
		//eroot.setAttribute("name", name);

		return eroot;
		}



	}

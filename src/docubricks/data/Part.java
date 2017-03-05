package docubricks.data;

import java.io.File;
import java.io.IOException;

import org.jdom2.Element;

import net.minidev.json.JSONObject;

/**
 * 
 * A part
 * 
 * @author Johan Henriksson
 *
 */
public class Part
	{
	public String name="";
	public String description = "";
	public String supplier="";
	public String supplierPartNum="";
	public String manufacturerPartNum="";
	public String url;
	public String id;
	
	public Double materialAmount;
	public MaterialUnit materialUnit=MaterialUnit.NONE;


	public MediaSet media=new MediaSet();
	public StepByStepInstruction instructions=new StepByStepInstruction();
	public String manufacturingMethod="";

	
	//should link to a digital part as well. yet unclear
	
	public void setDescription(String s)
		{
		name=s;
		}
	public String getDescription()
		{
		return name;
		}
	
	
	
	
	public void setSupplier(String s)
		{
		supplier=s;
		}
	public String getSupplier()
		{
		return supplier;
		}
	
	
	public void setSupplierPartNum(String s)
		{
		supplierPartNum=s;
		}
	public String getSupplierPartNum()
		{
		return supplierPartNum;
		}
	
	
	
	public void setManufacturerPartNum(String s)
		{
		manufacturerPartNum=s;
		}
	public String getManufacturerPartNum()
		{
		return manufacturerPartNum;
		}
	
	
	
	public void setURL(String s)
		{
		url=s;
		}
	public String getURL()
		{
		return url;
		}
	
	
	public Element toXML(File basepath) throws IOException
		{
//		Element eroot=new Element("physical_part");
		Element eroot=new Element("part");

		eroot.setAttribute("id", id);
		eroot.addContent(elWithContent("name", name));
		eroot.addContent(elWithContent("description", description));
		
		eroot.addContent(elWithContent("supplier", supplier));
		eroot.addContent(elWithContent("supplier_part_num", supplierPartNum));
		eroot.addContent(elWithContent("manufacturer_part_num", manufacturerPartNum));
		eroot.addContent(elWithContent("url", url));

		eroot.addContent(elWithContent("material_amount", materialAmount!=null ? ""+materialAmount : ""));
		eroot.addContent(elWithContent("material_unit", materialUnit.name()));

		eroot.addContent(media.toXML(basepath));
		
		Element elIns=instructions.toXML(basepath);
		elIns.setName("manufacturing_instruction");
		eroot.addContent(elIns);
		
		return eroot;
		}
	
	
	private static Element elWithContent(String el, String content)
		{
		Element e=new Element(el);
		e.addContent(content);
		return e;
		}
	
	
	public static Part fromXML(File basepath, Element c)
		{
		Part part=new Part();
		part.id=c.getAttributeValue("id");
		
		part.name=c.getChildText("name");
		part.description=c.getChildText("description");
		if(part.name==null)
			part.name=part.description; //hack
		part.supplier=c.getChildText("supplier");
		part.supplierPartNum=c.getChildText("supplier_part_num");
		part.manufacturerPartNum=c.getChildText("manufacturer_part_num");
		part.url=c.getChildText("url");

		String ma=c.getChildText("material_amount");
		part.materialAmount=ma.equals("") ? null : Double.parseDouble(ma);
		part.materialUnit=MaterialUnit.valueOf(c.getChildText("material_unit"));

		part.media=MediaSet.fromXML(basepath, c.getChild("media"));
		
		part.instructions=StepByStepInstruction.fromXML(null, basepath, c.getChild("manufacturing_instruction"));

		return part;
		}
	
	
	public JSONObject toJSON(File basepath) throws IOException
		{
		JSONObject eroot=new JSONObject();

		//eroot.put("id", id);
		eroot.put("name", name);
		eroot.put("description", description);
		
		eroot.put("supplier", supplier);
		eroot.put("supplier_part_num", supplierPartNum);
		eroot.put("manufacturer_part_num", manufacturerPartNum);
		eroot.put("url", url);

		eroot.put("material_amount", materialAmount!=null ? ""+materialAmount : "");
		eroot.put("material_unit", materialUnit.name());

		eroot.put("files",media.toJSON(basepath));
		
		JSONObject elIns=instructions.toJSON(basepath);
		eroot.put("manufacturing_instruction", elIns);

		return eroot;
		}
	
	

	}

package docubricks.data;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.jdom2.Element;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * 
 * Set of media files
 * 
 * @author Johan Henriksson
 *
 */
public class MediaSet
	{
	public LinkedList<MediaFile> files=new LinkedList<MediaFile>();
	
	
	public Element toXML(File basepath) throws IOException
		{
		Element root=new Element("media");
		for(MediaFile mf:files)
			root.addContent(mf.toXML(basepath));
		return root;
		}
	
	public static MediaSet fromXML(File basepath, Element root)
		{
		MediaSet media=new MediaSet();
		for(Element e:root.getChildren())
			media.files.add(MediaFile.fromXML(basepath, e));
		return media;
		}

	public JSONArray toJSON(File basepath) throws IOException
		{
		JSONArray root=new JSONArray();
				
		for(MediaFile mf:files)
			root.add(mf.toJSON(basepath));
		return root;
		}
	}

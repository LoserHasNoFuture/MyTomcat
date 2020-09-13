package myTomcat.http;


import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class HttpContext{
	private static HashMap<String,String> mimeMapping = new HashMap<String,String>();
	private final static int CR = 13;
	private final static int LF = 10;


	static {
		initMimeMapping();
	}

	private static void initMimeMapping() {
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(new File("./conf/web.xml"));
			Element root = doc.getRootElement();
			List<Element> list = root.elements("mime-mapping");
			for(Element mime_mapping: list) {
				String key = mime_mapping.elementText("extension");
				String value = mime_mapping.elementText("mime-type");
				mimeMapping.put(key, value);
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public static String getContentType(String ext) {
		return mimeMapping.get(ext);
	}

	public static int getCR(){
		return CR;
	}

	public static int getLF(){
		return LF;
	}
}
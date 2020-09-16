package myTomcat.core;

import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Method;

import org.dom4j.Attribute;
import java.io.File;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import myTomcat.servlet.HttpServlet;



public class ServletContext {
	private static HashMap<String,HttpServlet> servlets = new HashMap<String,HttpServlet>();
	
	static {
		initServlets();
	}
	
	private static void initServlets() {
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(new File("./conf/servlets.xml"));
			Element root = doc.getRootElement();
			List<Element> list = root.elements("servlet");
			for(Element servlet: list) {
				String key = servlet.attributeValue("path");
				String value = servlet.attributeValue("className");
				Class cls = Class.forName(value);
				servlets.put(key, (HttpServlet)cls.newInstance());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HttpServlet getServlet(String path) {
		return servlets.getOrDefault(path,null);
	}
	
}

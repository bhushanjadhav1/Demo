package com.dbyl.tests.training.day5;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.dbyl.tests.training.day5.Locator.ByType;



public class XMLUtils {

	/**
	 * @author Young
	 * @param path
	 * @param pageName
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, Locator> readXMLDocument(String path,
			String pageName) throws Exception {

		HashMap<String, Locator> locatorMap = new HashMap<String, Locator>();
		locatorMap.clear();
		File file = new File(path);
		if (!file.exists()) {
			return locatorMap=null;
			//throw new IOException("Can't find " + path);
		}
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		Element root = document.getRootElement();
		for (Iterator<?> i = root.elementIterator(); i.hasNext();) {
			Element page = (Element) i.next();
			if (page.attribute(0).getValue().equalsIgnoreCase(pageName)) {
				for (Iterator<?> l = page.elementIterator(); l.hasNext();) {
					String type = null;
					String timeOut = "3";
					String value = null;
					String locatorName = null;
					Element locator = (Element) l.next();
					for (Iterator<?> j = locator.attributeIterator(); j
							.hasNext();) {
						Attribute attribute = (Attribute) j.next();
						if (attribute.getName().equals("type")) {
							type = attribute.getValue();
						} else if (attribute.getName().equals("timeOut")) {
							timeOut = attribute.getValue();
						} else {
							value = attribute.getValue();
						}

					}
					Locator temp = new Locator(value,
							Integer.parseInt(timeOut), getByType(type));
					locatorName = locator.getText();
					locatorMap.put(locatorName, temp);
				}
				continue;
			}

		}
		return locatorMap;

	}

	/**
	 * @param type
	 */
	public static com.dbyl.tests.training.day5.Locator.ByType getByType(String type) {
		ByType byType = ByType.xpath;
		if (type == null || type.equalsIgnoreCase("xpath")) {
			byType = ByType.xpath;
		} else if (type.equalsIgnoreCase("id")) {
			byType = ByType.id;
		} else if (type.equalsIgnoreCase("linkText")) {
			byType = ByType.linkText;
		} else if (type.equalsIgnoreCase("name")) {
			byType = ByType.name;
		} else if (type.equalsIgnoreCase("className")) {
			byType = ByType.className;
		} else if (type.equalsIgnoreCase("cssSelector")) {
			byType = ByType.cssSelector;
		} else if (type.equalsIgnoreCase("partialLinkText")) {
			byType = ByType.partialLinkText;
		} else if (type.equalsIgnoreCase("tagName")) {
			byType = ByType.tagName;
		}
		return byType;
	}

	/**
	 * @author Young
	 * @throws IOException
	 */
	public static void writeXMLDocument() throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileWriter("output.xml"), format);
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("map");
		root.addComment("locator of page map info");
		Element page = root.addElement("page").addAttribute("pageName",
				"com.dbyl.libarary.pageAction.HomePage");
		page.addComment("Locator lists");
		page.addElement("locator").addAttribute("type", "ByName")
				.addAttribute("timeOut", "3")
				.addAttribute("value", "\\\\div[@name]").addText("loginButton");
		page.addElement("locator").addAttribute("type", "ById")
				.addAttribute("timeOut", "3")
				.addAttribute("value", "\\\\div[@id]").addText("InputButton");
		writer.write(document);
		writer.close();
	}

}

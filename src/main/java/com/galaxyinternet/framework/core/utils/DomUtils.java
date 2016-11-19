package com.galaxyinternet.framework.core.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

public class DomUtils
{
	public static Document getDocument(String xml)
	{
		try
		{
			return DocumentHelper.parseText(xml);
		}
		catch (DocumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String getNodeValue(Document doc, String path)
	{
		try
		{
			Node node = doc.selectSingleNode(path);
			if(node != null)
			{
				return node.getText();
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String getNodeValue(String xml, String path)
	{
		try
		{
			Document doc = DocumentHelper.parseText(xml);
			Node node = doc.selectSingleNode(path);
			if(node != null)
			{
				return node.getText();
			}
		}
		catch (DocumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

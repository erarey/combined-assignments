package com.cooksys.ftd.assignments.concurrency.model.config;

import java.util.ArrayList;
import java.util.List;

public class XMLParseTools {
	
	
	//public enum flags
	//{
		//OMIT_ERRORS, STOP_ON_AND_OMIT_ERROR, RETURN_EMPTY_ON_ERROR 
	//}

	/**
	 * The function attempts to separate multiple XML files contained in a single string by
	 * searching for the <?xml > tags. It will search within the element that follows any <?xml ?>
	 * under the assumption that that element is a root element. If it fails to find a closing tag
	 * for what it assumed was a root element it will return a list of strings representing xml files 
	 * it found up until that point, or an empty list if none were found.
	 * @param xmlString, a string containing one or more XML files
	 * @return array of XML files
	 */
	public static List<String> splitXMLFiles(String xmlString) //XMLParseTools.flags flag)
	{
		String temp = new String(xmlString);
		
		ArrayList<String> array = new ArrayList<String>();
		
		while(temp.contains("<?xml"))
		{
			int xmlStartTag = temp.indexOf("<?xml");
			if (xmlStartTag == -1) break;
			
			int xmlEndTag = temp.indexOf("?>", xmlStartTag+5);
			if (xmlEndTag == -1) break;
			
			int rootStartTag = temp.indexOf("<", xmlEndTag+2);
			if (rootStartTag == -1) break;
			
			int rootStartTagClose = temp.indexOf(">", rootStartTag+1);
			if (rootStartTagClose == -1) break;
			
			String rootTag = temp.substring(rootStartTag+1, rootStartTagClose); // should exclude < and >
			int rootEndTagClose = temp.indexOf("/" + rootTag) + rootTag.length();
			if (rootEndTagClose == (rootTag.length())) break; //roottag + / + -1 if indexOf returned -1
			
			array.add(temp.substring(rootStartTag, rootEndTagClose+1));
			System.out.println("Parsed and found " + temp.substring(rootStartTag, rootEndTagClose+1));
			
			temp = temp.substring(rootEndTagClose+1);
		}
		
		return new ArrayList<String>(array);
		
	}
}

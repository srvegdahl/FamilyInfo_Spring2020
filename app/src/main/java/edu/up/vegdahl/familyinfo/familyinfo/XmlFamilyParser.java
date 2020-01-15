package edu.up.vegdahl.familyinfo.familyinfo;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * XmlFamilyParser -- parses family information defined in an XML file
 *
 * @author Steven R. Vegdahl
 * @version 25 August 2017
 */

public class XmlFamilyParser {

	/**
	 * creates an ArrayList of Person object, parsing XML from an InputStream
	 *
	 * @param is the input stream
	 * @return the array-list of family object
	 * @throws XmlFamilyParseException whenever it is determined that the format of the XML is bad
	 */
	public static ArrayList<String[]> parseFamilyInfoList(InputStream is)
			throws XmlFamilyParseException {

		// create the XML parser
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(is);
		} catch (ParserConfigurationException e) {
			throw new XmlFamilyParseException("Problem parsing XML");
		} catch (SAXException e) {
			throw new XmlFamilyParseException("SAX problem");
		} catch (IOException e) {
			throw new XmlFamilyParseException("IO exception");
		}

		// example of how to write out the document
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			String root = Environment.getExternalStorageDirectory().toString();
			File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			FileWriter writer = new FileWriter(new File(dir, "test.xml"));
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
		}
		catch (TransformerConfigurationException tcx) {
		}
		catch (TransformerException tx) {
		}
		catch (IOException iox) {
		}

		// get the top document element; normalize it
		Node topNode = doc.getDocumentElement();
		topNode.normalize();

		// remove superfluous whitespace in the text
		removeEmptyText(topNode);

		// ensure that the top element is <FamilyInfo>
		if (!checkElementNode(topNode, "FamilyInfo")) {
			throw new XmlFamilyParseException("Top level XML node was not 'FamilyInfo'");
		}

		// create the ArrayList that we will return
		ArrayList<String[]> infoList = new ArrayList<String[]>();

		// list of names, so that we can check for duplicates
		HashSet<String> definedNames = new HashSet<String>();

		// run through the second-level nodes, all of which should be of type <Man>, <Woman>,
		// or <Relation>
		for (Node elNode = topNode.getFirstChild(); elNode != null; elNode = elNode.getNextSibling()) {

			if (checkElementNode(elNode, "Man")) {
				String name = getSingletonText(elNode);
				registerNewName(name,definedNames);
				infoList.add(new String[]{name, null});
				Log.i("Man",name);
			}
			else if (checkElementNode(elNode, "Woman")) {
				String name = getSingletonText(elNode);
				registerNewName(name,definedNames);
				infoList.add(new String[]{null, name});
				Log.i("Woman",name);
			}
			else if (checkElementNode(elNode, "Relation")) {
				// check that we have exactly two children, presumably a <Parent> and a <Child> node
				Node node1 = elNode.getFirstChild();
				Node node2 = null;
				if (node1 != null) node2 = node1.getNextSibling();
				Node node3 = null;
				if (node2 != null) node3 = node2.getNextSibling();

				// throw exception of relation does not have correct components
				if (node2 == null || node3 != null || !checkElementNode(node1,"Parent") ||
						!checkElementNode(node2,"Child")) {
					throw new XmlFamilyParseException("Relation not <Parent> then <Child>");
				}

				// get parent and child names
				String parentName = getSingletonText(node1);
				String childName = getSingletonText(node2);
				infoList.add(new String[]{parentName, childName});
				Log.i("Relation",parentName+" => "+childName);

			}
			else {
				Log.i("bad tag",""+elNode.getNodeName());
				throw new XmlFamilyParseException("Illegal node type: "+elNode.getNodeName());
			}

		}
		// return our list 
		return infoList;
	}

	/**
	 * registers a name into the hash-set
	 *
	 * @param name
	 * 		the name to register
	 * @param hSet
	 * 		the hash-set into which to register the name
	 * @throws XmlFamilyParseException
	 * 		if the element has already been registered
	 */
	private static void registerNewName(String name, HashSet<String> hSet)
			throws XmlFamilyParseException {
		if (hSet.contains(name)) {
			throw new XmlFamilyParseException("duplicate person name: "+name);
		}
		else {
			hSet.add(name);
		}
	}

	/**
	 * removes empty (whitespace) text from our XML-list
	 * @param n
	 */
	private static void removeEmptyText(Node n) {

		// start with our first child
		Node sub = n.getFirstChild();

		// keep going as long as we still have more nodes:
		// - if empty text, remove
		// - otherwise, recurse
		while (sub != null) {

			// save next sibling in case current one is removed
			Node next = sub.getNextSibling();

			// if empty text, remove; otherwise, recurse
			if (isEmptyText(sub)) {
				n.removeChild(sub);
			}
			else {
				removeEmptyText(sub);
			}

			// move to next node in list
			sub = next;
		}
	}

	/**
	 * tells where a node is text consisting only of whitespace
	 *
	 * @param n
	 * 		the node
	 * @return
	 * 		whether the node is a text node with only whitespace
	 */
	private static boolean isEmptyText(Node n) {
		boolean b1 = n.getNodeType() == Node.TEXT_NODE;
		boolean b2 = n.getTextContent().trim().length() == 0;
		return b1 && b2;
	}

	/**
	 * checks whether we have an element-node with given name
	 *
	 * @param n
	 * 		the node
	 * @param name
	 * 		the name
	 * @return
	 * 		whether we have a match
	 */
	private static boolean checkElementNode(Node n, String name) {
		boolean b1 = n.getNodeType() == Node.ELEMENT_NODE;
		boolean b2 = n.getNodeName().equals(name);
		return  b1 && b2;
	}

	/**
	 * fetches the text from a node; checks that the only data in the node is
	 * plain text
	 *
	 * @param node
	 * 		the node
	 * @return
	 * 		the text
	 * @throws XmlFamilyParseException
	 * 		whenever anything but plain text is found
	 */
	private static String getSingletonText(Node node) throws XmlFamilyParseException {
		// check that we have exactly one child, a text node
		Node sub = node.getFirstChild();
		if (sub == null) {
			// no children
			throw new XmlFamilyParseException("Node type <" + node.getNodeName() +
					"> with empty data");
		}
		else if (sub.getNextSibling() != null || sub.getNodeType() != Node.TEXT_NODE) {
			// more than one child, or non-text child
			throw new XmlFamilyParseException("Node type <" + node.getNodeName() +
					"> with data more complex than simple text");
		}
		else {
			// return the text, trimming leading/trailing whitespace
			return sub.getTextContent().trim();
		}
	}

}

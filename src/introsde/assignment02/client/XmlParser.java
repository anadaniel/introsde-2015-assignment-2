package introsde.assignment02.client;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;


public class XmlParser {  
  Document doc;
  XPath xpath;
  String xmlString;

  public XmlParser(String xmlString) throws ParserConfigurationException, SAXException, IOException {
    this.xmlString = xmlString;
    loadXML();
  }
  
  public void loadXML() throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    domFactory.setNamespaceAware(true);
    DocumentBuilder builder = domFactory.newDocumentBuilder();
    InputSource xmlStringAsSource = new InputSource(new StringReader(xmlString));
    doc = builder.parse(xmlStringAsSource);

    //creating xpath object
    getXPathObj();
  }
  
  public void getXPathObj() {
    XPathFactory factory = XPathFactory.newInstance();
    xpath = factory.newXPath();
  }

  /**
  * Print a list of all people in the people.xml file
  */
  public int countPeople() throws XPathExpressionException {
    XPathExpression expr = xpath.compile("//person");
    NodeList personNodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

    int peopleCount = (int) personNodes.getLength();
    return peopleCount;
  }

  /**
  * Return the id of the first person in the people list
  * NOTE: The people list xml string should have been loaded before performing this method
  */
  public int getFirstPersonId() throws XPathExpressionException {
    XPathExpression expr = xpath.compile("//person[1]");
    Node firstPerson = (Node) expr.evaluate(doc, XPathConstants.NODE);

    Element personElement = (Element) firstPerson;

    return Integer.parseInt( personElement.getElementsByTagName("personId").item(0).getTextContent() );
  }
}
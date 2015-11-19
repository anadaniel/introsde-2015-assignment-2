package introsde.assignment02.client;

import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.io.*;
import java.lang.Exception;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.parsers.ParserConfigurationException;

public class ClientApp {

  private static Client client;
  private static WebTarget service;
  private static String serverUri;
  private static String reqPath;
  private static XmlParser xmlParser;
  private static Response response;
  private static String responseBody;
  private static int responseStatus;
  private static String requestResult;
  private static int firstPersonId;

  public static void main(String[] args) throws Exception {
    System.out.println(">>>>> Server URL: http://127.0.1.1:3000");


    /*
    *****************************************
    **************** CONFIG *****************
    *****************************************
    */
    serverUri = "http://127.0.1.1:3000";
    client = ClientBuilder.newClient(new ClientConfig());
    service = client.target( getBaseURI() );

    /*
    *****************************************
    * Performs Request #1 - Print people and count if there are more than 3 persons in the db
    *****************************************
    */
    reqPath = "/persons";

    // Perform XML Request
    response = performGetRequest(reqPath, "application/xml");
    responseBody = response.readEntity(String.class);
    responseStatus = response.getStatus();

    // Parse response body - People List
    xmlParser = new XmlParser(responseBody);
    int peopleCount = xmlParser.countPeople();

    if ( peopleCount >= 3 )
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print XML Request
    printRequestDetails(1, "GET", reqPath, "application/xml", "");

    // Perform JSON Request
    response = performGetRequest(reqPath, "application/json");
    responseBody = response.readEntity(String.class);

    // Print JSON Request
    printRequestDetails(1, "GET", reqPath, "application/json", "");

    /*
    *****************************************
    * Performs Request #2 - Print first person
    *****************************************
    */
    firstPersonId = xmlParser.getFirstPersonId();
    String reqPath = "/persons/" + firstPersonId;

    // Perform XML Request
    response = performGetRequest(reqPath, "application/xml");
    responseBody = response.readEntity(String.class);
    responseStatus = response.getStatus();

    // Parse response body - Single Person
    xmlParser = new XmlParser(responseBody);
    String oldname = xmlParser.getPersonFirstname();

    if ( responseStatus == 200 )
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print XML Request
    printRequestDetails(2, "GET", reqPath, "application/xml", "");

    // Perform JSON Request
    response = performGetRequest(reqPath, "application/json");
    responseBody = response.readEntity(String.class);
    responseStatus = response.getStatus();

    // Print JSON Request
    printRequestDetails(2, "GET", reqPath, "application/json", "");

    /*
    *****************************************
    * Performs Request #3 - Edit first person
    *****************************************
    */
    reqPath = "/persons/" + firstPersonId;
    

    // Perform XML Request
    response = performPostPutRequest(reqPath, "application/xml", "PUT", "application/xml", "<person><firstname>Anidew</firstname></person>");
    responseBody = response.readEntity(String.class);
    responseStatus = response.getStatus();

    // Parse response body - Single Person
    xmlParser = new XmlParser(responseBody);
    String newname = xmlParser.getPersonFirstname();

    if ( oldname != newname )
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print XML Request
    printRequestDetails(3, "PUT", reqPath, "application/xml", "application/xml");
  }

  private static Response performPostPutRequest(String path, String accept, String method, String contentType, String requestBody){
    return service.path(path).request().accept(accept).build(method, Entity.entity(requestBody, contentType)).invoke();
  }

  private static Response performGetRequest(String path, String accept){
    return service.path(path).request().accept(accept).get();
  }

  private static void printRequestDetails(int n, String method, String path, String accept, String contentType) throws TransformerException {
    System.out.println("Request #" + n + ": " + method + " " + path + " Accept:" + accept + " Content-type: " + contentType ); 
    System.out.println("=> Result: " + requestResult);
    System.out.println("=> HTTP Status: " + responseStatus);

    if(accept == "application/xml")
      prettyPrintXml(responseBody);
    else
      System.out.println(responseBody);

    System.out.println("");
  }

  private static void prettyPrintXml(String input) throws TransformerException {
    Source inputXml = new StreamSource(new StringReader(input));
    StreamResult outputXml = new StreamResult(new StringWriter());

    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    transformer.transform(inputXml, outputXml);

    System.out.println(outputXml.getWriter().toString());
  }

  private static URI getBaseURI() {
    return UriBuilder.fromUri(serverUri).build();
  }
}
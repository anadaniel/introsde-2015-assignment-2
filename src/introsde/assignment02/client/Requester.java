package introsde.assignment02.client;

import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
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

public class Requester {

  private Client client;
  private WebTarget service;
  private String serverUri;
  private XmlParser xmlParser;
  private Response response;
  private String responseBody;
  private int responseStatus;
  private String requestResult;
  private int firstPersonId;

  public Requester(String serverUri){
    this.serverUri = serverUri;
    client = ClientBuilder.newClient(new ClientConfig());
    service = client.target( getBaseURI() );
  }


  /**
  * Performs Request #1 - Print people and count if there are more than 3 persons in the db
  */
  public void getAllPeople() throws Exception {
    String reqPath = "/persons";
    response = performGetRequest(reqPath, "application/xml");
    responseBody = response.readEntity(String.class);
    responseStatus = response.getStatus();

    xmlParser = new XmlParser(responseBody);
    int peopleCount = xmlParser.countPeople();

    if ( peopleCount >= 3 )
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print XmlRequest
    printRequestDetails(1, "GET", reqPath, "application/xml", "");

    response = performGetRequest(reqPath, "application/json");
    responseBody = response.readEntity(String.class);

    //Print JsonRequest
    printRequestDetails(1, "GET", reqPath, "application/json", "");
  }

  public void printFirstPerson() throws Exception {
    firstPersonId = xmlParser.getFirstPersonId();
    String reqPath = "/persons/" + firstPersonId;

    response = performGetRequest(reqPath, "application/xml");
    responseBody = response.readEntity(String.class);
    responseStatus = response.getStatus();

    if ( responseStatus == 200 )
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print XmlRequest
    printRequestDetails(2, "GET", reqPath, "application/xml", "");
  }

  private Response performGetRequest(String path, String accept){
    return service.path(path).request().accept(accept).get();
  }

  private void printRequestDetails(int n, String method, String path, String accept, String contentType) throws TransformerException {
    System.out.println("Request #" + n + ": " + method + " " + path + " Accept:" + accept + " Content-type: " + contentType ); 
    System.out.println("=> Result: " + requestResult);
    System.out.println("=> HTTP Status: " + responseStatus);

    if(accept == "application/xml")
      prettyPrintXml(responseBody);
    else
      System.out.println(responseBody);

    System.out.println("");
  }

  private void prettyPrintXml(String input) throws TransformerException {
    Source inputXml = new StreamSource(new StringReader(input));
    StreamResult outputXml = new StreamResult(new StringWriter());

    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    transformer.transform(inputXml, outputXml);

    System.out.println(outputXml.getWriter().toString());
  }

  private URI getBaseURI() {
    return UriBuilder.fromUri(serverUri).build();
  }
}
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

  public static void main(String[] args) throws Exception {
    System.out.println(">>>>> Server URL: http://127.0.1.1:3000");
    /*
    **********************************************************************************
    ************************************ CONFIG **************************************
    **********************************************************************************
    */
    serverUri = "http://127.0.1.1:3000";
    client = ClientBuilder.newClient(new ClientConfig());
    service = client.target( getBaseURI() );

    /*
    **********************************************************************************
    * Performs Request #1 - Get all Persons and count if there are more than 3 persons in the db
    **********************************************************************************
    */
    reqPath = "/persons";

    // Perform XML Request
    performGetRequest(reqPath, "application/xml");

    // Parse response body - People List
    xmlParser = new XmlParser(responseBody);
    int peopleCount = xmlParser.countNodes("person");

    //Save first and last persons ids
    int firstPersonId = xmlParser.getPersonId(1);
    int lastPersonId = xmlParser.getPersonId(peopleCount);

    if ( peopleCount >= 3 )
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print XML Request
    printRequestDetails(1, "GET", reqPath, "application/xml", "");

    // Perform JSON Request
    performGetRequest(reqPath, "application/json");
    
    // Print JSON Request
    printRequestDetails(1, "GET", reqPath, "application/json", "");

    /*
    **********************************************************************************
    * Performs Request #2 - Print first person
    **********************************************************************************
    */
    String reqPath = "/persons/" + firstPersonId;

    // Perform XML Request
    performGetRequest(reqPath, "application/xml");

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
    performGetRequest(reqPath, "application/json");

    // Print JSON Request
    printRequestDetails(2, "GET", reqPath, "application/json", "");

    /*
    **********************************************************************************
    * Performs Request #3 - Edit first person
    **********************************************************************************
    */
    reqPath = "/persons/" + firstPersonId;
    

    // Perform XML Request
    performPostPutRequest(
                reqPath, 
                "application/xml",
                "PUT", 
                "application/xml", 
                "<person><firstname>Ana XML</firstname></person>"
    );

    // Parse response body - Single Person
    xmlParser = new XmlParser(responseBody);
    String newname = xmlParser.getPersonFirstname();

    if ( oldname != newname )
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print XML Request
    printRequestDetails(3, "PUT", reqPath, "application/xml", "application/xml");

    // Perform JSON Request
    performPostPutRequest(reqPath, "application/json", "PUT", "application/json", "{\"firstname\":\"Ana JSON\"}");
    
    // Print JSON Request
    printRequestDetails(3, "PUT", reqPath, "application/json", "application/json");

    /*
    **********************************************************************************
    * Performs Request #4 - Create a person XML
    **********************************************************************************
    */
    reqPath = "/persons";

    // Perform XML Request
    performPostPutRequest(
                reqPath, 
                "application/xml",
                "POST", 
                "application/xml", 
                "<person>" +
                  "<firstname>Chuck XML</firstname>" +
                  "<lastname>Norris</lastname>" +
                  "<birthdate>1945-01-01</birthdate>" +
                "</person>"
    );

    if ( responseStatus == 201 )
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print XML Request
    printRequestDetails(4, "POST", reqPath, "application/xml", "application/xml");    

    /*
    **********************************************************************************
    * Performs Request #4 - Create a person JSON
    **********************************************************************************
    */
    reqPath = "/persons";

    // Perform JSON Request
    performPostPutRequest(
                reqPath, 
                "application/json",
                "POST", 
                "application/json", 
                "{" +
                  "\"firstname\": \"Chuck JSON\"," +
                  "\"lastname\": \"Norris\"," +
                  "\"birthdate\": \"1945-01-01\"" +
                "}"
    );

    if ( responseStatus == 201 )
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print JSON Request
    printRequestDetails(4, "POST", reqPath, "application/json", "application/json");

    /*
    **********************************************************************************
    * Performs Request #5 - Delete a person XML
    **********************************************************************************
    */
    reqPath = response.getLocation().getPath(); // Get the created person path from previous response

    // Request
    performDeleteRequest(reqPath);
    requestResult = "N/A"; // Performing the delete request is not enough to evaluate this request with 'OK' or 'ERROR'

    // Print Request
    printRequestDetails(5, "DELETE", reqPath, "", "");

    // Perform XML Request
    performGetRequest(reqPath, "application/xml");

    if ( responseStatus == 404 )
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print XML Request
    printRequestDetails(1, "GET", reqPath, "application/xml", "application/xml");

    /*
    **********************************************************************************
    * Performs Request #9 - Get all Measure Types
    **********************************************************************************
    */
    reqPath = "/measureTypes";

    // Perform XML Request
    performGetRequest(reqPath, "application/xml");

    // Parse response body - Measure Types
    xmlParser = new XmlParser(responseBody);
    int measureTypesCount = xmlParser.countNodes("measureType");
    String[] measureTypes = xmlParser.getMeasureTypes();

    if ( measureTypesCount >= 3 )
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print XML Request
    printRequestDetails(9, "GET", reqPath, "application/xml", "");

    // Perform JSON Request
    performGetRequest(reqPath, "application/json");

    // Print JSON Request
    printRequestDetails(9, "GET", reqPath, "application/json", "");

    /*
    **********************************************************************************
    * Performs Request #6 - Get the history of each Measure Type for the First and Last Person
    **********************************************************************************
    */
    int personMeasuresCount = 0;
    int measureId = -1;
    String measureType = "";
    requestResult = "N/A";

    for(int i = 0; i < measureTypesCount; i++){
      reqPath = "/persons/" + firstPersonId + "/" + measureTypes[i];

      // Perform XML Request
      performGetRequest(reqPath, "application/xml");

      // Parse response body - First Person measures
      xmlParser = new XmlParser(responseBody);
      personMeasuresCount = personMeasuresCount + xmlParser.countNodes("measure");

      // Save a measure id and a measure type. Only if it hasn't been saved
      if ( personMeasuresCount > 0 && measureId == -1 ){
        measureId = xmlParser.getMeasureId();
        measureType = xmlParser.getMeasureName();
      }

      // Print XML Request
      printRequestDetails(6, "GET", reqPath, "application/xml", "");

      // Perform JSON Request
      performGetRequest(reqPath, "application/json");

      // Print JSON Request
      printRequestDetails(6, "GET", reqPath, "application/json", "");

      // Perform XML Request
      reqPath = "/persons/" + lastPersonId + "/" + measureTypes[i];

      performGetRequest(reqPath, "application/xml");

      // Print XML Request
      printRequestDetails(6, "GET", reqPath, "application/xml", "");

      // Parse response body - Last Person measures
      xmlParser = new XmlParser(responseBody);
      personMeasuresCount = personMeasuresCount + xmlParser.countNodes("measure");

      // Perform JSON Request
      performGetRequest(reqPath, "application/json");

      // Print JSON Request
      printRequestDetails(6, "GET", reqPath, "application/json", "");
    }

    // Check if at least one measure (any type) had been registered for the first or last user
    if(personMeasuresCount > 0)
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print XML Request - Result of requesting every measure history for the first and last person
    responseBody = "";
    responseStatus = 0;

    printRequestDetails(6, "", "", "", "");

    /*
    **********************************************************************************
    * Performs Request #7 - Get a measure from the first person
    **********************************************************************************
    */
    reqPath = "/persons/" + firstPersonId + "/" + measureType + "/" + measureId;

    // Perform XML Request
    performGetRequest(reqPath, "application/xml");

    if(responseStatus == 200)
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print XML Request
    printRequestDetails(7, "GET", reqPath, "application/xml", "");

    // Perform JSON Request
    performGetRequest(reqPath, "application/json");

    // Print JSON Request
    printRequestDetails(7, "GET", reqPath, "application/json", "");

    /*
    **********************************************************************************
    * Performs Request #6 - Get the history of a measure of a person
    **********************************************************************************
    */
    reqPath = "/persons/" + firstPersonId + "/" + measureType;
    requestResult = "N/A";

    // Perform XML Request
    performGetRequest(reqPath, "application/xml");

    // Parse response body - First Person measures
    xmlParser = new XmlParser(responseBody);
    int firstPersonMeasuresCount = xmlParser.countNodes("measure");

    // Print XML Request
    printRequestDetails(6, "GET", reqPath, "application/xml", "");

    // Perform XML Request
    performGetRequest(reqPath, "application/json");

    // Print JSON Request
    printRequestDetails(6, "GET", reqPath, "application/json", "");

    // Perform XML Request
    performPostPutRequest(
                reqPath, 
                "application/xml",
                "POST", 
                "application/xml", 
                "<measure><value>123</value></measure>"
    );

    // Print XML Request
    printRequestDetails(8, "POST", reqPath, "application/xml", "");

    // Perform JSON Request
    performPostPutRequest(
                reqPath, 
                "application/json",
                "POST", 
                "application/json", 
                "{ \"value\" : \"123\" }"
    );

    // Print JSON Request
    printRequestDetails(8, "POST", reqPath, "application/json", "");

    // Perform XML Request
    performGetRequest(reqPath, "application/xml");

    // Parse response body - First Person measures
    xmlParser = new XmlParser(responseBody);

    if( firstPersonMeasuresCount > xmlParser.countNodes("measures"))
      requestResult = "OK";
    else
      requestResult = "ERROR";

    // Print XML Request
    printRequestDetails(6, "GET", reqPath, "application/xml", "");

    // Perform JSON Request
    performGetRequest(reqPath, "application/json");

    // Print JSON Request
    printRequestDetails(6, "GET", reqPath, "application/json", "");
  }

  private static void performPostPutRequest(String path, String accept, String method, String contentType, String requestBody){
    response = service.path(path).request().accept(accept).build(method, Entity.entity(requestBody, contentType)).invoke();
    loadResponseBodyAndStatus();
  }

  private static void performGetRequest(String path, String accept){
    response = service.path(path).request().accept(accept).get();
    loadResponseBodyAndStatus();
  }

  private static void performDeleteRequest(String path){
    response = service.path(path).request().delete();
    loadResponseBodyAndStatus();
  }

  private static void loadResponseBodyAndStatus(){
    responseBody = response.readEntity(String.class);
    responseStatus = response.getStatus();
  }

  private static void printRequestDetails(int n, String method, String path, String accept, String contentType) throws TransformerException {
    System.out.println("Request #" + n + ": " + method + " " + path + " Accept:" + accept + " Content-type: " + contentType ); 
    System.out.println("=> Result: " + requestResult);
    System.out.println("=> HTTP Status: " + responseStatus);

    if( responseBody != null && !responseBody.isEmpty() ){
      if(accept == "application/xml")
        prettyPrintXml(responseBody);
      else
        System.out.println(responseBody);
    }

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
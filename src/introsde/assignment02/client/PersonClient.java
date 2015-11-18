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
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class PersonClient {

  private Client client;
  private WebTarget service;

  public PersonClient(String serverUri){
    client = ClientBuilder.newClient(new ClientConfig());
    service = client.target( getBaseURI(serverUri) );
  }

  public void getAllPeople() throws TransformerException {
    Response res = service.path("/persons").request().accept("application/xml").get();
    printRequestDetails(1, "GET", "/persons", "application/xml", "", res);
  }

  private void printRequestDetails(int n, String method, String path, String accept, String contentType, Response res) throws TransformerException {
    System.out.println("Request #" + n + ": " + method + " " + path + " Accept:" + accept + " Content-type: " + contentType ); 
    System.out.println("=> HTTP Status: " + res.getStatus());
    prettyPrintXml(res.readEntity(String.class));
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

  private static URI getBaseURI(String uri) {
    return UriBuilder.fromUri(uri).build();
  }
}
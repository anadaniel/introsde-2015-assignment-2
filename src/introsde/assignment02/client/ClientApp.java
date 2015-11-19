package introsde.assignment02.client;

import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientConfig;

import java.io.*;
import java.lang.Exception;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.parsers.ParserConfigurationException;

public class ClientApp {
  public static void main(String[] args) throws Exception {
  	System.out.println(">>>>> Server URL: http://127.0.1.1:3000");
    Requester requester = new Requester("http://127.0.1.1:3000");

    //Perform Request #1
    requester.getAllPeople();

    //Perform Request #2
    requester.printFirstPerson();
  }
}
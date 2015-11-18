package introsde.assignment02.client;

import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientConfig;

import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class ClientApp {
  public static void main(String[] args) throws TransformerException {
    PersonClient personClient = new PersonClient("http://127.0.1.1:3000");
    personClient.getAllPeople();
  }
}
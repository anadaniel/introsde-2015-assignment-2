package introsde.assignment02.client;

import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientConfig;

import java.io.*;
import javax.xml.transform.TransformerException;

public class ClientApp {
  public static void main(String[] args) throws TransformerException {
    Requester requester = new Requester("http://127.0.1.1:3000");
    requester.getAllPeople();
  }
}
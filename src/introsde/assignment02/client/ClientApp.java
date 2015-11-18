package introsde.assignment02.client;

import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientConfig;

public class ClientApp {
  public static void main(String[] args) {
    ClientConfig clientConfig = new ClientConfig();
    Client client = ClientBuilder.newClient(clientConfig);
    WebTarget service = client.target(getBaseURI());

    String test = service.path("/persons").request().accept(MediaType.TEXT_XML).get().readEntity(String.class);
    System.out.println(">>>>> Test GET Request: " + test);
  }

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://127.0.1.1:3000").build();
  }
}
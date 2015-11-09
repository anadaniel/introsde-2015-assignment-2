package introsde.assignment02;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("assignment02")
public class MyApplicationConfig extends ResourceConfig {
  public MyApplicationConfig () {
    packages("introsde.assignment02");
  }
}

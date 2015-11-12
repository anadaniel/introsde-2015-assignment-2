package introsde.assignment02.resources;
import introsde.assignment02.model.Person;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container
public class PersonResource {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;
  int id;

  EntityManager entityManager; // only used if the application is deployed in a Java EE container

  public PersonResource(UriInfo uriInfo, Request request,int id, EntityManager em) {
    this.uriInfo = uriInfo;
    this.request = request;
    this.id = id;
    this.entityManager = em;
  }

  public PersonResource(UriInfo uriInfo, Request request,int id) {
    this.uriInfo = uriInfo;
    this.request = request;
    this.id = id;
  }

  // Application integration
  @GET
  @Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Person getPerson() {
    Person person = Person.getPersonById(this.id);
    if (person == null)
      throw new RuntimeException("Get: Person with " + id + " not found");
    return person;
  }
}
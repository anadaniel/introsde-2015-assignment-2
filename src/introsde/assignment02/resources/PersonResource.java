package introsde.assignment02.resources;
import introsde.assignment02.model.Person;
import introsde.assignment02.model.Measure;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container
public class PersonResource {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;
  int id;

  public PersonResource(UriInfo uriInfo, Request request, int id) {
    this.uriInfo = uriInfo;
    this.request = request;
    this.id = id;
  }

  @GET
  @Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Person getPerson() {
    Person person = Person.getPersonById(this.id);
    if (person == null)
      throw new RuntimeException("Get: Person with " + id + " not found");
    return person;
  }

  @PUT
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Response putPerson(Person person) {
    Response res;
    Person existing_person = Person.getPersonById(this.id);

    if (existing_person == null) {
      res = Response.status(Status.NOT_FOUND).build();
    } else {
      person.setPersonId(this.id);
      Person.updatePerson(person);
      res = Response.status(Status.OK).location(uriInfo.getAbsolutePath()).build();
    }
    return res;
  }

  @DELETE
  public Response deletePerson() {
    Response res;
    Person person = Person.getPersonById(this.id);

    if (person == null){
      res = Response.status(Status.NOT_FOUND).build();
    }
    else {
      Person.deletePerson(person);
      res = Response.status(Status.NO_CONTENT).build();
    }

    return res;
  }

  @GET
  @Path("{measureType}")
  @Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Measure> getMeasureHistory(@PathParam("measureType") String measureName) {
    System.out.println(">>>>>>>>>>>>>> param:" + measureName);
    List<Measure> measures = Measure.getMeasuresFromPerson(this.id, measureName);
    return measures;
  }
}
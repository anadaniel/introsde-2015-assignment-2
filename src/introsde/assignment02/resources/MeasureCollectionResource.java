package introsde.assignment02.resources;
import introsde.assignment02.model.Measure;

import java.io.IOException;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container
public class MeasureCollectionResource {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;
  int personId;
  String measureName;

  public MeasureCollectionResource(UriInfo uriInfo, Request request, int personId, String measureName) {
    this.uriInfo = uriInfo;
    this.request = request;
    this.personId = personId;
    this.measureName = measureName;
  }


  @GET
  @Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public List<Measure> getMeasureHistory() {
    List<Measure> measures = Measure.getMeasuresFromPerson(this.personId, this.measureName);
    return measures;
  }

  @POST
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Measure createMeasure(Measure measure) throws IOException {
    return Measure.createMeasure(measure, this.personId, this.measureName);
  }

  // Let the MeasureResource class to handle operations on a single Person
  @Path("{measureId}")
  public MeasureResource getMeasureResource(@PathParam("measureId") int id) {
    return new MeasureResource(uriInfo, request, id);
  }
}
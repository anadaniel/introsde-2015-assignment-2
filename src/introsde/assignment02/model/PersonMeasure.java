package introsde.assignment02.model;

import introsde.assignment02.dao.Assignment02Dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="PersonMeasure") // to whole table must be persisted 
@NamedQuery(
  name="PersonMeasure.findMeasuresFromPerson",
  query = "SELECT pm FROM PersonMeasure pm"
)
@XmlRootElement(name="measure")
public class PersonMeasure implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id // defines this attributed as the one that identifies the entity
  @GeneratedValue(generator="sqlite_person_measure")
  @TableGenerator(name="sqlite_person_measure", table="sqlite_sequence",
    pkColumnName="name", valueColumnName="seq",
    pkColumnValue="PersonMeasure")
  @Column(name="personMeasureId")
  private int personMeasureId;
  @Column(name="value")
  private String value;
  @Column(name="personId")
  private int personId;
  
  // getters
  public int getPersonMeasureId(){
    return personMeasureId;
  }
  public String getValue(){
    return value;
  }
  public int getPersonId(){
    return personId;
  }
  
  // setters
  public void setPersonMeasureId(int personMeasureTypeId){
    this.personMeasureId = personMeasureId;
  }
  public void setValue(String value){
    this.value = value;
  }
  public void setPersonId(int personId){
    this.personId = personId;
  }

  public static List<PersonMeasure> getMeasuresFromPerson(int personId, String measureName) {
    EntityManager em = Assignment02Dao.instance.createEntityManager();
    List<PersonMeasure> list = em.createNamedQuery("PersonMeasure.findMeasuresFromPerson", PersonMeasure.class).getResultList();
    Assignment02Dao.instance.closeConnections(em);
    return list;
  }
}
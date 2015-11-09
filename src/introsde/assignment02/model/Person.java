package introsde.assignment02.model;

import introsde.assignment02.dao.Assignment02Dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@Entity  // indicates that this class is an entity to persist in DB
@Table(name="Person") // to whole table must be persisted 
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
@XmlRootElement
public class Person implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id // defines this attributed as the one that identifies the entity
  @GeneratedValue(generator="sqlite_person")
  @TableGenerator(name="sqlite_person", table="sqlite_sequence",
    pkColumnName="name", valueColumnName="seq",
    pkColumnValue="Person")
  @Column(name="personId")
  private int personId;
  @Column(name="lastname")
  private String lastname;
  @Column(name="firstname")
  private String firstname;
  
  // getters
  public int getPersonId(){
    return personId;
  }
  public String getLastname(){
    return lastname;
  }
  public String getFirstame(){
    return firstname;
  }
  
  // setters
  public void setPersonId(int personId){
    this.personId = personId;
  }
  public void setLastname(String lastname){
    this.lastname = lastname;
  }
  public void setFirstame(String firstname){
    this.firstname = firstname;
  }
  
  public static List<Person> getAll() {
    EntityManager em = Assignment02Dao.instance.createEntityManager();
    List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
    .getResultList();
    Assignment02Dao.instance.closeConnections(em);
    return list;
  }
}
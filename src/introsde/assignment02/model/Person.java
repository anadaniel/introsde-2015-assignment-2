package introsde.assignment02.model;

import introsde.assignment02.dao.Assignment02Dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlTransient;
@Entity  // indicates that this class is an entity to persist in DB
@Table(name="Person") // to whole table must be persisted 
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
@XmlRootElement
@XmlType(propOrder={"personId","firstname","lastname", "birthdate", "currentMeasures"})
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
  @Temporal(TemporalType.DATE)
  @Column(name="birthdate")
  private Date birthdate;

  @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
  @JoinColumn(name="personId", referencedColumnName="personId")
  private List<Measure> measures;

  @Transient
  private List<Measure> currentMeasures;
  
  // getters
  public int getPersonId(){
    return personId;
  }
  public String getLastname(){
    return lastname;
  }
  public String getFirstname(){
    return firstname;
  }
  public Date getBirthdate(){
    return birthdate;
  }
  @XmlTransient
  public List<Measure> getMeasures(){
    return measures;
  }

  @XmlElementWrapper(name="healthProfile")
  @XmlElement(name="measure")
  public List<Measure> getCurrentMeasures(){
    if (this.currentMeasures == null) // wWhen the currentMeasures is not empty it means it's readding a person that's coming from a post
      this.currentMeasures = Measure.getCurrentMeasuresFromPerson(this.personId);

    return currentMeasures;
  }
  
  // setters
  public void setPersonId(int personId){
    this.personId = personId;
  }
  public void setLastname(String lastname){
    this.lastname = lastname;
  }
  public void setFirstname(String firstname){
    this.firstname = firstname;
  }
  public void setBirthdate(Date birthdate){
    this.birthdate = birthdate;
  }
  public void setMeasures(List<Measure> measures){
    this.measures = measures;
  }
  public void setCurrentMeasures(List<Measure> currentMeasures){
    this.currentMeasures = currentMeasures;
  }

  public static List<Person> getAll() {
    EntityManager em = Assignment02Dao.instance.createEntityManager();
    List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
    .getResultList();
    Assignment02Dao.instance.closeConnections(em);
    return list;
  }

  public static Person getPersonById(int personId) {
    EntityManager em = Assignment02Dao.instance.createEntityManager();
    Person person = em.find(Person.class, personId);
    if(person != null)
      em.refresh(person);
    Assignment02Dao.instance.closeConnections(em);
    return person;
  }

  public static Person createPerson(Person person) {
    EntityManager em = Assignment02Dao.instance.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    em.persist(person);
    tx.commit();
    Assignment02Dao.instance.closeConnections(em);
    return person;
  }

  public static Person syncPerson(Person oldPerson, Person updatedPerson) {
    updatedPerson.setPersonId(oldPerson.getPersonId());

    if (updatedPerson.getFirstname() == null)
      updatedPerson.setFirstname(oldPerson.getFirstname());

    if (updatedPerson.getLastname() == null)
      updatedPerson.setLastname(oldPerson.getLastname());

    if (updatedPerson.getBirthdate() == null)
      updatedPerson.setBirthdate(oldPerson.getBirthdate());

    return updatedPerson;
  }

  public static Person updatePerson(Person oldPerson, Person updatedPerson) {
    updatedPerson = syncPerson(oldPerson, updatedPerson);

    EntityManager em = Assignment02Dao.instance.createEntityManager(); 
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    updatedPerson = em.merge(updatedPerson);
    tx.commit();
    Assignment02Dao.instance.closeConnections(em);
    return updatedPerson;
  }

  public static void deletePerson(Person person) {
    EntityManager em = Assignment02Dao.instance.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    person = em.merge(person);
    em.remove(person);
    tx.commit();
    Assignment02Dao.instance.closeConnections(em);
  }
}
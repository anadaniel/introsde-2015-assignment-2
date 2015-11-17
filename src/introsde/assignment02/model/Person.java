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
  @Temporal(TemporalType.DATE)
  @Column(name="birthdate")
  private Date birthdate;
  
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
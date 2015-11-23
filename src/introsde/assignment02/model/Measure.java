package introsde.assignment02.model;

import introsde.assignment02.dao.Assignment02Dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="Measure") // to whole table must be persisted 
@NamedQueries({
  @NamedQuery(
    name = "Measure.findMeasuresFromPerson",
    query = "SELECT m FROM Measure m, MeasureType mt WHERE m.person.personId = :pid AND m.measureType.measureTypeId = mt.measureTypeId AND mt.name = :measureName ORDER BY m.measureId DESC"
  ),
  @NamedQuery(
    name = "Measure.findCurrentMeasuresFromPerson",
    query = "SELECT m FROM Measure m, MeasureType mt WHERE m.person.personId = :pid AND m.measureType.measureTypeId = mt.measureTypeId GROUP BY mt.measureTypeId"
  )
})
@XmlRootElement
@XmlType(propOrder={"measureId","value","measureType","created"})
public class Measure implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id // defines this attributed as the one that identifies the entity
  @GeneratedValue(generator="sqlite_measure")
  @TableGenerator(name="sqlite_measure", table="sqlite_sequence",
    pkColumnName="name", valueColumnName="seq",
    pkColumnValue="Measure")
  @Column(name="measureId")
  private int measureId;
  @Column(name="value")
  private String value;
  @Temporal(TemporalType.DATE)
  @Column(name="created")
  private Date created;

  @ManyToOne
  @JoinColumn(name="measureTypeId",referencedColumnName="measureTypeId")
  private MeasureType measureType;

  @ManyToOne
  @JoinColumn(name="personId",referencedColumnName="personId", insertable=false, updatable=false)
  private Person person;
  
  // getters
  @XmlElement(name="mid")
  public int getMeasureId(){
    return measureId;
  }
  public String getValue(){
    return value;
  }
  @XmlTransient
  public Person getPerson(){
    return person;
  }
  @XmlElement(name="measureName")
  public MeasureType getMeasureType(){
    return measureType;
  }
  public Date getCreated(){
    return created;
  }
  
  // setters
  public void setMeasureId(int measureId){
    this.measureId = measureId;
  }
  public void setValue(String value){
    this.value = value;
  }
  public void setPerson(Person person){
    this.person = person;
  }
  public void setMeasureType(MeasureType measureType){
    if( measureType.getMeasureTypeId() == 0 ) // New measure with just the measure type name set
      measureType = MeasureType.findFromName(measureType.getName()); // Set the measureType from the name
    
    this.measureType = measureType;
  }
  public void setCreated(Date created){
    this.created = created;
  }
  
  public void setMeasureTypeFromMeasureName(String measureName){
    MeasureType measureType = MeasureType.findFromName(measureName);
    setMeasureType(measureType);
  }

  public static Measure getMeasureById(int measureId) {
    EntityManager em = Assignment02Dao.instance.createEntityManager();
    Measure measure = em.find(Measure.class, measureId);
    Assignment02Dao.instance.closeConnections(em);
    return measure;
  }

  public static List<Measure> getMeasuresFromPerson(int personId, String measureName) {
    EntityManager em = Assignment02Dao.instance.createEntityManager();
    List<Measure> list = em.createNamedQuery("Measure.findMeasuresFromPerson", Measure.class)
      .setParameter("pid", personId)
      .setParameter("measureName", measureName)
      .getResultList();
    Assignment02Dao.instance.closeConnections(em);
    return list;
  }

  public static List<Measure> getCurrentMeasuresFromPerson(int personId) {
    EntityManager em = Assignment02Dao.instance.createEntityManager();
    List<Measure> list = em.createNamedQuery("Measure.findCurrentMeasuresFromPerson", Measure.class)
      .setParameter("pid", personId)
      .getResultList();
    Assignment02Dao.instance.closeConnections(em);
    return list;
  }

  public static Measure createMeasure(Measure measure, int personId, String measureName) {
    measure.setPerson(Person.getPersonById(personId));
    measure.setMeasureTypeFromMeasureName(measureName);
    measure.setCreated(new Date());

    EntityManager em = Assignment02Dao.instance.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    em.persist(measure);
    tx.commit();
    Assignment02Dao.instance.closeConnections(em);
    return measure;
  }

  public static Measure updateMeasure(Measure oldMeasure, Measure updatedMeasure) {
    //When updating a measure only the value attribute can be changed
    updatedMeasure.setMeasureId(oldMeasure.getMeasureId());
    updatedMeasure.setPerson(oldMeasure.getPerson());
    updatedMeasure.setMeasureType(oldMeasure.getMeasureType());

    EntityManager em = Assignment02Dao.instance.createEntityManager(); 
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    updatedMeasure = em.merge(updatedMeasure);
    tx.commit();
    Assignment02Dao.instance.closeConnections(em);
    return updatedMeasure;
  }
}
package introsde.assignment02.model;

import introsde.assignment02.dao.Assignment02Dao;
import introsde.assignment02.model.MeasureType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="Measure") // to whole table must be persisted 
@NamedQuery(
  name = "Measure.findMeasuresFromPerson",
  query = "SELECT pm FROM Measure pm, MeasureType mt WHERE pm.personId = :pid AND mt.name = :measureName"
)
@XmlRootElement(name="measure")
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
  @Column(name="personId")
  private int personId;
  @Temporal(TemporalType.DATE)
  @Column(name="created")
  private Date created;

  @ManyToOne
  @JoinColumn(name="measureTypeId",referencedColumnName="measureTypeId")
  private MeasureType measureType;
  
  // getters
  @XmlElement(name="mid")
  public int getMeasureId(){
    return measureId;
  }
  public String getValue(){
    return value;
  }
  @XmlTransient
  public int getPersonId(){
    return personId;
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
  public void setPersonId(int personId){
    this.personId = personId;
  }
  public void setMeasureType(MeasureType measureType){
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

  public static Measure createMeasure(Measure measure, int personId, String measureName) {
    measure.setPersonId(personId);
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

  public static Measure updateMeasure(Measure old_measure, Measure updated_measure) {
    //When updating a measure only the value attribute can be changed
    updated_measure.setMeasureId(old_measure.getMeasureId());
    updated_measure.setPersonId(old_measure.getPersonId());
    updated_measure.setMeasureType(old_measure.getMeasureType());

    EntityManager em = Assignment02Dao.instance.createEntityManager(); 
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    updated_measure = em.merge(updated_measure);
    tx.commit();
    Assignment02Dao.instance.closeConnections(em);
    return updated_measure;
  }
}
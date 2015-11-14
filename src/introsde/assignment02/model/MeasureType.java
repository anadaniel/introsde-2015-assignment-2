package introsde.assignment02.model;

import introsde.assignment02.dao.Assignment02Dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@Entity  // indicates that this class is an entity to persist in DB
@Table(name="MeasureType") // to whole table must be persisted 
@NamedQuery(name="MeasureType.findAll", query="SELECT mt FROM MeasureType mt")
@XmlRootElement
public class MeasureType implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id // defines this attributed as the one that identifies the entity
  @GeneratedValue(generator="sqlite_measure_type")
  @TableGenerator(name="sqlite_measure_type", table="sqlite_sequence",
    pkColumnName="name", valueColumnName="seq",
    pkColumnValue="MeasureType")
  @Column(name="measureTypeId")
  private int measureTypeId;
  @Column(name="name")
  private String name;
  
  // getters
  public int getMeasureTypeId(){
    return measureTypeId;
  }
  public String getName(){
    return name;
  }
  
  // setters
  public void getMeasureTypeId(int measureTypeId){
    this.measureTypeId = measureTypeId;
  }
  public void setName(String name){
    this.name = name;
  }

  public static List<MeasureType> getAll() {
    EntityManager em = Assignment02Dao.instance.createEntityManager();
    List<MeasureType> list = em.createNamedQuery("MeasureType.findAll", MeasureType.class).getResultList();
    Assignment02Dao.instance.closeConnections(em);
    return list;
  }
}
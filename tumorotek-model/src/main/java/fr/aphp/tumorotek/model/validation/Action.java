package fr.aphp.tumorotek.model.validation;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * Entity implementation class for Entity: Action
 *
 */
@Entity
@Table(name = "ACTION", uniqueConstraints = @UniqueConstraint(columnNames = {"ENTITE_ID", "TYPE_ACTION"}))
@NamedQueries({@NamedQuery(name = "Action.findByLibelle", query = "FROM Action a WHERE a.libelle = ?1"),
   @NamedQuery(name = "Action.findByEntiteAndType", query = "FROM Action a WHERE a.entite = ?1 AND a.typeAction = ?2")})
public class Action implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer id;
   private Entite entite;
   private ActionType typeAction;
   private String libelle;
   private List<Validation> listValidations;

   /**
    * Constructeur par défaut
    */
   public Action(){}

   @Id
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   @Column(name = "ACTION_ID")
   public Integer getId(){
      return this.id;
   }

   public void setId(Integer id){
      this.id = id;
   }

   @Column(name = "LIBELLE")
   public String getLibelle(){
      return this.libelle;
   }

   public void setLibelle(String libelle){
      this.libelle = libelle;
   }

   @ManyToOne
   @JoinColumn(name = "ENTITE_ID", nullable=false)
   public Entite getEntite(){
      return entite;
   }

   public void setEntite(Entite entite){
      this.entite = entite;
   }

   @Enumerated(EnumType.STRING)
   @Column(name = "TYPE_ACTION", nullable=false)
   public ActionType getTypeAction(){
      return typeAction;
   }

   public void setTypeAction(ActionType typeAction){
      this.typeAction = typeAction;
   }

   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "ACTION_VALIDATION", joinColumns = @JoinColumn(name = "ACTION_ID"),
      inverseJoinColumns = @JoinColumn(name = "VALIDATION_ID"))
   public List<Validation> getListValidations(){
      return this.listValidations;
   }

   public void setListValidations(List<Validation> listValidations){
      this.listValidations = listValidations;
   }

}

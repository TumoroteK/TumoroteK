/** 
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * 
 * Ce logiciel est un programme informatique servant à la gestion de 
 * l'activité de biobanques. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français
 * et respectant les principes de diffusion des logiciels libres. Vous 
 * pouvez utiliser, modifier et/ou redistribuer ce programme sous les 
 * conditions de la licence CeCILL telle que diffusée par le CEA, le 
 * CNRS et l'INRIA sur le site "http://www.cecill.info". 
 * En contrepartie de l'accessibilité au code source et des droits de   
 * copie, de modification et de redistribution accordés par cette 
 * licence, il n'est offert aux utilisateurs qu'une garantie limitée. 
 * Pour les mêmes raisons, seule une responsabilité restreinte pèse sur 
 * l'auteur du programme, le titulaire des droits patrimoniaux et les 
 * concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les 
 * risques associés au chargement,  à l'utilisation,  à la modification 
 * et/ou au  développement et à la reproduction du logiciel par 
 * l'utilisateur étant donné sa spécificité de logiciel libre, qui peut 
 * le rendre complexe à manipuler et qui le réserve donc à des 	
 * développeurs et des professionnels  avertis possédant  des 
 * connaissances  informatiques approfondies.  Les utilisateurs sont 
 * donc invités à charger  et  tester  l'adéquation  du logiciel à leurs
 * besoins dans des conditions permettant d'assurer la sécurité de leurs
 * systèmes et ou de leurs données et, plus généralement, à l'utiliser 
 * et l'exploiter dans les mêmes conditions de sécurité. 
 *	
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous 
 * avez pris connaissance de la licence CeCILL, et que vous en avez 
 * accepté les termes. 
 **/
package fr.aphp.tumorotek.model.interfacage.scan;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * Objet persistant mappant la table SCAN_DEVICE.
 * Classe créée le 24/04/2016.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.1
 * 
 */
@Entity
@Table(name = "SCAN_DEVICE")
public class ScanDevice implements java.io.Serializable {

	private static final long serialVersionUID = 1035198779653396102L;
	
	private Integer scanDeviceId;
	private String name;
	private String version;
	private Set<ScanTerminale> scanTerminales = new HashSet<ScanTerminale>();
	
	public ScanDevice() {
		super();
	}
	
	@Id
	@Column(name = "SCAN_DEVICE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getScanDeviceId() {
		return scanDeviceId;
	}

	public void setScanDeviceId(Integer _i) {
		this.scanDeviceId = _i;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}
	
	public void setName(String _n) {
		this.name = _n;
	}
	
	@Column(name = "VERSION", nullable = true)
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String _v) {
		this.version = _v;
	}
	
	@OneToMany(mappedBy = "scanDevice", cascade = CascadeType.ALL)
	public Set<ScanTerminale> getScanTerminales() {
		return scanTerminales;
	}

	public void setScanTerminales(Set<ScanTerminale> _s) {
		this.scanTerminales = _s;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		ScanDevice test = (ScanDevice) obj;
		return ((this.name == test.name 
				|| (this.name != null 
				&& this.name.equals(test.name))) 
			&& (this.version == test.version 
					|| (this.version != null 
					&& this.version.equals(test.version)))
		);
	}
	
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashName = 0;
		int hashVersion = 0;
		
		if (this.name != null) {
			hashName = this.name.hashCode();
		}
		if (this.version != null) {
			hashVersion = this.version.hashCode();
		}
		
		hash = 7 * hash + hashName;
		hash = 7 * hash + hashVersion;
		
		return hash;
	}

}

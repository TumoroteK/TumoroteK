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
package fr.aphp.tumorotek.manager.impl.coeur.echantillon;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Suite des ids correspondant au max Ids actuellement en base pour 
 * les objets crées lors de la création d'un échantillon:
 *  1) échantillon max Echantillon Id
 *  2) annotation max AnnotationValeur Id
 *  3) code org et les max CodeAssigne Id
 *  4) non conformites max Objet non conforme Id
 * Classe créée le 12/01/2015
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.10.6
 *
 */
public class EchantillonJdbcSuite {
	
	private Integer maxEchantillonId = 0;
	private Integer maxAnnotationValeurId = 0;
	private Integer maxCodeAssigneId = 0;
	private Integer maxObjetNonConformeId = 0;
	
	private PreparedStatement pstmt;
	private PreparedStatement pstmtAnno;
	private PreparedStatement pstmtCd;
	private PreparedStatement pstmtNc;
	private PreparedStatement pstmtOp;
	
	public Integer getMaxEchantillonId() {
		return maxEchantillonId;
	}
	
	public void setMaxEchantillonId(Integer eId) {
		this.maxEchantillonId = eId;
	}
	
	public Integer getMaxAnnotationValeurId() {
		return maxAnnotationValeurId;
	}
	
	public void setMaxAnnotationValeurId(Integer vId) {
		this.maxAnnotationValeurId = vId;
	}
	
	public Integer getMaxCodeAssigneId() {
		return maxCodeAssigneId;
	}
	
	public void setMaxCodeAssigneId(Integer cId) {
		this.maxCodeAssigneId = cId;
	}
	
	public Integer getMaxObjetNonConformeId() {
		return maxObjetNonConformeId;
	}
	
	public void setMaxObjetNonConformeId(Integer ocId) {
		this.maxObjetNonConformeId = ocId;
	}
	
	public void incrementMaxEchantillonId() {
		maxEchantillonId ++;
	}
	
	public void incrementMaxAnnotationValeurId() {
		maxAnnotationValeurId ++;
	}
	
	public void incrementMaxCodeAssigneId() {
		maxCodeAssigneId ++;
	}
	
	public void incrementMaxObjetNonConformeId() {
		maxObjetNonConformeId ++;
	}

	public PreparedStatement getPstmt() {
		return pstmt;
	}

	public void setPstmt(PreparedStatement p) {
		this.pstmt = p;
	}

	public PreparedStatement getPstmtAnno() {
		return pstmtAnno;
	}

	public void setPstmtAnno(PreparedStatement p) {
		this.pstmtAnno = p;
	}

	public PreparedStatement getPstmtCd() {
		return pstmtCd;
	}

	public void setPstmtCd(PreparedStatement p) {
		this.pstmtCd = p;
	}

	public PreparedStatement getPstmtNc() {
		return pstmtNc;
	}

	public void setPstmtNc(PreparedStatement p) {
		this.pstmtNc = p;
	}

	public PreparedStatement getPstmtOp() {
		return pstmtOp;
	}

	public void setPstmtOp(PreparedStatement p) {
		this.pstmtOp = p;
	}

	/**
	 * Ferme les PreparedStatements
	 */
	public void closePs() {
		if (pstmt != null) {
			try { pstmt.close(); 
			} catch (Exception ex) { pstmt = null; }
		}
		if (pstmtCd != null) {
			try { pstmtCd.close(); 
			} catch (Exception ex) { pstmtCd = null; }
		}
		if (pstmtAnno != null) {
			try { pstmtAnno.close(); 
			} catch (Exception ex) { pstmtAnno = null; }
		}
		if (pstmtNc != null) {
			try { pstmtNc.close(); 
			} catch (Exception ex) { pstmtNc = null; }
		}
		if (pstmtOp != null) {
			try { pstmtOp.close(); 
			} catch (Exception ex) { pstmtOp = null; }
		}			
	}

	public void executeBatches() throws SQLException {
		if (pstmt != null) {
			pstmt.executeBatch();
		}
		if (pstmtCd != null) {
			pstmtCd.executeBatch();
		}
		if (pstmtAnno != null) {
			pstmtAnno.executeBatch();
		}
		if (pstmtNc != null) {
			pstmtNc.executeBatch();
		}
		if (pstmtOp != null) {
			pstmtOp.executeBatch();
		}		
	}
	
	public void clearBatches() throws SQLException {
		if (pstmt != null) {
			pstmt.clearBatch();
		}
		if (pstmtCd != null) {
			pstmtCd.clearBatch();
		}
		if (pstmtAnno != null) {
			pstmtAnno.clearBatch();
		}
		if (pstmtNc != null) {
			pstmtNc.clearBatch();
		}
		if (pstmtOp != null) {
			pstmtOp.clearBatch();
		}		
	}
}

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
package fr.aphp.tumorotek.manager.impl.stats;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import oracle.jdbc.internal.OracleTypes;

import org.springframework.jdbc.datasource.DataSourceUtils;

import fr.aphp.tumorotek.manager.stats.GraphesModeleManager;
import fr.aphp.tumorotek.model.stats.GraphesModele;

/**
 * date: 19/12/2013
 * 
 * @author Marc DESCHAMPS
 */

public class GraphesModeleManagerImpl implements GraphesModeleManager {

	private DataSource dataSource;
	
	
	
	public void setDataSource(DataSource d) {
		this.dataSource = d;
	}

	GraphesModeleManagerImpl() {
	}

	@Override
	public GraphesModele platformeViewByPatientManager(Date date_debut, Date date_fin, boolean isOracle) {
		return callOnDBManager("platformeViewByPatient", date_debut, date_fin, null, null, isOracle);
	}

	@Override
	public GraphesModele platformeViewByPrelevementManager(Date date_debut, Date date_fin, boolean isOracle) {
		return callOnDBManager("platformeViewByPrelevement", date_debut, date_fin, null, null, isOracle);
	}

	@Override
	public GraphesModele platformeViewByEchantillonManager(Date date_debut, Date date_fin, boolean isOracle) {
		return callOnDBManager("platformeViewByEchantillon", date_debut, date_fin, null, null, isOracle);
	}

	@Override
	public GraphesModele platformeViewByDeriveManager(Date date_debut, Date date_fin, boolean isOracle) {
		return callOnDBManager("platformeViewByDerive", date_debut, date_fin, null, null, isOracle);
	}

	@Override
	public GraphesModele platformeViewByCessionManager(Date date_debut, Date date_fin, boolean isOracle) {
		return callOnDBManager("platformeViewByCession", date_debut, date_fin, null, null, isOracle);
	}

	@Override
	public GraphesModele collectionViewByPatientManager(Date date_debut, Date date_fin, String pfNom, boolean isOracle) {
		return callOnDBManager("collectionViewByPatient", date_debut, date_fin, pfNom, null, isOracle);
	}

	@Override
	public GraphesModele collectionViewByPrelevementManager(Date date_debut, Date date_fin, String pfNom, boolean isOracle) {
		return callOnDBManager("collectionViewByPrelevement", date_debut, date_fin, pfNom, null, isOracle);
	}

	@Override
	public GraphesModele collectionViewByEchantillonManager(Date date_debut, Date date_fin, String pfNom, boolean isOracle) {
		return callOnDBManager("collectionViewByEchantillon", date_debut, date_fin, pfNom, null, isOracle);
	}
	
	@Override
	public GraphesModele collectionViewByDeriveManager(Date date_debut, Date date_fin, String pfNom, boolean isOracle) {
		return callOnDBManager("collectionViewByDerive", date_debut, date_fin, pfNom, null, isOracle);
	}
	
	@Override
	public GraphesModele collectionViewByCessionManager(Date date_debut, Date date_fin, String pfNom, boolean isOracle) {
		return callOnDBManager("collectionViewByCession", date_debut, date_fin, pfNom, null, isOracle);
	}

	@Override
	public GraphesModele prelevementTypeByCollectionManager(Date date_debut, Date date_fin, 
			String banqueNom, String pfNom, boolean isOracle) {
		return callOnDBManager("prelTypeByCollection", date_debut, date_fin, banqueNom, pfNom, isOracle);
	}

	@Override
	public GraphesModele prelevementByEtablissementByCollectionManager(Date date_debut, Date date_fin, 
			String banqueNom, String pfNom, boolean isOracle) {
		return callOnDBManager("prelByEtabByCollection", date_debut, date_fin, 
				banqueNom, pfNom, isOracle);
	}

	@Override
	public GraphesModele prelevementByConsentementByCollectionManager(Date date_debut, Date date_fin, 
			String banqueNom, String pfNom, boolean isOracle) {
		return callOnDBManager("prelByConsentByCollection", date_debut, date_fin, 
				banqueNom, pfNom, isOracle);
	}

	@Override
	public GraphesModele echantillonTypeByCollectionManager(Date date_debut, Date date_fin, 
			String banqueNom, String pfNom, boolean isOracle) {
		return callOnDBManager("echanTypeByCollection", date_debut, date_fin, 
				banqueNom, pfNom, isOracle);
	}

	@Override
	public GraphesModele echantillonsCIM10ByCollectionManager(Date date_debut, Date date_fin, 
			String banqueNom, String pfNom, boolean isOracle) {
		return callOnDBManager("echanCIMByCollection", date_debut, date_fin, 
				banqueNom, pfNom, isOracle);
	}

	@Override
	public GraphesModele echantillonsADICAPByCollectionManager(Date date_debut, Date date_fin, 
			String banqueNom, String pfNom, boolean isOracle) {	
		return callOnDBManager("echanADICAPByCollection", date_debut, date_fin, 
				banqueNom, pfNom, isOracle);
	}

	@Override
	public GraphesModele deriveTypeByCollectionManager(Date date_debut, Date date_fin, String banqueNom, 
			String pfNom, boolean isOracle) {
		return callOnDBManager("deriveTypeByCollection", date_debut, date_fin, banqueNom, pfNom, isOracle);
	}


	@Override
	public GraphesModele cessionTypeByCollectionManager(Date date_debut, Date date_fin, String banqueNom, 
			String pfNom, boolean isOracle) {

		return callOnDBManager("cessionTypeByCollection", date_debut, date_fin, banqueNom, pfNom, isOracle);
	}

	@Override
	public GraphesModele callOnDBManager(String procName, Date date_debut, Date date_fin,
			String param1, String param2, boolean isOracle) {
		
		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
		GraphesModele grM = null;
		
		java.sql.Date date_debut_sql = null;
		java.sql.Date date_fin_sql = null;
		// si une des deux dates est != null
		if (date_debut != null || date_fin != null) {
			date_debut_sql = date_debut != null ? 
					new java.sql.Date(date_debut.getTime()) : new java.sql.Date(0);
			date_fin_sql = date_fin != null ?
					new java.sql.Date(date_fin.getTime()) : new java.sql.Date(Calendar.getInstance().getTimeInMillis());
		}

			
		if (procName != null) {
			
			ResultSet res = null;
			PreparedStatement call = null;
	
			String sql = "{call " + procName + "(?,?";
			if (param1 != null) {
				if (param2 != null) {
					sql = sql + ",?,?";
				} else {
					sql = sql + ",?";
				}
			} 
			// ajout param refcursor
			if (isOracle) {
				sql = sql + ",?";
			}
			
			sql = sql + ")}";
			
			Connection con = null;
			try {
				con = DataSourceUtils.getConnection(dataSource);
				call = con.prepareCall(sql);
				if (date_debut_sql != null) {
					call.setDate(1, date_debut_sql);
				} else {
					call.setNull(1, java.sql.Types.DATE);
				}
				if (date_fin_sql != null) {
					call.setDate(2, date_fin_sql);
				} else {
					call.setNull(2, java.sql.Types.DATE);
				}
//				if (date_debut_sql != null) {
//					call.setString(1, df.format(date_debut_sql));
//				} else {
//					call.setNull(1, java.sql.Types.VARCHAR);
//				}
//				if (date_fin_sql != null) {
//					call.setString(2, df.format(date_fin_sql));
//				} else {
//					call.setNull(2, java.sql.Types.VARCHAR);
//				}
				int cursorParamIdx = 3;
				if (param1 != null) {
					call.setString(3, param1);
					cursorParamIdx ++;
				}
				if (param2 != null) {
					call.setString(4, param2);
					cursorParamIdx ++;
				}
				
				ResultSet rset = null;
				
				// DBMS
				if (isOracle) {
					((CallableStatement) call)
						.registerOutParameter(cursorParamIdx, OracleTypes.CURSOR);
					call.executeQuery();
					rset = (ResultSet) ((CallableStatement) call).getObject(cursorParamIdx);
				} else {
					if (call.execute()) {
						rset = call.getResultSet();
					}
				}
				
				grM = new GraphesModele(rset);
	
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						con = null;
					}
				}
				if (call != null) {
					try {
						call.close();
					} catch (Exception e) {
						call = null;
					}
				}
				if (res != null) {
					try {
						res.close();
					} catch (Exception e) {
						res = null;
					}
				}
			}
		}
		
		return grM;
	}

	@Override
	public GraphesModele collectionViewByEchansCedesManager(Date date_debut,
			Date date_fin, String pfNom, boolean isOracle) {
		return callOnDBManager("echansCedesByCollection", date_debut, date_fin, pfNom, null, isOracle);
	}

	@Override
	public GraphesModele collectionViewByDerivesCedesManager(Date date_debut,
			Date date_fin, String pfNom, boolean isOracle) {
		return callOnDBManager("derivesCedesByCollection", date_debut, date_fin, pfNom, null, isOracle);

	}

}

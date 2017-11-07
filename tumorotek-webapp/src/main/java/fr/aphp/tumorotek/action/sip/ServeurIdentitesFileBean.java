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
package fr.aphp.tumorotek.action.sip;

import java.io.Serializable;

public class ServeurIdentitesFileBean implements Serializable {
    /* declaration des variables locales */
    private String dbms;
    private String host;
    private String driver;
    private String database;
    private String login;
    private String password;
    
    private String tablePatient;
    private String nip;
    private String nipType;
    private String nom;
    private String nomPatron;
    private String prenom;
    private String sexe;
    private String sexeType;
    private String sexeFemme;
    private String sexeHomme;
    private String dateNaiss;
    
    private String numDossier;
    
	/* constructeur par defaut */
    public ServeurIdentitesFileBean() {
        this.dbms = "";
        this.host = "";
        this.driver = "";
        this.database = "";
        this.login = "";
        this.password = "";
        
        this.tablePatient = "";
        this.nip = "";
        this.nipType = "";
        this.nom = "";
        this.nomPatron = "";
        this.prenom = "";
        this.sexe = "";
        this.sexeType = "";
        this.sexeFemme = "";
        this.sexeHomme = "";
        this.dateNaiss = "";
        
        this.numDossier = "";
    }
    
	/* constructeur value */
    public ServeurIdentitesFileBean(
    		String dbms,
    		String host,
    		String driver,
    		String database,
    		String login,
    		String password,
    		
    		String tablePatient,
    	    String nip,
    	    String nipType,
    	    String nom,
    	    String nomPatron,
    	    String prenom,
    	    String sexe,
    	    String sexeType,
    	    String sexeFemme,
    	    String sexeHomme,
    	    String dateNaiss,
    	    String numDossier) {
        this.dbms = dbms;
        this.host = host;
        this.driver = driver;
        this.database = database;
        this.login = login;
        this.password = password;

        this.tablePatient = tablePatient;
        this.nip = nip;
        this.nipType = nipType;
        this.nom = nom;
        this.nomPatron = nomPatron;
        this.prenom = prenom;
        this.sexe = sexe;
        this.sexeType = sexeType;
        this.sexeFemme = sexeFemme;
        this.sexeHomme = sexeHomme;
        this.dateNaiss = dateNaiss;
        
        this.numDossier = numDossier;
    }
    
	/* accesseurs */
	
	/* getters */
    public String getDbms(){
    	return this.dbms;
    }
    public String getHost(){
        return this.host;
    }
    public String getDriver(){
    	return this.driver;
    }
    public String getDatabase(){
    	return this.database;
    }	
    public String getLogin(){ 	
    	return this.login;
    }
    public String getPassword(){
    	return this.password;
    }
    
	public String getDateNaiss() {
		return dateNaiss;
	}
	public String getNip() {
		return nip;
	}
	public String getNipType() {
		return nipType;
	}
	public String getNom() {
		return nom;
	}
	public String getNomPatron() {
		return nomPatron;
	}
	public String getPrenom() {
		return prenom;
	}
	public String getSexe() {
		return sexe;
	}
	public String getSexeType() {
		return sexeType;
	}
	public String getSexeFemme() {
		return sexeFemme;
	}
	public String getSexeHomme() {
		return sexeHomme;
	}
	public String getTablePatient() {
		return tablePatient;
	}
    
    
	/* setters */
    public void setDbms(String dbms){
    	this.dbms = dbms;
    }
    public void setHost(String host){
        this.host = host;
    }
    public void setDriver(String driver){
    	this.driver = driver;
    }
    public void setDatabase(String database){
    	this.database = database;
    }	
    public void setLogin(String login){ 	
    	this.login = login;
    }
    public void setPassword(String password){
    	this.password = password;
    }
    
	public void setDateNaiss(String dateNaiss) {
		this.dateNaiss = dateNaiss;
	}
	public void setNip(String nip) {
		this.nip = nip;
	}
	public void setNipType(String nipType) {
		this.nipType = nipType;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public void setNomPatron(String nomPatron) {
		this.nomPatron = nomPatron;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public void setSexe(String sexe) {
		this.sexe = sexe;
	}
	public void setSexeType(String sexeType) {
		this.sexeType = sexeType;
	}
	public void setSexeFemme(String sexeFemme) {
		this.sexeFemme = sexeFemme;
	}
	public void setSexeHomme(String sexeHomme) {
		this.sexeHomme = sexeHomme;
	}
	public void setTablePatient(String tablePatient) {
		this.tablePatient = tablePatient;
	}

	//numDossier
	public String getNumDossier() {
		return numDossier;
	}
	public void setNumDossier(String numDossier) {
		this.numDossier = numDossier;
	}
}
/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de 
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.model.contexte.gatsbi;

public enum ContexteType {
	
	PATIENT(1, Values.PATIENT), 
	PRELEVEMENT(2, Values.PRELEVEMENT),
	ECHANTILLON(3, Values.ECHANTILLON);
    
	private Integer entiteId;
	private String type;

	ContexteType(Integer _i, String _t) {
        this.entiteId = _i;
        this.type = _t;
    }

	private ContexteType (String val) {
	     if (!this.type.equals(val))
	        throw new IllegalArgumentException();
	  }

    public static class Values {
        public static final String PATIENT = "Patient";
        public static final String PRELEVEMENT = "Prelevement";
        public static final String ECHANTILLON = "Echantillon";
    }
    
    public static ContexteType getById(Integer _i) {
        for(ContexteType ref : values()) {
            if(ref.entiteId.equals(_i)) return ref;
        }
        return null;
    }
    
    public static ContexteType getByType(String _s) {
        for(ContexteType ref : values()) {
            if(ref.type.equals(_s)) return ref;
        }
        return null;
    }
    
    public String getType() {
        return type;
    }

    public Integer getEntiteId() {
        return entiteId;
    }
}
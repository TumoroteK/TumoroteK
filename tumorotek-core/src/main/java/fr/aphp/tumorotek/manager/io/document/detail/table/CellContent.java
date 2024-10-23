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
package fr.aphp.tumorotek.manager.io.document.detail.table;


/**
 * La classe représente le contenu d'une cellule dans un tableau.
 *
 * <p>Cette classe encapsule les informations à afficher dans une cellule, y compris le texte principal, tout
 * complément de texte, et les options de mise en forme pour le complément.</p>
 *
 * <p>Le modèle de conception et l'architecture de cette classe ont été fournis par C.H.</p>
 */
public class CellContent {
    // Le texte principal affiché dans la cellule.
    private String text;

    // Le complément de texte à ajouter après le texte principal.
    private String complement;


    private boolean complementInItalic = true;//CHT : passage à true cf commentaire dans CellContent(String text, String complement)

    // Indique si le complément de texte doit être affiché sur une autre ligne.
    private boolean complementOnAnotherLine = false;

    //CHT :
    //les 4 méthodes ci-dessous sont des constructeurs donc elles doivent juste compléter l'initialisation sans paramètre
    //(pour les String, valeur à null et pour les booleans valeur à false
    //Il vaut mieux garder la valeur null si non défini et c'est avec le get qu'il faut renvoyer "" si null si cela est plus pratique pour la suite
    //mais je me rends-compte que l'appelant teste également le null (sauf dans un cas). Il faut homogénéiser et idéalement garder le null dans cette classe
    //On revient alors des à constructeur à l'implémentation basique
    public CellContent(String text) {
        //CHT
        this.text = text;
        this.complementInItalic = false;
        //this.text = (text == null) ? "" : text;
        //this.complement = "";
    }

    public CellContent(String text, String complement) {
        //CHT
        //this.text = (text == null) ? "" : text;
        //this.complement = (complement == null) ? "" : complement;
        //CHT : si je comprends bien, ça revient à dire que par défaut this.complementInItalic est à true.
        //dans ce cas, il vaut mieux forcer true au niveau de la déclaration de complementInItalic, comme proposé dans le code que je t'avais fourni
        //mettre en italique vide n'est pas gênant et complementInItalic a bien une seule responsabilité indépendante du fait que complement est non null 
        //this.complementInItalic = !this.complement.isEmpty();
        this(text);
        this.complement=complement;
    }

    public CellContent(String text, String complement, boolean complementOnAnotherLine) {
        this(text, complement);
        this.complementOnAnotherLine = complementOnAnotherLine;
    }

    public CellContent(String text, String complement, boolean complementInItalic, boolean complementOnAnotherLine) {
        //CHT
        //this.text = (text == null) ? "" : text;
        //this.complement = (complement == null) ? "" : complement;
        //this.complementInItalic = complementInItalic && !this.complement.isEmpty();
        //this.complementOnAnotherLine = complementOnAnotherLine;
        this(text, complement, complementOnAnotherLine);
        this.complementInItalic = complementInItalic;
    }

    public String getText() {
        //CHT : si on considère que pour les appelants, c'est plus pratique de renvoyer "" à la place de null, on gère ici
        if(text == null) {
           return "";
        }
        return text;
    }

    public void setText(String text) {
        //CHT :
        //this.text = (text == null) ? "" : text;
        this.text = text;
    }

    public String getComplement() {
        //CHT : si on considère que pour les appelants, c'est plus pratique de renvoyer "" à la place de null, on gère ici      
        if(complement == null) {
           return "";
        }
        return complement;
    }

    public void setComplement(String complement) {
        //CHT : 
        //this.complement = (complement == null) ? "" : complement;
        this.complement = complement;
        //this.complementInItalic = !this.complement.isEmpty();
    }

    // CHT : le rôle de l'attribut complementInItalic est d'indiquer que le complement est en italic mais ce n'est pas de sa responsabilité
    // de savoir si le complement existe
    //=> son getter isComplementInItalic() ne doit retourner que cette valeur sans autre règle de gestion
    public boolean isComplementInItalic() {
        //return complement != null && !complement.isEmpty() && complementInItalic;
       return complementInItalic;
    }

    public void setComplementInItalic(boolean complementInItalic) {
//CHT :
//        if (!this.complement.isEmpty()) {
//            this.complementInItalic = complementInItalic;
//        }
       this.complementInItalic = complementInItalic;
    }

    //CHT : même règle que pour isComplementInItalic
    public boolean isComplementOnAnotherLine() {
        //return complement != null && !complement.isEmpty() && complementOnAnotherLine;
       return complementOnAnotherLine;
    }

    public void setComplementOnAnotherLine(boolean complementOnAnotherLine) {
        this.complementOnAnotherLine = complementOnAnotherLine;
    }

    public String buildContentValue() {
       //faire une méthode dans CellContent qui ramène la concaténation
       String separateur = " ";
       if(isComplementOnAnotherLine()) {
          separateur = System.getProperty("line.separator");
       }
       if(text == null) {
          return "";
       }
       if(complement != null){
          return new StringBuilder(text).append(separateur).append(complement).toString();
       }
       return text;
       
    }


    @Override
    public String toString() {
       //CHT : il ne faut pas prendre l'habitude d'utiliser String.format() pour faire de la concaténation basique.
       //ce n'est pas fait pour ça : ce n'est pas performant dans ce cas.
       //return String.format("| %s %s |", text, complement);
       return new StringBuilder("| ").append(text).append(" ").append(complement).append(" |").toString();
    }
}

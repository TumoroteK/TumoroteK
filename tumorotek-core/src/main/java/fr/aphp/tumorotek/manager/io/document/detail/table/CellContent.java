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

    // Indique si le complément de texte doit être affiché en italique.
    private boolean complementInItalic;

    // Indique si le complément de texte doit être affiché sur une autre ligne.
    private boolean complementOnAnotherLine = false;


    //CHT :
    //les 4 méthodes ci-dessous sont des constructeurs donc elles doivent juste compléter l'initialisation faite par le constructeur sans paramètre
    //(pour les String, valeur à null et pour les booleans valeur à false
    //Il vaut mieux garder la valeur null si non défini et c'est avec le get qu'il faut renvoyer "" si null si cela est plus pratique pour les appelants
    //=> constructeurs basiques
    public CellContent(String text) {
        this.text = (text == null) ? "" : text;
        this.complement = "";
        this.complementInItalic = false;
    }

    public CellContent(String text, String complement) {
        this.text = (text == null) ? "" : text;
        this.complement = (complement == null) ? "" : complement;
        this.complementInItalic = !this.complement.isEmpty();
    }

    public CellContent(String text, String complement, boolean complementOnAnotherLine) {
        this(text, complement);
        this.complementOnAnotherLine = complementOnAnotherLine;
    }

    public CellContent(String text, String complement, boolean complementInItalic, boolean complementOnAnotherLine) {
        this.text = (text == null) ? "" : text;
        this.complement = (complement == null) ? "" : complement;
        this.complementInItalic = complementInItalic && !this.complement.isEmpty();
        this.complementOnAnotherLine = complementOnAnotherLine;
    }

    public String getText() {
        //CHT : si on considère que pour les appelants, c'est plus pratique de renvoyer "" à la place de null, on gère ici
        //ajout mis en commentaire pour ne pas impacter le reste du code pour le moment
        //if(text == null) {
        //   return "";
        //} 
        return text;
    }

    public void setText(String text) {
        //CHT : revenir au setter classique - cf commenaire sur les constructeurs
        this.text = (text == null) ? "" : text;
    }

    public String getComplement() {
        return complement;
    }

    //CHT : un setter ne doit pas gérer 2 attributs. A ce niveau il faut les considérer indépendamment
    //si besoin d'une méthode qui gère les 2 attributs, il faut en définir une méthode spécifique pour cela qui n'est pas un setter
    public void setComplement(String complement) {
        this.complement = (complement == null) ? "" : complement;
        this.complementInItalic = !this.complement.isEmpty();
    }

    // CHT : même principe que ci dessus
    // le rôle de l'attribut complementInItalic est d'indiquer que le complement est en italic mais ce n'est pas de sa responsabilité
    // de savoir si le complement existe
    //=> son getter isComplementInItalic() ne doit retourner que cette valeur sans autre règle de gestion
    public boolean isComplementInItalic() {
        return complement != null && !complement.isEmpty() && complementInItalic;
    }

    //CHT : idem setComplement
    public void setComplementInItalic(boolean complementInItalic) {
        if (!this.complement.isEmpty()) {
            this.complementInItalic = complementInItalic;
        }
    }

    //CHT : idem isComplementInItalic()
    public boolean isComplementOnAnotherLine() {
        return complement != null && !complement.isEmpty() && complementOnAnotherLine;
    }

    public void setComplementOnAnotherLine(boolean complementOnAnotherLine) {
        this.complementOnAnotherLine = complementOnAnotherLine;
    }



    @Override
    public String toString() {
        //CHT : il ne faut pas prendre l'habitude d'utiliser String.format() pour faire de la concaténation basique.
        //ce n'est pas fait pour ça : ce n'est pas performant dans ce cas. Il faut utiliser le code ci-dessous mis en commentaire pour rester sur le code d'origine
        //return new StringBuilder("| ").append(text).append(" ").append(complement).append(" |").toString();
        return String.format("| %s %s |", text, complement);
    }
}

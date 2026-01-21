/**
 * Copyright ou © ou Copr. SESAN
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
package fr.aphp.tumorotek.manager.exception;

/**
 * Cette classe permet de gérer un message internationalisé avec des paramètres, comme {@link TKBasicRuntimeException}, mais 
 * avec un autre format d'affichage pour l'utilisateur (utilisation du logo warning au lieu d'erreur),
 * via un catch spécifique. 
 * Toutefois, cette classe n'hérite pas de {@link TKBasicRuntimeException} qui elle même hérite de RuntimeException car 
 * on souhaite qu'elle puisse être lancée dans une méthode gérant une transaction avec la base de données 
 * <b>sans</b> entrainer un rollback
 * A noter que cette classe ne va pas jusqu'à AbstractController.handleExceptionMessage()
 * car il aurait fallu modifier la signature d'une méthode utilisée par héritage dans de nombreuses classes
 * (Or TK ne travaille jusqu'à présent qu'avec des RuntimException donc il y a peu de throws au niveau des signatures)
 * TKWarningException est donc transformée en {@link TKWarningRuntimeException} dès qu'elle est catchée par la couche web (où il n'y a plus 
 * de risque de rollback)
 * @author chuet
 * @since 2.3.1.0 (TK-817)
 */
public class TKWarningException extends Exception
{
   private static final long serialVersionUID = -5436964792231689991L;

   private Object[] messageParams;
   
   public TKWarningException(){
      super();
   }
   
   public TKWarningException(String message){
      super(message);
   }
   
   public TKWarningException(String message, Object[] messageParams){
      this(message);
      this.messageParams=messageParams;
   }
   
   public Object[] getMessageParams(){
      return messageParams;
   }

   public boolean hasParam() {
      return messageParams != null && messageParams.length > 0;
   }
}

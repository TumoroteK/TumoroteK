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
 * Classe Exception basique qui permet de gérer un message internationalisé
 * A noter que cette classe aurait dû s'appeler TKException (voire TKRuntimeException) et être la classe parent de toutes les classes Exception de l'application...
 * car la TKException gère le cas spécifique où un seul paramètre est passé : cas d'une exception portant sur un 
 * seul objet dont la référence doit être transmise à l'utilisateur
 * @author chuet
 * @since 2.3.1.0 (TK-817)
 */
public class TKBasicRuntimeException extends TKException
{

   /**
    * 
    */
   private static final long serialVersionUID = -5436964792231689991L;
   
   private Object[] messageParams;

   public TKBasicRuntimeException(){
      super();
   }
   
   /**
    * si le message correspond à la clé d'un message internationalisé, les paramètres doivent commencer à 0
    * contrairement au message dans TKException où le param doit avoir l'indice 1 ce qui n'est pas habituel ... 
    * @param message
    */
   public TKBasicRuntimeException(String message){
      super(message);
   }
   
   public TKBasicRuntimeException(String message, Object[] messageParams){
      this(message);
      this.messageParams = messageParams;
   }

   @Override
   public String getMessage(){
      // /!\ cette methode doit renvoyer l'attribut message et non passer par le traitement de la méthode getMessage() du parent
      return message;
   }
   
   public Object[] getMessageParams(){
      return messageParams;
   }

   protected void setMessageParams(Object[] messageParams){
      this.messageParams = messageParams;
   }
   
   public boolean hasParam() {
      return messageParams != null && messageParams.length > 0;
   }



}

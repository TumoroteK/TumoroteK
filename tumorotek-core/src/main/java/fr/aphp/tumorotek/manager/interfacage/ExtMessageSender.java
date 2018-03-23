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
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.aphp.tumorotek.manager.interfacage;

import java.util.List;

import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.interfacage.Recepteur;

/**
 * SuperInterface pour tous les beans envoyant des messages de transmission
 * de création/modifications de données dans TK (prélèvements)
 * Date: 08/10/2014
 *
 * @author mathieu BARTHELEMY
 * @version 2.0.10.3
 *
 */
public interface ExtMessageSender
{

   /**
    * Production et envoi d'un message à partir d'un objet TK.
    * @param obj qui sert de base à la création du message
    * @param dosExtId id du dossier ext (peut être diff du dossier TK)
    * @param url d'accès à la ressource dans TK
    */
   void sendMessage(TKAnnotableObject obj, String dosExtId, String url);

   /**
    * Production et envoi d'un ou plusieurs messages à partir d'une liste d'objets TK.	 
    * @param objs liste de'objets qui serviront de base à la création de(s) message(s).
    * @param b nombre d'objets à grouper par message
    */
   void sendMessages(List<TKAnnotableObject> objs, Integer b);

   /**
    * Formatage du nom de fichier à partir de l'obj. 
    * @param obj
    * @param si true, correspond au fichier .ok associé au fichier envoyé
    * @param part si non null, suffixe par l'indice message lors de l'envoi plusieurs 
    * @param date de production du message au bon format 
    * objs groupés
    * @return String filename
    */
   String setFileName(TKAnnotableObject obj, boolean isOkFile, Integer part, String currtime);

   /**
    * Renvoie true si le sender est compatible au récepteur
    * @param r Recepteur
    * @return true/false
    */
   boolean useRecepteur(Recepteur r);
}

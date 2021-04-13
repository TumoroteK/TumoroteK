/************************************************************************/
/* Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)	*/
/*																		*/
/* dsi-projet.tk@aphp.fr    											*/
/* 																		*/
/* Ce logiciel est un programme informatique servant à la gestion de 	*/
/* l'activité de biobanques. 											*/
/*																		*/
/* Ce logiciel est régi par la licence CeCILL soumise au droit français */
/* et respectant les principes de diffusion des logiciels libres. Vous  */
/* pouvez utiliser, modifier et/ou redistribuer ce programme sous les 	*/
/* conditions de la licence CeCILL telle que diffusée par le CEA, le 	*/
/* CNRS et l'INRIA sur le site "http://www.cecill.info". 				*/
/*																		*/
/* En contrepartie de l'accessibilité au code source et des droits de   */
/* copie, de modification et de redistribution accordés par cette 		*/
/* licence, il n'est offert aux utilisateurs qu'une garantie limitée.   */
/* Pour les mêmes raisons, seule une responsabilité restreinte pèse sur */
/* l'auteur du programme, le titulaire des droits patrimoniaux et les 	*/
/* concédants successifs. 												*/
/*																		*/
/* A cet égard  l'attention de l'utilisateur est attirée sur les 		*/
/* risques associés au chargement,  à l'utilisation,  à la modification */
/* et/ou au  développement et à la reproduction du logiciel par 		*/
/* l'utilisateur étant donné sa spécificité de logiciel libre, qui peut */
/* le rendre complexe à manipuler et qui le réserve donc à des 			*/
/* développeurs et des professionnels  avertis possédant  des  			*/
/* connaissances  informatiques approfondies.  Les utilisateurs sont 	*/
/* donc invités à charger  et  tester  l'adéquation  du logiciel à leurs*/
/* besoins dans des conditions permettant d'assurer la sécurité de leurs*/
/* systèmes et ou de leurs données et, plus généralement, à l'utiliser  */
/* et l'exploiter dans les mêmes conditions de sécurité. 				*/
/*																		*/
/* Le fait que vous puissiez accéder à cet en-tête signifie que vous 	*/
/* avez pris connaissance de la licence CeCILL, et que vous en avez 	*/
/* accepté les termes. 													*/
/************************************************************************/
<%@ page contentType="text/css;charset=UTF-8" %> 
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %> 

.orange {
	background-color: #FD9B0F;
}

.font-orange {
	color: #FD9B0F;
}

.red {
	background-color: #D9534F;
}

.green {
	background-color: #5CB85C;
}

.buttonBsDiv {
	#color: #214584;
	color: #FFFFFF; 
	padding: 7px; 
	margin: 0; 
	border-style : solid;
	border-width: 1px;
	border-color: #FFFFFF;
	# line-height: 15px;
	border-radius: 5px 5px 5px 5px;
	cursor: pointer; 
}

.buttonBsDiv:hover {
	border-radius: 5px 5px 5px 5px;
	border-style : solid;
	border-width: 2px;
	border-color: #FFFFFF;
	padding: 7px; 
	cursor: pointer; 
}

.buttonBsDiv .buttonLabel {
	font-size: 14px; 
	text-decoration: inherited;
	font-weight: bold;
		// font-family: Tahoma; 
	font-family: proxima-nova,"Helvetica Neue",Helvetica,Arial,sans-serif;
	line-height: 1.3333333;
	color: #FFFFFF
}

.buttonBsDiv:hover .buttonLabel {
	background: none;
	#text-decoration: underline;  
}

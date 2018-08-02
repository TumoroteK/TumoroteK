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

.formGroupLabel {
	font-weight: bold;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formLabel {
	font-weight: bold;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.incaLabel {
	font-weight: bold;
	color : #3E79C6;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formValue {
	font-style : normal;
	font-weight : normal;
	text-decoration : none;
	color : #003399;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formValueEmph {
	font-style : normal;
	font-weight : bold;
	text-decoration : none;
	color : #FF0000;
	background-color : #FFFF00; 
	font-family: Verdana, Arial, Helvetica, sans-serif;
}


.formErrorValue {
	font-style : italic;
	color : #FF0000;
	font-weight : normal;
	text-decoration : none;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formArchiveValue {
	font-style : italic;
	color : #7F7F7F;
	font-weight : normal;
	text-decoration : none;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formValueItalics {
	font-style : italic;
	font-weight : normal;
	text-decoration : none;
	color : #003399;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formSubTitleNormal {
	font-style : normal;
	font-weight : normal;
	text-decoration : none;
	color : #00227c;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formSubTitle {
	font-style : normal;
	font-weight : bold;
	text-decoration : none;
	color : #00227c;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.requiredMark {
	font-weight: bold;
	color: #FF0000;
}

.formBlueLabel {
	font-weight: bold;
	color : #003399;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formLink {
	color : #003399;
	font-style : normal;
	font-weight : bold;
	text-decoration : none;
	cursor:pointer;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formLink:hover {
	color : #003399;
	font-style : normal;
	font-weight : bold;
	text-decoration : underline;
	cursor:pointer;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formAnonymeValue {
	color : #FFFFFF; 
	background-color : #000000; 
	font-style : normal;
	font-weight : bold;
	text-decoration : none;
	cursor:pointer;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formAnonymeValue:hover {
	color : #FFFFFF; 
	background-color : #000000; 
	font-style : normal;
	font-weight : bold;
	text-decoration : underline;
	cursor:pointer;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formLink-disabled {
	color : #7F7F7F;
	font-style : italic;
	font-weight : normal;
	text-decoration : none;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formLinkArchive {
	color : #7F7F7F;;
	font-style : italic;
	font-weight : bold;
	text-decoration : none;
	cursor:pointer;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formLinkArchive:hover {
	color : #7F7F7F;;
	font-style : italic;
	font-weight : bold;
	text-decoration : underline;
	cursor:pointer;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.formLinkArchive-disabled {
	color : #7F7F7F;;
	font-style : italic;
	font-weight : normal;
	text-decoration : none;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.buttonDsb.z-button {
	text-decoration: none;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

.buttonDsb.z-button .z-button-tl{
	background-image : none; /*PRO*/
	background-position : top left;
	border-top-style : none;
	background:transparent;
}

.buttonDsb.z-button .z-button-tm{
	background-image : none; /*PRO*/
	background-position : top center;
	background:transparent;
}

.buttonDsb.z-button .z-button-tr{
	background-image : none; /*PRO*/
	background-position : top right;
	background:transparent;
}

.buttonDsb.z-button .z-button-cl{
	background-image : none; /*PRO*/
	background-position : center left;
	background:transparent;
}

.buttonDsb.z-button .z-button-cm{
	background-image : none; /*PRO*/
	background-position : center center;
	color : #00227c; /*PRO*/
}

.buttonDsb.z-button .z-button-cr{
	background-image : none; /*PRO*/
	background-position : center right;
	background:transparent;
}

.buttonDsb.z-button .z-button-bl{
	background-image : none; /*PRO*/
	background-position : bottom left;
	background:transparent;
}

.buttonDsb.z-button .z-button-bm{
	background-image : none; /*PRO*/
	background-position : bottom center;
	background:transparent;
}

.buttonDsb.z-button .z-button-br{
	background-image : none; /*PRO*/
	background-position : bottom right;
	background:transparent;
}

.vert {
	
	color: #00CC00;
	background-color: #00CC00;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.rouge {
	color: #CC3300;
	background-color: #CC3300;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.bleu {
	color: #3333CC;
	background-color: #3333CC;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.jaune {
	color: #FFFF00;
	background-color: #FFFF00;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.orange {
	color: #FF6600;
	background-color: #FF6600;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.noir {
	color: #000000;
	background-color: #000000;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.gris {
	color: #CCCCCC;
	background-color: #CCCCCC;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.cyan {
	color: #00CCFF;
	background-color: #00CCFF;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.magenta {
	color: #9900FF;
	background-color: #9900FF;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.saumon {
	color: #FFCC99;
	background-color: #FFCC99;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.transparent {
	color: #FFFFFF;
	background-color: #FFFFFF;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.marron {
	color: #582900;
	background-color: #582900;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.parme {
	color: #CFA0E9;
	background-color: #CFA0E9;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.rose {
	color: #FD6C9E;
	background-color: #FD6C9E;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.pistache {
	color: #BEF574;
	background-color: #BEF574;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.patientTk {
	color: #BEFF96;
	background-color: #BEFF96;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.patientSip {
	color: #e2e9fe;
	background-color: #e2e9fe;
	text-decoration : none;
	border: 1;
	border-style: solid;
	border-width: 1px;
	border-color: #000000;
}

.upArrow {
	background: url(${c:encodeURL('/images/icones/uparrow.png')});
	cursor:pointer;
}

.downArrow {
	background: url(${c:encodeURL('/images/icones/downarrow.png')});
	cursor:pointer;
}

.gridEdit {
	background: url(${c:encodeURL('/images/icones/small_edit12.png')});
	cursor:pointer;	
}

.gridEditDsb {
	background: url(${c:encodeURL('/images/icones/small_edit12_dsb.png')});
}

.gridDelete {
	background: url(${c:encodeURL('/images/icones/small_delete12.png')});
	cursor:pointer;
}

.gridValidate {
	background: url(${c:encodeURL('/images/icones/small_validate12.png')});
	cursor:pointer;
}

.gridCancel {
	background: url(${c:encodeURL('/images/icones/small_cancel12.png')});
	cursor:pointer;
}

.biohazard {
	background: url(${c:encodeURL('/images/icones/biohazard.png')});
}

.nonConformeArrivee {
	background: url(${c:encodeURL('/images/icones/non_conforme_arrivee.png')});
}

.nonConformeTraitement {
	background: url(${c:encodeURL('/images/icones/non_conforme_traitement.png')});
}

.nonConformeCession {
	background: url(${c:encodeURL('/images/icones/non_conforme_cession.png')});
}

.conformeArrivee {
	background: url(${c:encodeURL('/images/icones/conforme_arrivee.png')});
}

.conformeTraitement {
	background: url(${c:encodeURL('/images/icones/conforme_traitement.png')});
}

.conformeCession {
	background: url(${c:encodeURL('/images/icones/conforme_cession.png')});
}

tr.z-group.grpInca .z-group-inner .z-group-cnt {
	background: url(${c:encodeURL('/images/icones/catalogues/inca.gif')});
	background-position: center right;
	background-repeat: no-repeat;
}

div.z-north {
	border-bottom-style: none;
	border-bottom-width: 0px;
}

li.z-menu-item.exports a.z-menu-item-cnt span.z-menu-item-img {
	width: 30px;
}

div.z-menu-popup.exportsMenu {
	background: url(${c:encodeURL('/images/pp-bg_large.png')});
	background-repeat: y;
}

.dossierInbox {
	background: url(${c:encodeURL('/images/icones/dossier_inbox.png')});
}

div.impact {
	background: url(${c:encodeURL('/images/icones/flag.png')});
}

div.inca {
	background: url(${c:encodeURL('/images/icones/catalogues/inca.gif')});
}

div.tvgso {
	background: url(${c:encodeURL('/images/icones/catalogues/tvgso.gif')});
}

div.biocap {
	background: url(${c:encodeURL('/images/icones/catalogues/biocap.gif')});
}

div.bto {
	background: url(${c:encodeURL('/images/icones/catalogues/bto.gif')});
}

div.pelican {
	background: url(${c:encodeURL('/images/icones/catalogues/pelican.png')});
}

tr.z-group.grpPelican .z-group-inner .z-group-cnt {
	background: url(${c:encodeURL('/images/icones/catalogues/pelican_small.png')});
	background-position: center right;
	background-repeat: no-repeat;
}

div.mns {
	background: url(${c:encodeURL('/images/portail/ministere-sante.jpg')});
	cursor:pointer;
}

div.zkpow {
	background: url(${c:encodeURL('/images/portail/zkpowered_l.png')});
	cursor:pointer;
}

div.bulb {
	background: url(${c:encodeURL('/images/icones/bulb.png')});
	cursor: help;
}
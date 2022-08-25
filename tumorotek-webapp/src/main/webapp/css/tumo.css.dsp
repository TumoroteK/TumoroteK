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

@CHARSET "UTF-8";/*SEC:GRID:gridForm1*/
/*RULE:ROW*/
div.z-grid.gridForm1 tr.z-row td.z-row-inner{
	border-color : #FFFFFF; /*PRO*/
	background-color : #FFFFFF; /*PRO*/
}
tr.z-row-over.gridForm1 > td.z-row-inner, tr.z-row-over > .z-cell {
	background-image: none;
}
/*RULE_E*//*RULE:GRID-DEFAULT*/
div.z-grid.gridForm1{
	border-style : none; /*PRO*/
}
/*RULE_E*//*RULE:GROUP*/
div.z-grid.gridForm1 .z-group-inner .z-group-cnt span, div.z-grid.gridForm1 .z-group-inner .z-group-cnt{
	color : #00227c; /*PRO*/
	font-weight : bold; /*PRO*/
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

div.z-grid.gridForm1 .z-group-dsd .z-group-inner .z-group-cnt span, div.z-grid.gridForm1 .z-group .z-group-inner .z-group-cnt{
	color : #7F7F7F; 
	font-weight : normal; 
	font-style: italics;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

div.z-grid.gridForm1 .z-group-dsd .z-group-inner{
	border-bottom-style : solid; 
	border-bottom-width : 1px;
	border-bottom-color : #7F7F7F;
}

div.z-grid.gridForm1 .z-group-inner{	
	border-bottom-width : 2px; /*PRO*/
	border-bottom-color : #E03F06; /*PRO*/
	border-top-style : none; /*PRO*/
	border-right-style : none; /*PRO*/
	border-left-style : none; /*PRO*/
	border-top-width : 1px; /*PRO*/
	border-top-color : #E03F06; /*PRO*/
}
/*RULE_E*//*RULE:ODDROW*/
div.z-grid.gridForm1 tr.z-grid-odd td.z-row-inner{
	background-color : #FFFFFF; /*PRO*/
}

div.z-grid.gridForm1 tr.z-grid-odd .z-cell {
	background-color : #FFFFFF; /*PRO*/
	border: none;
}

/*RULE_E*/
/*SEC_E*//*SEC:GRID:gridError1*/
/*RULE:GRID-DEFAULT*/
div.z-grid.gridError1{
	border-color : #FF0000; /*PRO*/
}
/*RULE_E*//*RULE:ROW*/
div.z-grid.gridError1 tr.z-row td.z-row-inner{
	background-color : #FFFFFF; /*PRO*/
	border-color : #FFFFFF; /*PRO*/
}
/*RULE_E*//*RULE:ODDROW*/
div.z-grid.gridError1 tr.z-grid-odd td.z-row-inner{
	background-color : #FFFFFF; /*PRO*/
}
/*RULE_E*//*RULE:GROUP*/
div.z-grid.gridError1 .z-group-inner .z-group-cnt span, div.z-grid.gridError1 .z-group-inner .z-group-cnt{
	color : #FF0000; /*PRO*/
	font-weight : bold; /*PRO*/
}
div.z-grid.gridError1 .z-group{
	background-color : #FF0000; /*PRO*/
}
div.z-grid.gridError1 .z-group-inner{
	border-color : #FF0000; /*PRO*/
}
/*RULE_E*//*RULE:DETAIL*/
div.z-grid.gridError1 .z-row.z-detail-faker{
	color : #FF0000; /*PRO*/
	font-style : normal; /*PRO*/
}
/*RULE_E*/
/*SEC_E*//*SEC:TOOLBAR-DEFAULT:toolbarError1*/
/*RULE:TOOLBAR-DEFAULT*/
.toolbarError1.z-toolbar {
	background-color : #FF0000;  /*PRO*/
	border-color : #FF0000;  /*PRO*/
}
	
/*RULE_E*/
/*SEC_E*//*SEC:POPUP:popupError*/
/*RULE:TOP-LEFT*/
.popupError.z-popup .z-popup-tl{
	background-color : #FFC0CB; /*PRO*/
}
/*RULE_E*//*RULE:TOP-RIGHT*/
.popupError.z-popup .z-popup-tr{
	background-color : #FFC0CB; /*PRO*/
}
/*RULE_E*//*RULE:CENTER-LEFT*/
.popupError.z-popup .z-popup-cl{
	background-color : #FFC0CB; /*PRO*/
}
/*RULE_E*//*RULE:CENTER-MIDDLE*/
.popupError.z-popup .z-popup-cm{
	background-color : #FFC0CB; /*PRO*/
}
/*RULE_E*//*RULE:CENTER-RIGHT*/
.popupError.z-popup .z-popup-cr{
	background-color : #FFC0CB; /*PRO*/
}
/*RULE_E*//*RULE:BOTTOM-LEFT*/
.popupError.z-popup .z-popup-bl{
	background-color : #FFC0CB; /*PRO*/
}
/*RULE_E*//*RULE:BOTTOM-MIDDLE*/
.popupError.z-popup .z-popup-bm{
	background-color : #FFC0CB; /*PRO*/
}
/*RULE_E*//*RULE:BOTTOM-RIGHT*/
.popupError.z-popup .z-popup-br{
	background-color : #FFC0CB; /*PRO*/
}
/*RULE_E*//*RULE:CONTENT-FONT*/
.popupError.z-popup .z-popup-cnt .z-label{
	font-style : normal; /*PRO*/
	font-weight : bold; /*PRO*/
}
/*RULE_E*/
/*SEC_E*//*SEC:LABEL:myname1*/
/*RULE:BASIC*/
.lien1.z-label {
	font-family : arial; /*PRO*/
	color : #003399; /*PRO*/
	font-size : 8pt; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bold; /*PRO*/
	text-decoration : none; /*PRO*/
}

.lien1.z-label:hover {
	font-family : arial; /*PRO*/
	color : #003399; /*PRO*/
	font-size : 8pt; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bold; /*PRO*/
	text-decoration : underline; /*PRO*/
}
/*RULE_E*/
/*SEC_E*//*SEC:TABBOX-DEFAULT:mainTabBox*/
/*RULE:TABS-BODY*/
.mainTabBox.z-tabbox .z-tabs.z-tabs-scroll {
	border-right-style : solid; /*PRO*/
	border-right-width : thin; /*PRO*/
	border-right-color : #7F7F7F; /*PRO*/
	border-bottom-style : none; /*PRO*/
	border-bottom-width : thin; /*PRO*/
	border-bottom-color : #FFFFFF; /*PRO*/
	border-left-style : solid; /*PRO*/
	border-left-width : thin; /*PRO*/
	border-left-color : #7F7F7F; /*PRO*/
	border-top-style: none;
}
.mainTabBox.z-tabbox .z-tabs.z-tabs-scroll .z-tabs-cnt{
	border-bottom-style : solid; /*PRO:zk-tabs-cnt-border-style*/
	border-bottom-width : 3px; /*PRO:zk-tabs-cnt-border-width*/
	border-bottom-color : #EB3700; /*PRO:zk-tabs-cnt-border-color*/
	background-image : url(${c:encodeURL('/images/themes/blue/downTumoBanniere.png')}); /*PRO*/
	background-repeat: no-repeat;
	background-color: #74A5E8;
	background-position : 0px 0px;
}
	
/*RULE_E*//*RULE:TABS-FONT*/
.mainTabBox.z-tabbox .z-tab .z-tab-text{
	color : #FFFFFF; /*PRO*/
	font-size : 12px; /*PRO*/
	font-weight : bold; /*PRO*/
	font-style : normal; /*PRO*/
}
/*RULE_E*//*RULE:TABS-SELD-FONT*/
.mainTabBox.z-tabbox .z-tab.z-tab-seld .z-tab-text{
	color : #FFFFFF; /*PRO*/
	font-size : 12px; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bolder; /*PRO*/
}
.mainTabBox.z-tabbox .z-tab.z-tab-disd .z-tab-text{
	color : #CCC; /*PRO*/
	font-size : 12px; /*PRO*/
	font-weight : bold; /*PRO*/
	font-style : normal; /*PRO*/
}
/*RULE_E*//*RULE:PANELS-BODY*/
.mainTabBox.z-tabbox .z-tabpanels .z-tabpanel{
	border-right-style : solid; /*PRO*/
	border-right-width : 1px; /*PRO*/
	border-right-color : #00227c; /*PRO*/
	border-left-style : solid; /*PRO*/
	border-left-width : 1px; /*PRO*/
	border-left-color : #00227c; /*PRO*/
}
.mainTabBox.z-tabbox .z-tab-hl {
	background-image : url(${c:encodeURL('/images/themes/blue/tab-corner.png')});
}
.mainTabBox.z-tabbox .z-tab-hr {
	background-image : url(${c:encodeURL('/images/themes/blue/tab-corner.png')});
}
.mainTabBox.z-tabbox .z-tab-hm {
	background-image : url(${c:encodeURL('/images/themes/blue/tab-corner-big.png')});
}
/*RULE_E*/
/*SEC_E*/

/*SEC:WINDOW-EMBEDDED:topWindow*/
/*RULE:BORDER*/
.topWindow.z-window-embedded {
	border-top-style : solid; /*PRO*/
	border-top-width : 1px; /*PRO*/
	border-top-color : #7F7F7F; /*PRO*/
	border-right-style : solid; /*PRO*/
	border-right-width : 1px; /*PRO*/
	border-right-color : #7F7F7F; /*PRO*/
	border-left-style : solid; /*PRO*/
	border-left-width : 1px; /*PRO*/
	border-left-color : #7F7F7F; /*PRO*/
}

/*RULE_E*/
/*SEC_E*//*SEC:BORDERLAYOUT:mainBorderLayout*/
/*RULE:NORTH*/
.mainBorderLayout .z-north{
	background-color : #EDEDFA; /*PRO*/
	border-top-style : solid; /*PRO*/
	border-top-color : #7F7F7F; /*PRO*/
	border-left-style : solid; /*PRO*/
	border-left-color : #7F7F7F; /*PRO*/
	border-right-style : solid; /*PRO*/
	border-right-color : #7F7F7F; /*PRO*/
	border-bottom : 0px; /*PRO*/
	padding:0px;
	/* border-bottom-color : #6ca1e6; /*PRO*/
}
/*RULE_E*//*RULE:WEST*/
.tabBorderLayout .z-west{
	border-color : #00227c; /*PRO*/
	background-color : #FFFFFF; /*PRO*/
	background-image : none;
}
/*RULE_E*//*RULE:WEST-HEADER*/
.tabBorderLayout .z-west-header{
	color : #FFFFFF; /*PRO*/
	font-size : 13px; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bold; /*PRO*/
	border-bottom-style : solid; /*PRO*/
	border-bottom-color : #00227c; /*PRO*/
	font-family: Verdana, Arial, Helvetica, sans-serif;
	background-image : url(${c:encodeURL('/images/themes/blue/borderlayout-hm.png')});
}
/*RULE_E*//*RULE:CENTER*/
.tabBorderLayout .z-center{
	border-style : solid; /*PRO*/
	border-color : #00227c; /*PRO*/
	border-top-style : none;
}
/*RULE_E*//*RULE:WEST-COLLAPSED*/
.tabBorderLayout .z-west-colpsd{
	border-size : 3px; /*PRO*/
	right : 0px; /*PRO*/
	width : 22px; /*PRO*/
	height : 22px; /*PRO*/
	z-index : 0; /*PRO*/
	border : 1px solid  #00227c; /*PRO*/
	overflow : hidden; /*PRO*/
	position : absolute; /*PRO*/
	background-image : url(${c:encodeURL('/images/themes/blue/bigSplt-h.png')}); /*PRO*/
	background-repeat : repeat-x repeat-y; /*PRO*/
}

/*RULE_E*//*RULE:EAST*/
.tabBorderLayout .z-east{
	border-color : #00227c; /*PRO*/
	background-color : #FFFFFF; /*PRO*/
	background-image : none;
}
/*RULE_E*//*RULE:EAST-COLLAPSED*/
.tabBorderLayout .z-east-colpsd{
	border : 1px solid  #00227c; /*PRO*/
	background-image : url(${c:encodeURL('/images/themes/blue/bigSplt-h.png')}); /*PRO*/
	background-repeat : repeat-x repeat-y; /*PRO*/
}
/*RULE_E*//*RULE:EAST-SPLIT*/
.tabBorderLayout .z-east-splt{
	background-image : url(${c:encodeURL('/images/themes/blue/splt-h.png')}); /*PRO*/
}
/*RULE_E*//*RULE:EAST-HEADER*/
.tabBorderLayout .z-east-header{
	color : #FFFFFF; /*PRO*/
	font-size : 13px; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bold; /*PRO*/
	border-bottom-style : solid; /*PRO*/
	border-bottom-color : #00227c; /*PRO*/
	background-image : url(${c:encodeURL('/images/themes/blue/borderlayout-hm.png')});
}
/*RULE_E*//*RULE:EAST-BODY*/
.tabBorderLayout .z-east-body{
	zoom : 1; /*PRO:background-color:#FFFFFF*/
	background-color : #FFFFFF; /*PRO*/
}
/*RULE_E*//*RULE:WEST-BODY*/
.tabBorderLayout .z-west-body{
	zoom : 1; /*PRO:background-color:#FFFFFF*/
	background-color : #FFFFFF; /*PRO*/
}
/*RULE_E*//*RULE:BASIC*/
.tabBorderLayout {
	background-color : #FFFFFF; /*PRO*/
}
/*RULE_E*//*RULE:WEST-SPLIT*/
.tabBorderLayout .z-west-splt{
	background-image : url(${c:encodeURL('/images/themes/blue/splt-h.png')}); /*PRO*/
}
/*RULE_E*//*RULE:SOUTH*/
.tabBorderLayout .z-south{
	border-color : #00227c; /*PRO*/
	background-color : #FFFFFF; /*PRO*/
	background-image : none; /*PRO*/
}
/*RULE_E*//*RULE:SOUTH-COLLAPSED*/
.tabBorderLayout .z-south-colpsd{
	border : 1px solid  #00227c; /*PRO*/
	background-image : url(${c:encodeURL('/images/themes/blue/bigSplt-h.png')}); /*PRO*/
	background-repeat : repeat-x repeat-y; /*PRO*/
}
/*RULE_E*//*RULE:SOUTH-HEADER*/
.tabBorderLayout .z-south-header{
	color : #FFFFFF; /*PRO*/
	font-size : 13px; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bold; /*PRO*/
	border-bottom-style : solid; /*PRO*/
	border-bottom-color : #00227c; /*PRO*/
	background-image : url(${c:encodeURL('/images/themes/blue/borderlayout-hm.png')});
}
/*RULE_E*//*RULE:SOUTH-BODY*/
.tabBorderLayout .z-south-body{
	zoom : 1; /*PRO:background-color:#FFFFFF*/
	background-color : #FFFFFF; /*PRO*/
}
/*RULE_E*/
/*SEC_E*//*SEC:PANEL-NON-FRAME:panelv2*/
/*RULE:HEADER*/
.panelv2.z-panel .z-panel-header {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	color : #00227c; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bold; /*PRO*/
	border-style : none; /*PRO*/
	background-image : none; /*PRO*/
	background-color : #FFFFFF; /*PRO*/
	background-position : 0px 0px;
	padding: 4px 3px 4px 5px;
}
/*RULE_E*//*RULE:CHILDREN*/
.panelv2.z-panel .z-panel-tl
, .panelv2.z-panel .z-panel-tr
, .panelv2.z-panel .z-panel-hl
, .panelv2.z-panel .z-panel-hr {
	background-image : none; /*PRO*/
	background-color : #FFFFFF; /*PRO*/
}
/*RULE_E*//*RULE:CHILDREN*/
.panelv2.z-panel .z-panel-children {
	background-color : #FFFFFF; /*PRO*/
	background-image : none; /*PRO*/
	background-position : 0px 0px;
	border-top-style : solid; /*PRO*/
	border-top-width : 1px; /*PRO*/
	border-top-color : #C7D4FA; /*PRO*/
	border-bottom-style : none; /*PRO*/
	border-bottom-width : 1px; /*PRO*/
	border-bottom-color : #00227c; /*PRO*/
}
/*RULE_E*//*RULE:BORDER*/
.panelv2.z-panel  {
	border-style : none; /*PRO*/
	border-width : thin; /*PRO*/
	border-color : #00227c; /*PRO*/
	border-top-style : ridge; /*PRO*/
	border-top-width : 1px; /*PRO*/
	border-top-color : #00227c; /*PRO*/
}
/*RULE_E*//*RULE:TBAR*/
.panelv2.z-panel .z-panel-top .z-toolbar ,.panelv2.z-panel .z-panel-top .z-toolbar span {
	font-family : sans-serif; /*PRO*/
	color : #00227c; /*PRO*/
	font-size : 10px; /*PRO*/
	font-style : normal; /*PRO*/
	text-decoration : underline; /*PRO*/
	background-color : #E6E6FA; /*PRO*/
	background-image : none; /*PRO*/
	background-position : 0px 0px;
}
	
	
.panelv2.z-panel .z-panel-tbar .z-toolbar ,.panelv2.z-panel .z-panel-tbar .z-toolbar span {
	font-family : sans-serif; /*PRO*/
	color : #00227c; /*PRO*/
	font-size : 10px; /*PRO*/
	font-style : normal; /*PRO*/
	text-decoration : underline; /*PRO*/
	background-color : #E6E6FA; /*PRO*/
	background-image : none; /*PRO*/
	background-position : 0px 0px;
}
/*RULE_E*//*RULE:BBAR*/

/*RULE_E*/
/*SEC_E*//*SEC:PANEL-NON-FRAME:fichePanelv2*/
/*RULE:HEADER*/
.fichePanelv2.z-panel .z-panel-header {
	color : #FFFFFF; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bold; /*PRO*/
	font-size : 13px; /*PRO*/
	font-family: Verdana, Arial, Helvetica, sans-serif;
	border-bottom-style : solid; /*PRO*/
	border-bottom-color : #FFFFFF; /*PRO*/
	padding: 0px 0px 4px 0px;
}
/*RULE_E*//*RULE:BORDER*/
.fichePanelv2.z-panel  {
	border-style : none; /*PRO*/
	border-width : thin; /*PRO*/
	border-color : #00227c; /*PRO*/
}
.fichePanelv2.z-panel .z-panel-hm  {
	background-image : url(${c:encodeURL('/images/themes/blue/panel-hm.png')});
}
.fichePanelv2.z-panel .z-panel-hl  {
	background-image : url(${c:encodeURL('/images/themes/blue/panel-hl.png')});
}
.fichePanelv2.z-panel .z-panel-hr  {
	background-image : url(${c:encodeURL('/images/themes/blue/panel-hr.png')});
}
.fichePanelv2.z-panel .z-panel-tl  {
	background-image : url(${c:encodeURL('/images/themes/blue/panel-corner.png')});
}
.fichePanelv2.z-panel .z-panel-tr  {
	background-image : url(${c:encodeURL('/images/themes/blue/panel-corner.png')});
}
/*RULE_E*//*RULE:CHILDREN*/
.fichePanelv2.z-panel .z-panel-children {
	background-image : none; /*PRO*/
	background-position : 0px 0px;
	border-top-style : solid; /*PRO*/
	border-top-width : 2px; /*PRO*/
	border-top-color : #C7D4FA; /*PRO*/
}
/*RULE_E*//*RULE:BBAR*/
.fichePanelv2.z-panel .z-panel-btm .z-toolbar , .fichePanelv2.z-panel .z-panel-btm .z-toolbar span {
	font-family : sans-serif; /*PRO*/
	color : #00227c; /*PRO*/
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size : 10px; /*PRO*/
	font-style : normal; /*PRO*/
	text-decoration : none; /*PRO*/
	border-top-style : none; /*PRO*/
	border-bottom-style : none; /*PRO*/
	border-bottom-width : thin; /*PRO*/
	border-bottom-color : #00227c; /*PRO*/
}
.fichePanelv2.z-panel .z-panel-close {
	background-image : url(${c:encodeURL('/images/fichePanelv2-icon.png')});
}
.fichePanelv2.z-panel .z-panel-max {
	background-image : url(${c:encodeURL('/images/fichePanelv2-icon.png')});
}
.fichePanelv2.z-panel .z-panel-maxd {
	background-image : url(${c:encodeURL('/images/fichePanelv2-icon.png')});
}
.fichePanelv2.z-panel .z-panel-bbar .z-toolbar , .fichePanelv2.z-panel .z-panel-bbar .z-toolbar span {
	font-family : sans-serif; /*PRO*/
	color : #00227c; /*PRO*/
	font-size : 10px; /*PRO*/
	font-style : normal; /*PRO*/
	font-family: Verdana, Arial, Helvetica, sans-serif;
	text-decoration : underline; /*PRO*/
	border-top-style : ridge; /*PRO*/
	border-top-width : 10px; /*PRO*/
	border-top-color : #00227c; /*PRO*/
	border-bottom-style : none; /*PRO*/
	border-bottom-width : thin; /*PRO*/
	border-bottom-color : #00227c; /*PRO*/
}
/*RULE_E*/
/*SEC_E*//*SEC:PANEL-NON-FRAME:fichePanelv2Embedded*/
/*RULE:HEADER*/
.fichePanelv2Embedded.z-panel .z-panel-header {
	color : #00227c; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bold; /*PRO*/
	font-size : 12px; /*PRO*/
	font-family: Verdana, Arial, Helvetica, sans-serif;
	border-bottom-style : solid; /*PRO*/
	border-bottom-color : #FFFFFF; /*PRO*/
	background-image : none; /*PRO*/
	background-position : 0px 0px;
	background-color : #FFFFFF; /*PRO*/
	padding-left: 0px;
}

.fichePanelv2Embedded.z-panel .z-panel-tl
, .fichePanelv2Embedded.z-panel .z-panel-tr
, .fichePanelv2Embedded.z-panel .z-panel-hl
, .fichePanelv2Embedded.z-panel .z-panel-hr {
	background-image : none; /*PRO*/
	background-color : #FFFFFF; /*PRO*/
}
/*RULE_E*/

/*RULE:CHILDREN*/
.fichePanelv2Embedded.z-panel .z-panel-children {
	background-color : #FFFFFF; /*PRO*/
	background-image : none; /*PRO*/
	background-position : 0px 0px;
	border-top-width : 2px; /*PRO*/
	border-top-color : #00227c; /*PRO*/
}
/*RULE_E*/
/*RULE:BBAR*/
.fichePanelv2Embedded.z-panel .z-panel-btm .z-toolbar , .fichePanelv2Embedded.z-panel .z-panel-btm .z-toolbar span {
	font-family : sans-serif; /*PRO*/
	color : #00227c; /*PRO*/
	font-size : 10px; /*PRO*/
	font-style : normal; /*PRO*/
	font-family: Verdana, Arial, Helvetica, sans-serif;
	text-decoration : underline; /*PRO*/
	border-top-style : ridge; /*PRO*/
	border-top-width : thin; /*PRO*/
	border-top-color : #00227c; /*PRO*/
	border-bottom-style : none; /*PRO*/
	border-bottom-width : thin; /*PRO*/
	border-bottom-color : #00227c; /*PRO*/
}
	
.fichePanelv2Embedded.z-panel .z-panel-bbar .z-toolbar , .fichePanelv2Embedded.z-panel .z-panel-bbar .z-toolbar span {
	font-family : sans-serif; /*PRO*/
	color : #00227c; /*PRO*/
	font-size : 10px; /*PRO*/
	font-style : normal; /*PRO*/
	text-decoration : underline; /*PRO*/
	font-family: Verdana, Arial, Helvetica, sans-serif;
	border-top-style : ridge; /*PRO*/
	border-top-width : thin; /*PRO*/
	border-top-color : #00227c; /*PRO*/
	border-bottom-style : none; /*PRO*/
	border-bottom-width : thin; /*PRO*/
	border-bottom-color : #00227c; /*PRO*/
}
/*RULE_E*/
/*SEC_E*//*SEC:BUTTON-DEFAULT:buttonv2*/
/*RULE:TOP-LEFT*/
.buttonv2.z-button .z-button-tl{
	background-image : none; /*PRO*/
	background-position : top left;
	border-top-style : none;
	background:transparent;
}
/*RULE_E*//*RULE:TOP-MIDDLE*/
.buttonv2.z-button .z-button-tm{
	background-image : none; /*PRO*/
	background-position : top center;
	background:transparent;
}
/*RULE_E*//*RULE:TOP-RIGHT*/
.buttonv2.z-button .z-button-tr{
	background-image : none; /*PRO*/
	background-position : top right;
	background:transparent;
}
/*RULE_E*//*RULE:CENTER-LEFT*/
.buttonv2.z-button .z-button-cl{
	background-image : none; /*PRO*/
	background-position : center left;
	background:transparent;
}
/*RULE_E*//*RULE:CENTER-MIDDLE*/
.buttonv2.z-button .z-button-cm{
	background-image : none; /*PRO*/
	background-position : center center;
	color : #00227c; /*PRO*/
}
/*RULE_E*//*RULE:CENTER-RIGHT*/
.buttonv2.z-button .z-button-cr{
	background-image : none; /*PRO*/
	background-position : center right;
	background:transparent;
}
/*RULE_E*//*RULE:BOTTOM-LEFT*/
.buttonv2.z-button .z-button-bl{
	background-image : none; /*PRO*/
	background-position : bottom left;
	background:transparent;
}
/*RULE_E*//*RULE:BOTTOM-MIDDLE*/
.buttonv2.z-button .z-button-bm{
	background-image : none; /*PRO*/
	background-position : bottom center;
	background:transparent;
}
/*RULE_E*//*RULE:BOTTOM-RIGHT*/
.buttonv2.z-button .z-button-br{
	background-image : none; /*PRO*/
	background-position : bottom right;
	background:transparent;
}
/*RULE_E*/
/*SEC_E*/
.buttonv2.z-button:hover .z-button-tl{
	/* border-top-style : solid;*/
	/* border-top-width : 1px;*/
	/* border-top-color : #6F7C9D;*/
	/* border-left-style : solid;*/
	/* border-left-width : 1px;*/
	/* border-left-color : #6F7C9D;*/
	background-image : none;
	background-position : top left;
	background-color : #e3e9fb; 
	
}
.buttonv2.z-button:hover .z-button-tm{
	background-image : none;
	background-position : top center;
	background-color : #e3e9fb;
	/* border-top-style : solid;*/
/* 	border-top-width : 1px;*/
	/* border-top-color : #6F7C9D;*/
}
.buttonv2.z-button:hover .z-button-tr{
	background-image : none;
	background-position : top right;
	background-color : #e3e9fb;
	/* border-top-style : solid;*/
	/* border-top-width : thin;*/
	/* border-top-color : #6F7C9D;*/
	/* border-right-style : solid;*/
	/* border-right-width : thin;*/
	/* border-right-color : #6F7C9D;*/
}
.buttonv2.z-button:hover .z-button-cl{
	background-image : none;
	background-position : center left;
	background-color : #e3e9fb; 
	/* border-left-style : solid;*/
	/* border-left-width : 1px;*/
	/* border-left-color : #6F7C9D;*/
}
.buttonv2.z-button:hover .z-button-cm{
	background-image : none;
	background-position : center center;
	font-style : normal;
	text-decoration : none; 
	background-color : #e3e9fb; 
}
.buttonv2.z-button:hover .z-button-cr{
	background-image : none; 
	background-position : center right;
	background-color : #e3e9fb;
	/* border-right-style : solid;*/
	/* border-right-width : 1px;*/
	/* border-right-color : #6F7C9D;*/
}
.buttonv2.z-button:hover .z-button-bl{
	background-image : none; 
	background-position : bottom left;
	background-color : #e3e9fb; 
	/* border-bottom-style : solid;*/
	/* border-bottom-width : thin;*/
	/* border-bottom-color : #6F7C9D;*/
	/* border-left-style : solid;*/
	/* border-left-width : thin;*/
	/* border-left-color : #6F7C9D;*/
}
.buttonv2.z-button:hover .z-button-bm{
	background-image : none; 
	background-position : bottom center;
	background-color : #e3e9fb; 
	/* border-bottom-style : solid;*/
	/* border-bottom-width : 1px;*/
	/* border-bottom-color : #6F7C9D;*/
}
.buttonv2.z-button:hover .z-button-br{
	background-image : none; 
	background-position : bottom right;
	background-color : #e3e9fb; 
	/* border-bottom-style : solid;*/
/* 	border-bottom-width : thin;*/
	/* border-bottom-color : #6F7C9D;*/
	/* border-right-style : solid;*/
	/* border-right-width : thin;*/
	/* border-right-color : #6F7C9D;*/
}



.buttonv2.z-button:active .z-button-tl{
	background-image : none;
	background-position : top left;
	background-color : #AABEFA; 
	border-top-style : solid;
	border-top-width : thin;
	border-top-color : #6F7C9D;
	border-left-style : solid;
	border-left-width : thin;
	border-left-color : #6F7C9D;
}
.buttonv2.z-button:active .z-button-tm{
	background-image : none;
	background-position : top center;
	background-color : #AABEFA;
	border-top-style : solid;
	border-top-width : thin;
	border-top-color : #6F7C9D;
}
.buttonv2.z-button:active .z-button-tr{
	background-image : none;
	background-position : top right;
	background-color : #AABEFA;
	border-top-style : solid;
	border-top-width : thin;
	border-top-color : #6F7C9D;
	border-right-style : solid;
	border-right-width : thin;
	border-right-color : #6F7C9D;
}
.buttonv2.z-button:active .z-button-cl{
	background-image : none;
	background-position : center left;
	background-color : #AABEFA; 
	border-left-style : solid;
	border-left-width : thin;
	border-left-color : #6F7C9D;
}
.buttonv2.z-button:active .z-button-cm{
	background-image : none;
	background-position : center center;
	font-style : normal;
	text-decoration : none; 
	background-color : #AABEFA; 
}
.buttonv2.z-button:active .z-button-cr{
	background-image : none; 
	background-position : center right;
	background-color : #AABEFA;
	border-right-style : solid;
	border-right-width : thin;
	border-right-color : #6F7C9D;
}
.buttonv2.z-button:active .z-button-bl{
	background-image : none; 
	background-position : bottom left;
	background-color : #AABEFA; 
	border-bottom-style : solid;
	border-bottom-width : thin;
	border-bottom-color : #6F7C9D;
	border-left-style : solid;
	border-left-width : thin;
	border-left-color : #6F7C9D;
}
.buttonv2.z-button:active .z-button-bm{
	background-image : none; 
	background-position : bottom center;
	background-color : #AABEFA; 
	border-bottom-style : solid;
	border-bottom-width : thin;
	border-bottom-color : #6F7C9D;
}
.buttonv2.z-button:active .z-button-br{
	background-image : none; 
	background-position : bottom right;
	background-color : #AABEFA; 
	border-bottom-style : solid;
	border-bottom-width : thin;
	border-bottom-color : #6F7C9D;
	border-right-style : solid;
	border-right-width : thin;
	border-right-color : #6F7C9D;
}



div.z-listbox.listBox tr.z-listbox-odd:hover{
	background-color : #d2d8f3;
	background-image : none;
}

.z-menubar-hor.menuBarV2:hover .z-menu-inner-l
, .z-menubar-hor.menuBarV2:hover .z-menu-inner-r
, .z-menubar-hor.menuBarV2:hover .z-menu-inner-m
, .z-menubar-hor.menuBarV2:hover {
	background-image : none;
	background-color : #e3e9fb; 
}

.z-menubar-hor.menuBarV2 .z-menu-inner-l
, .z-menubar-hor.menuBarV2 .z-menu-inner-r
, .z-menubar-hor.menuBarV2 .z-menu-inner-m 
, .z-menubar-hor.menuBarV2 {
	background-image : none;
	background : transparent; 
}

.z-menubar-hor.menuBarV2:active .z-menu-inner-l
, .z-menubar-hor.menuBarV2:active .z-menu-inner-r
, .z-menubar-hor.menuBarV2:active .z-menu-inner-m 
, .z-menubar-hor.menuBarV2:active {
	background-image : none;
	background-color : #C7D4FA; 
}

.z-menubar-hor.menuBarV2 .z-menu-item .z-menu-item-inner-l
	, .z-menubar-hor.menuBarV2 .z-menu-item .z-menu-item-inner-m
	, .z-menubar-hor.menuBarV2 .z-menu-item .z-menu-item-inner-r
	, .z-menubar-hor.menuBarV2 .z-menu-item .z-menu-item-btn
{
	background : transparent;
	background-image : none;
}

div.imageNonSelectableEmplacement {
	border-style : solid;
	border-width : 1px;
	border-color : #d3d3d3;
}

div.imageEmplacement {
	border-style : solid;
	border-width : 1px;
	border-color : #d3d3d3;
}

div.imageEmplacement:hover {
	border-style : solid;
	border-width : 1px;
	border-color : #000000;
}

.encours { 
	position: relative; 
	top: 0px; 
	width: 28px; 
	height: 21px; 
	background: #ff3300; 
	opacity: 0.5; 
	filter: alpha(opacity=50);
} 

.reserve { 
	position: relative; 
	top: 0px; 
	width: 28px; 
	height: 21px; 
	background: #000080; 
	opacity: 0.3; 
	filter: alpha(opacity=30);
} 

.mismatch { 
	position: relative; 
	top: 0px; 
	width: 28px; 
	height: 21px; 
	background: #cc0000; 
	opacity: 0.5; 
	filter: alpha(opacity=50);
} 

.missing { 
	position: relative; 
	top: 0px; 
	width: 28px; 
	height: 21px; 
	background: #ff9900; 
	opacity: 0.3; 
	filter: alpha(opacity=30);
} 

.filled { 
	position: relative; 
	top: 0px; 
	width: 28px; 
	height: 21px; 
	background: #6600ff; 
	opacity: 0.3; 
	filter: alpha(opacity=30);
} 

div.imageCurrentEmplacement {
	border-style : solid;
	border-width : 1px;
	border-color : #FF0000;
	background-color : #C7D4FA;
}

div.imageCurrentEmplacement:hover {
	border-style : solid;
	border-width : 1px;
	border-color : #000000;
}

div.imageMovedEmplacement {
	border-style : solid;
	border-width : 1px;
	border-color : #d3d3d3;
	background-color : #40ff40;
}

 div.imageMovedEmplacement:hover {
	border-style : solid;
	border-width : 1px;	
	border-color : #000000;
	background-color : #40ff40;
}

div.imageSelectedEmplacement {
	border-style : solid;
	border-width : 1px;
	border-color : #d3d3d3;
	background-color : #0060ff;
}

div.imageValideEmplacement {
	border-style : solid;
	border-width : 1px;
	border-color : #d3d3d3;
	background-color : #b5007f;
}

div.imageDestockEmplacement {
	border-style : solid;
	border-width : 1px;
	border-color : #d3d3d3;
	background-color : #ff0000;
}

div.imageSelectedEmplacement:hover {
	border-style : solid;
	border-width : 1px;
	border-color : #000000;
}

div.imageValideEmplacement:hover {
	border-style : solid;
	border-width : 1px;
	border-color : #000000;
}

div.imageDestockEmplacement:hover {
	border-style : solid;
	border-width : 1px;
	border-color : #000000;
}

.groupBoxTk .z-groupbox-cnt {
	border-style : solid;
	border-width : 1px;
	border-color : #1a58ab;
}

.groupBoxTk .z-groupbox-hl {
	border-bottom-style : solid;
	border-bottom-width : 1px;
	border-bottom-color : #1a58ab;
}

.formAnonymeLink {
	color : #003399; 
	background-color : #aeb4c6; 
	font-style : normal;
	font-weight : bold;
	text-decoration : none;
	cursor:pointer;
}

.formAnonymeLink:hover {
	color : #003399; 
	background-color : #aeb4c6; 
	font-style : normal;
	font-weight : bold;
	text-decoration : underline;
	cursor:pointer;
}

/*SEC:PANEL-NON-FRAME:innerPanel*/
/*RULE:HEADER*/
.innerPanel.z-panel .z-panel-header {
	font-style : oblique; /*PRO*/
	color : #00227c; /*PRO*/
	background-image : none; /*PRO*/
	background-position : 0px 0px;
	background-color : #FFFFFF; /*PRO*/
	border-bottom-style: none;
}
/*RULE_E*//*RULE:BORDER*/
.innerPanel.z-panel  {
	border-style : groove; /*PRO*/
	border-width : 1px; /*PRO*/
	border-color : #00227c; /*PRO*/
}
/*RULE_E*//*RULE:CHILDREN*/
.innerPanel.z-panel .z-panel-tl
, .innerPanel.z-panel .z-panel-tr
, .innerPanel.z-panel .z-panel-hl
, .innerPanel.z-panel .z-panel-hr {
	background-image : none; /*PRO*/
	background-color : #FFFFFF; /*PRO*/
}
.innerPanel.z-panel .z-panel-icon {
	overflow: hidden; width: 16px; height: 16px; float: right;
	background-color : transparent; background-position : 0 0; background-repeat : no-repeat; 
	margin-left: 2px; 
	background-image : url(${c:encodeURL('/images/icones/viewmag.png')});
	cursor: pointer;
}

.innerPanel.z-panel .z-panel-exp {
	background-position: 0 -16px; 
}

.innerPanel.z-panel .z-panel-exp-over {
	background-position: -16px -16px; 
}

.innerPanel.z-panel-colpsd .z-panel-exp {
	background-position: 0 0px; 
}

.innerPanel.z-panel-colpsd .z-panel-exp-over {
	background-position: -16px 0px; 
}

.innerPanel.z-panel .z-panel-close {
	background-position: 0 -48px; 
}

.innerPanel.z-panel .z-panel-close-over {
	background-position: -16px -48px; 
}

.portalPanel.z-panel .z-panel-header {
	color : #1B58A7;
	background-image : url(${c:encodeURL('/images/header-panel-portal.png')});
	background-position : 0px 0px;
	background-color : #FFFFFF;
	border-bottom-style: none;
	border-bottom-width : 1px;
	border-bottom-color : #C7CCD5;
	border-left-style : solid;
	border-left-width : 1px;
	border-left-color : #C7CCD5;
	border-right-style : solid;
	border-right-width : 1px;
	border-right-color : #C7CCD5;
	border-top-style : solid;
	border-top-width : 1px;
	border-top-color : #C7CCD5;
	padding : 0px 0px 0px 0px;
}

/*RULE_E*//*RULE:CHILDREN*/
.portalPanel.z-panel .z-panel-tl
, .portalPanel.z-panel .z-panel-tr {
	background-image : url(${c:encodeURL('/images/panel-portal-corner.png')}); /*PRO*/
}

/*RULE_E*//*RULE:CHILDREN*/
.portalPanel.z-panel .z-panel-hl
, .portalPanel.z-panel .z-panel-hr {
	background-image : url(${c:encodeURL('/images/header-panel-portal.png')}); /*PRO*/
}

.portalPanel.z-panel .z-panel-max {
	background-image : url(${c:encodeURL('/images/fichePanelv2-icon.png')});
}

.portalPanel.z-panel .z-panel-maxd {
	background-image : url(${c:encodeURL('/images/fichePanelv2-icon.png')});
}

.portalPanel.z-panel  {
	border-style : none;
	border-width : thin;
	border-color : #00227c;
}

.portalPanel.z-panel .z-panel-children {
	background-image : none;
	background-position : 0px 0px;
	border-top-style : solid;
	border-top-width : 1px;
	border-top-color : #C7CCD5;
	border-bottom-style : solid;
	border-bottom-width : 1px;
	border-bottom-color : #C7CCD5;
	border-left-style : solid;
	border-left-width : 1px;
	border-left-color : #C7CCD5;
	border-right-style : solid;
	border-right-width : 1px;
	border-right-color : #C7CCD5;
}


/*RULE_E*//*RULE:TBAR*/
.innerPanel.z-panel .z-panel-top .z-toolbar ,.innerPanel.z-panel .z-panel-top .z-toolbar span {
	background-image : none; /*PRO*/
	background-position : 0px 0px;
}
	
	
.innerPanel.z-panel .z-panel-tbar .z-toolbar ,.innerPanel.z-panel .z-panel-tbar .z-toolbar span {
	background-image : none; /*PRO*/
}
	
	
.innerPanel.z-panel .z-panel-tbar .z-toolbar ,.innerPanel.z-panel .z-panel-tbar .z-toolbar span {
	background-image : none; /*PRO*/
	background-position : 0px 0px;
}

.innerPanel.z-panel .z-panel-btm.z-panel-btm-noborder div:first-child {
	text-align: center;
	margin-left: 15%; margin-right: 15%;
}
	
	
/*RULE_E*//*RULE:BBAR*/
.innerPanel.z-panel .z-panel-btm .z-toolbar , .innerPanel.z-panel .z-panel-btm .z-toolbar span {
	background-image : none; /*PRO*/
	background-position : 0px 0px;
	background-color : #FFFFFF; /*PRO*/
}
	
.innerPanel.z-panel .z-panel-bbar .z-toolbar , .innerPanel.z-panel .z-panel-bbar .z-toolbar span {
	background-image : none; /*PRO*/
	background-position : 0px 0px;
	background-color : #FFFFFF; /*PRO*/
}
/*RULE_E*/
/*SEC_E*//*SEC:HBOX:buttonsBox*/
/*RULE:SEPARATOR*/
.buttonsBox.z-hbox td.z-hbox-sep {
	width : 5px; /*PRO:separator*/
}
/*RULE_E*/
/*SEC_E*//*SEC:RADIO:radiov2*/
/*RULE:CONTENT*/
.radiov2.z-radio .z-radio-cnt{
	font-weight : bold; /*PRO*/
}
/*RULE_E*/
/*SEC_E*//*SEC:COMBOBOX:simpleCombov2*/
/*RULE:FOCUS*/
.simpleCombov2.z-combobox.z-combobox-focus .z-combobox-inp{
	color : #00227c; /*PRO*/
	font-style : normal; /*PRO*/
}
/*RULE_E*//*RULE:COMBOBOX*/
.simpleCombov2.z-combobox .z-combobox-inp{
	color : #00227c; /*PRO*/
	font-style : normal; /*PRO*/
}
/*RULE_E*//*RULE:READONLY*/
.simpleCombov2.z-combobox .z-combobox-readonly.z-combobox-inp{
	color : #00227c; /*PRO*/
	font-style : normal; /*PRO*/
}
/*RULE_E*/
/*SEC_E*//*SEC:LABEL:infoLabel*/
/*RULE:BASIC*/
.infoLabel.z-label {
	color : #00227c; /*PRO*/
	font-style : italic; /*PRO*/
	font-weight : normal; /*PRO*/
}
/*RULE_E*/
/*SEC_E*/

/*SEC:GRID:gridListStyle*/
/*RULE:COLUMN*/
div.z-grid.gridListStyle {
	overflow-y: auto;
}

/* IE8 */
div.z-grid.gridListStyle div.z-grid-body {
	overflow-y: hidden;
	/* padding-bottom: 12px; */
}

div.z-grid.gridListStyle .z-column-cnt {
	color : #FFFFFF; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bold; /*PRO*/
	text-align : left; /*PRO*/
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

div.z-grid.gridListStyle .z-column-btn {
	background-image : url(${c:encodeURL('/images/themes/blue/hd-btn.png')});
}

div.z-grid.gridListStyle div.z-grid-header th.z-column {
	background-color : #1E62BB; /*PRO*/
	background-image : none; /*PRO*/
	border-top-style : solid; /*PRO*/
	border-top-width : 1px; /*PRO*/
	border-top-color : #FFFFFF; /*PRO*/
	border-right-style : solid; /*PRO*/
	border-right-width : 1px; /*PRO*/
	border-right-color : #FFFFFF; /*PRO*/
	border-bottom-style : solid; /*PRO*/
	border-bottom-width : 1px; /*PRO*/
	border-bottom-color : #FFFFFF; /*PRO*/
	border-left-style : solid; /*PRO*/
	border-left-width : 1px; /*PRO*/
	border-left-color : #FFFFFF; /*PRO*/
	padding : 0px 0px 0px 0px;
}
/*RULE_E*//*RULE:COLUMNS*/
div.z-grid.gridListStyle div.z-grid-header {
	border-style : none; /*PRO*/
}
/*RULE_E*//*RULE:GRID-DEFAULT*/
div.z-grid.gridListStyle {
	border-style : none; /*PRO*/
}
/*RULE_E*//*RULE:ODDROW*/
div.z-grid.gridListStyle tr.z-grid-odd td.z-row-inner {
	background-color : #e2e9fe; /*PRO*/
	background-image : none; /*PRO*/
}
/*RULE_E*//*RULE:ROW*/
div.z-grid.gridListStyle tr.z-row td.z-row-inner {
	border-style : solid; /*PRO*/
	border-width : 1px; /*PRO*/
	border-color : #FFFFFF; /*PRO*/
	background-color : #e2e9fe; /*PRO*/
	background-image : none; /*PRO*/
}

div.z-grid.gridListStyle tr.greenrow td.z-row-inner {
	background-color : #5CB85C;
	background-image : none;
}

div.z-grid.gridListStyle tr.gold td.z-row-inner {
	background-color : #FFD700;
	background-image : none;
}

div.z-grid.gridListStyle div.z-grid-autopaging tr.z-row div.z-row-cnt {
	height: 16px;
	vertical-align: bottom;
}


/*RULE_E*//*RULE:DETAIL*/
div.z-grid.gridListStyle .z-row.z-detail-faker {
	background-color : #EBEBFA; /*PRO*/
	background-image : none; /*PRO*/
	border-style : solid; /*PRO*/
	border-color : #000000; /*PRO*/
}
/*RULE_E*//*RULE:DETAIL-SIDEBAR*/
div.z-grid.gridListStyle tr.z-row .z-detail-outer {
	background-color : #1E62BB; /*PRO*/
	background-image : none; /*PRO*/
}
/*RULE_E*//*RULE:GROUPFOOT*/
div.z-grid.gridListStyle .z-group-foot {
	background-color : #FFFFFF; /*PRO*/
	background-image : none; /*PRO*/
}
/*RULE_E*/
/*SEC_E*//*SEC:GRID:gridListEditStyle*/
/*RULE:GRID-DEFAULT*/
div.z-grid.gridListEditStyle{
	border-style : none; /*PRO*/
}
/*RULE_E*//*RULE:COLUMN*/
div.z-grid.gridListEditStyle .z-column-cnt{
	color : #FFFFFF; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bold; /*PRO*/
	text-align : left; /*PRO*/
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

div.z-grid.gridListEditStyle .z-column-btn {
	background-image : url(${c:encodeURL('/images/themes/blue/hd-btn.png')});
}

div.z-grid.gridListEditStyle div.z-grid-header th.z-column{
	background-color : #1E62BB; /*PRO*/
	background-image : none; /*PRO*/
	border-top-style : solid; /*PRO*/
	border-top-width : 1px; /*PRO*/
	border-top-color : #FFFFFF; /*PRO*/
	border-right-style : solid; /*PRO*/
	border-right-width : 1px; /*PRO*/
	border-right-color : #FFFFFF; /*PRO*/
	border-bottom-style : solid; /*PRO*/
	border-bottom-width : 1px; /*PRO*/
	border-bottom-color : #FFFFFF; /*PRO*/
	border-left-style : solid; /*PRO*/
	border-left-width : 1px; /*PRO*/
	border-left-color : #FFFFFF; /*PRO*/
}
/*RULE_E*//*RULE:COLUMNS*/
div.z-grid.gridListEditStyle div.z-grid-header{
	border-style : none; /*PRO*/
}
/*RULE_E*//*RULE:ODDROW*/
div.z-grid.gridListEditStyle tr.z-grid-odd td.z-row-inner{
	background-color : #e2e9fe; /*PRO*/
	background-image : none; /*PRO*/
}
/*RULE_E*//*RULE:ROW*/
div.z-grid.gridListEditStyle tr.z-row td.z-row-inner{
	border-style : solid; /*PRO*/
	border-width : 1px; /*PRO*/
	border-color : #FFFFFF; /*PRO*/
}
/*RULE_E*//*RULE:DETAIL*/
div.z-grid.gridListEditStyle .z-row.z-detail-faker{
	background-color : #EBEBFA; /*PRO*/
	background-image : none; /*PRO*/
}
/*RULE_E*//*RULE:DETAIL-SIDEBAR*/
div.z-grid.gridListEditStyle tr.z-row .z-detail-outer{
	background-color : #1E62BB; /*PRO*/
	background-image : none; /*PRO*/
}
/*RULE_E*//*RULE:DETAIL-SIDEBARBTN*/
div.z-grid.gridListEditStyle tr.z-row td.z-detail-outer div.z-row-cnt .z-detail .z-detail-img{
	background-image : url(${c:encodeURL('/images/icones/edit.png')}); /*PRO*/
	background-position : 0px 0px; /*PRO:background-image:url(${c:encodeURL('/images/icones/edit.png')})*/
}
/*RULE_E*/
/*SEC_E*/


tr.z-grid-odd-sel td.z-row-inner-sel{
	background-color : #FF0000;
	background-image : none;
}

div.z-grid {
	background:#FFFFFF none repeat scroll 0 0;
	border:1px solid #86A4BE;
	overflow:hidden;
}/*SEC:MENUBAR-HORIZONTAL:menuBarV2*/
/*RULE:MENU*/
.z-menubar-hor.menuBarV2 .z-menu-btn{
	color : #00227c; /*PRO*/
	font-family: Verdana, Arial, Helvetica, sans-serif;
}
	
/*RULE_E*//*RULE:MENUITEM*/
.z-menubar-hor.menuBarV2 .z-menu-item-btn{
	color : #00227c; /*PRO*/
	font-family: Verdana, Arial, Helvetica, sans-serif;
}
	.z-menubar-hor.menuBarV2 .z-menu-item .z-menu-item-inner-l
	, .z-menubar-hor.menuBarV2 .z-menu-item .z-menu-item-inner-m
	, .z-menubar-hor.menuBarV2 .z-menu-item .z-menu-item-inner-r
{
	background-color : #AABEFA; /*PRO*/
	background-image : none; /*PRO*/
}

/*RULE_E*/
/*SEC_E*//*SEC:GROUPBOX-3D:groupBoxTk*/
/*RULE:CONTENT*/
.z-groupbox.groupBoxTk .z-groupbox-cnt{
	border-style : solid; /*PRO*/
	border-width : 1px; /*PRO*/
	border-color : 1a58ab; /*PRO*/
	border-right-style : solid; /*PRO*/
	border-right-width : 1px; /*PRO*/
	border-right-color : 1a58ab; /*PRO*/
	border-bottom-style : solid; /*PRO*/
	border-bottom-width : 1px; /*PRO*/
	border-bottom-color : 1a58ab; /*PRO*/
	border-left-style : solid; /*PRO*/
	border-left-width : 1px; /*PRO*/
	border-left-color : 1a58ab; /*PRO*/
}
/*RULE_E*/
/*SEC_E*//*SEC:GROUPBOX-DEFAULT:groupBoxNormal*/
/*RULE:CAPTION*/
.z-fieldset.groupBoxNormal legend{
	color : #00227c; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bold; /*PRO*/
}
/*RULE_E*//*RULE:BASIC*/
.z-fieldset.groupBoxNormal{
	border-style : solid; /*PRO*/
	border-width : 1px; /*PRO*/
	border-color : #1a58ab; /*PRO*/
}
/*RULE_E*/
/*SEC_E*//*SEC:TABBOX-VERTICAL:vertTabBox*/
/*RULE:TABS-FONT*/
.vertTabBox.z-tabbox-ver .z-tab-ver .z-tab-ver-text{
	color : #00227c; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bold; /*PRO*/
}
/*RULE_E*//*RULE:TABS-SELD-FONT*/
.vertTabBox.z-tabbox-ver .z-tab-ver-seld .z-tab-ver-text{
	color : #FFFFFF; /*PRO*/
	font-style : normal; /*PRO*/
	font-weight : bolder; /*PRO*/
}
/*RULE_E*//*RULE:TABS-BODY*/
.vertTabBox.z-tabbox-ver .z-tabs-ver.z-tabs-ver-scroll {
	border-left-style : none; /*PRO*/
	border-left-width : thin; /*PRO*/
	border-left-color : #FFFFFF; /*PRO*/
	border-top-style : solid; /*PRO*/
	border-top-width : thin; /*PRO*/
	border-top-color : #7F7F7F; /*PRO*/
	border-bottom-style : solid; /*PRO*/
	border-bottom-width : thin; /*PRO*/
	border-bottom-color : #7F7F7F; /*PRO*/
	border-right-style : none; /*PRO*/
	border-right-width : thin; /*PRO*/
	border-right-color : #FFFFFF; /*PRO*/
	background-color : #FFFFFF; /*PRO*/
}
.vertTabBox.z-tabbox-ver .z-tabs-ver.z-tabs-ver-scroll .z-tabs-ver-cnt{
	background-color : #FFFFFF; /*PRO*/
	border-right-style : solid; /*PRO:zk-tabs-cnt-border-style*/
	border-right-width : 3px; /*PRO:zk-tabs-cnt-border-width*/
	border-right-color : #6ca1e6; /*PRO:zk-tabs-cnt-border-color*/
}
.vertTabBox.z-tabbox-ver .z-tab-ver-hl {
	background-image : url(${c:encodeURL('/images/themes/blue/tab-v-corner.png')});
}
.vertTabBox.z-tabbox-ver .z-tab-ver-hr {
	background-image : url(${c:encodeURL('/images/themes/blue/tab-v-corner.png')});
}
.vertTabBox.z-tabbox-ver .z-tab-ver-hm {
	background-image : url(${c:encodeURL('/images/themes/blue/tab-v-hm.png')});
}
/*RULE_E*/
/*SEC_E*//*SEC:TABBOX-ACCORDION:tabAcc*/
/*RULE:TABS-FONT*/
.tabAcc.z-tabbox-accordion .z-tab-accordion .z-tab-accordion-text, .tabAcc .z-tab-accordion-hm{
	color : #00227c; /*PRO*/
}
/*RULE_E*//*RULE:TABS-DISBD-FONT*/
.tabAcc.z-tabbox-accordion .z-tab-accordion.z-tab-accordion-disd .z-tab-accordion-text, .tabAcc .z-tab-accordion-disd .z-tab-accordion-hm{
	color : #AAAAAA; /*PRO*/
}
/*RULE_E*//*RULE:TABS-SELD-FONT*/
.tabAcc.z-tabbox-accordion .z-tab-accordion.z-tab-accordion-seld .z-tab-accordion-text,.tabAcc .z-tab-accordion-seld .z-tab-accordion-hm{
	color : #00227; /*PRO*/
}
/*RULE_E*//*RULE:TABS-DISBD*/
.tabAcc.z-tabbox-accordion .z-tab-accordion.z-tab-accordion-disd {
	border-bottom-style : #00227c; /*PRO*/
	border-bottom-width : 1px; /*PRO*/
	border-bottom-color : #667ab0; /*PRO*/
}
/*RULE_E*//*RULE:TABS-SELD*/
.tabAcc.z-tabbox-accordion .z-tab-accordion.z-tab-accordion-seld {
	border-bottom-style : none; /*PRO*/
}
/*RULE_E*//*RULE:PANELS-BODY*/
.tabAcc.z-tabbox-accordion .z-tabpanels-accordion .z-tabpanel-accordion {
	border-right-style : solid; /*PRO*/
	border-right-width : 1px; /*PRO*/
	border-right-color : #7b7b7b; /*PRO*/
	border-left-style : solid; /*PRO*/
	border-left-width : 1px; /*PRO*/
	border-left-color : #7b7b7b; /*PRO*/
	border-bottom-style : solid; /*PRO*/
	border-bottom-width : 1px; /*PRO*/
	border-bottom-color : #7b7b7b; /*PRO*/
}
/*RULE_E*/
/*SEC_E*//*SEC:LABEL:formAnonymeBlock*/
/*RULE:BASIC*/
.formAnonymeBlock.z-label {
	color : #FFFFFF; /*PRO*/
	background-color : #aeb4c6; /*PRO*/
}
/*RULE_E*/
/*SEC_E*/

@CHARSET "UTF-8";/*SEC:GRID:gridForm1*/
/*RULE:ROW*/
div.z-grid.annoGrid tr.z-row td.z-row-inner{
	border-color : #FFFFFF; /*PRO*/
	background-color : #FFFFFF; /*PRO*/
}
tr.z-row-over.annoGrid > td.z-row-inner, tr.z-row-over > .z-cell {
	background-image: none;
}
/*RULE_E*//*RULE:GRID-DEFAULT*/
div.z-grid.annoGrid{
	border-style : none; /*PRO*/
	padding: 0;
	margin: 0;
}
/*RULE_E*//*RULE:GROUP*/
div.z-grid.annoGrid .z-group-inner .z-group-cnt span, div.z-grid.gridForm1 .z-group-inner .z-group-cnt{
	color : #00227c; /*PRO*/
	font-weight : bold; /*PRO*/
}
div.z-grid.annoGrid .z-cell, td.z-row-inner, td.z-groupfoot-inner {
	padding: 0;
	overflow: hidden;
}

tr.z-row.annoGrid .z-cell {
padding: 0;
	margin: 0;
}

tr.z-grid-odd.annoGrid td.z-row-inner,
tr.z-grid-odd .z-cell,
tr.z-grid-odd {
	padding: 0;
	margin: 0;
}

tr.z-row.annoGrid td.z-row-inner,
tr.z-row.annoGrid .z-cell {
	padding: 0;
	margin: 0;
}

tr.z-treerow-seld {
	background-color : #bcd2ef;
	background-image : none;
}

.northBanniere.z-north {
	background: url(${c:encodeURL('/images/themes/blue/topTumoBanniere_left_repeat.png')}); 
	border:0;
}

.southWindow.z-south {
	background-color: #1756aa;
	background-repeat: repeat-x;color: #ffffff; 
}

.boxBanniere {
	background: url(${c:encodeURL('/images/themes/blue/topTumoBanniere.png')});
	background-repeat: no-repeat; 
	height:100%;
	width:100%;
}

.imageLogo {
	background: url(${c:encodeURL('/images/themes/blue/logo_tumo.png')});
}

.z-popup, .z-popup-plain {
position: absolute;
top: 0;
left: 0;
border: 0 none;
font-family: ${fontFamilyC};
font-size: ${fontSizeM};
font-weight: normal;
margin:0;
overflow:hidden;
padding:0;
border-radius: 1px;
-moz-border-radius: 1px;
-webkit-border-radius: 1px;
box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.35);
-moz-box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.35);
-webkit-box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.35);
}
.z-popup .z-popup-cl {
background: transparent repeat-x 0 0;
background-image: url(${c:encodeThemeURL('~./zul/img/popup/popup-bg.png')});
background-position: 0 -2px;
border: 1px solid #CFCFCF;
padding-left: 0;
}
.z-popup .z-popup-cnt {
margin: 0 !important;
line-height: 14px;
color: #555555;
padding: 5px 7px 5px 7px;
zoom: 1;
}

<%-- IE 6 GIF --%>
<c:if test="${zk.ie == 6}">
.z-popup .z-popup-cl {
background-image: url(${c:encodeThemeURL('~./zul/img/popup/popup-bg.gif')});
}
</c:if>

<%-- notification --%>
.z-notification {
position: absolute;
top: 0;
left: 0;
border: 0 none;
margin: 0;
padding: 0;
background: none;
}
.z-notification .z-notification-cl,
.z-notification .z-notification-cnt {
	width: auto;
	vertical-align: middle
	height: 150px;
}
.z-notification-ref .z-notification-cl,
.z-notification-ref .z-notification-cnt {
width: 300px;
height: 150px;
}
.z-notification .z-notification-cl {
padding: 20px;
padding-left: 55px;
padding-right: 30px;
border-radius: 10px;
-moz-border-radius: 10px;
box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.35);
-moz-box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.35);
background: transparent no-repeat 15px 15px;
}
.z-notification-ref .z-notification-cl {
overflow: hidden;
padding: 5px;
padding-left: 55px;
border-radius: 5px;
-moz-border-radius: 5px;
background-position: 17px 18px;
}
.z-notification .z-notification-cnt {
overflow: hidden;
zoom: 1;

margin: 0 !important;
background: none;
font-family: ${fontFamilyC};
font-size: 16px;
font-weight: normal;
}
.z-notification-ref .z-notification-cnt {
display: table-cell;
vertical-align: middle;
font-size: ${fontSizeM};
}
.z-notification .z-notification-pointer {
position: absolute;
display: none;
width: 0;
height: 0;
border: 10px solid transparent;
}

<%-- notification: base style --%>
.z-notification .z-notification-cl {
background-color: ${zk.ie <= 8 ? "#444444" : "rgba(51, 51, 51, 0.9)"}; <%-- bug ZK-1135: IE8 does not support rgba --%>
color: #FFFFFF;
}
.z-notification .z-notification-pointer-l,
.z-notification .z-notification-pointer-r,
.z-notification .z-notification-pointer-u,
.z-notification .z-notification-pointer-d {
border: 10px solid transparent;
}
.z-notification .z-notification-pointer-l {
border-right-color: ${zk.ie <= 8 ? "#444444" : "rgba(51, 51, 51, 0.9)"};
}
.z-notification .z-notification-pointer-r {
border-left-color: ${zk.ie <= 8 ? "#444444" : "rgba(51, 51, 51, 0.9)"};
}
.z-notification .z-notification-pointer-u {
border-bottom-color: ${zk.ie <= 8 ? "#444444" : "rgba(51, 51, 51, 0.9)"};
}
.z-notification .z-notification-pointer-d {
border-top-color: ${zk.ie <= 8 ? "#444444" : "rgba(51, 51, 51, 0.9)"};
}

<%-- bug ZK-1135: IE8 does not support rgba --%>
<c:set var="infoColor" value="${zk.ie <= 8 ? '#3CA7B1' : 'rgba(33, 155, 166, 0.88)'}" />
<c:set var="warningColor" value="${zk.ie <= 8 ? '#ED5A33' : 'rgba(234, 67, 23, 0.88)'}" />
<c:set var="errorColor" value="${zk.ie <= 8 ? '#C61F23' : 'rgba(190, 0, 5, 0.88)'}" />

<%-- notification: info --%>
.z-notification-info .z-notification-cl {
background-color: ${infoColor};
background-image: url(${c:encodeThemeURL('~./zul/img/popup/notif-info.png')});
}
<c:if test="${zk.ie == 6}">
.z-notification-info .z-notification-cl {
background-image: url(${c:encodeThemeURL('~./zul/img/popup/notif-info.gif')});
}
</c:if>
.z-notification-info .z-notification-pointer-l {
border-right-color: ${infoColor};
}
.z-notification-info .z-notification-pointer-r {
border-left-color: ${infoColor};
}
.z-notification-info .z-notification-pointer-u {
border-bottom-color: ${infoColor};
}
.z-notification-info .z-notification-pointer-d {
border-top-color: ${infoColor};
}

<%-- notification: warning --%>
.z-notification-warning .z-notification-cl {
background-color: ${warningColor};
background-image: url(${c:encodeThemeURL('~./zul/img/popup/notif-warning.png')});
}
<c:if test="${zk.ie == 6}">
.z-notification-warning .z-notification-cl {
background-image: url(${c:encodeThemeURL('~./zul/img/popup/notif-warning.gif')});
}
</c:if>
.z-notification-warning .z-notification-pointer-l {
border-right-color: ${warningColor};
}
.z-notification-warning .z-notification-pointer-r {
border-left-color: ${warningColor};
}
.z-notification-warning .z-notification-pointer-u {
border-bottom-color: ${warningColor};
}
.z-notification-warning .z-notification-pointer-d {
border-top-color: ${warningColor};
}

<%-- notification: error --%>
.z-notification-error .z-notification-cl {
background-color: ${errorColor};
background-image: url(${c:encodeThemeURL('~./zul/img/popup/notif-error.png')});
}
<c:if test="${zk.ie == 6}">
.z-notification-error .z-notification-cl {
background-image: url(${c:encodeThemeURL('~./zul/img/popup/notif-error.gif')});
}
</c:if>
.z-notification-error .z-notification-pointer-l {
border-right-color: ${errorColor};
}
.z-notification-error .z-notification-pointer-r {
border-left-color: ${errorColor};
}
.z-notification-error .z-notification-pointer-u {
border-bottom-color: ${errorColor};
}
.z-notification-error .z-notification-pointer-d {
border-top-color: ${errorColor};
}

div.square{
float: left;
width: 20px;
height: 20px;
border: 1px solid rgba(0, 0, 0, .2);
margin-right: 10px;
}

# .gridForm1 .z-grid-body {
#	height: -webkit-fill-available;
# }

.rechWindow .z-window-modal, .rechWindow .z-window-embedded, 
.rechWindow .z-window-modal-cnt-noborder, .rechWindow .z-window-embedded-cnt-noborder,
.rechWindow .z-window-modal-cnt > span {
	display: block;
	height: inherit;
}
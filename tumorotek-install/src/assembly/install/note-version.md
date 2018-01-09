#### Notes de version

##### Version ${project.version} - Publiée le ${timestamp}
- YouTrack [TKB-3](https://tumorotek.myjetbrains.com/youtrack/issue/TKB-3) : La suppression d'une cession depuis la liste de ces dernières bloque les échantillons concernés dans le statut RESERVE
- YouTrack [TK-64](https://tumorotek.myjetbrains.com/youtrack/issue/TK-64) : Les annotations de cession ne se déverrouillent pas quand on fait une nouvelle cession  

***

##### Version 2.1.2 - Publiée le 02/01/2018
- YouTrack [TKB-1](https://tumorotek.myjetbrains.com/youtrack/issue/TKB-1) : La recherche ne fonctionne pas sur les ID de cession  
- YouTrack [TKB-2](https://tumorotek.myjetbrains.com/youtrack/issue/TKB-2) : La recherche de date dans les annotations sur les dérivés ne fonctionne pas  
- YouTrack [TK-47](https://tumorotek.myjetbrains.com/youtrack/issue/TK-47) : Erreurs sur les propriétés d'internationalisation  
- YouTrack [TK-68](https://tumorotek.myjetbrains.com/youtrack/issue/TK-68) : \[TK-DIAMIC] Erreur lors de la création d'un UDM Q05: Unsolicited display update message quand on est au mois de Janvier

***

##### Version 2.1.1  
25/01/2017  
Interfacage robot IRELEC: ajout emetteur fichier tabulé (package: storageRobot) + paramétrage Camel.  
13/02/2017  
InterfacageParsingUtils getValueFromEmplacement aucune valeur enregistrée si ""  
10/03/2013  
Premiers développements couche service prelechansManager + tests d'integration en vue de la livraison d'un formulaire unique permettant l'enregistrement minimaliste d'un dossier complet patient/prelevement/echantillons  
10/05/2017  
AnnotationValeur formateAnnotationValeur prend en compte formatage si annotation type fichier (affichage recherche complexe)  
12/06/2017  
refactorisation code + correctifs + tests sur les méthodes de revert objects lors de suppression, refus, retrait echantillon/dérivé d'une cession totale.  
15/06/2017  
FicheEtiquetteModale getPremierNumeroTube() <= getDernierNumeroTube sinon bug quand impression incrementation 1 à 1 (Boudot Grenoble)  

***

##### Version 2.1  
04/04/2016  
Contrôle doublons élevés à la PF pour entités Prelevement/Echantillon/ProdDerive/Cession  
16/04/2016  
Profil devient archivable et géré par PF  
Ajout liste utilisateurs + banque, cliquable + filtre affichage sur état archivé dans FicheProfil.zul  
Ajout lien cliquable vers Administration>Profil dans FicheBanque.zul  + filtre affichage sur état archivé utilisateur  
Correction Bug FichePlateforme InitEditableMode pfs empty where superadmin  
20/05/2016  
Correction FicheTerminale.getDroitProdDeriveConsultation superadmin  
Correction NullPointerException FicheProdDeriveEdit.onClick$operateurAideSaisieDerive si banqueProprietaire == null  
27/07/2016  
Thermofischer barcode 2D: liste contenu, recherche boite par code, stockage/déstockage/déplacement à partir de la boite, validations contenus  
02/12/2016  
CodeAssigneEditableGrid fix codebox.onBlur / validate.onClick problem (L. Milian CHU BDX)  
09/12/2016  
Prelevement RowRenderer message onClickDossierExt pour appeler dossier externe depuis la liste  
30/12/2016  
ImportDossierExterneModale: onClickImporter Ajout checkEchans pour avertir lorsqu'aucun échantillon n'est sélectionné (N. Dufay)  
InterfacagesParsingUtilsImpl: getValueFromEmplacement: Vérifie que la valeur retournée n'est pas une suite d'espaces, return null si oui  

***

##### Version 2.0.13.3  
14/10/2016  
Merge Impression JScript from branche 2.1  
Correction stream = null cause AsbtractFicheEditController updateObjectWithAnnots() clearValeurLists(false)  
07/11/2016  
Modification ImprimanteModeleModale pour prise en charge BarcodeByJS  
Ajout \r dans la printJSCRIPT + ligne vide à la fin du fichier.  
ComponentPrinter.printRaw remplacement outpustream.write(byte[]) par writeBytes  
01/01/2017  
Export code assigne mysql group_concat order by ca.ordre  
23/03/2017  
Correction import dérivés batch parent=null (pas transformation)  
Modification DerivesImportBatches et ImportManagerImpl pour gérer parent = null   

***

##### Version 2.0.13.2  
30/05/2016  
Onglet connexion quand profil désactive PrelevementTab MainTabbox.initFirstTab (D. Desigaux IRCM)  
Premier onglet accessible quand updateSelectedBanque MainWindow.setStartingPanel  
01/06/2016  
Methode translateBoolValue jamais accessible dans setPropertyForImportColonne cause matches boolean  
02/06/2016  
Import thesaurus multiple annotations  
15/06/2016  
Modification EchantillonManager.checkEmplacementDerive en checkEmplacementOccupied pour elargir verification occupation emplacement aux echantillons également  
Modification ProdDeriveManager.checkEmplacementDerive en checkEmplacementOccupied pour elargir verification occupation emplacement aux dérivés également  
Gestion revert statut stockage + emplacement FicheMultiEchantillons et FicheMultiProdDerive createNewObjectet updateObject en cas rollback transaction.  
Si Rollback erreur stockage, met à jour le TkObjet concerné pour être restockable.  
24/07/2016  
Modification Export.closeAll()  getConnection.Rollback pour éviter Dead lock sur table OPERATION lors click Cancel sur ProgressBarComponent (bien que blocage soit non reproduit)  
28/07/2016  
AnnotationComponent.setAnnotationValeurFichier Remplacement InputStream par AMedia car Stream ne se ferment pas, et erreur observée par C. Avignon lors de l'ouverture d'une centaine de fichier SocketException too many open files.  
01/08/2016  
formatImpressionHtml.xsl remplacement &amp;&nbsp; en &#160; pour impression HMTL7  
28/09/2016  
AbstractObjectTabController postIdsToOtherEntiteTab nettoie doublons ids venant de resultat recherche complexe (Vergely Nîmes + Schummer Grenoble)  

***

##### Version 2.0.13.1  
25/02/2016  
Traçage consultation des dossiers externes  
09/03/2016  
Ajout x-scrollbar terminale et terminale type grande largeur  
10/03/2016  
Ajout flexibilité horizontale tous champs raison non-conformités (B. Leveaux VIROBIOTEC)  
Ajout icônes impact + nonconfs dans tableaux échantillons/dérivés cessions consultation/édition  
16/03/2016  
Correction bug 'Objets associés' CorrespondanceIdManager.execTypedQuery: utilisation HashSet pour éviter doublons ids pour grandes listes (G. Schummer - SEPAGES)  
21/03/2016  
BIOCAP export ajout ligne intermédiaire dans ResulsetToExcel.java  
22/03/2016  
Ajout contrainte ConstWordNoPunct pour application sur noms conteneurs/enceintes/terminales afin d'éviter l'utilisation du '.'  
30/03/2016  
Correction double-click selectPatientModale 'not unique id page'  
Correction CodesController.getSelectedParentNodeFromBrowser si destruction CodeBrowser  
12/05/2016  
Correction export anonymisé C. Girard ResultSetToExcel.writeCell force cellType à Varchar  

***

##### Version 2.0.13  
05/11/2015  
Ajout température stockage dans champEntite pour recherche avancee + complexe (affichage + critere)  
23/11/2015  
Recherche thesaurus multiple risque + patient etat + sexe + annotation  
25/11/2015  
Assistant consentement déja utilisés lors de la création modification/prélèvement  
02/12/2015  
Dessins emplacement: remplacement Img par EmplacementDiv (pile de Div) permettant l'ajout d'un calque coloré fonction du statut de l'objet  
18/12/2015  
Correction Import patient ajout getExistingPatientManager setAllPropertiesForPatient (3B Pereira + Mao)  
21/12/2015  
Correction deleteItem droit isCanDelete AsbtractListController.disableObjetsSelectionItemsdisableObjetsSelectionItems  
03/01/2016  
Ajout Index PATIENT_SIP(NIP) suite à faible performance observée lors récupération 700 000 fichiers HCLs.   
07/01/2016  
Ajout Schedule Spring ExpireAccountScheduler + UtilisaterManager.archiveScheduledUtilisateursManager (modification UtilisateurDao.findByTimeoutBefore)  
22/02/2016  
Correction update Patient onglet List après fusion  
24/02/2016  
Modification interfacage SGL pour intégration echantillon lors import du dossier + non suppression dossier si validation depuis onglet Prélèvement  

***

##### Version 2.0.12b1  
31/07/2015  
Choix du séparateur dans code échantillon/dérivés  
modifications multiples fichier + recherche fichier  
Recherche patient état (sauf depuis Recherche avancée dérivé)  
Ajout NIP dans ListePrelevementRowRendere  
09/09/2015  
Correction modification multiple + effacement non conformités (FicheModifMultiEchantillon, FicheModifMultiPrelevement, FicheModifMultiProdDerive)  
15/10/2015  
Distinction de l'export de type texte pour augmenter le nombre de caractères exportés (demande PELICAN)  
Export.java + export_mysql.sql  
!! Non testé sous Oracle !!  
02/11/2015  
ActiveDirectory Authentification avec expiration compte TK associé (SelectBanqueController.InitWindow)  

***

##### Version 2.0.12  
Import en modification PRELEVEMENT + PATIENT pour projet PELICAN  
16/02/2015  
Refactoring creation derive List Manager  
Import derives derives  

***

##### Version 2.0.11  
Modification générale du système de gestion des fichiers (annotations et CR_ANAPATH) associés.  
27/01/2015  
Modification taille varchar(50) PROD_DERIVE code labo  
Modification taille varchar(20) CONTENEUR piece  
09/02/2015  
Intégration impression RAW Socket & Driver ZPL  

***

##### Version 2.0.10.6  
15/12/2014  
Correction CHAMP_ENTITE ConsentDate type = DATE (Jean-Baptiste GAILLARD CHU Nimes)  
18/12/2014  
Import: import formules format xlsx   
07/01/2015  
Import: choix du feuillet  
12/01/2015  
Correction export nb derives PRELVEMENT/ECHANTILLON/DERIVE (B. Massemin)  
Ajout '=' alphanum validation MOTREGEXP ValidationUtilities  
19/01/2015  
Correction action.historique OperationRenderer l.65 utilisateur_peut être null!  

***

##### Version 2.0.10.5  
Date: 20/11/2014 svn 1542  
Correction opération Destockage seulement validation statut EPUISE dans FicheCessionEdit.  
Export BIOCAP  
01/12/2014 svn 1544  
Etiquettes codes-barres -> 33 chars  
Export cession modification ordre colonne quantite demandée  
02/12/2014  
Correction Export Annotations Dérivés RestrictTableModale getTables  
03/12/2014  
Correction bug ORA-01795 CorrespondanceIdManager execTypedQuery() TypeQuery sur liste toujours < 1000  
Mise à jour des accès catalogues en toutes collections si toutes les collections y ont accès  
04/12/2014  
Correction Export GSO Type echantillon et 022 : TYPE_EVENT export totalité champ (xx : xxxx)  

***

##### Version 2.0.10.4  
Date: 14/11/2014 svn: 1541  
Modifications nulls et taille varchar de champs dans CONTRAT, CESSION, CONTENEUR suite   
à des problèmes rencontrés dans la récupération des données sigbio-bt5 pour le CHU de Nantes  
Date: 18/11/2014  
Finalisation de l'émission des messages lors de l'enregistrement/modification/import   
de prélèvements dans l'interfacage DME demandé par le centre Eugene Marquis de Rennes.   

***

##### Version 2.0.10.3  
Date 10/10/2014 svn: 1537  
Correction recherche annotation numérique dérivé FicheRechercheAvancee  
Ajout de l'interface DME + ConnexionUtils recepteurs  
Ordre échantillons stockage (NEUROBIOTEC)  
Gestion \n modification in-line code échantillons (NEUROBIOTEC)  
Date 17/10/2014  
CorrespondanceManager recupEntiteViaDautres l162 Hibernate.getClass à cause de lazy loading  

***

##### Version 2.0.10.2  
Date 10/09/2014 svn : 1530  
Ajout type Echantillon Copeau (Blandine Massemin)  
Correction affichage des annotations hflex + div remplace grid (Christophe Avignon)  

***

##### Version 2.0.10.1  
Date 10/09/2014 svn : 1529  
Correction Cession dérivé n'ayant unité quantité spécifiée (Bug remonté par Blandine Massemin)  
Ajout des unités discretes à la quantité des dérivés  
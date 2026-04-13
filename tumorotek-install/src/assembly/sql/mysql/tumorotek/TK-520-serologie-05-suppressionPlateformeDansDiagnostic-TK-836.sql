-- reprise de données :
-- on ne garde que les valeurs de la plateforme 1 => modification de DIAGNOSTIC_ID dans MALADIE

UPDATE MALADIE mal 
	inner join DIAGNOSTIC diagAvecPlateforme on diagAvecPlateforme.DIAGNOSTIC_ID = mal.DIAGNOSTIC_ID 
	inner join DIAGNOSTIC diagSansPlateforme on diagSansPlateforme.NOM = diagAvecPlateforme.NOM and diagSansPlateforme.PLATEFORME_ID = 1
set mal.DIAGNOSTIC_ID = diagSansPlateforme.DIAGNOSTIC_ID;


DELETE from DIAGNOSTIC where plateforme_id <> 1;


-- ----------------------------------------------
-- si validation OK, drop de la colonne PLATEFORME_ID (après suppression de la contrainte) cf fichier TK-520-serologie-10-drop.sql
-- ----------------------------------------------

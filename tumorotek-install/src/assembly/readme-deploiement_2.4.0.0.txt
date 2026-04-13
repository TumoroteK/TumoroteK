Cette version supprime l'utilisation de certaines tables dans le cadre du ticket TK-520.
Cela nécessite des traitements de reprise de de données. 
Il est possible de ne pas lancer EN AUTOMATIQUE (via liquibase) le script qui regroupe toutes les suppressions de tables et de colonnes pour éventuellement
faire avant des contrôles sur la reprise de données.
Pour cela, il suffit d'enregistrer une ligne dans la table CESSION_DELEGATE (check du lancement du script par liquibase) qui est vide et sera supprimée par le traitement
INSERT INTO CESSION_DELEGATE values (1, (select min(cession_id) from CESSION));

Il faudra dans ce cas lancer manuellement le script TK-520-serologie-10-drop.sql présent dans le livrable tumorotek-install-2.4.0.0.zip, répertoire sql/mysql/tumorotek 
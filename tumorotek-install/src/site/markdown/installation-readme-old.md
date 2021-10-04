#### Installation de TumoroteK version 2.2.3-fix302
![TumoroteK version 2.2.3-fix302](https://img.shields.io/badge/TumoroteK-2.2.3-fix302-brightgreen.svg "TumoroteK version 2.2.3-fix302")
![Java version 8](https://img.shields.io/badge/Java->=_8-blue.svg "Java version 8")
![Apache Tomcat version 7](https://img.shields.io/badge/Apache_Tomcat->=_7-yellow.svg "Apache Tomcat version 7")

##### Voir le document d'exploitation détaillant l'architecture de l'environnement applicatif

***

##### Installation de l'environnement applicatif :
- MySQL ou MariaDB  
> Paramétrage éventuel : modifier le path du dossier contenant les données dans la variable `datadir` du fichier *`my.cnf`*.

- Java 8 minimum
- Apache Tomcat 7 minimum
- Paramétrage obligatoire : ajouter les variables de démarrage Java `-Duser.language=en -XX:MaxPermSize=256m`  
- Paramétrage conseillé : ajouter les variables de démarrage Java `-Xms512m -Xmx1024m`  
(ces paramétrages sont renseignés directement sous Windows dans l'utilitaire `configureTomcat`, 
sous Unix, dans la variable `CATALINA_OPTS`, par l'intermédiaire du script de démarrage `/etc/init.d/tomcat.sh` par exemple)

***

##### Télécharger puis dézipper le package d'installation :
[Dossier d'installation](https://github.com/TumoroteK/TumoroteK/releases/download/v2.2.3-fix302/tumorotek-install-2.2.3-fix302.zip)


##### Mise en place des bases de données UTF-8 :
1. Base de données 'statiques' contenant les codifications médicales : **tumorotek_codes**

        mysql> create database tumorotek_codes default character set utf8;
    
    Injection du contenu en ligne de commande (depuis le dossier */sql*)
    
        mysql -u root -p tumorotek_codes --default-character-set=utf8 < tumorotek_codes\tumorotek_codes-init.sql

2. Base de données 'temporaires' recevant les données transmises par les interfaçages : **tumorotek_interfacages**

        mysql> create database tumorotek_interfacages default character set utf8;
    
    Injection du contenu en ligne de commande (depuis le dossier */sql*)
    
        mysql -u root -p tumorotek_interfacages --default-character-set=utf8 < tumorotek_interfacages\tumorotek_interfacages-init.sql
        mysql -u root -p tumorotek_interfacages --default-character-set=utf8 < tumorotek_interfacages\tumorotek_interfacages_FK.sql
        mysql -u root -p tumorotek_interfacages --default-character-set=utf8 < tumorotek_interfacages\live_scans.sql

3. Base de données de PRODUCTION : **tumorotek**

        mysql> create database tumorotek default character set utf8;
    
    Injection du contenu en ligne de commande (depuis le dossier */sql*)
    
        mysql -u root -p tumorotek --default-character-set=utf8 < tumorotek\tumorotek-init.sql
        mysql -u root -p tumorotek --default-character-set=utf8 < tumorotek\createINCAtables.sql
        mysql -u root -p tumorotek --default-character-set=utf8 < tumorotek\createSTATStables.sql
    
    Création du compte SuperAdministrateur s'il n'existe pas encore en base (par défaut **login :** ADMIN_TUMO, **mdp :** ADMIN_TUMO)
    
        mysql> insert into tumorotek.UTILISATEUR (utilisateur_id, login, password, archive, timeout, super) values (1, 'ADMIN_TUMO', md5('ADMIN_TUMO'), 0, null, 1);

4. Recommandé : création d'un utilisateur dédié à l'application TumoroteK
    
        mysql> create user tumo@'localhost' identified by 'tumo';
        mysql> GRANT ALL PRIVILEGES ON tumorotek_codes.* TO tumo@'localhost';
        mysql> GRANT ALL PRIVILEGES ON tumorotek_interfacages.* TO tumo@'localhost';
        mysql> GRANT ALL PRIVILEGES ON tumorotek.* TO tumo@'localhost';
    
    Injection des procédures stockées (depuis le dossier */sql*)
    
        mysql -u tumo -p tumorotek --default-character-set=utf8 --delimiter="&&" < tumorotek\charts.sql
        mysql -u tumo -p tumorotek --default-character-set=utf8 --delimiter="&&" < tumorotek\export_biobanques.sql
        mysql -u tumo -p tumorotek --default-character-set=utf8 --delimiter="&&" < tumorotek\export_BIOCAP.sql
        mysql -u tumo -p tumorotek --default-character-set=utf8 --delimiter="&&" < tumorotek\export_INCA.sql
        mysql -u tumo -p tumorotek --default-character-set=utf8 --delimiter="&&" < tumorotek\export_mysql.sql
        mysql -u tumo -p tumorotek --default-character-set=utf8 --delimiter="&&" < tumorotek\export_TGVSO.sql
        mysql -u tumo -p tumorotek --default-character-set=utf8 --delimiter="&&" < tumorotek\getBoite.sql
        mysql -u tumo -p tumorotek --default-character-set=utf8 --delimiter="&&" < tumorotek\indicateurs.sql
    
    Si les procédures sont créées en tant que root... 
    
        mysql> GRANT SELECT ON mysql.proc TO 'tumo'@'localhost';
    
        mysql> flush privileges;
    
    > Cet exemple de création de compte dédié se base sur le principe d'une connexion uniquement en localhost, 
    MySQL et Apache Tomcat étant donc installés sur la même machine, 
    adaptez ces lignes et le niveau de sécurité en cas de l'utilisation d'un serveur de base de données dédié

***

##### Déploiement de l'application TumoroteK
- Arrêter le service Apache Tomcat  
- Déplacer la web archive `tumorotek##2.2.3-fix302.war` dans le dossier `<PATH_TOMCAT>/webapps`
- Déplacer le contenu du dossier `localhost` dans le dossier `<PATH_TOMCAT>/conf/Catalina/localhost`  
- Démarrer le service Apache Tomcat
- Vérifier le bon déploiement de l'application `tumorotek` dans l'interface Manager du Tomcat `http://<SERVEUR>:8080/manager/html`

***

##### Configuration de l'application TumoroteK
- Arrêter l'application `tumorotek` dans l'interface Manager du Tomcat `http://<SERVEUR>:8080/manager/html`
- Le paramétrage de l'application se fait dans le fichier `<PATH_TOMCAT>/conf/Catalina/localhost/tumorotek.properties` :

1. Paramètres de connexion JDBC à la base de PRODUCTION **tumorotek** :

        db.url = jdbc:mysql://localhost:3306/tumorotek?createDatabaseIfNotExist=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&serverTimezone=Europe/Paris
        db.user = tumo
        db.password = tumo

2. Paramètrage du dossier racine des fichiers associés aux données  
    > Il est recommandé pour faciliter les sauvegardes de placer ce dossier dans le même 
    lecteur/dossier que le `datadir` mysql

        # exemple : tk.filesystem = D:/data/TK
        tk.filesystem = <RACINE_FILESYSTEM>

3. Paramètres de connexion JDBC à la base de codifications médicales **tumorotek_codes**
    
        db.codes.url = jdbc:mysql://localhost:3306/tumorotek_codes?createDatabaseIfNotExist=true&characterEncoding=UTF-8&serverTimezone=Europe/Paris
        db.codes.user = tumo
        db.codes.password = tumo

4. Déclarations par défaut des fichiers de emplacements des fichiers de configuration fonctionnelle de TumoroteK :

    - Configuration générale : **tumorotek.properties**
    
            tk.conf.dir = <PATH_ABSOLU_TOMCAT>/conf/Catalina/localhost/
    
    - Configuration connexion directe serveur identités : 
        
            tk.sip.system = <PATH_ABSOLU_TOMCAT>/conf/Catalina/localhost/sip/
    
    - API imprimante modul-bio :
        
            tk.mbio.system = <PATH_ABSOLU_TOMCAT>/conf/Catalina/localhost/mbio/
         
    - Configuration interfacages TK :  
    
            camel.conf.dir = <PATH_ABSOLU_TOMCAT>/conf/Catalina/localhost/camel/

5. Paramètres de connexion JDBC à la base interfaces **tumorotek_interfacages**
    
        db.interfacages.url = jdbc:mysql://localhost:3306/tumorotek_interfacages?createDatabaseIfNotExist=true&characterEncoding=UTF-8&serverTimezone=Europe/Paris
        db.interfacages.user = tumo
        db.interfacages.password = tumo

6. LDAP / ActiveDirectory
    
    L'activation de l'authentification nécessite de remplacer la ligne du fichier <PATH_TOMCAT>/conf/Catalina/localhost/tumorotek.properties suivante
    
    	ldap.authentication = false
    
    par
    
    	ldap.authentication = true
    
    et d'indiquer les paramètres de connexion au LDAP dans les lignes suivantes de ce même fichier :
        
    	activedirectory.url = <URL_LDAP> (exemple : activedirectory.url = ldap://127.0.0.1:389/dc=sls,dc=aphp,dc=fr)
    	ldap.userdn = <USER_LDAP>
    	ldap.password = <MDP_LDAP>

7. Niveaux de logs Log4j
    
    Se référer à la documentation Log4j pour l'édition du fichier `<TOMCAT_PATH>/webapps/tumorotek##2.2.3-fix302/WEB-INF/classes/log4j.properties`
    
    - Démarrer l'application `tumorotek` dans l'interface Manager du Tomcat `http://<SERVEUR>:8080/manager/html`

***

##### Connexion Super Administrateur TK
- **URL d'accès** : http://localhost:8080/tumorotek  
- **Utilisateur** : ADMIN_TUMO  
- **Mot de passe par défaut** <span class="fas fa-exclamation-triangle" aria-hidden="true"></span> A modifier : ADMIN_TUMO

***

##### Premiers paramétrages applicatifs (consulter [le manuel utilisateur](TumoroteK-Manuel.pdf)) :
- Onglet **Administration** > **Collaborations** : créer l'environnement médical (Hôpital, Service, Collaborateurs) responsable de la biobanque
- Onglet **Administration** > **Comptes** : les comptes des utilisateurs du personnel de la biobanque  
> Il est recommandé de confier à un des utilisateurs les droits d'Administration de la Plateforme dans l'onglet **Administration** > **Plateforme** afin que cet 
utilisateur deviennent l'utilisateur 'référent' pour la biobanque.  
Cet utilisateur pourra alors compléter la configuration selon l'activité spécifique de la biobanque 
(collections, thésaurus, conteneurs de stockage, annotations, autres comptes, profils, modèles d'impressions etc...) 

- Onglet **Administration** > **Collection** : créer éventuellement une première collection de travail en attribuant des profils sur la collection aux utilisateurs non administrateurs de plateforme.
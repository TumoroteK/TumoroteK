![TumoroteK version ${project.parent.version}](https://img.shields.io/badge/TumoroteK-${project.parent.version}-brightgreen.svg "TumoroteK version ${project.parent.version}")

#### Installation de TumoroteK version ${project.parent.version}

##### Voir le document d'exploitation détaillant l'architecture de l'environnement applicatif

***

##### Installation de l'environnement applicatif :
- MySQL v5  
> Paramétrage éventuel : modifier le path du dossier contenant les données dans la variable `datadir` du fichier *`my.cnf`*.
- JAVA JSE 1.7 Oracle distribution
- Apache Tomcat 7 
- Paramétrage obligatoire : ajouter les variables de démarrage JAVA `-Duser.language=en -XX:MaxPermSize=256m`  
- Paramétrage conseillé : ajouter les variables de démarrage JAVA `-Xms512m -Xmx1024m`  
(ces paramétrages sont renseignés directement sous Windows dans l'utilitaire `configureTomcat`, 
sous Unix, dans la variable `CATALINA_OPTS`, par l'intermédiaire du script de démarrage `/etc/init.d/tomcat.sh` par exemple)

***

##### Télécharger puis dézipper le package d'installation :
[Dossier d'installation](${project.url}/releases/download/v${project.parent.version}/${project.artifactId}-${project.parent.version}.zip)


##### Mise en place des bases de données UTF-8 :
1. Base de données 'statiques' contenant les codifications médicales : **tumorotek_codes**

        mysql> create database tumorotek_codes default character set utf8;
    
    Injection du contenu en ligne de commande (depuis le dossier */sql*)
    
        mysql -u root -p tumorotek_codes --default-character-set=utf8 < tumorotek_codes\tumorotek_codes-init.sql

2. Base de données 'temporaires' recevant les données transmises par les interfacages : **tumorotek_interfacages**

        mysql> create database tumorotek_interfacages default character set utf8;
    
    Injection du contenu en ligne de commande (depuis le dossier */sql*)
    
        mysql -u root -p tumorotek_interfacages --default-character-set=utf8 < tumorotek_interfacages\tumorotek_interfacages-init.sql
        mysql -u root -p tumorotek_interfacages --default-character-set=utf8 < tumorotek_interfacages\tumorotek_interfacages_FK.sql
        mysql -u root -p tumorotek_interfacages --default-character-set=utf8 < tumorotek_interfacages\live_scans.sql

3. Base de données de PRODUCTION : **tumorotek**

        mysql> create database tumorotek default character set utf8;
    
    Injection du contenu en ligne de commande (depuis le dossier */sql*)
    
        mysql -u root -p tumorotek --default-character-set=utf8 < tumorotek\tumorotek-init.sql
    
    > MySQL est sensible à la casse du noms des tables sous Linux, le script `renametables.sql` renomme toutes les tables en majuscules
    
    Création du compte SuperAdministrateur s'il n'existe pas encore en base (par défaut **login :** ADMIN_TUMO, **mdp :** tk4[teAm])
    
        mysql> insert into tumorotek.UTILISATEUR (utilisateur_id, login, password, archive, timeout, super) values (1, 'ADMIN_TUMO', md5('tk4[teAm]'), 0, null, 1);

4. Recommandé : création d'un utilisateur dédié à l'application TumoroteK
    
        mysql> create user tumo@'localhost' identified by 'tumo';
        mysql> GRANT ALL PRIVILEGES ON tumorotek_codes.* TO tumo@'localhost';
        mysql> GRANT ALL PRIVILEGES ON tumorotek_interfacages.* TO tumo@'localhost';
        mysql> GRANT ALL PRIVILEGES ON tumorotek.* TO tumo@'localhost';
    
    Injection des procédures stockées (depuis le dossier */sql*)
    
        mysql -u tumo -p tumorotek --default-character-set=utf8 < tumorotek\export_mysql.sql
        mysql -u tumo -p tumorotek --default-character-set=utf8 < tumorotek\export_INCA.sql
        mysql -u tumo -p tumorotek --default-character-set=utf8 < tumorotek\export_BIOCAP.sql
        mysql -u tumo -p tumorotek --default-character-set=utf8 < tumorotek\export_TGVSO.sql
        mysql -u tumo -p tumorotek --default-character-set=utf8 < tumorotek\charts.sql
        mysql -u tumo -p tumorotek --default-character-set=utf8 < tumorotek\getBoite.sql
        mysql -u tumo -p tumorotek --default-character-set=utf8 < tumorotek\indicateurs.sql
    
    Si les procédures sont créées en tant que root... 
    
        mysql> GRANT SELECT ON mysql.proc TO 'tumo'@'localhost';
    
        mysql> flush privileges;
    
    > Cet exemple de création de compte dédié se base sur le principe d'une connexion uniquement en localhost, 
    MySQL et Apache Tomcat étant donc installés sur la même machine, 
    adaptez ces lignes et le niveau de sécurité en cas de l'utilisation d'un serveur de base de données dédié

***

##### Déploiement de l'application TumoroteK
- Arrêter le service Apache Tomcat  
- Déplacer la web archive `${webapp.packaging.finalName}.war` dans le dossier `<PATH_TOMCAT>/webapps`
- Déplacer le contenu du dossier `localhost` dans le dossier `<PATH_TOMCAT>/conf/Catalina/localhost`  
- Démarrer le service Apache Tomcat
- Vérifier le bon déploiement de l'application `${webapp.name}` dans l'interface Manager du Tomcat `http://<SERVEUR>:8080/manager/html`

***

##### Configuration de l'application TumoroteK
- Arrêter l'application `${webapp.name}` dans l'interface Manager du Tomcat `http://<SERVEUR>:8080/manager/html`
- Edition des variables d'environnement JNDI dans le fichier `<PATH_TOMCAT>/conf/Catalina/localhost/${webapp.packaging.finalName}.xml` :

1. Paramètres de connexion JDBC à la base de PRODUCTION **tumorotek** :

        <Environment name="/jdbc/url" ... value="jdbc:mysql://localhost:3306/tumorotek?characterEncoding=UTF-8&amp;rewriteBatchedStatements=true" ... />
        <Environment name="/jdbc/user" ... value="tumo" .../>
        <Environment name="/jdbc/password" ... value="tumo" .../>

2. Paramètrage du dossier racine des fichiers associés aux données  
> Il est recommandé pour faciliter les sauvegardes de placer ce dossier dans le même 
lecteur/dossier que le `datadir` mysql

        <Environment name="/tk/tkFileSystem" ... value="D://data/TK/" ... />

3. Paramètres de connexion JDBC à la base de codifications médicales **tumorotek_codes**
    
        <Environment name="/codes/jdbc/url" ... value="jdbc:mysql://localhost:3306/tumorotek_codes?characterEncoding=UTF-8" ... />
        <Environment name="/codes/jdbc/user" ... value="tumo" ... />
        <Environment name="/codes/jdbc/password" ... value="tumo" ... />

4. Déclarations par défaut des fichiers de emplacements des fichiers de configuration fonctionnelle de TumoroteK :

    - Configuration générale : **tumorotek.properties**
    
            <Environment name="/tk/tkTumoPropertiesSystem" ... value="<PATH_ABSOLU_TOMCAT>/conf/Catalina/localhost/" /> 
    
    - Configuration connexion directe serveur identités : 
        
            <Environment name="/tk/tkSipSystem" ... value="<PATH_ABSOLU_TOMCAT>/conf/Catalina/localhost/sip/" /> 
    
    - API imprimante modul-bio :
        
            <Environment name="/tk/tkMbioSystem" ... value="<PATH_ABSOLU_TOMCAT>/conf/Catalina/localhost/mbio/" />
         
    - Configuration interfacages TK :  
    
            <Environment name="/interfacage/conf/location" ... value="<PATH_ABSOLU_TOMCAT>/conf/Catalina/localhost/camel/" />

5. Paramètres de connexion JDBC à la base interfaces
    
        <Environment name="/interfacages/jdbc/url" ... value="jdbc:mysql://localhost:3306/tumorotek_interfacages?characterEncoding=UTF-8" ... />
        <Environment name="/interfacages/jdbc/user" ... value="tumo" ... />
        <Environment name="/interfacages/jdbc/password" ... value="tumo" ... />

6. LDAP / ActiveDirectory
    
    > L'authentification par annuaire implique l'édition du bloc `<authentication-manager>` dans le fichier 
    `<TOMCAT_PATH>/webapps/${webapp.name}/WEB-INF/classes/applicationContext-security.xml` afin de commenter/décommenter 
    les différents systèmes d'authentifications, qui seront consultés selon leur ordre dans ce bloc de 
    configuration
    
    - LDAP
    
        <Environment name="/ldap/url" ... value="ldap://127.0.0.1:389/dc=sls,dc=aphp,dc=fr" ... />
        
    - ActiveDirectory
    
        <Environment name="/activedirectory/domain" ... value="<DOMAINE>" ... />  
        <Environment name="/activedirectory/url" ... value="ldap://<HOST>:<PORT>/" ... />  

7. Niveaux de logs Log4j
    
    Se référer à la documentation Log4j pour l'édition du fichier `<TOMCAT_PATH>/webapps/${webapp.name}/WEB-INF/classes/log4j.properties`
    
    - Démarrer l'application `${webapp.name}` dans l'interface Manager du Tomcat `http://<SERVEUR>:8080/manager/html`

***

##### Connexion Super Administrateur TK
- **URL** : http://localhost:8080/${webapp.name}  
- **login** : ADMIN_TUMO  
- **pass** : tk4\[teAm]

***

##### Premiers paramétrages applicatifs (consulter le manuel utilisateur accessible depuis la page d'accueil) :
- Onglet **Administration** > **Collaborations** : créer l'environnement médical (Hôpital, Service, Collaborateurs) responsable de la biobanque
- Onglet **Administration** > **Comptes** : les comptes des utilisateurs du personnel de la biobanque  
> Il est recommandé de confier à un des utilisateurs les droits d'Administration de la Plateforme dans l'onglet **Administration** > **Plateforme** afin que cet 
utilisateur deviennent l'utilisateur 'référent' pour la biobanque. 
Cet utilisateur pourra alors compléter la configuration selon l'activité spécifique de la biobanque 
(collections, thésaurus, conteneurs de stockage, annotations, autres comptes, profils, modèles d'impressions etc...) 
- Onglet **Administration** > **Collection** : créer éventuellement une première collection de travail en attribuant des profils sur la collection aux utilisateurs non administrateurs de plateforme.
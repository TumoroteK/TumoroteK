#### Installation de TumoroteK version 2.2.3.2-rc1
![TumoroteK version 2.2.3.2-rc1](https://img.shields.io/badge/TumoroteK-2.2.3.2-rc1-brightgreen.svg "TumoroteK version 2.2.3.2-rc1")
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

##### A. Télécharger puis dézipper le package d'installation :
[Dossier d'installation](https://github.com/TumoroteK/TumoroteK/releases/download/v2.2.3.2-rc1/tumorotek-install-2.2.3.2-rc1.zip)


##### B. Recommandé : création d'un utilisateur de base de données dédié à l'application TumoroteK + autorisations sur les trois bases de données
    
    mysql> create user tumo@'localhost' identified by 'tumo';
    mysql> GRANT ALL PRIVILEGES ON tumorotek_codes.* TO tumo@'localhost';
    mysql> GRANT ALL PRIVILEGES ON tumorotek_interfacages.* TO tumo@'localhost';
    mysql> GRANT ALL PRIVILEGES ON tumorotek.* TO tumo@'localhost';
    
> Cet exemple de création de compte dédié se base sur le principe d'une connexion uniquement en `localhost`, 
MySQL et Apache Tomcat étant donc installés sur la même machine, 
adaptez ces lignes et le niveau de sécurité en cas de l'utilisation d'un serveur de base de données dédié

##### C. Préparation au déploiement de l'application TumoroteK
1. Arrêter le service Apache Tomcat  
2. Déplacer le contenu du dossier `localhost` dans le dossier `<PATH_TOMCAT>/conf/Catalina/localhost`
    > <span class="fas fa-exclamation-triangle" aria-hidden="true"></span> Pour que l'application fonctionne correctement, le fichier `tumorotek.properties` ne doit pas être déplacé ou renommé. 
3. Modification du fichier de paramétrage de l'application `<PATH_TOMCAT>/conf/Catalina/localhost/tumorotek.properties` :

    1. Paramètres de connexion JDBC à la base de PRODUCTION **tumorotek** :
    
            db.url = jdbc:mysql://localhost:3306/tumorotek?createDatabaseIfNotExist=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&serverTimezone=Europe/Paris
            db.user = tumo
            db.password = tumo
    
    2. Paramètres de connexion JDBC à la base de codifications médicales **tumorotek_codes**
        
            db.codes.url = jdbc:mysql://localhost:3306/tumorotek_codes?createDatabaseIfNotExist=true&characterEncoding=UTF-8&serverTimezone=Europe/Paris
            db.codes.user = tumo
            db.codes.password = tumo
            
    3. Paramètres de connexion JDBC à la base interfaces **tumorotek_interfacages**
            
            db.interfacages.url = jdbc:mysql://localhost:3306/tumorotek_interfacages?createDatabaseIfNotExist=true&characterEncoding=UTF-8&serverTimezone=Europe/Paris
            db.interfacages.user = tumo
            db.interfacages.password = tumo
            
    4. Paramètrage du dossier racine des fichiers associés aux données  
        > Il est recommandé pour faciliter les sauvegardes de placer ce dossier dans le même 
        lecteur/dossier que le `datadir` mysql
    
            # exemple : tk.filesystem = D:/data/TK
            tk.filesystem = <RACINE_FILESYSTEM>
    
    5. Emplacements des fichiers de configuration fonctionnelle de TumoroteK :
    
        - Racine des fichiers de configuration de l'application : 
        
                tk.conf.dir = <PATH_ABSOLU_TOMCAT>/conf/Catalina/localhost/
        
        - Configuration connexion directe serveur identités : 
            
                tk.sip.system = <PATH_ABSOLU_TOMCAT>/conf/Catalina/localhost/sip/
        
        - API imprimante modul-bio :
            
                tk.mbio.system = <PATH_ABSOLU_TOMCAT>/conf/Catalina/localhost/mbio/
             
        - Configuration interfacages TK :  
        
                camel.conf.dir = <PATH_ABSOLU_TOMCAT>/conf/Catalina/localhost/camel/
    
    6. LDAP / ActiveDirectory
        
        L'activation de l'authentification nécessite de remplacer la ligne du fichier `<PATH_TOMCAT>/conf/Catalina/localhost/tumorotek.properties` suivante
        
            ldap.authentication = false
        
         par
        
            ldap.authentication = true
        
        et d'indiquer les paramètres de connexion au LDAP dans les lignes suivantes de ce même fichier :
            
            activedirectory.url = <URL_LDAP> (exemple : activedirectory.url = ldap://127.0.0.1:389/dc=sls,dc=aphp,dc=fr)
            ldap.userdn = <USER_LDAP>
            ldap.password = <MDP_LDAP>
        
4. Niveaux de logs Log4j
    
    Se référer à la documentation Log4j pour l'édition du fichier `<TOMCAT_PATH>/webapps/tumorotek##2.2.3.2-rc1/WEB-INF/classes/log4j.properties`

5. Déplacer la web archive `tumorotek##2.2.3.2-rc1.war` dans le dossier `<PATH_TOMCAT>/webapps`  
6. Démarrer le service Apache Tomcat
    > L'application TumoroteK va créer et initialiser les bases de données (tumorotek, tumorotek_codes et tumorotek_interfacages) automatiquement.
    
7. Vérifier le bon déploiement de l'application `tumorotek` dans l'interface Manager du Tomcat `http://<SERVEUR>:8080/manager/html`

***

##### D. Connexion Super Administrateur TK
- **URL d'accès** : http://localhost:8080/tumorotek  
- **Utilisateur** : ADMIN_TUMO  
- **Mot de passe par défaut** <span class="fas fa-exclamation-triangle" aria-hidden="true"></span> A modifier : ADMIN_TUMO

***

##### E. Premiers paramétrages applicatifs (consulter [le manuel utilisateur](TumoroteK-Manuel.pdf)) :
- Onglet **Administration** > **Collaborations** : créer l'environnement médical (Hôpital, Service, Collaborateurs) responsable de la biobanque
- Onglet **Administration** > **Comptes** : les comptes des utilisateurs du personnel de la biobanque  
> Il est recommandé de confier à un des utilisateurs les droits d'Administration de la Plateforme dans l'onglet **Administration** > **Plateforme** afin que cet 
utilisateur deviennent l'utilisateur 'référent' pour la biobanque.  
Cet utilisateur pourra alors compléter la configuration selon l'activité spécifique de la biobanque 
(collections, thésaurus, conteneurs de stockage, annotations, autres comptes, profils, modèles d'impressions etc...) 

- Onglet **Administration** > **Collection** : créer éventuellement une première collection de travail en attribuant des profils sur la collection aux utilisateurs non administrateurs de plateforme.
#### Mise à jour de TumoroteK version 2.3.0.3-SNAPSHOT
![TumoroteK version 2.3.0.3-SNAPSHOT](https://img.shields.io/badge/TumoroteK-2.3.0.3-SNAPSHOT-brightgreen.svg "TumoroteK version 2.3.0.3-SNAPSHOT")
![Java version 8](https://img.shields.io/badge/Java->=_8-blue.svg "Java version 8")
![Apache Tomcat version 7](https://img.shields.io/badge/Apache_Tomcat->=_7-yellow.svg "Apache Tomcat version 7")

**<span class="fas fa-exclamation-triangle" aria-hidden="true"></span> Utiliser ce README uniquement pour mettre à jour l'application depuis la version 2.1.1 ou supérieure.**

***

##### A. Modification du paramétrage du serveur Tomcat pour utiliser Java 8
1. Arrêter le service Apache Tomcat (prévoir avec vos utilisateurs une heure d'interruption)

2. Ajouter ou modifier la variable `JAVA_HOME` en y indiquant le chemin vers Java 8

    Exemple dans le fichier `setenv.sh` :
        
        export JAVA_HOME=/opt/java8
          
##### B. Mise à jour de l'application
> Préférer la mise à jour dans des périodes creuses.  
> La mise à jour est censée être transparente pour les utilisateurs :   
> Une fois effectuée, ils seront automatiquement redirigés vers la nouvelle version lors d'une reconnexion.

1. Aller dans le dossier `<PATH_TOMCAT>/conf/Catalina/localhost`  

2. **Dupliquer** le fichier `tumorotek##x.x.x.xml`.  
Renommer la copie en :
    
        tumorotek##2.3.0.3-SNAPSHOT.xml
        
3. Modifier dans ce même fichier `tumorotek##2.3.0.3-SNAPSHOT.xml`, les variables suivantes :

    - **/jdbc/driverClass**
    
        Nouvelle valeur : `com.mysql.cj.jdbc.Driver`
        
        Exemple : 
        
            <Context>
                ...
                    <Environment name="/jdbc/driverClass" type="java.lang.String" value="com.mysql.cj.jdbc.Driver" override="false"/>
                ...
            </Context>

    - **/codes/jdbc/driverClass**
    
        Nouvelle valeur : `com.mysql.cj.jdbc.Driver`
                
        Exemple : 
        
            <Context>
                ...
                    <Environment name="/codes/jdbc/driverClass" type="java.lang.String" value="com.mysql.cj.jdbc.Driver" override="false"/>
                ...
            </Context>    
    
    - **/interfacages/jdbc/driverClass**
    
        Nouvelle valeur : `com.mysql.cj.jdbc.Driver`
                        
        Exemple : 
        
            <Context>
                ...
                    <Environment name="/interfacages/jdbc/driverClass" type="java.lang.String" value="com.mysql.cj.jdbc.Driver" override="false"/>
                ...
            </Context>  

    - **/jdbc/dialect**
    
        Nouvelle valeur : `org.hibernate.dialect.MySQL5Dialect`
        
        Exemple : 
        
            <Context>
                ...
                    <Environment name="/jdbc/dialect" type="java.lang.String" value="org.hibernate.dialect.MySQL5Dialect" override="false"/>
                ...
            </Context>

    - **/codes/jdbc/dialect**
    
        Nouvelle valeur : `org.hibernate.dialect.MySQL5Dialect`
                
        Exemple : 
        
            <Context>
                ...
                    <Environment name="/codes/jdbc/dialect" type="java.lang.String" value="org.hibernate.dialect.MySQL5Dialect" override="false"/>
                ...
            </Context>    
    
    - **/interfacages/jdbc/dialect**
    
        Nouvelle valeur : `org.hibernate.dialect.MySQL5Dialect`
                        
        Exemple : 
        
            <Context>
                ...
                    <Environment name="/interfacages/jdbc/dialect" type="java.lang.String" value="org.hibernate.dialect.MySQL5Dialect" override="false"/>
                ...
            </Context>  
               
    - **/jdbc/url**
    
        Ajouter un paramètre à la fin de l'URL : `&amp;serverTimezone=Europe/Paris`
        
        Exemple : 
                
            <Context>
                ...
                    <Environment name="/jdbc/url" type="java.lang.String" value="jdbc:mysql://localhost:3306/tumorotek?characterEncoding=UTF-8&amp;rewriteBatchedStatements=true&amp;serverTimezone=Europe/Paris" override="false"/>
                ...
            </Context>  

    - **/codes/jdbc/url**
    
        Ajouter un paramètre à la fin de l'URL : `&amp;serverTimezone=Europe/Paris`
            
        Exemple : 
                
            <Context>
                ...
                    <Environment name="/codes/jdbc/url" type="java.lang.String" value="jdbc:mysql://localhost:3306/tumorotek_codes?characterEncoding=UTF-8&amp;serverTimezone=Europe/Paris" override="false"/>
                ...
            </Context>  

    - **/interfacages/jdbc/url**
    
        Ajouter un paramètre à la fin de l'URL : `&amp;serverTimezone=Europe/Paris`
                
        Exemple : 
                
            <Context>
                ...
                    <Environment name="/interfacages/jdbc/url" type="java.lang.String" value="jdbc:mysql://localhost:3306/tumorotek_interfacages?characterEncoding=UTF-8&amp;serverTimezone=Europe/Paris" override="false"/>
                ...
            </Context> 

4. Télécharger le [Dossier d'installation](https://github.com/TumoroteK/TumoroteK/releases/download/v2.3.0.3-SNAPSHOT/tumorotek-install-2.3.0.3-SNAPSHOT.zip)

5. Dézipper le dossier

6. Copier/déplacer l'archive `tumorotek##2.3.0.3-SNAPSHOT.war` sur le serveur Tomcat dans le dossier `<PATH_TOMCAT>/webapps`

    <span class="fas fa-exclamation-triangle" aria-hidden="true"></span> A partir de cette version, la configuration de l'application est portée par le fichier `<PATH_TOMCAT>/conf/Catalina/localhost/tumorotek.properties`.  
    A cette étape de l'installation, le contenu du fichier `tumorotek##2.3.0.3-SNAPSHOT.xml` est automatiquement reporté dans `tumorotek.properties` et le fichier `tumorotek##2.3.0.3-SNAPSHOT.xml` est supprimé.

	<span class="fas fa-exclamation-triangle" aria-hidden="true"></span> Si l'authentification LDAP est utilisée, remplacer la ligne du fichier `<PATH_TOMCAT>/conf/Catalina/localhost/tumorotek.properties` suivante
    
    	ldap.authentication = false
    
	par
    
    	ldap.authentication = true

7. Dans le Manager du Tomcat `http://<SERVEUR>:8080/manager/html` vérifier que l'application dans sa dernière version est bien déployée et démarrée
    - Si OK et si les utilisateurs se sont déconnectés, vous pouvez retirer l'ancienne version de TumoroteK depuis l'interface du Manager 
    - Si KO, analyser les logs dans le dossier `<PATH_TOMCAT>/logs` :
        - localhost.xxx.log
        - catalina.xxx.log
        - tumorotek_trace.log

    > Si vous n'arrivez pas à résoudre le problème, l'ancienne version de TumoroteK devrait être active.

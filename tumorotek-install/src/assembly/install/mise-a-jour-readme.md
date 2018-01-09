![TumoroteK version ${project.parent.version}](https://img.shields.io/badge/TumoroteK-${project.parent.version}-brightgreen.svg "TumoroteK version ${project.parent.version}")

#### Mise à jour de TumoroteK version ${project.parent.version}  

**<span class="fa fa-exclamation-triangle" aria-hidden="true"></span> Utiliser ce README uniquement pour passer de la version 2.1.1 vers 2.1.3.**  
**Si le README de la mise à jour de la 2.1.2 a déjà été passé, il faut effectuer seulement les étapes B.1, B.3, B.4, B.5 et B.7.**

***

##### A. Modification du paramétrage du serveur Tomcat pour permettre la mise à jour automatique de la base de données
1. Arrêter le service Apache Tomcat (prévoir avec vos utilisateurs une heure d'interruption)

2. Ajouter dans `<PATH_TOMCAT>/conf/server.xml`, la partie `<Resource ... />` :  
Pensez à modifier `url="jdbc:mysql://localhost:3306/tumorotek"` si votre base de données ne 
s'appelle pas `tumorotek` mais `tumo2` et/ou si l'adresse et le port ne correspondent pas.

        <GlobalNamingResources>
            ...
                <Resource name="jdbc/TumoroteK" 
                      global="jdbc/TumoroteK" 
                      auth="Container" 
                      type="javax.sql.DataSource" 
                      driverClassName="com.mysql.jdbc.Driver" 
                      url="jdbc:mysql://localhost:3306/tumorotek" 
                      username="tumo" 
                      password="tumo" 
                      
                      maxTotal="100" 
                      maxIdle="20" 
                      minIdle="5" 
                      maxWaitMillis="-1"/>
              ...
          </GlobalNamingResources>

##### B. Mise à jour de l'application
1. Dupliquer le fichier `${webapp.name}##x.x.x.xml`.  
Renommer la copie en :
    
        ${webapp.packaging.finalName}.xml
    
2. Ajouter dans ce même fichier `${webapp.packaging.finalName}.xml`, la partie `<ResourceLink ... />` et `<context-param>...</context-param>` :

        <Context>
            ...
                <ResourceLink name="jdbc/TumoroteK-1" global="jdbc/TumoroteK" auth="Container" type="javax.sql.DataSource"/>
                <context-param>
                    <param-name>liquibase.contexts</param-name>
                    <param-value>${project.parent.version}</param-value>
                </context-param>
            ...
        </Context>

3. Télécharger le [Dossier d'installation](${project.url}/releases/download/v${project.parent.version}/${project.artifactId}-${project.parent.version}.zip)

4. Dézipper le dossier

5. Copier/déplacer l'archive `${webapp.packaging.finalName}.war` sur le serveur Tomcat dans le dossier `<PATH_TOMCAT>/webapps`

6. Redémarrer le service Apache Tomcat

7. Dans le Manager du Tomcat `http://<SERVEUR>:8080/manager/html` vérifier que l'application dans sa dernière version est bien déployée et démarrée
    - Si OK, vous pouvez retirer l'ancienne version de TumoroteK depuis l'interface du Manager
    - Si KO, analyser les logs dans le dossier `<PATH_TOMCAT>/logs` :
        - localhost.xxx.log
        - catalina.xxx.log
        - tumorotek_trace.log

    > Si vous n'arrivez pas à résoudre le problème, l'ancienne version de TumoroteK devrait être active.
![TumoroteK version ${project.version}](https://img.shields.io/badge/TumoroteK-${project.version}-brightgreen.svg "TumoroteK version ${project.version}")

#### Mise à jour de TumoroteK version ${parent.version}  

**<span class="fa fa-exclamation-triangle" aria-hidden="true"></span> Mise à jour depuis la version précédente**

***

##### Renommage du fichier tumo2.properties
Sur le serveur Tomcat, dans le dossier `/conf/Catalina/localhost`, dupliquer le fichier `tumo2.propeties`.  
Renommer la copie en :  

    tumorotek.properties
        
##### Renommage du fichier tumo2.xml
Sur le serveur Tomcat, dans le dossier `/conf/Catalina/localhost`, dupliquer le fichier `tumo2.xml`.  
Renommer la copie en :  
   
    ${webapp.packaging.finalName}.xml

##### Déploiement de la nouvelle version de TumoroteK  
Sur le serveur Tomcat, dans le dossier `/webapps`, déposer l'archive `${webapp.packaging.finalName}.war`

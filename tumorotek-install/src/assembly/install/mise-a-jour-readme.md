![TumoroteK version ${project.version}](https://img.shields.io/badge/TumoroteK-${project.version}-brightgreen.svg "TumoroteK version ${project.version}")

#### Mise à jour de TumoroteK version ${parent.version}  

**<span class="fa fa-exclamation-triangle" aria-hidden="true"></span> Mise à jour depuis la version précédente : 2.1**

***

#### Mise à jour automatique
Ajouter dans \<TOMCAT\>/conf/server.xml :  

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
                  
                  maxActive="100" 
                  maxIdle="20" 
                  minIdle="5" 
                  maxWait="10000"/>
          ...
      </GlobalNamingResources>

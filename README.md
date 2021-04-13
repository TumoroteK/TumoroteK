![TumoroteK](tumorotek-webapp/src/main/webapp/images/logo_tumo.png "Logo de TumoroteK")

Logiciel de gestion des collections de prélèvements biologiques

## Commencer

Ces instructions vous permettront d'obtenir une copie du projet opérationnel sur votre machine locale à des fins de développement et de test.

### Prérequis

Télécharger le `.jar` correspondant à la dépendance pour Oracle :

    <groupId>oracle</groupId>
    <artifactId>ojdbc6</artifactId>
    <version>11.2.0.4</version>

Ensuite ajouter cette dépendance avec Maven :

    mvn install:install-file -Dfile=D:/Downloads/ojdbc6.jar -DgroupId=oracle -DartifactId=ojdbc6 -Dversion=11.2.0.4 -Dpackaging=jar

### Pour compiler le projet et générer une archive déposable sur le Tomcat
Exécuter la commande Maven :

    mvn install
    
L'archive est disponible dans `tumorotek-webapp/target`

#### Pour lancer les tests
Utiliser une base MySQL ou MariaDB et vérifier les coordonnées d'accès à la base dans le [pom.xml](pom.xml).
    
Ensuite exécuter la commande Maven :

    mvn test

## License

TumoroteK est sous licence GPL (CeCILL) - voir la [licence](tumorotek-webapp/Licence_fr.txt) pour plus de détails
# TumoroteK

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
Désactiver les tests dans le [pom.xml](tumorotek-model/pom.xml), la propriété `skip-test` mettre à true :

    <properties>
        ...
        <skip-test>true</skip-test>
    </properties>

Ensuite faire :

    mvn install
    
L'archive est disponible dans `tumorotek-webapp/target`

#### Pour lancer les tests
Vérifier que dans le [pom.xml](tumorotek-model/pom.xml), la propriété `skip-test` est à false :

    <properties>
        ...
        <skip-test>false</skip-test>
    </properties>

De plus, vérifier que les bases de données de test sont accessibles :
- test_tumorotek
- test_tumorotek_codes
- test_tumorotek_interfacages
    
Ensuite faire :

    mvn test

## License

TumoroteK est sous licence GPL (CeCILL) - voir la [licence](tumorotek-webapp/Licence_fr.txt) pour plus de détails
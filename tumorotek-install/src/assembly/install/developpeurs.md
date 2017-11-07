Avoir le .jar sur le disque  
Pour ajouter la dépendance de ojdbc

Add a comment to this line

    mvn install:install-file -Dfile=/Users/julien/Desktop/ojdbc14.jar -DgroupId=com.oracle -DartifactId=ojdbc14 -Dversion=10.2.0.4.0 -Dpackaging=jar



    create database testtumo2 default character set utf8;
    
    mysql -u root -p testtumo2 --default-character-set=utf8 < D:\Projets\TUMOROTEK\tumorotek\tumorotek-install\src\assembly\sql\tumo2\dumpTumo21.sql
    
    mysql -u root -p testtumo2 --default-character-set=utf8 < D:\Projets\TUMOROTEK\tumorotek\tumorotek-install\src\assembly\sql\tumo2\export_mysql.sql
    mysql -u root -p testtumo2 --default-character-set=utf8 < D:\Projets\TUMOROTEK\tumorotek\tumorotek-install\src\assembly\sql\tumo2\export_INCA.sql
    mysql -u root -p testtumo2 --default-character-set=utf8 < D:\Projets\TUMOROTEK\tumorotek\tumorotek-install\src\assembly\sql\tumo2\export_BIOCAP.sql
    mysql -u root -p testtumo2 --default-character-set=utf8 < D:\Projets\TUMOROTEK\tumorotek\tumorotek-install\src\assembly\sql\tumo2\export_TGVSO.sql
    mysql -u root -p testtumo2 --default-character-set=utf8 < D:\Projets\TUMOROTEK\tumorotek\tumorotek-install\src\assembly\sql\tumo2\charts.sql
    mysql -u root -p testtumo2 --default-character-set=utf8 < D:\Projets\TUMOROTEK\tumorotek\tumorotek-model\src\database\mysql\getBoite.sql
    
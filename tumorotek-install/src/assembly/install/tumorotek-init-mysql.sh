#!/bin/sh

read -p 'Username for MySQL: ' user_mysql
read -sp 'Password for MySQL: ' password_mysql
echo
echo Thank you $user_mysql we now have your MySQL login details

# tumorotek_codes
#echo
#read -p 'Nom de la base de donnees tumorotek_codes ? ' tumorotek_codes
tumorotek_codes=${tumorotek_codes:-tumorotek_codes}
#echo OK, for tumorotek_codes -\> $tumorotek_codes

# tumorotek_interfacages
#echo
#read -p 'Nom de la base de donnees tumorotek_interfacages ? ' tumorotek_interfacages
tumorotek_interfacages=${tumorotek_interfacages:-tumorotek_interfacages}
#echo OK, for tumorotek_interfacages -\> $tumorotek_interfacages

# tumorotek
echo
read -p 'Nom de la base de donnees tumorotek ? ' tumorotek
tumorotek=${tumorotek:-tumorotek}
echo OK, for tumorotek -\> $tumorotek

if ! mysql -u $user_mysql -p$password_mysql -e "use $tumorotek_codes"; then
    echo
    echo Creation de la base de donnees : $tumorotek_codes
    echo mysql -u $user_mysql -p$password_mysql -e "create database $tumorotek_codes default character set utf8";
    mysql -u $user_mysql -p$password_mysql -e "create database $tumorotek_codes default character set utf8";

    echo
    echo Ajout des donnees
    echo mysql -u $user_mysql -p$password_mysql $tumorotek_codes --default-character-set=utf8 \< tumorotek_codes/tumorotek_codes-init.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek_codes --default-character-set=utf8 < tumorotek_codes/tumorotek_codes-init.sql
else
    echo
    echo La base de donnees $tumorotek_codes existe deja.
    echo Aucune modification pour cette base n\'a ete faite.
fi

if ! mysql -u $user_mysql -p$password_mysql -e "use $tumorotek_interfacages"; then
    echo
    echo Creation de la base de donnees : $tumorotek_interfacages
    echo mysql -u $user_mysql -p$password_mysql -e "create database $tumorotek_interfacages default character set utf8";
    mysql -u $user_mysql -p$password_mysql -e "create database $tumorotek_interfacages default character set utf8";

    echo
    echo Ajout des donnees
    echo mysql -u $user_mysql -p$password_mysql $tumorotek_interfacages --default-character-set=utf8 \< tumorotek_interfacages/tumorotek_interfacages-init.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek_interfacages --default-character-set=utf8 < tumorotek_interfacages/tumorotek_interfacages-init.sql
    echo mysql -u $user_mysql -p$password_mysql $tumorotek_interfacages --default-character-set=utf8 \< tumorotek_interfacages/tumorotek_interfacages_FK.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek_interfacages --default-character-set=utf8 < tumorotek_interfacages/tumorotek_interfacages_FK.sql
    echo mysql -u $user_mysql -p$password_mysql $tumorotek_interfacages --default-character-set=utf8 \< tumorotek_interfacages/live_scans.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek_interfacages --default-character-set=utf8 < tumorotek_interfacages/live_scans.sql
else
    echo
    echo La base de donnees $tumorotek_interfacages existe deja.
    echo Aucune modification pour cette base n\'a ete faite.
fi

if ! mysql -u $user_mysql -p$password_mysql -e "use $tumorotek"; then
    echo
    echo Creation de la base de donnees : $tumorotek
    echo mysql -u $user_mysql -p$password_mysql -e "create database $tumorotek default character set utf8";
    mysql -u $user_mysql -p$password_mysql -e "create database $tumorotek default character set utf8";

    echo
    echo Ajout des donnees
    echo mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 \< tumorotek/tumorotek-init.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 < tumorotek/tumorotek-init.sql
    echo mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 \< tumorotek/export_mysql.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 < tumorotek/export_mysql.sql
    echo mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 \< tumorotek/export_INCA.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 < tumorotek/export_INCA.sql
    echo mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 \< tumorotek/export_BIOCAP.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 < tumorotek/export_BIOCAP.sql
    echo mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 \< tumorotek/export_TGVSO.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 < tumorotek/export_TGVSO.sql
    echo mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 \< tumorotek/charts.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 < tumorotek/charts.sql
    echo mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 \< tumorotek/getBoite.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 < tumorotek/getBoite.sql
    echo mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 \< tumorotek/indicateurs.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 < tumorotek/indicateurs.sql
    echo mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 \< tumorotek/createSTATStables.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 < tumorotek/createSTATStables.sql
    echo mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 \< tumorotek/add-bto-context.sql
    mysql -u $user_mysql -p$password_mysql $tumorotek --default-character-set=utf8 < tumorotek/add-bto-context.sql
else
    echo
    echo La base de donnees $tumorotek existe deja.
    echo Aucune modification pour cette base n\'a ete faite.
fi
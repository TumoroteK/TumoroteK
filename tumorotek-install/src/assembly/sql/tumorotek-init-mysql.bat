@ECHO OFF

set user_mysql=
set /p user_mysql="Username for MySQL ? "
if [%user_mysql%] == [] (goto :end)

set password_mysql=
set /p password_mysql="Password for MySQL ? "
echo.
echo Thank you %user_mysql% we now have your MySQL login details

rem ------------------------- tumorotek_codes ----------------------------------------------
:tumorotek_codes_again
    echo.
    set answer=
    set /p answer="Ajouter tumorotek_codes ? (Y/N)? "
    if /i "%answer:~,1%" EQU "Y" goto tumorotek_codes_add
    if /i "%answer:~,1%" EQU "N" goto tumorotek_interfacages_again
    echo Please type Y for Yes or N for No
    goto tumorotek_codes_again

:tumorotek_codes_add
set tumorotek_codes=
set /p tumorotek_codes="Nom de la base de donnees tumorotek_codes ? "
echo.
echo OK, for tumorotek_codes -^> %tumorotek_codes%
if [%tumorotek_codes%] == [] set tumorotek_codes=tumorotek_codes

echo.
echo Creation de la base de donnees : %tumorotek_codes%
echo mysql -u %user_mysql% -p%password_mysql% -e "create database %tumorotek_codes% default character set utf8";
mysql -u %user_mysql% -p%password_mysql% -e "create database %tumorotek_codes% default character set utf8";

echo.
echo Ajout des donnees
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek_codes% --default-character-set=utf8 ^< tumorotek_codes\tumorotek_codes-init.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek_codes% --default-character-set=utf8 < tumorotek_codes\tumorotek_codes-init.sql

rem ------------------------- tumorotek_interfacages ----------------------------------------------
:tumorotek_interfacages_again
    echo.
    set answer=
    set /p answer="Ajouter tumorotek_interfacages ? (Y/N)? "
    if /i "%answer:~,1%" EQU "Y" goto tumorotek_interfacages_add
    if /i "%answer:~,1%" EQU "N" goto tumorotek_again
    echo Please type Y for Yes or N for No
    goto tumorotek_interfacages_again

:tumorotek_interfacages_add
set tumorotek_interfacages=
set /p tumorotek_interfacages="Nom de la base de donnees tumorotek_interfacages ? "
echo.
echo OK, for tumorotek_interfacages -^> %tumorotek_interfacages%
if [%tumorotek_interfacages%] == [] set tumorotek_interfacages=tumorotek_interfacages

echo.
echo Creation de la base de donnees : %tumorotek_interfacages%
echo mysql -u %user_mysql% -p%password_mysql% -e "create database %tumorotek_interfacages% default character set utf8";
mysql -u %user_mysql% -p%password_mysql% -e "create database %tumorotek_interfacages% default character set utf8";

echo.
echo Ajout des donnees
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek_interfacages% --default-character-set=utf8 ^< tumorotek_interfacages\tumorotek_interfacages-init.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek_interfacages% --default-character-set=utf8 < tumorotek_interfacages\tumorotek_interfacages-init.sql
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek_interfacages% --default-character-set=utf8 ^< tumorotek_interfacages\tumorotek_interfacages_FK.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek_interfacages% --default-character-set=utf8 < tumorotek_interfacages\tumorotek_interfacages_FK.sql
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek_interfacages% --default-character-set=utf8 ^< tumorotek_interfacages\live_scans.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek_interfacages% --default-character-set=utf8 < tumorotek_interfacages\live_scans.sql

rem ------------------------- tumorotek ----------------------------------------------
:tumorotek_again
    echo.
    set answer=
    set /p answer="Ajouter tumorotek ? (Y/N)? "
    if /i "%answer:~,1%" EQU "Y" goto tumorotek_add
    if /i "%answer:~,1%" EQU "N" goto end /b
    echo Please type Y for Yes or N for No
    goto tumorotek_again

:tumorotek_add
set tumorotek=
set /p tumorotek="Nom de la base de donnees tumorotek ? "
echo.
echo OK, for tumorotek -^> %tumorotek%
if [%tumorotek%] == [] set tumorotek=tumorotek

echo.
echo Creation de la base de donnees : %tumorotek%
echo mysql -u %user_mysql% -p%password_mysql% -e "create database %tumorotek% default character set utf8";
mysql -u %user_mysql% -p%password_mysql% -e "create database %tumorotek% default character set utf8";

echo.
echo Ajout des donnees
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 ^< tumorotek\tumorotek-init.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 < tumorotek\tumorotek-init.sql
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 ^< tumorotek\charts.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 < tumorotek\charts.sql
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 ^< tumorotek\export_biobanques.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 < tumorotek\export_biobanques.sql
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 ^< tumorotek\export_BIOCAP.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 < tumorotek\export_BIOCAP.sql
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 ^< tumorotek\export_INCA.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 < tumorotek\export_INCA.sql
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 ^< tumorotek\export_mysql.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 < tumorotek\export_mysql.sql
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 ^< tumorotek\export_TGVSO.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 < tumorotek\export_TGVSO.sql
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 ^< tumorotek\getBoite.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 < tumorotek\getBoite.sql
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 ^< tumorotek\indicateurs.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 < tumorotek\indicateurs.sql
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 ^< tumorotek\createSTATStables.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 < tumorotek\createSTATStables.sql

:tumorotek_add-bto-context_again
    echo.
    set answer=
    set /p answer="Ajouter le contexte BTO ? (Y/N)? "
    if /i "%answer:~,1%" EQU "Y" goto tumorotek_add-bto-context_add
    if /i "%answer:~,1%" EQU "N" goto end /b
    echo Please type Y for Yes or N for No
    goto tumorotek_add-bto-context_again

:tumorotek_add-bto-context_add
echo mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 ^< tumorotek\add-bto-context.sql
mysql -u %user_mysql% -p%password_mysql% %tumorotek% --default-character-set=utf8 < tumorotek\add-bto-context.sql

:end
echo.
echo Fin du script
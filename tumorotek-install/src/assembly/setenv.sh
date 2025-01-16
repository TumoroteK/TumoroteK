#!/bin/bash

# Attention : pour les tomcats de la distribution Redhat (ex : CHU Tours, HCL) les paramètres de démarrage doivent être définis dans le fichier .../conf/tomcat.conf, avec la clé CATALINA_OPTS :
# CATALINA_OPTS="-Xms512m -Xmx1024m -Duser.language=en -Dlogback.configurationFile=$CATALINA_BASE/conf/Catalina/localhost/logback/logback.xml"
# et ce fichier setenv.sh ne doit pas être déposé dans .../bin
export CATALINA_OPTS="$CATALINA_OPTS -Xms512m -Xmx1024m -Duser.language=en -Dlogback.configurationFile=$CATALINA_BASE/conf/Catalina/localhost/logback/logback.xml"

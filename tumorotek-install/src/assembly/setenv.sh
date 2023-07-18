#!/bin/bash

export CATALINA_OPTS="$CATALINA_OPTS -Xms512m -Xmx1024m -Duser.language=en -Dlogback.configurationFile=$CATALINA_BASE/conf/Catalina/localhost/logback/logback.xml"

#!/usr/bin/env sh

nginx -g "daemon off";
nginx
tini java -Dfile.encoding=utf-8 -jar /app.jar


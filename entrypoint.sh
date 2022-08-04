#!/usr/bin/env sh
chmod -R 777 /music
groupadd sqmusic --gid "${GID:-1000}"
useradd  -u "${UID:-1000}" --gid "${GID:-1000}" -m -s /bin/bash sqmusic
chown -R sqmusic /music
java -jar /sqmusic.jar

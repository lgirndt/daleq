#!/bin/sh

./gradlew --stacktrace --continue --quiet --no-color \
-Pintegration.db=${INTEGRATION_DB} \
-Pintegration.mysql.url=jdbc:mysql://127.0.0.1/daleq_test \
-Pintegration.mysql.user=root \
-Pintegration.mysql.password= \
check


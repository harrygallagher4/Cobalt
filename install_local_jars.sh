#!/bin/sh
mvn install:install-file -Dfile=lib-extra/DarkMagician-EventAPI-f3d6d30.jar -DgroupId=com.darkmagician6 -DartifactId=eventapi -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=lib-extra/ByteTools.jar -DgroupId=eu.bibl -DartifactId=bytetools -Dversion=1.0 -Dpackaging=jar
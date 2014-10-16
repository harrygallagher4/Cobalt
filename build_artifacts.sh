#!/bin/sh
NOW=$(date +"%Y-%m-%dT%H:%M:%S%:z")
sed -e "s/\${time}/$NOW/" -e "s/\${release_time}/$NOW/" < Cobalt.json.example > target/Cobalt.json
mv target/cobalt-2.0.jar target/Cobalt.jar

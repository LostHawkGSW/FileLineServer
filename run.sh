#!/bin/bash
export FILE_URL="$1"
java -jar target/filelineserver-0.0.1-SNAPSHOT.jar server config.yml
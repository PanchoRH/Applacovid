#!/bin/bash

ScriptDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

docker stop admin-postgres
sleep 5
docker rm admin-postgres
sleep 3
rm -rf $ScriptDir/pgdata/admin

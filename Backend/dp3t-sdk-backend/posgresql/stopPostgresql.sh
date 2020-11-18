#!/bin/bash

ScriptDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

docker stop dpppt-postgres
sleep 5
docker rm dpppt-postgres
sleep 3
rm -rf $ScriptDir/pgdata/dpppt

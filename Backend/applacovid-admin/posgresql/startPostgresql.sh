#!/usr/bin/env bash

ScriptDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

docker run --name admin-postgres -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=admin -p 5433:5432 -v $ScriptDir/pgdata/admin:/var/lib/postgresql/data -d postgres:10.6

# Assuming postgres is running on port 5432 in the container and you want to expose it on the host on 5433.
# ports:
#    -"5433:5432"


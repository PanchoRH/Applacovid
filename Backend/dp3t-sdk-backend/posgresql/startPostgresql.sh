#!/usr/bin/env bash

ScriptDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

docker run --name dpppt-postgres -e POSTGRES_USER=dpppt -e POSTGRES_PASSWORD=dpppt -e POSTGRES_DB=dpppt -p 5434:5432 -v $ScriptDir/pgdata/dpppt:/var/lib/postgresql/data -d postgres:10.6

# Assuming postgres is running on port 5432 in the container and you want to expose it on the host on 5433.
# ports:
#    -"5433:5432"
# will expose the server on port 5433 on the host.
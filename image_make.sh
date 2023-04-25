#!/bin/bash

docker_name="martinipool-scheduler"

# gralew
./gradlew clean
./gradlew bootJar

# remove container
docker rm -f ${docker_name}

# remove image
docker rmi -f ${docker_name}
rm -rf ${docker_name}.tar

# build
docker build --platform=linux/amd64 -t ${docker_name} .

# get container_id
container_id=`docker images | grep ${docker_name}  | awk '{print $3}'`

# make image
docker save ${docker_name} -o ${docker_name}.tar ${container_id}


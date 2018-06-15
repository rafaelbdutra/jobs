# bash

echo "### Building application ###"
./mvnw clean install

echo "### Building Docker image ###"
docker build -f Dockerfile -t dutra/jobs .

echo "### Running container ###"
docker run -d --name jobs -p 8080:8080 dutra/jobs

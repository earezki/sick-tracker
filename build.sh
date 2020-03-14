mvn clean install
mvn package
docker build . --file ./Dockerfile --tag localhost:5000/sick-tracker
docker push localhost:5000/sick-tracker
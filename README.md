To build the project:
    1) CD to the project directory, and
    2) mvn clean install tomcat7:run

To run the application:
    1) mvn clean install tomcat7:run, then
    2) open a browser and navigate to the following address: http://localhost:8181

To run a test:
    mvn -Dtest=ApplicationDataServiceTest#testReadCSV test
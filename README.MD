### Maven

Most of the project, if not all, is configured around Maven. You can run the server or set it up running Maven commands. 

### Project setup

This OAuth2 Server implementation uses JWT tokens. These tokens are signed with an RSA key pair. Before running the
 server you need to generate an RSA key pair in a key store and configure it in the server overriding following
 properties:

```yaml
- keystore.path
- keystore.password
- keystore.key.alias
- keystore.key.password
```

The application is by default configured to use a keystore in `${project.build.path}`.
 
You can generate an initialized key store for development and testing purposes using the following command: 

```bash
mvn keytool:generateKeyPair
```

### Building the project

```bash
mvn -Ppostgresql -Dspring.profiles.active=dev,postgresql clean install
```

Will compile the project and run all tests for the `postgresql`. Use `mysql` to build the project for a MySQL 
 database instead.

```bash
mvn -Pmysql -Dspring.profiles.active=dev,mysql clean install
```

### Running the server

To run the server locally use the following command.

```bash
 mvn  -Ppostgresql -Dspring.profiles.active=dev,postgresql docker:run spring-boot:run
```

Substitute `postgresql` with `mysql` to change the database infrastructure.


### Profiles

Maven is configured to use the Maven profile with the same name as the Spring profile used. The following profiles are
 available:
 
* Maven compilation database profiles
  * mysql: adds mysql drivers
  * postgresql: adds postgresql drivers

* Spring runtime database profiles:
  * mysql
  * postgresql

* Spring runtime environment profiles:
  * dev: for executing the oauth server locally. Will spin up a container with the specified database, initialize the
    database with a schema and some data, and will start the oauth server.
  * pro: only starts the oauth server. You will need to provide the connection details for an existing database.

Maven compilation profiles and Spring runtime database profiles MUST match.

For example, if you want to execute the project using `postgresql` in your laptop you should execute the following
 command:
 
```bash
 mvn  -Ppostgresql -Dspring.profiles.active=dev,postgresql docker:run spring-boot:run
```

Here we are setting Maven profile to `postgresql` so that Maven will include only postgresql libraries.
We are also setting `spring.profiles.active` property  to `postgresql, dev`so Spring will run the server with
 `postgresql` settings, so that it tries to connect to a PostgreSQL database locally.
We are also specifying with `docker:run` that we spin up a docker container with the PostgreSQL, so that the
 database expected locally by the `dev` environment is available for the server.


### Starting a database for development and testing

The Oauth server is ready to work with MySQL and PostgreSQL databases. 
 
If you have Docker installed in your system, you can start a Docker container with your database of choice using maven.
 To do so, you must select your preferred database using profile options and execute the `docker:run` goal. For example,
 to run MySQL container:

```bash
mvn -Dspring.profiles.active=postgresql docker:run
```

To check that your postgresql container is up and running you can execute the following command:

```bash
docker ps
```

You can also run the following command to stop the postgresql database container:

```bash
mvn -Dspring.profiles.active=postgresql docker:stop
```


### Application properties

You can override any application properties in the command line. For example, if you are running a PostgreSQL database
 in port `5433` instead of the default `5432` and its access credentials are `postgres:postgres` you could override
 default values with the following command:
 
```bash
 mvn -Dspring.profiles.active=postgresql -Dspring.datasource.username=postgres -Dspring.datasource.password=postgres -Dspring.datasource.port=5433 spring-boot:run
```

Property `spring.datasource.initialize` controls the database schema & sample data setup. By default, this property is
 set to `true`. If you don't want to re-create the schema, for example for running in production, this value should be
 changed to `false`.


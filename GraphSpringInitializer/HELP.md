# Read Me First
The following was discovered as part of building this project:

* No Docker Compose services found. As of now, the application won't start! Please add at least one service to the `compose.yaml` file.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.0-SNAPSHOT/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.0-SNAPSHOT/maven-plugin/build-image.html)
* [Docker Compose Support](https://docs.spring.io/spring-boot/4.0.0-SNAPSHOT/reference/features/dev-services.html#features.dev-services.docker-compose)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/4.0.0-SNAPSHOT/reference/using/devtools.html)
* [Spring for GraphQL](https://docs.spring.io/spring-boot/4.0.0-SNAPSHOT/reference/web/spring-graphql.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a GraphQL service](https://spring.io/guides/gs/graphql-server/)

### Docker Compose support
This project contains a Docker Compose file named `compose.yaml`.

However, no services were found. As of now, the application won't start!

Please make sure to add at least one service in the `compose.yaml` file.

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.


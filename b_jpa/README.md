## Working with Spring Boot JPA

### Note #1: Do stop other running projects before testing this
### Note #2: Please update the following properties in application settings:

```
    spring.application.name=b_jpa
    spring.datasource.url=jdbc:mysql://localhost:<your_port_number .e.g. 3306>/<your_database_name>
    
    spring.datasource.username=<your_MySQLServer_username>
    spring.datasource.password=<your_MySQLServer_password>
    
    # TO DO: Implemented create (when run) and drop (when stopped) tables
    spring.jpa.hibernate.ddl-auto=create-drop
    spring.datasource.drive-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.show-sql: true
```
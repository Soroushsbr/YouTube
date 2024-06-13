# Getting Started

First, you need to install PostgreSQL from [here](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads).

We use JDBC to connect our database and run SQL queries from our Java program. You can read a tutorial on how it works [here](https://www.tutorialspoint.com/jdbc/jdbc-statements.htm).

## PostgreSQL Login

After installation, open the SQL Shell to work with databases. At first, wants you to type server, database, port, username, and password. Don't type anything and leave them to be default, just press `Enter` until you reach `Password for user postgres`. Here, you must enter the password you chose when installing PostgreSQL. After that, you will see the PostgreSQL command line, and you can enter SQL commands. Here are some useful commands to know:
- **List of all databases:**
  ```sql
  \l
- **Connect to the new database:**
  ```sql
  \c database_name
- **List of tables:**
  ```sql
  \dt
## Creating Database and Tables

You can use methods inside the setup class to create the database, and create or reset tables the program needs. The schema of tables is shown below:
[Table Schema](https://dbdiagram.io/d/665f31beb65d9338797c763a)

## Features

Each class has utils related to its class object. You can call them to work with the database. Here is a list of them:

- Account:

- Comment:

- Channel

## Note

Dependencies of PostgreSQL and JDBC are already added to the project, but if you need them, you can find the links here:

- [PostgreSQL JDBC Driver](https://mvnrepository.com/artifact/org.postgresql/postgresql/42.7.3)
- [Bouncy Castle for hashing account password](https://mvnrepository.com/artifact/org.mindrot/jbcrypt/0.4)





<p align="center">

  <img src="media/youtube-image.png" width="350" alt="YouTube Logo" style="margin-bottom: 0;"/>

</p>

---

[//]: # (<p align="center">)

[//]: # (  <iframe width="560" height="315" src="media/video_2024-07-10_09-34-51.mp4" frameborder="0" allowfullscreen></iframe>)

[//]: # (</p>)
<video width="320" height="240" controls>
  <source src="https://raw.githubusercontent.com/Soroushsbr/YouTube/tree/database/media/video_2024-07-10_09-34-51.mp4" type="video/mp4">
</video>

[//]: # (<video width="320" height="240" controls>)

[//]: # (  <source src="media/video_2024-07-10_09-34-51.mp4" type="video/mp4">)

[//]: # (</video>)


<div style="padding-left: 45px; padding-right: 20px;">

## 📋 Table of Contents
- 👀 [Overview](#overview)
- 🔨 [Installation](#installation)
    - 🐘 [PostgreSQL Login](#postgresql-login)
    - [Creating Database and Tables](#creating-database-and-tables)
- ✨ [Key Features](#key-features)
- 🎨 [Front-End Overview](#front-end-0verview)
- 🖥️ [Server/Client Mechanism](#serverclient-mechanism)
    - [Server](#server)
    - [Client](#client)
    - [Communication with Responses](#communication-with-responses)
- 📊 [Database Info](#database-info)
- 🔗 [Resources](#resources)
- ❤️ [Contributors](#contributors)
- 💰 [Donations](#donations)
- 📞 [Contact Us](#contact-us)

***

<h2 id="overview">👀 Overview</h2>
This project is a mini version of YouTube, developed as a university project for our Advanced Programming (AP) course. The goal was to create an application closely resembling the real YouTube app while implementing core object-oriented programming concepts such as Object, Encapsulation, Polymorphism, and Inheritance. Additionally, the project incorporates advanced programming concepts like multi-threading for the client/server, socket programming for server-client communication, and a database for data management.

The project was divided into three parts: Front-end, Server, and Database. Each team member was responsible for one part.

***

<h2 id="installation">🔨 Installation</h2>

At first, you need to install PostgreSQL database from [here](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads).

<h3 id="postgresql-login">🐘 PostgreSQL Login</h3>

After installation, open the SQL Shell to work with databases. At first, wants you to type server, database, port, username, and password. Don't type anything and leave them to be default, just press `Enter` until you reach `Password for user postgres`. Here, you must set the password `123`, Don't set anything else!. After that, you will see the PostgreSQL command line, and you can enter SQL commands. Here are some useful commands to know:
- **List of all databases:**
  ```sql
  \l
- **Connect to the new database:**
  ```sql
  \c database_name
- **List of tables:**
  ```sql
  \dt
- **View a table:**
  ```sql
  SELECT * FROM table_name;

### Creating Database and Tables

You can use methods inside the setup class to create the database and reset tables as needed.

If its your first time using the project, **You MUST create tables for app to work correctly**, using create_database() and then create_tables() methods.

<h2 id="key-features">✨ Key Features</h2>
- Hashing for account security
- Recommendation algorithm
- Live notifications
- Video preview and shorts
- Dynamic video speed change
- Watch later/history feature
- Live search recommendations
- Multi-threaded server
- Concurrent video uploads/downloads
- Request saving

***
<h2 id="front-end-0verview">🎨 Front-End Overview</h2>
front end of project uses javaFX framework.
you can see a tutorial on how it works [here](https://www.youtube.com/watch?v=9XJicRt_FaI).
other libraries:
media: to show and work with video and images
timeline: animation and transition? of nodes

mechnism:
frame multi thread handling?

any other mechanism??

***

<h2 id="serverclient-mechanism">🖥️ Server/Client Mechanism</h2>

The server and client are the main components of the project, each divided into several sub-components.

### ▶ Server

---

The server is the primary thread managing three other threads: Client_part, Receiver_part, and Sender_part.

- **Client_part** → Handles incoming client connection requests and creates a Client_Handler thread for managing regular requests.
- **Receiver_part** → Manages user uploads by spawning a new thread for each upload connection.
- **Sender_part** → Handles media downloads similarly to Receiver_part.

### ▶ Client

---

Upon running the application, an instance of the Client class is created, establishing a connection with the Client Handler and creating a thread to listen for server responses.

### ▶ Communication with Responses

---

Models (e.g., Account) consist of three classes: Model, Server Model, and Client Model, all extending the abstract ClassInfo class. Requests are models converted to JSON.

- **Model** → Contains data and functions for the model.
- **Client_model** → Instantiates Model and sends requests from the front-end.
- **Server_model** → Used in the Client_Handler to process requests and interact with the database.

Server responses are instances of the ServerResponse class, containing a hashmap for request_id and response data.

***

<h2 id="database-info">📊 Database Info</h2>
We use JDBC to connect our database and run SQL queries from our Java program. You can read a tutorial on how it works [here](https://www.tutorialspoint.com/jdbc/jdbc-statements.htm).

The schema of tables can be found [here](https://dbdiagram.io/d/665f31beb65d9338797c763a).

***

<h2 id="resources">🔗 Resources</h2>

To send data between client and server, we used the Jackson library for JSON API. Dependencies are available here:
- [Jackson Core](https://mvnrepository.com/artifact/com.fasterxml.jackson.core)
- [Jackson Datatype](https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310)

PostgreSQL and JDBC dependencies are also included:
- [PostgreSQL JDBC Driver](https://mvnrepository.com/artifact/org.postgresql/postgresql/42.7.3)
- [Bouncy Castle for hashing account passwords](https://mvnrepository.com/artifact/org.mindrot/jbcrypt/0.4)

___

<h2 id="contributors">❤️ Contributors</h2>
- [Soroush Saberi](https://github.com/Soroushsbr)
- [Peyman Nikravan](https://github.com/peymanik)
- [Mobin Askari](https://github.com/MobinAskariN)


***

<h2 id="donations">💰 Donations</h2>


If you would like to support this project, you can make a donation [here](#).

***

<h2 id="contact-us">📞 Contact Us</h2>

- Discord: [Join our Discord](https://discord.gg/sb8FKWVR)
- Telegram: [@srshsbr](https://t.me/srshsbr)
- 💌 [soroush.13830o@gmail.com](mailto:soroush.13830o@gmail.com)


</div>



table of content-->>https://github.com/amitmerchant1990/electron-markdownify?tab=readme-ov-file#readme
https://github.com/antoinezanardi/werewolves-assistant-api-next#readme


gif +image

image for database tables schema















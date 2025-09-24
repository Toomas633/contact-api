# Contact API

- [Requirements](#requirements)
- [Solution](#solution)
  - [Software](#software)
  - [Database](#database)
  - [Running](#running)

### Requirements

Your client, who deals with managing secret contacts, has a problem. They urgently need a minimalist information system for this purpose. In discussions with the client and the architect, you all agree that this solution will not be long-term, but rather temporary, fulfilling only the minimal requirements.

The following is agreed:

- Client’s requirements:
- Can view contact records.
- Can add new contact records.
- A contact record stores the contact’s real name, secret code name, and phone number.
- The solution can be used through a web browser.

Technical requirements:
- The information system must have a REST API that supports the JSON data format so that the solution can be integrated with other systems.
- Back-end technology is preferably Java.
- A JavaScript user interface that communicates with the system’s REST API for adding and displaying records (no need to think about design or styling). A JavaScript framework of your choice may also be used.
- The solution must support contact names containing any possible symbols.
- System users and contact data are stored in a PostgreSQL database.
- Bonus: The user can search for contacts.

Final deliverable:
- The software code and the database model (for example, an SQL dump) must be delivered as a file.
- Instructions on how to run the software.

## Solution
### JSON

JSON format for sending and receiving data: `{"id":10,"nimi":"Test","salajane":"Test","tel":"9812432"}` (When searching, an array is returned).

Java program files under `src/main/java/App`.
React website in `src/`.

http://localhost:8000/get displays information about the contact with the given ID in JSON format. Payload: `id:value`.

http://localhost:8000/add saves the information to the database when the payload is a user JSON object, e.g. `{id=1, nimi=test, salajane=test, tel=52310232}`.

http://localhost:8000/search searches the database fields for the given value when the request payload is `search:value`.

http://localhost:8000/delete deletes the contact with the given ID when the request payload is `id:value`.

### Software

* [Java](https://www.java.com/en/) (18)
* [PostgreSQL](https://www.postgresql.org/)
* [Node.js](https://nodejs.org/en)

### Database

- Database dump: `smit.sql`
- Database configuration in file `DB.java`.
- Test data is randomly generated with ChatGPT.

### Running

Run the program using `java -jar test.jar` and the API is exposed on [http:localhost:8000](http://localhost:8000/).

Webpage:
`npm install -g serve`
`serve -s build`
At address [http://localhost:3000](http://localhost:3000)

If .jar is not working you can also use [Maven](https://maven.apache.org/) or run the `App.java` file directly.

```
mvn clean install
```

```
java -jar target/test-0.1.jar
```

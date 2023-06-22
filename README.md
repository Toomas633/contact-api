# SMIT-proovitöö

- [Ülesanne](#ülesanne)
- [Lahendus](#lahendus)
  - [Tarkvara](#tarkvara)
  - [Andmebaas](#andmebaas)
  - [Käivitamine](#käivitamine)

### Ülesanne

Sinu kliendil, kes tegeleb salajaste kontaktide haldamisega, on probleem. Tal on vaja kiires korras selle tarbeks minimalistlikku infosüsteemi. Arutades võimalikku lahendust üheskoos kliendi ja arhitektiga olete ühel meelel, et see lahendus ei saa olema pikaaegne vaid pigem ajutine, minimaalseid nõudeid täitev.

Lepitakse kokku järgnevas:

Kliendi nõuded:

- [X] Näeb kontaktide kirjeid.
- [X] Saab lisada uusi kontakti kirjeid.
- [X] Kontakti kirjes talletatakse kontakti pärisnimi, salajane kood-nimi ja telefoninumber.
- [X] Lahendust saab kasutada läbi veebibrauseri.

Tehnilised nõuded:

- [X] Infosüsteemil peab olema REST API, mis toetab JSON andmeformaati, et lahendust saaks liidestada teiste süsteemidega.
- [X] Back-end tehnoloogia soovituslikult Java.
- [X] JavaScript kasutajaliides, mis suhtleb infosüsteemi REST API'ga kirjete lisamisel ja kuvamisel (ei pea mõtlema disaini või kujunduse peale).Võib kasutada ka oma valitud JavaScript raamistiku
- [X] Lahendus peab toetama kõikvõimalike sümbolitega kontaktide nimesid.
- [X] Süsteemi kasutajad ja kontaktandmed on talletatud andmebaasis PostgreSQL.
- [X] Boonus: Kasutaja saab otsida kontakte

Lõpptulemus:

- [X] Tarkvara kood ja andmebaasi mudel (näiteks SQL dump) edastada failina.
- [X] Juhend kuidas tarkvara käivitada

## Lahendus

Ajakulu ~15h.

### Json

Json formaat saatmiseks ja vastusena andmebaasist `{"id":10,"nimi":"Test","salajane":"Test","tel":"9812432"}` (otsingul tagastatakse array).

Java programmifailid kaustas `src/main/java/App`.
React veebilehe failid kaustas `src/`.

http://localhost:8000/get kuvab vastava id-ga kontakti kohta infot antud json formaadis. Payload on id:väärtus.

http://localhost:8000/add salvestab selle info andmebaasi kui payload on antud kasutaja json {id=1, nimi=test, salajane=test, tel=52310232}.

http://localhost:8000/search otsib anmebaasi lahtritest vastavat väärtust kui request payload on search:väärtus .

http://localhost:8000/delete otsib anmebaasi lahtritest vastavat väärtust kui request payload on id:väärtus .

### Tarkvara

* [Java](https://www.java.com/en/) (18)
* [PostgreSQL](https://www.postgresql.org/) andmebaas
* [Node.js](https://nodejs.org/en)

### Andmebaas

Andmebaasi dump: `smit.sql`

Aadress ja autentimise andmed failis `DB.java`.

Testandmed suvaliselt genereeritud GPT-ga.

### Käivitamine

Käivitada programm kasutades `java -jar test.jar` alustab tööd api endpoint [http:localhost:8000](http://localhost:8000/).

Veebileht:
`npm install -g serve`
`serve -s build`
Aadressil [http://localhost:3000](http://localhost:3000)

Kui .jar ei tööta kasutada [Mavenit](https://maven.apache.org/) või otse App.java faili.

```
mvn clean install
```

```
java -jar target/test-0.1.jar
```

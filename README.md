# SMIT-proovitöö

[- Tarkvara](#tarkvara)
[- Käivitamine](#käivitamine)

Sinu kliendil, kes tegeleb salajaste kontaktide haldamisega, on probleem. Tal on vaja kiires korras selle tarbeks minimalistlikku infosüsteemi. Arutades võimalikku lahendust üheskoos kliendi ja arhitektiga olete ühel meelel, et see lahendus ei saa olema pikaaegne vaid pigem ajutine, minimaalseid nõudeid täitev.

Lepitakse kokku järgnevas:

Kliendi nõuded:

- [X] Näeb kontaktide kirjeid.
- [X] Saab lisada uusi kontakti kirjeid.
- [X] Kontakti kirjes talletatakse kontakti pärisnimi, salajane kood-nimi ja telefoninumber.
- [ ] Lahendust saab kasutada läbi veebibrauseri.

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

Json formaat saatmiseks ja vastusena andmebaasist `{"id":10,"nimi":"Test","salajane":"Test","tel":"9812432"}`.

Otsingul tagastatakse array `[{"nimi":"Mart Laanemäe","salajane":"Kivimurdja","tel":"55512345","id":1},{"nimi":"Liis Saar","salajane":"Metslind","tel":"+372 55567890","id":2},{"nimi":"Jaanika Tamm","salajane":"Salurüütel","tel":"+372 55524680","id":3},{"nimi":"Peeter Vaher","salajane":"Öökull","tel":"55510101","id":4},{"nimi":"Mari-Liis Kask","salajane":"Sinitihane","tel":"55588899","id":5},{"nimi":"Märten Põldmäe","salajane":"Virmalaine","tel":"+37255598765","id":54}]`.

Programmifailid kaustas `src/main/java/App`.

http://localhost:8000/?get=1 kuvab vastava id-ga kontakti kohta infot antud json formaadis.

http://localhost:8000/add salvestab selle info andmebaasi kui payload on antud kasutaja json {id=1, nimi=test, salajane=test, tel=52310232}.

http://localhost:8000/search otsib anmebaasi lahtritest vastavat väärtust kui request payload on search:väärtus .

Andmebaasi dump: `smit.sql`

Ajakulu ~12h.

### Tarkvara

* [Java](https://www.java.com/en/) (18)
* [PostgreSQL](https://www.postgresql.org/) andmebaas
* [Node.js](https://nodejs.org/en)

### Andmebaas

Aadress ja autentimise andmed failis `DB.java`.

Testandmed suvaliselt genereeritud GPT-ga.

### Käivitamine

Käivitada programm kasutades `java -jar test.jar` alustab tööd api endpoint [http:localhost:8000](http://localhost:8000/).

Kui .jar ei tööta kasutada [Mavenit](https://maven.apache.org/) või otse App.java faili.

```
mvn clean install
```

```
java -jar target/test-0.1.jar
```

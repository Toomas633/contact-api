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
- [ ] JavaScript kasutajaliides, mis suhtleb infosüsteemi REST API'ga kirjete lisamisel ja kuvamisel (ei pea mõtlema disaini või kujunduse peale).Võib kasutada ka oma valitud JavaScript raamistiku
- [X] Lahendus peab toetama kõikvõimalike sümbolitega kontaktide nimesid.
- [X] Süsteemi kasutajad ja kontaktandmed on talletatud andmebaasis PostgreSQL.
- [X] Boonus: Kasutaja saab otsida kontakte

Lõpptulemus:

- [ ] Tarkvara kood ja andmebaasi mudel (näiteks SQL dump) edastada failina.
- [X] Juhend kuidas tarkvara käivitada

## Lahendus

Json formaat `{"id":10,"nimi":"Test","salajane":"Test","tel":"9812432"}`.

Programmifailid kaustas `src/main/java/App`.

http://localhost:8000/api/1 kuvab vastava id-ga kasutaja kohta infot antud json formaadis.

http://localhost:800/api/{id=1, nimi=test, salajane=test, tel=52310232} salvestab selle info andmebaasi.

Andmebaasi dump: `smit.sql`

Ajakulu ~8h.

### Tarkvara

* [Java](https://www.java.com/en/) 18
* [PostgreSQL](https://www.postgresql.org/) andmebaas

### Andmebaas

Aadress ja autentimise andmed failis `DB.java`.

Testandmed suvaliselt genereeritud GPT-ga.

### Käivitaminejava -jar test.jar

Kui see ei tööta kasutada [Mavenit](https://maven.apache.org/).

```
mvn clean install
```

```
java -jar target/test-0.1.jar
```

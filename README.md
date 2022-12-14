# miavortaro
miavortaro estas nova apo vortara por ke, oni esperante povus serĉi, organizi, diskutadi, kaj voĉdoni por vortoj kiujn si konsideras precizaj.

## Kiel Kurigi
Por kurigi la apon, oni devas fari la jenajn paŝojn:


1. Malfermi la projekton en Intellij IDEA de JetBrains (Community Edition)
2. Krei ŝloson sekuran por la HTTPS-a haveno, jene (terminale):
    `keytool -keystore keystore.jks -alias sampleAlias -genkeypair -keyalg RSA -keysize 4096 -validity 3 -dname 'CN=localhost, OU=ktor, O=ktor, L=Unspecified, ST=Unspecified, C=US'`
    Tiam la dosiero estos kreita nomite "keystore.jks". Ktor uzos tiun dosieron kiam vi kurigos la servilon
3. Permesi la kurigon de Gradle en Intellij (tuj kiam vi malfermas la projekton, do vi devas atendi kelkajn minutojn)
4. Kiam ĝi finiĝas, alklaku per la maldekstra musklavo la ludan butonon supre de la fenestro, ĝi diros al vi kiam ĝi pretiĝas sube en la konzolo
   a. La havenoj, kiuj konektebliĝas, estas 5000 (HTTP) kaj 8443 (HTTPS) se ĉio estas en ordo
5. Konekti al la HTTP aŭ HTTPS havenoj por testi ilin
    Rem: Kiam vi konektas al HTTPS, via retumilo eble diras al vi, ke la retejo estas malsekura. Ĉio, kion vi devas fari estas:
        1. Alklaki "Spertula" aŭ "Advanced" angle
        2. Alklaki "Akcepti la riskon kaj daŭrigi..."

## Dokumentaĵo de la kodo
TODO: Skribi la dokumentaĵojn de la kodo

## API

### GET /?tranĉi=N,M
Tiu ĉi peto estas uzata por havigi la tutan vortaron, tranĉite de *N* ĝis *M* indeksoj, 
kie *N* estas la indekso de la komenco de la listo, kaj *M* estas la indekso de la fino de la listo.

Peto:
```
HTTP/1.1 localhost:5000
GET /?tranĉi=0,10
```

Respondo:
```
HTTP/1.1 localhost:5000
GET /?tranĉi=0,10
BODY
[
    {
        "word": "akvo",
        "definition": "Likva kombinaĵo de hidrogeno k oksigeno, H2O"
    },
    {
        "word": "besto",
        "definition": "Ĉiu animalo escepte de homo: hejma, sovaĝa besto"
    },
    {
        "word": "diri",
        "definition": "Voĉe komuniki sencohavajn vortojn"
    },
    {
        "word": "enmeti",
        "definition": "Meti en"
    },
    {
        "word": "homo",
        "definition": "de mamuloj, karakterizata de vertikala teniĝo, forte evoluinta cerbo k parolkapablo"
    },
    {
        "word": "iri",
        "definition": "Moviĝi per la tiucelaj membroj, piedoj, flugiloj, naĝiloj"
    },
    {
        "word": "komputilo",
        "definition": "Aparato, kiu aŭtomate prilaboras datenojn laŭ instrukcioj"
    },
    {
        "word": "lando",
        "definition": "Parto de la tersurfaco, rigardata kiel geografia apartaĵo"
    },
    {
        "word": "ludi",
        "definition": "Vigle, libere, kaprice, sencele sin movi tien k reen en vivoĝojo"
    },
    {
        "word": "mano",
        "definition": "Ekstrema, artikigita parto de la brako de homo aŭ de simio, havanta kvin fingrojn, el kiuj unu estas kontraŭmetebla al la ceteraj"
    }
]
```

### GET /?vortoj=V
Tiu ĉi peto estas uzata por serĉi vortojn per iu(j) litero(j) sekva(j). La sola parametro estas "vortoj", kaj ĝi uzas `V`, kiu estas la vorto, kiun uzas oni por serĉi ĉiuj vortojn kiuj kongruas ĝin

```
HTTP/1.1 localhost:5000
GET /?vortoj=en
BODY
[
    {
        "word": "enmeti",
        "definition": "Meti en"
    }
]
```

### POST /
Tiu ĉi peto estas por aldoni novajn vortojn, aŭ por ŝanĝi vortojn. La korpo de la peto devas enhavi la informon de la vorto ŝanĝata. Tiu ĉi peto ankaŭ postulas rajtigon de Admino.

Por uzi `POST /` ĝuste, oni devas sendi JSON mesaĝon jene:

```
HTTP localhost:8443
POST /
Authorization: Bearer {token}

BODY
{
    "word": {vorto},
    "definition": {difino/priskribo}
}
```

En la proksima estonteco, ni havos kapablon por aldoni plurajn difinojn kaj ankaŭ tradukojn en diversaj linvoj, kaj ankaŭ gramatikajn rimarkojn kaj notojn.

Oni ne devus zorgi pri la respondo, ĉar ĝi ja revenos malplena, krom la respondan kodon. Ĝi ja estu `200 OK`, kontraŭe ĝi diros al vi tion kio malĝustas de la peto.

### DELETE /
Por forigi vortojn, oni povus uzi la DELETEan peton radike, per la rajtigo

```
HTTP localhost:8443
DELETE /
Authorization: Bearer {token}

BODY
enmeti
```

### POST /registri
Tiu ĉi peto estas por krei novajn kontojn, sed oni devas uzi la Adminan konton por krei novajn kontojn (nuntempe). Tiamaniere, ni povas regi la kontojn kaj kiu povas krei novajn kontojn. Por plu sciiĝi pri kontoj, vizitu [kontojn](#kontoj).

Por uzi `POST /registri` ĝuste, oni devas sendi JSON mesaĝon kiu enhavas la detalojn de la konto, kiel jene:
```
HTTP localhost:8443
POST /?registri
Authorization: Bearer {token}

BODY
{
    "uzantnomo": {uzanto},
    "pasvorto": {pasvorto}
}
```

### POST /ensaluti
Tiu ĉi peto estas por ensaluti per kontoj. Tiu ĉi donos al vi la ĵetonon uzi por la aliaj POST petoj, nome `POST /`, `GET/POST /admin`, ktp.

```
HTTP localhost:8443
POST /?ensaluti

BODY
{
    "uzantnomo": {uzanto},
    "pasvorto": {pasvorto}
}
```

Ĝi ja respondu al vi per la ĵetono, kiel jene:
```
HTTP/1.1 localhost:5000
GET /?ensaluti
BODY
{
    "token": "{ĵetono}"
}
```

Mi rekomendas konservi tiun ĵetonon ĝis la servilo diras al vi ke, la ĵetono eksvalidiĝis, tiam resendu la saman peton por akiri novan ĵetonon.

## Kontoj
Nune kontoj nur ekzistas kiel administraj kontoj, por aldoni, forigi, kaj ŝanĝi enigaĵojn. Nur la Admina konto povas krei novajn kontojn. La kontojn permesas oni por sendi API-mesaĝojn kiuj postulas la ĵetonojn.

Ĵetonoj estas kreataj per la tempo, uzantnomo, kaj pasvorto de la konto. Ili permesas ke, la uzanto (de la ĵetono) povu sendi mesaĝojn al la kutimaj malpermesataj vojoj de la servilo. Ekzemple, la vojo "HTTPS POST /", kiu postulas la korpon de la mesaĝo enhavas la informaĵon de la vorto, kiun ŝanĝas aŭ aldonas oni.

Ekzemple:
```
Peto:
POST / HTTP/1.1
Host: localhost:8443
Authorization: Bearer {ĵetono}
...

{
    "vorto": "enmeti",
    "priskribo": "Meti en"
}

Respondo:
HTTP/1.1 200 OK
...
```

Kutime, la ĵetonoj regule kaj ofte eksvalidiĝas por ke neniuj povus havigi ies ĵetonon kaj uzi ĝin por malbonaj agojn al la servilo. Rimarkeble, la nuraj malbonaj aferoj kiujn oni povus fari per la ĵetono estas DDoS, sed ne longe, ĉar la ĵetono ja eksvalidiĝos mallonge kaj rapide, kutime post 3 minutoj.

Por certigi ke la konto restas aktiva kaj valida, oni devas regule kaj ofte revalidigi la ĵetonojn. Tial oni devas uzi [oficiale subtenatajn librarojn](#oficialaj-libraroj) por simpligi la proceson.

### La Proceso de la Validigado de Kontoj
![Ĵetono kaj Rajtigo](dokoj/ĵetono_kaj_rajtigo_travidebla.png)

## Oficialaj Libraroj
| Programa Lingvo | Statuso     | Loko                                       |
|-----------------|-------------|--------------------------------------------|
| Python          | Aktiva      | https://github.com/AlexCouch/miavortaro-py |
| Kotlin          | Nekomencata | Nul                                        |

### Rimarkoj pri la libraroj
Oni ankoraŭ ne rajtas krei siajn proprajn eksterajn librarojn, ĉar mi devas fiksi kaj plibonigi multajn aliajn aferojn. Ankaŭ, mi devas finigi la tutan programan arkitekturon de la libraroj, por ke oni povas programi librarojn baze de la oficialaj libraroj, per komuna programa arkitekturo.

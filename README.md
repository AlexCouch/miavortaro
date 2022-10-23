# miavortaro
miavortaro estas nova apo vortara por ke, oni esperante povus serĉi, organizi, diskutadi, kaj voĉdoni por vortoj kiujn si konsideras precizaj.

## Kiel Kurigi
Por kurigi la apon, oni devas fari la jenajn ŝtupojn:


1. Malfermi la projekton en Intellij IDEA de JetBrains (Community Edition)
2. Krei ŝloson sekuran por la HTTPS-a haveno, jene (terminale):
    `keytool -keystore keystore.jks -alias sampleAlias -genkeypair -keyalg RSA -keysize 4096 -validity 3 -dname 'CN=localhost, OU=ktor, O=ktor, L=Unspecified, ST=Unspecified, C=US'`
    Tiam la dosiero estos kreita nomite "keystore.jks". Ktor uzos tiun dosieron kiam vi kurigos la servilon
3. Permesi la kurigon de Gradle en Intellij (tuj kiam vi malfermas la projekton, do vi devas atendi kelkajn minutojn)
4. Kiam ĝi finiĝas, alklaku per la maldekstra musklavo la ludan butonon supre de la fenestro, ĝi diros al vi kiam ĝi pretiĝas sube en la konzolo
   a. La havenoj, kiuj konektebliĝas, estas 5000 (HTTP) kaj 8443 (HTTPS) se ĉio estas en ordo
5. Konekti al la HTTP aŭ HTTPS havenoj por testi ilin
    Rem: Kiam vi konektas al HTTPS, via retumilo eble diras al vi, ke la retejo estas malsekura. Ĉio, kion vi devas fari estas:
        1. Alklaki "Altnivela" aŭ "Advanced" angle
        2. Alklaki "Procedi..."

## Dokumentaĵo de la kodo
TODO: Skribi la dokumentaĵojn de la kodo
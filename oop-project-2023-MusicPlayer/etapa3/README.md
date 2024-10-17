# Proiect GlobalWaves  - Etapa 3

Codul sursa folosit pentru aceasta etapa este adaugat peste rezolvarea proprie pentru etapa II, mentionez
insa ca am folosit la baza rezolvarea oficiala a primei etape a acestui proiect.

## Wrapped:
  * Comenzile wrapped au fost implementate intr-un mod similar cu un command pattern. Toate cele trei tipuri
de comenzi wrapped (pentru users, artists si hosts) implementeaza o interfata numita "WrappedCommand",
aceasta avand scopul de a facilita apelul de comenzi wrapped prin polimorfism. 
  * Statisticile despre useri sunt updatate constant in StatisticsWrapped dupa fiecare schimbare a
melodiei din player.

## Monetization
  * Toate datele despre castigurile artistitilor sunt retinute intr-un obiect static denumit
"MonetizationStatistics".
  * Pe deasupra, fiecare user va avea in Playerul sau un nou obiect denumit "MonetizationCalculator", acesta
retine ce melodii trebuie sa fie platite de catre user fie prin ascultarea unor reclame, sau printr-un
abonament premium.

## Notifications
  * Notificarile pentru useri urmeaza un observer pattern. Artistul retine intr-o lista toti utilizatorii
care i-au dat follow, pentru a-i putea notifica ulterior la nevoie. Notificarile sunt un nou element adaugat
in pachetul page_elements, deoarece se folosesc doar in homepage.

## Recommendations
  * Un nou obiect care se comporta similar cu SearchBar, recomandarile pentru user sunt generate conform
cerintei si rezultatele sunt retinute in obiectul Recommendation.

## Alte Mentiuni
  * Pe langa cele doua design pattern-uri mentionate mai sus mai exista alte doua de care m-am folosit si 
in etapa anterioara. Unul este Adminul care este un obiect de tip singleton, si celalalt este un factory
pattern care este folosit pentru a creea userii noi adaugati de comanda "addUser".
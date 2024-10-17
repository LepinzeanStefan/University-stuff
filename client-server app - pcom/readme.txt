# Implementarea temei

## structura pachetului pentru mesaje
  -  Structura folosita pentru pachet reprezinta o extindere a structurii date in cerinta.
  Primele 3 campuri se folosesc conform cerintei pentru a trasmite mesajele date de clientii
  udp, desi campul type are mai multe tipuri adaugate fata de cele din cerinta pentru a
  putea transmite diferite comenzi, precum subscribe sau exit. Pe langa campurile din
  cerinta au mai fost adaugate doua campuri si pentru transmiterea adresei clientului udp.

## server.cpp:
  -  Serverul are rolul de intermediar intre clientii de pe platforma. Functia main incepe
  cu initializarea socketului upd si tcp pentru a primii mesaje de la clienti. Dupa
  intializare, se apeleaza functia run_chat_multiserver, unde este creat un poll pentru
  multiplexare care are original socketul pentru udp, tcp si fisierul stdin. Odata ce
  primeste o cerere de conexiune de la un client tcp acesta accepta conexiunea si asteapta
  innapoi un pachet tcp care contine ID-ul clientului care cere sa se conecteze, daca ID-ul
  dat este deja folosit de alt client conectat, serverul refuza conexiunea si ii trimite
  inapoi un mesaj sa se inchida singur. Odata conectat clientul tcp poate transmite cereri
  de subscribe pentru a se abona la diferite topicuri. Topicurile sunt retinute ca o mapare,
  in care cheia este numele topicului in sine si valorile dinauntru sunt ID-urile
  clientilor care s-au abonat la acel topic. Daca un mesaj este pe primit pe socketul udp,
  serverul verifica in acel Map daca exista vreun client abonat la acel topic, caz in care
  verifica daca clientul este conectat si ii trimite mesajul respectiv.

## subscriber.cpp:
  -  Subscriberul este clientul de tip tcp si are rolul de a primi comenzi de la tastatura
  de abonare sau dezabonare si a primi mesajele de la server cu informatiile de la
  topicurile abonate. El incepe prin a cere o conexiune de tip tcp cu serverul de la adresa
  data ca argument, apoi trimite imediat un mesaj care contine ID-ul sau in topic. Odata ce
  conexiunea este finalizata, clientul se poate abona sau dezabona de la diferite topicuri
  prin intermediul pachetelor de mesaje. Daca clientul primeste un pachet de la server,
  acesta ori este o comanda care ii cere sa se deconecteze ori un mesaj trimis pe unul
  dintre topicurile la care este abonat, caz in care il decodeaza conform instructiunilor
  din cerinta.

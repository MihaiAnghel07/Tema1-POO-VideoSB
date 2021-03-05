Nume: Anghel Mihai-Gabriel
Grupa: 323CD

	In implementarea temei am creat 5 clase care impreuna cu clasele din 
schelet m-au ajutat sa rezolv o parte din tema. Unde am putut, am adaugat 
campuri noi claselor din schelet (mi s-a parut mult mai simplu).

In main, am stocat datele de intrare si le-am parsat instantei clasei 
SolveAction, care rezolva fiecare actiune in parte.
Numele claselor create reflecta tipul actiunei pe care o prelucreaza (comenzi,
queries sau recomandări), iar metodele acestora rezolva cate un subtip al
actiunei respective ( Favorite command, Average Query etc.). Exista si 
metode care au ca scop prelucrarea datelor despre filme, show-uri sau sezoane,
date de care este nevoie in cazul mai multor actiuni si astfel se apeleaza 
metoda corespunzatoare, fara a duplica cod.
Clasa Video are scopul de a retine atat filme cat si show-uri in scopul 
anumitor sortari care nu tin cont de tipul video-ului.
Fiecare metoda ce, rezolva proriu zis o actiune, primeste in antet listele de 
care are nevoie, prelucreaza datele apoi returneaza un string ce reprezinta 
raspunsul.
	In ceea ce priveste modul de prelucrare, exista un flow asemanator 
intre acestea. Se parcurg listele, se tine cont de filtrele din comanda, se fac
sortarile tinand cont de criteriile multiple, iar daca totul este ok, se 
formeaza stringul de output.






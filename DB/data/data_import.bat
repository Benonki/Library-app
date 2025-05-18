@echo off
chcp 65001 > nul

set /p USER=Podaj Login:
set /p PASSWORD=Podaj Haslo:
set /p HOST=Podaj Hosta:
set /p PORT=Podaj Port:
set /p SID=Podaj SID:

set CONNSTR=%USER%/%PASSWORD%@%HOST%:%PORT%/%SID%

sqlldr userid=%CONNSTR% control='Rola.ctl'
sqlldr userid=%CONNSTR% control='Autor.ctl'
sqlldr userid=%CONNSTR% control='Typ_Okladki.ctl'
sqlldr userid=%CONNSTR% control='Wydawnictwo.ctl'
sqlldr userid=%CONNSTR% control='Ksiazka.ctl'
sqlldr userid=%CONNSTR% control='Uzytkownik.ctl'
sqlldr userid=%CONNSTR% control='Dostawca.ctl'
sqlldr userid=%CONNSTR% control='Zamowienie.ctl'
sqlldr userid=%CONNSTR% control='Zamowienie_Ksiazka.ctl'
sqlldr userid=%CONNSTR% control='Magazyn.ctl'
sqlldr userid=%CONNSTR% control='Dostawa.ctl'
sqlldr userid=%CONNSTR% control='Karta_Biblioteczna.ctl'
sqlldr userid=%CONNSTR% control='Raport.ctl'
sqlldr userid=%CONNSTR% control='Rezerwacja.ctl'
sqlldr userid=%CONNSTR% control='Wydarzenie.ctl'
sqlldr userid=%CONNSTR% control='Uczestnik_Wydarzenia.ctl'
sqlldr userid=%CONNSTR% control='Wypozyczenie.ctl'

echo Importowanie zakonczone.
pause

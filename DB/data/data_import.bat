@echo off
chcp 65001 > nul

set /p USER=Podaj Login:
set /p PASSWORD=Podaj Haslo:
set /p HOST=Podaj Hosta:
set /p PORT=Podaj Port:
set /p SID=Podaj SID:

set CONNSTR=%USER%/%PASSWORD%@%HOST%:%PORT%/%SID%

sqlldr userid=%CONNSTR% control='uzytkownik.ctl'
sqlldr userid=%CONNSTR% control='ksiazka.ctl'
sqlldr userid=%CONNSTR% control='karta_biblioteczna.ctl'
sqlldr userid=%CONNSTR% control='raport.ctl'
sqlldr userid=%CONNSTR% control='wydarzenie.ctl'
sqlldr userid=%CONNSTR% control='uczestnik_wydarzenia.ctl'
sqlldr userid=%CONNSTR% control='rezerwacja.ctl'
sqlldr userid=%CONNSTR% control='wypozyczenie.ctl'
sqlldr userid=%CONNSTR% control='zamowienie.ctl'
sqlldr userid=%CONNSTR% control='dostawa.ctl'
sqlldr userid=%CONNSTR% control='pozycja_zamowienia.ctl'
sqlldr userid=%CONNSTR% control='pozycja_dostawy.ctl'
sqlldr userid=%CONNSTR% control='magazyn.ctl'

echo Importowanie zakonczone.
pause

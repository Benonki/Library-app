LOAD DATA
INFILE 'Rezerwacja.csv'
INTO TABLE REZERWACJA
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
Data_Rezerwacji DATE "YYYY-MM-DD",
Data_Wygasniecia DATE "YYYY-MM-DD",
Status,
Uzytkownik_ID,
Ksiazka_ID
)

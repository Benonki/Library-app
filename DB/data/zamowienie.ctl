LOAD DATA
INFILE 'Zamowienie.csv'
INTO TABLE ZAMOWIENIE
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
Uzytkownik_ID,
Dostawca_ID,
Ilosc,
Data_Zamowienia DATE "YYYY-MM-DD",
Termin_Realizacji DATE "YYYY-MM-DD",
Status
)

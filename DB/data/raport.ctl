LOAD DATA
INFILE 'Raport.csv'
INTO TABLE RAPORT
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
Typ,
Data_Generacji DATE "YYYY-MM-DD",
Zawartosc,
Uzytkownik_ID
)

LOAD DATA
INFILE 'Karta_Biblioteczna.csv'
INTO TABLE KARTA_BIBLIOTECZNA
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
Karta_Biblioteczna_ID,
Numer_Karty,
Data_Wydania DATE "YYYY-MM-DD",
Data_Waznosci DATE "YYYY-MM-DD",
Status,
Uzytkownik_ID
)

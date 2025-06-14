LOAD DATA
INFILE 'Wypozyczenie.csv'
INTO TABLE WYPOZYCZENIE
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
Data_Wypozyczenia DATE "YYYY-MM-DD",
Planowana_Data_Zwrotu DATE "YYYY-MM-DD",
Rzeczywista_Data_Zwrotu DATE "YYYY-MM-DD",
Kara,
Uzytkownik_ID,
Ksiazka_ID
)

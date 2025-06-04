LOAD DATA
INFILE 'Biblioteka_ksiazka.csv'
INTO TABLE Biblioteka_Ksiazka
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    Ksiazka_ID,
    Status,
    Lokalizacja
)

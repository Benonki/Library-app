LOAD DATA
INFILE 'rezerwacja.csv'
INTO TABLE rezerwacja
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    id,
    data_rezerwacji DATE "YYYY-MM-DD",
    data_wygasniecia DATE "YYYY-MM-DD",
    status,
    uzytkownik_id,
    ksiazka_id
)
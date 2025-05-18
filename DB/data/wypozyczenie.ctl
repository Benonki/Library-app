LOAD DATA
INFILE 'wypozyczenie.csv'
INTO TABLE wypozyczenie
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    id,
    data_wypozyczenia DATE "YYYY-MM-DD",
    planowana_data_zwrotu DATE "YYYY-MM-DD",
    rzeczywista_data_zwrotu DATE "YYYY-MM-DD",
    kara,
    uzytkownik_id,
    ksiazka_id
)
LOAD DATA
INFILE 'uzytkownik.csv'
INTO TABLE uzytkownik
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    id,
    imie,
    nazwisko,
    email,
    haslo,
    typ_uzytkownika
)
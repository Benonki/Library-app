LOAD DATA
INFILE 'magazyn.csv'
INTO TABLE magazyn
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    id,
    ilosc,
    lokalizacja,
    ksiazka_id
)
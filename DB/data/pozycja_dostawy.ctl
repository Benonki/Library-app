LOAD DATA
INFILE 'pozycja_dostawy.csv'
INTO TABLE pozycja_dostawy
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    id,
    ilosc,
    ksiazka_id,
    dostawa_id
)
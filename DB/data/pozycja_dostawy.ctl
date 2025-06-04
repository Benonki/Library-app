LOAD DATA
INFILE 'pozycja_dostawy.csv'
INTO TABLE pozycja_dostawy
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    ilosc,
    ksiazka_id,
    dostawa_id
)
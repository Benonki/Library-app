LOAD DATA
INFILE 'pozycja_zamowienia.csv'
INTO TABLE pozycja_zamowienia
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    ilosc,
    ksiazka_id,
    zamowienie_id
)
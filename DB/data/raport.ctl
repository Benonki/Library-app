LOAD DATA
INFILE 'raport.csv'
INTO TABLE raport
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    id,
    typ,
    data_generacji DATE "YYYY-MM-DD",
    zawartosc,
    uzytkownik_id
)
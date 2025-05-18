LOAD DATA
INFILE 'karta_biblioteczna.csv'
INTO TABLE karta_biblioteczna
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    id,
    numer_karty,
    data_wydania DATE "YYYY-MM-DD",
    data_waznosci DATE "YYYY-MM-DD",
    status,
    uzytkownik_id
)
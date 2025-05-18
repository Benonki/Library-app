LOAD DATA
INFILE 'ksiazka.csv'
INTO TABLE ksiazka
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    id,
    tytul,
    autor,
    data_wydania DATE "YYYY-MM-DD",
    wydawnictwo,
    typ_okladki,
    gatunek,
    status
)
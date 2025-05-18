LOAD DATA
INFILE 'uczestnik_wydarzenia.csv'
INTO TABLE uczestnik_wydarzenia
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    id,
    uzytkownik_id,
    wydarzenie_id
)
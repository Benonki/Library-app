LOAD DATA
INFILE 'wydarzenie.csv'
INTO TABLE wydarzenie
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    id,
    temat,
    data DATE "YYYY-MM-DD",
    godzina,
    miejsce
)
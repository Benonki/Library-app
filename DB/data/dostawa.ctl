LOAD DATA
INFILE 'dostawa.csv'
INTO TABLE dostawa
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    id,
    data_dostawy DATE "YYYY-MM-DD",
    status,
    zamowienie_id
)
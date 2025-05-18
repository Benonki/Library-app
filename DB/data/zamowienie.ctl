LOAD DATA
INFILE 'zamowienie.csv'
INTO TABLE zamowienie
APPEND
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
    id,
    dostawca,
    data_zamowienia DATE "YYYY-MM-DD",
    termin_realizacji DATE "YYYY-MM-DD",
    status
)
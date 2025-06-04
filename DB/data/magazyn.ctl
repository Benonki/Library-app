LOAD DATA
INFILE 'Magazyn.csv'
INTO TABLE MAGAZYN
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
TRAILING NULLCOLS
(
Ksiazka_ID,
Ilosc,
Sektor,
Rzad,
Polka,
MiejsceNaPolce
)

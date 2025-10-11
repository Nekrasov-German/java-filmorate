INSERT INTO genre (name)
SELECT 'Комедия' WHERE NOT EXISTS (SELECT 1 FROM genre WHERE name = 'Комедия');
INSERT INTO genre (name)
SELECT 'Драма' WHERE NOT EXISTS (SELECT 1 FROM genre WHERE name = 'Драма');
INSERT INTO genre (name)
SELECT 'Мультфильм' WHERE NOT EXISTS (SELECT 1 FROM genre WHERE name = 'Мультфильм');
INSERT INTO genre (name)
SELECT 'Триллер' WHERE NOT EXISTS (SELECT 1 FROM genre WHERE name = 'Триллер');
INSERT INTO genre (name)
SELECT 'Документальный' WHERE NOT EXISTS (SELECT 1 FROM genre WHERE name = 'Документальный');
INSERT INTO genre (name)
SELECT 'Боевик' WHERE NOT EXISTS (SELECT 1 FROM genre WHERE name = 'Боевик');

INSERT INTO MPA (name, age_limit)
SELECT 'G', 0 WHERE NOT EXISTS (SELECT 1 FROM MPA WHERE name = 'G');
INSERT INTO MPA (name, age_limit)
SELECT 'PG', 6 WHERE NOT EXISTS (SELECT 1 FROM MPA WHERE name = 'PG');
INSERT INTO MPA (name, age_limit)
SELECT 'PG-13', 12 WHERE NOT EXISTS (SELECT 1 FROM MPA WHERE name = 'PG-13');
INSERT INTO MPA (name, age_limit)
SELECT 'R', 16 WHERE NOT EXISTS (SELECT 1 FROM MPA WHERE name = 'R');
INSERT INTO MPA (name, age_limit)
SELECT 'NC-17', 18 WHERE NOT EXISTS (SELECT 1 FROM MPA WHERE name = 'NC-17');
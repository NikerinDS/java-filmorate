MERGE INTO ratings AS Target
USING (VALUES
    (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17')
) AS Source (id, name)
ON (Target.id = Source.id)
WHEN MATCHED THEN
    UPDATE SET
        Target.name = Source.name
WHEN NOT MATCHED THEN
    INSERT (id, name)
    VALUES (Source.id, Source.name);

MERGE INTO genres AS Target
USING (VALUES
        (1, 'Комедия'),
        (2, 'Драма'),
        (3, 'Мультфильм'),
        (4, 'Триллер'),
        (5, 'Документальный'),
        (6, 'Боевик')
) AS Source (id, name)
ON (Target.id = Source.id)
WHEN MATCHED THEN
    UPDATE SET
        Target.name = Source.name
WHEN NOT MATCHED THEN
    INSERT (id, name)
    VALUES (Source.id, Source.name);
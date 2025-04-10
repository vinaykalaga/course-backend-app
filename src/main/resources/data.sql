-- Auto insert roles if table is empty
INSERT INTO authdb.role (authority)
SELECT * FROM (SELECT 'ROLE_INSTRUCTOR') AS tmp
WHERE NOT EXISTS (
    SELECT authority FROM authdb.role WHERE authority = 'ROLE_INSTRUCTOR'
) LIMIT 1;

INSERT INTO authdb.role (authority)
SELECT * FROM (SELECT 'ROLE_LEARNER') AS tmp
WHERE NOT EXISTS (
    SELECT authority FROM authdb.role WHERE authority = 'ROLE_LEARNER'
) LIMIT 1;

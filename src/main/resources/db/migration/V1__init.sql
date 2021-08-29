DROP INDEX IF EXISTS headlines_title_idx;
DROP TABLE IF EXISTS headlines;

CREATE TABLE headlines
(
    link  VARCHAR PRIMARY KEY,
    title VARCHAR NOT NULL
);

CREATE INDEX headlines_title_idx ON headlines (title);

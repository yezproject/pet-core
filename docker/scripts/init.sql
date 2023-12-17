DROP TABLE IF EXISTS test_table;
CREATE TABLE test_table
(
    id      serial PRIMARY KEY,
    content VARCHAR(50)
);

INSERT INTO test_table (content) VALUES ('This is content should be on init database');
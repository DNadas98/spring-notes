CREATE TABLE IF NOT EXISTS notes
(
    id         SERIAL PRIMARY KEY,
    title      VARCHAR(100) UNIQUE                 NOT NULL,
    content    TEXT                                NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
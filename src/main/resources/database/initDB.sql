CREATE TABLE IF NOT EXISTS reservations(
    id SERIAL PRIMARY KEY ,
    name VARCHAR(200) NOT NULL,
    date TIMESTAMP NOT NULL,
    email VARCHAR(254) NOT NULL,
    party VARCHAR(4) NOT NULL,
    status VARCHAR(10) NOT NULL,
    CHECK ( party IN ('1', '2', '3', '4', '5', '6-10')),
    CHECK ( status IN ('confirmed', 'canceled', 'waiting'))
);
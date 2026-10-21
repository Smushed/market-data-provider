CREATE TABLE IF NOT EXISTS market_days (
    id SERIAL PRIMARY KEY,
    ticker TEXT,
    open_price NUMERIC(9,4),
    close_price NUMERIC(9,4),
    session_high NUMERIC(9,4),
    session_low NUMERIC(9,4),
    volume INT
)
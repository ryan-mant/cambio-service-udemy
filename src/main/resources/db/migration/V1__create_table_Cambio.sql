CREATE TABLE cambio (
  id BIGSERIAL PRIMARY KEY,
  from_currency CHAR(10) NOT NULL,
  to_currency CHAR(10) NOT NULL,
  conversion_factor DECIMAL(65, 2) NOT NULL
);

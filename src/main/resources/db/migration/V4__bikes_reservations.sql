-- BIKES
CREATE TABLE bikes (
                       id              BIGSERIAL PRIMARY KEY,
                       owner_id        BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                       model           VARCHAR(120) NOT NULL,
                       type            VARCHAR(32)  NOT NULL,
                       cost_per_minute NUMERIC(10,2) NOT NULL CHECK (cost_per_minute >= 0),
                       image_url       VARCHAR(512),
                       latitude        DOUBLE PRECISION,
                       longitude       DOUBLE PRECISION,
                       status          VARCHAR(24)  NOT NULL DEFAULT 'AVAILABLE'
);

CREATE INDEX idx_bikes_owner ON bikes(owner_id);

-- RESERVATIONS
CREATE TABLE reservations (
                              id           BIGSERIAL PRIMARY KEY,
                              renter_id    BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
                              bike_id      BIGINT NOT NULL REFERENCES bikes(id) ON DELETE RESTRICT,
                              start_date   TIMESTAMP NOT NULL,
                              end_date     TIMESTAMP NOT NULL,
                              total_price  NUMERIC(12,2),
                              status       VARCHAR(24) NOT NULL,
                              CONSTRAINT chk_reservation_window CHECK (end_date > start_date)
);

CREATE INDEX idx_reservations_bike ON reservations(bike_id, start_date);
CREATE INDEX idx_reservations_renter ON reservations(renter_id, start_date);
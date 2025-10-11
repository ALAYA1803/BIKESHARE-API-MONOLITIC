-- REVIEWS
CREATE TABLE reviews (
                         id             BIGSERIAL PRIMARY KEY,
                         reservation_id BIGINT NOT NULL UNIQUE REFERENCES reservations(id) ON DELETE CASCADE,
                         reviewer_id    BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
                         owner_id       BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
                         bike_id        BIGINT REFERENCES bikes(id) ON DELETE SET NULL,
                         rating         INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
                         comment        TEXT,
                         created_at     TIMESTAMP NOT NULL DEFAULT NOW()
);

-- SUPPORT_TICKETS
CREATE TABLE support_tickets (
                                 id             BIGSERIAL PRIMARY KEY,
                                 user_id        BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                 subject        VARCHAR(160) NOT NULL,
                                 category       VARCHAR(64),
                                 message        TEXT NOT NULL,
                                 status         VARCHAR(24) NOT NULL DEFAULT 'OPEN', -- OPEN, IN_PROGRESS, RESOLVED, CLOSED
                                 created_at     TIMESTAMP NOT NULL DEFAULT NOW(),
                                 attachment_url VARCHAR(512)
);

CREATE INDEX idx_tickets_user ON support_tickets(user_id, created_at DESC);

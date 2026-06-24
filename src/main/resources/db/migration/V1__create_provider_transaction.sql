CREATE TABLE provider_transactions (
   transaction_id VARCHAR(255) PRIMARY KEY,
   saga_id VARCHAR(255),
   amount BIGINT,
   status VARCHAR(50),
   delay_ms INTEGER,
   fail_rate INTEGER,
   forced_outcome VARCHAR(20),
   created_at TIMESTAMP DEFAULT now(),
   updated_at TIMESTAMP DEFAULT now()
);

CREATE TABLE "covid_code"(
 "pk_covid_code_id" Serial NOT NULL,
 "code" Text NOT NULL,
 "created_at" Timestamp with time zone DEFAULT now() NOT NULL,
 "used" boolean NOT NULL
)
WITH (autovacuum_enabled=true);

-- Add keys for table -- Add keys for table covid_code

ALTER TABLE "covid_code" ADD CONSTRAINT "PK_covid_code" PRIMARY KEY ("pk_covid_code_id");
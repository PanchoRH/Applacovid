CREATE TABLE "beacon_route"(
 "pk_beacon_route_id" Serial NOT NULL,
 "app_uuid" Text NOT NULL,
 "beacon_uuid" Text NOT NULL,
 "major" Int NOT NULL,
 "minor" Int NOT NULL,
 "timestamp" Timestamp with time zone DEFAULT now() NOT NULL,
 "rssi" Int NOT NULL
)
WITH (autovacuum_enabled=true);

-- Add keys for table -- Add keys for table beacon_route

ALTER TABLE "beacon_route" ADD CONSTRAINT "PK_beacon_route" PRIMARY KEY ("pk_beacon_route_id");
-- Create the "access_event" table to store the access events of vehicles
CREATE TABLE access_event (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plate VARCHAR(10) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    direction VARCHAR(10) NOT NULL,
    result VARCHAR(10) NOT NULL
);
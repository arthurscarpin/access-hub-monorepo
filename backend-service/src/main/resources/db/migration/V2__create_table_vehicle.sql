-- Create the "vehicle" table with a foreign key reference to the "owner" table
CREATE TABLE vehicle (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plate VARCHAR(10) UNIQUE NOT NULL,
    model VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    owner_id UUID NOT NULL,

    CONSTRAINT fk_vehicle_owner
        FOREIGN KEY (owner_id)
        REFERENCES owner(id)
);
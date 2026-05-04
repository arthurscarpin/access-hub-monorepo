-- Create the "owner" table with the specified columns and constraints
CREATE TABLE owner (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    document VARCHAR(20) UNIQUE NOT NULL,
    document_type VARCHAR(10) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);
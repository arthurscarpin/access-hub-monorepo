-- Create the "users_scopes" table to establish a many-to-many relationship between users and scopes
CREATE TABLE users_scopes (
    user_id UUID NOT NULL,
    scope_id UUID NOT NULL,
    PRIMARY KEY (user_id, scope_id),
    CONSTRAINT fk_user_scopes_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_scopes_scope FOREIGN KEY (scope_id) REFERENCES scopes(id)
);
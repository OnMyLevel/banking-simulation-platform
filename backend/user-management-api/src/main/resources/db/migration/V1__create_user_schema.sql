CREATE SCHEMA IF NOT EXISTS user_schema;

CREATE TABLE IF NOT EXISTS user_schema.users (
    id UUID PRIMARY KEY,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(40) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS user_schema.user_roles (
    user_id UUID NOT NULL,
    role_name VARCHAR(80) NOT NULL,
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES user_schema.users(id),
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_name)
);

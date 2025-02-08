
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       login VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);


CREATE TABLE locations (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           user_id INT NOT NULL,
                           latitude DECIMAL(9,6) NOT NULL,
                           longitude DECIMAL(9,6) NOT NULL,
                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                           UNIQUE (user_id, latitude, longitude)
);


CREATE TABLE sessions (
                          id VARCHAR(36) PRIMARY KEY,
                          user_id INT NOT NULL,
                          expires_at TIMESTAMP NOT NULL,
                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
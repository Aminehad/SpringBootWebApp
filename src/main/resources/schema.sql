CREATE TABLE app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    created_at DATE,
    updated_at DATE
);

CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(255),
    key_words VARCHAR(255),
    picture VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    delivery VARCHAR(50) NOT NULL,
    category_id BIGINT,
    condition VARCHAR(255),
    created_at DATE,
    updated_at DATE,
    user_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);


CREATE TABLE user_favorites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Optional: unique ID for the relationship
    user_id BIGINT NOT NULL, -- Links to app_user
    item_id BIGINT NOT NULL, -- Links to item
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of when the favorite was created
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE, -- Deletes all favorites if the user is deleted
    FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE, -- Deletes all favorites if the item is deleted
    UNIQUE (user_id, item_id) -- Ensures a user can't favorite the same item multiple times
);
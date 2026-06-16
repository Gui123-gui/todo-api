CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50) NOT NULL,
    due_date DATE,
    created_at DATETIME,
    updated_at DATETIME,
    user_id BIGINT NOT NULL,
    category_id BIGINT,
    CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_tasks_category FOREIGN KEY (category_id) REFERENCES categories(id)
);
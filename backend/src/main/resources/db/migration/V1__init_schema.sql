-- =============================================================================
-- V1 : Schéma initial — toutes les tables du projet
--
-- Pour une base existante (créée par ddl-auto=update), configurer :
--   spring.flyway.baseline-on-migrate=true
-- Flyway marque alors V1 comme "déjà appliquée" sans l'exécuter.
-- =============================================================================

CREATE TABLE IF NOT EXISTS users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255)  NOT NULL UNIQUE,
    password   VARCHAR(255)  NOT NULL,
    first_name VARCHAR(255)  NOT NULL,
    last_name  VARCHAR(255)  NOT NULL,
    enabled    BOOLEAN       NOT NULL DEFAULT FALSE,
    role       VARCHAR(50)   NOT NULL DEFAULT 'USER',
    created_at DATETIME(6)   NOT NULL,
    updated_at DATETIME(6)
);

CREATE TABLE IF NOT EXISTS confirmation_tokens (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    token        VARCHAR(255) NOT NULL UNIQUE,
    created_at   DATETIME(6)  NOT NULL,
    expires_at   DATETIME(6)  NOT NULL,
    confirmed_at DATETIME(6),
    user_id      BIGINT       NOT NULL,
    CONSTRAINT fk_confirmation_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS refresh_tokens (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    token       VARCHAR(255) NOT NULL UNIQUE,
    expiry_date DATETIME(6)  NOT NULL,
    user_id     BIGINT       NOT NULL,
    CONSTRAINT fk_refresh_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS applications (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    company          VARCHAR(255) NOT NULL,
    position         VARCHAR(255) NOT NULL,
    application_date DATE         NOT NULL,
    current_status   VARCHAR(20)  NOT NULL,
    notes            TEXT,
    created_at       DATETIME(6)  NOT NULL,
    updated_at       DATETIME(6),
    user_id          BIGINT,
    CONSTRAINT fk_application_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS documents (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_filename VARCHAR(255) NOT NULL,
    stored_filename   VARCHAR(255) NOT NULL,
    content_type      VARCHAR(255),
    file_size         BIGINT,
    uploaded_at       DATETIME(6)  NOT NULL,
    application_id    BIGINT       NOT NULL,
    CONSTRAINT fk_document_application FOREIGN KEY (application_id) REFERENCES applications (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS status_history (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    status         VARCHAR(20)  NOT NULL,
    changed_at     DATETIME(6)  NOT NULL,
    notes          TEXT,
    application_id BIGINT       NOT NULL,
    CONSTRAINT fk_status_history_application FOREIGN KEY (application_id) REFERENCES applications (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS flashcards (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    question         VARCHAR(1000) NOT NULL,
    answer           VARCHAR(5000) NOT NULL,
    category         VARCHAR(50)   NOT NULL,
    difficulty       VARCHAR(10)   NOT NULL,
    last_reviewed    DATETIME(6),
    review_count     INT           NOT NULL DEFAULT 0,
    confidence_level INT           NOT NULL DEFAULT 0,
    created_at       DATETIME(6)   NOT NULL,
    user_id          BIGINT        NOT NULL,
    CONSTRAINT fk_flashcard_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS coding_challenges (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255)  NOT NULL,
    platform     VARCHAR(255)  NOT NULL,
    difficulty   VARCHAR(10)   NOT NULL,
    status       VARCHAR(20)   NOT NULL DEFAULT 'TODO',
    link         VARCHAR(1000),
    notes        VARCHAR(5000),
    completed_at DATETIME(6),
    created_at   DATETIME(6)   NOT NULL,
    user_id      BIGINT        NOT NULL,
    CONSTRAINT fk_coding_challenge_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS technical_questions (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    question         VARCHAR(1000) NOT NULL,
    answer           VARCHAR(5000) NOT NULL,
    category         VARCHAR(50)   NOT NULL,
    confidence_level INT           NOT NULL DEFAULT 0,
    last_reviewed    DATETIME(6),
    created_at       DATETIME(6)   NOT NULL,
    user_id          BIGINT        NOT NULL,
    CONSTRAINT fk_technical_question_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS skills (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255)  NOT NULL,
    category   VARCHAR(50)   NOT NULL,
    level      INT           NOT NULL DEFAULT 0,
    notes      VARCHAR(2000),
    updated_at DATETIME(6),
    user_id    BIGINT        NOT NULL,
    CONSTRAINT fk_skill_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS study_sessions (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    date             DATE         NOT NULL,
    topic            VARCHAR(50)  NOT NULL,
    duration_minutes INT          NOT NULL,
    notes            VARCHAR(2000),
    created_at       DATETIME(6)  NOT NULL,
    user_id          BIGINT       NOT NULL,
    CONSTRAINT fk_study_session_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

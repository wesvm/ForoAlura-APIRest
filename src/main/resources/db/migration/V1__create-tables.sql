CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL unique,
    password VARCHAR(300) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE courses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

create table topics (
    id bigint not null auto_increment,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    creation_date DATETIME NOT NULL,
    topic_status VARCHAR(20) NOT NULL,
    author_id bigint NOT NULL,
    course_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_topics_author FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT fk_topics_course FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE replies (
    id BIGINT NOT NULL AUTO_INCREMENT,
    message TEXT NOT NULL,
    creation_date DATETIME NOT NULL,
    topic_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    solution TINYINT(1) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_replies_topic FOREIGN KEY (topic_id) REFERENCES topics(id),
    CONSTRAINT fk_replies_author FOREIGN KEY (author_id) REFERENCES users(id)
);


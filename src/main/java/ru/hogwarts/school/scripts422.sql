
CREATE TABLE persons (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    has_rights BOOLEAN NOT NULL,
    CONSTRAINT age_check CHECK (
        ( has_rights = TRUE AND age >= 18) OR
        ( has_rights = FALSE)
    )
);

CREATE TABLE cars (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    price DECIMAL NOT NULL
);

CREATE TABLE people_cars (
    person_id INT NOT NULL,
    car_id INT NOT NULL,
    PRIMARY KEY (person_id, car_id),
    FOREIGN KEY (person_id) REFERENCES persons(id),
    FOREIGN KEY (car_id) REFERENCES cars(id)
);
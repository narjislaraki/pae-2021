-- Last modification date : 04/03/2021 --
DROP SCHEMA IF EXISTS pae CASCADE;
CREATE SCHEMA pae;

CREATE TYPE pae.roles AS ENUM('admin','antiquaire','client');

CREATE TABLE pae.addresses(
	id_address SERIAL PRIMARY KEY,
	street VARCHAR(50) NOT NULL,
	building_number VARCHAR(7) NOT NULL,
	unit_number INTEGER,
	city VARCHAR(15) NOT NULL,
	postcode VARCHAR(10) NOT NULL,
	country VARCHAR(15) NOT NULL
);

CREATE TABLE pae.users(
	id_user SERIAL PRIMARY KEY,
	username VARCHAR(30) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	first_name VARCHAR(30) NOT NULL,
	email VARCHAR(60) NOT NULL,
	role pae.roles NOT NULL,
	registration_date TIMESTAMP NOT NULL,
	is_validated BOOLEAN NOT NULL,
	password CHARACTER(60) NOT NULL,
	address INTEGER REFERENCES pae.addresses(id_address) NOT NULL
);

INSERT INTO pae.addresses VALUES(default, 'rue des sentiers', '7', 1, 'Bruxelles', '1300', 'Belgique');
-- the mdp of the 2 users is 1234 --
INSERT INTO pae.users VALUES
		(default, 'test', 'Heuzer','Nina', 'test@test.com', 'admin', '05/01/2021', true, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1),
		(default, 'test2', 'Laraki', 'Narjis', 'test2@test.com', 'client', '06/02/2021', true, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1),
		(default, 'test3', 'de Theux', 'Boris', 'test3@test.com', 'client', '07/02/2021', false, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1);
		
SELECT * FROM pae.users;	
		
		
		

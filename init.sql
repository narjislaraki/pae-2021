-- Last modification date : 04/03/2021 --
DROP SCHEMA IF EXISTS pae CASCADE;
CREATE SCHEMA pae;

CREATE TYPE pae.roles AS ENUM('admin','antiquaire','client');
CREATE TYPE pae.conditions AS ENUM('proposé','acheté','refusé','en restauration',
'déposé en magasin','en vente','sous option','vendu','emporté','livré','réservé','retiré de la vente');

CREATE TYPE pae.visits_conditions AS ENUM('en attante','accepté','annulé');
 
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

CREATE TABLE pae.types_of_furnitures(
	id_type SERIAL PRIMARY KEY,
	label VARCHAR(50) NOT NULL
);

CREATE TABLE pae.requests_for_visits(
	id_request SERIAL PRIMARY KEY,
	time_slot VARCHAR(150) NOT NULL,
	condition pae.visits_conditions NOT NULL,
	explanatory_note VARCHAR(150),
	scheduled_date_time TIMESTAMP,
	warehouse_address INTEGER REFERENCES pae.addresses(id_address),
	client INTEGER REFERENCES pae.users(id_user) NOT NULL
);


CREATE TABLE pae.furnitures(
	id_furniture SERIAL PRIMARY KEY,
	condition pae.conditions NOT NULL,
	description VARCHAR (150) NOT NULL,
	purchase_price DOUBLE PRECISION,
	pick_up_date TIMESTAMP,
	store_deposit BOOLEAN,
	deposit_date TIMESTAMP,
	offered_selling_price DOUBLE PRECISION,
	id_type INTEGER REFERENCES pae.types_of_furnitures(id_type) NOT NULL,
	request_visit INTEGER REFERENCES pae.requests_for_visits(id_request),
	seller INTEGER REFERENCES pae.users(id_user)
);

CREATE TABLE pae.photos(
	id_photo SERIAL PRIMARY KEY,
	photo VARCHAR(400) NOT NULL,
	is_visible BOOLEAN NOT NULL,
	description VARCHAR(200),
	is_a_client_photo BOOLEAN NOT NULL, 
	furniture INTEGER REFERENCES pae.furnitures(id_furniture) NOT NULL
);	

ALTER TABLE pae.furnitures
ADD favorite_photo INTEGER REFERENCES pae.photos(id_photo);

INSERT INTO pae.addresses VALUES(default, 'rue des sentiers', '7', 1, 'Bruxelles', '1300', 'Belgique');
-- the mdp of the 2 users is 1234 --
INSERT INTO pae.users VALUES
		(default, 'test', 'Heuzer','Nina', 'test@test.com', 'admin', '05/01/2021', true, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1),
		(default, 'test2', 'Laraki', 'Narjis', 'test2@test.com', 'client', '06/02/2021', true, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1),
		(default, 'test3', 'de Theux', 'Boris', 'test3@test.com', 'client', '07/02/2021', false, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1);
		
SELECT * FROM pae.users;	
		
		
		

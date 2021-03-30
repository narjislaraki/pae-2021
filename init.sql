-- Last modification date : 04/03/2021 --
DROP SCHEMA IF EXISTS pae CASCADE;
CREATE SCHEMA pae;

CREATE TABLE pae.addresses(
	id_address SERIAL PRIMARY KEY,
	street VARCHAR(50) NOT NULL,
	building_number VARCHAR(7) NOT NULL,
	unit_number VARCHAR(10),
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
	role VARCHAR(10) NOT NULL,
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
	condition VARCHAR(10) NOT NULL,
	explanatory_note VARCHAR(150),
	scheduled_date_time TIMESTAMP,
	warehouse_address INTEGER REFERENCES pae.addresses(id_address),
	client INTEGER REFERENCES pae.users(id_user) NOT NULL
);


CREATE TABLE pae.furnitures(
	id_furniture SERIAL PRIMARY KEY,
	condition VARCHAR(18) NOT NULL,
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

CREATE TABLE pae.options(
	id_option SERIAL PRIMARY KEY,
	date TIMESTAMP NOT NULL,
	option_term INTEGER NOT NULL,
	cancellation_reason VARCHAR (150),
	condition VARCHAR(9) NOT NULL,
	id_user INTEGER REFERENCES pae.users(id_user) NOT NULL,
	id_furniture INTEGER REFERENCES pae.furnitures(id_furniture) NOT NULL
);

CREATE TABLE pae.photos(
	id_photo SERIAL PRIMARY KEY,
	photo TEXT NOT NULL,
	is_visible BOOLEAN NOT NULL,
	--description VARCHAR(200),
	is_a_client_photo BOOLEAN NOT NULL, 
	id_furniture INTEGER REFERENCES pae.furnitures(id_furniture) NOT NULL
);	

ALTER TABLE pae.furnitures
ADD favorite_photo INTEGER REFERENCES pae.photos(id_photo);

--INSERT INTO pae.addresses VALUES(default, 'rue des sentiers', '7', 1, 'Bruxelles', '1300', 'Belgique');
-- the mdp of the 2 users is 1234 --
--INSERT INTO pae.users VALUES
	--	(default, 'test', 'Heuzer','Nina', 'test@test.com', 'admin', '05/01/2021', true, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1),
		--(default, 'test2', 'Laraki', 'Narjis', 'test2@test.com', 'client', '06/02/2021', true, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1),
		--(default, 'test3', 'de Theux', 'Boris', 'test3@test.com', 'client', '07/02/2021', false, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1),
		--(default, 'test4', 'Pouet4', 'Pouet4', 'Pouet4@test.com', 'client', '06/02/2021', false, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1),
		--(default, 'test5', 'Pouet5', 'Pouet5', 'Pouet5@test.com', 'client', '06/02/2021', false, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1),
		--(default, 'test6', 'Pouet6', 'Pouet6', 'Pouet6@test.com', 'client', '06/02/2021', false, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1),
		--(default, 'test7', 'Pouet7', 'Pouet7', 'Pouet7@test.com', 'client', '06/02/2021', false, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1),
		--(default, 'test8', 'Pouet8', 'Pouet8', 'Pouet8@test.com', 'client', '06/02/2021', false, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1),
		--(default, 'test9', 'Pouet9', 'Pouet9', 'Pouet9@test.com', 'client', '06/02/2021', false, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1),
		--(default, 'test10', 'Pouet10', 'Pouet10', 'Pouet10@test.com', 'client', '06/02/2021', false, '$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu', 1);
		
SELECT * FROM pae.users;	
		
		
-- Inserts for the demo --
INSERT INTO pae.addresses VALUES
		(default, 'sente des artistes', '1bis', null, 'Verviers', '4800', 'Belgique'),
		(default, 'sente des artistes', '18', null, 'Verviers', '4800', 'Belgique'),
		(default, 'Rue de l''Eglise', '11', 'B1', 'Stoumont', '4987', 'Belgique'),
		(default, 'Rue de Renkin', '7', null, 'Verviers', '4800', 'Belgique'),
		(default, 'Lammerskreuzstrasse', '6', null, 'Roetgen', '52159', 'Allemagne');

INSERT INTO pae.users VALUES
		(default, 'bert', 'Satcho', 'Albert', 'bert.satcho@gmail.be', 'admin', '20210322', true, '$2a$10$eq5fAnhEKa9oSjBAu1B38.hd6uJv50n4EsXWqc3d2pdWSd0LdDOES', 1),--Jaune;10.
		(default, 'lau', 'Satcho', 'Laurent', 'laurent.satcho@gmail.be', 'admin', '20210322', true, '$2a$10$57whA09ftjlLqfKN9T4ei.Mzq/FYvNFxzlDt07DpTK2tNRxKsSF0a', 2),--Mauve;7?
		(default, 'Caro', 'Line', 'Caroline', 'caro.line@hotmail.com', 'antiquaire', '20210323', true, '$2a$10$3vxTLk8nb5zADB2gJzov9.TnM42ythdFfxdzOyS5nIu8oyH94oduS', 3),--mdpusr.2
		(default, 'achil', 'Ile', 'Achille', 'ach.ile@gmail.com', 'client', '20210323', true, '$2a$10$3vxTLk8nb5zADB2gJzov9.TnM42ythdFfxdzOyS5nIu8oyH94oduS', 4),--mdpusr.2
		(default, 'bazz', 'Ile', 'Basile', 'bazz.ile@gmail.be', 'client', '20210323', true, '$2a$10$3vxTLk8nb5zADB2gJzov9.TnM42ythdFfxdzOyS5nIu8oyH94oduS', 5); --mdpusr.2

INSERT INTO pae.requests_for_visits VALUES
		(default, 'lundi de 18h à 22h', 'accepté', null, '2021-03-29 20:00:00', 4, 4),
		(default, 'lundi de 18h à 22h', 'annulé', 'Meuble trop récent', null, 4, 4),
		(default, 'tous les jours de 15h à 18h', 'acceptée', null, '2021-03-29 15:00:00', 5, 5);
	
INSERT INTO pae.types_of_furnitures VALUES
		(default, 'Bahut'),
		(default, 'Bureau'),
		(default, 'Table'),
		(default, 'Secrétaire');
		
INSERT INTO pae.furnitures VALUES
		(default, 'acheté', 'Bahut profond d''une largeur de 112cm et d''une hauteur de 147cm.', 200.00, '2021-03-30', null, null, null, 1, 1, null, null),
		(default, 'acheté', 'Large bureau 1m87 cm, 2 colonnes de tiroirs', 150.00, '2021-03-30', null, null, null, 2, 1, null, null),
		(default, 'refusé', 'Table jardin en bois brut', null, null, null, null, null, 3, 2, null, null),
		(default, 'acheté', 'Table en chêne, pieds en fer forgé', 140.00, '2021-03-29', null, null, null, 3, 3, null, null),
		(default, 'acheté', 'Secrétaire en acajou, marqueterie', 90.00, '2021-03-29', null, null, null, 4, 3, null, null),
		-- test perso
		(default, 'en vente', 'Meuble test', 90.00, '2021-03-29', null, null, 500, 4, 3, null, null);
		
INSERT INTO pae.photos VALUES
		(default, 'Bahut_2.png', false, true, 1),
		(default, 'Bureau_1.png', false, true, 2),
		(default, 'table-jardin-recente.jpg', false, true, 3),
		(default, 'Table.jpg', false, true, 4),
		(default, 'Secretaire.png', false, true, 5);

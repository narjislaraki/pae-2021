-- Date dernière modif : 26/02/2021 --
DROP SCHEMA IF EXISTS pae CASCADE;
CREATE SCHEMA pae;

CREATE TYPE pae.roles AS ENUM('admin','antiquaire','client');

CREATE TABLE pae.adresses(
	id_adresse SERIAL PRIMARY KEY,
	rue VARCHAR(50) NOT NULL,
	numero VARCHAR(7) NOT NULL,
	boite INTEGER,
	ville VARCHAR(15) NOT NULL,
	code_postal VARCHAR(10) NOT NULL,
	pays VARCHAR(15) NOT NULL
);

CREATE TABLE pae.utilisateurs(
	id_personne SERIAL PRIMARY KEY,
	pseudo VARCHAR(30) NOT NULL,
	nom VARCHAR(50) NOT NULL,
	prenom VARCHAR(30) NOT NULL,
	email VARCHAR(60) NOT NULL,
	role pae.roles NOT NULL,
	date_inscription TIMESTAMP NOT NULL,
	est_valide BOOLEAN NOT NULL,
	mot_de_passe CHARACTER(60) NOT NULL,
	adresse INTEGER REFERENCES pae.adresses(id_adresse) NOT NULL
);

INSERT INTO pae.adresses VALUES(default, 'rue des sentiers', '7', 1, 'Bruxelles', '1300', 'Belgique');
--le mdp des 2 utilisateurs est 1234--
INSERT INTO pae.utilisateurs VALUES
		(default, 'test', 'Heuzer','Nina', 'test@test.com', 'admin', '05/01/2021', true, '$2y$16$KTslMCwnE7lZNJ2QgB9ScO50mn2q8PNyPZBUX9DLPpUqx6Z06xH/y', 1),
		(default, 'test2', 'Laraki', 'Narjis', 'test2@test.com', 'client', '06/02/2021', true, '$2y$16$KTslMCwnE7lZNJ2QgB9ScO50mn2q8PNyPZBUX9DLPpUqx6Z06xH/y', 1),
		(default, 'test3', 'de Theux', 'Boris', 'test3@test.com', 'client', '07/02/2021', false, '$2y$16$KTslMCwnE7lZNJ2QgB9ScO50mn2q8PNyPZBUX9DLPpUqx6Z06xH/y', 1);
		
SELECT * FROM pae.utilisateurs;	
		
		
		

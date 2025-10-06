DROP TABLE personas;

CREATE TABLE personas (
  id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  apellidos VARCHAR(50) NOT NULL,
  cumpleanos DATE NOT NULL
);

INSERT INTO personas (nombre, apellidos, cumpleanos) VALUES ('Miguel', 'De La Fuente', '2000-04-14');
INSERT INTO personas (nombre, apellidos, cumpleanos) VALUES ('Ana', 'Perez', '1999-03-25');
INSERT INTO personas (nombre, apellidos, cumpleanos) VALUES ('Angela', 'Lopez', '2003-09-01');
-- creearea bazei de date
IF NOT EXISTS(SELECT * FROM sys.databases WHERE NAME = 'CentruDeTransfuzieSanguina')
BEGIN
	CREATE DATABASE CentruDeTransfuzieSanguina;
END
USE CentruDeTransfuzieSanguina;

-- creearea tabelelor
IF NOT EXISTS(SELECT * FROM sys.tables WHERE NAME = 'TipSange')
BEGIN
	CREATE TABLE TipSange(
		idTipSange INT PRIMARY KEY IDENTITY(1,1), -- definirea de constrangeri
		grupa VARCHAR(2),
		rh VARCHAR(1),
	)
END

IF NOT EXISTS(SELECT * FROM sys.tables WHERE NAME = 'Persoana')
BEGIN
	CREATE TABLE Persoana(
		idPersoana INT PRIMARY KEY IDENTITY(1,1),
		nume VARCHAR(20),
		prenume VARCHAR(20),
		sex VARCHAR(1),
		dataNasterii DATE,
		idTipSange INT FOREIGN KEY REFERENCES TipSange(idTipSange), -- definirea de constrangeri
		greutate INT,
		CONSTRAINT ck_Persoana CHECK (sex IN ('M', 'F')) -- definirea de constrangeri
	)
END

IF NOT EXISTS(SELECT * FROM sys.tables WHERE NAME = 'Donator')
BEGIN
	CREATE TABLE Donator(
		idDonator INT PRIMARY KEY IDENTITY(1,1),
		idPersoana INT FOREIGN KEY REFERENCES Persoana(idPersoana) ON DELETE CASCADE ON UPDATE CASCADE,
		dataUltimeiDonari DATE,
	)
END

IF NOT EXISTS(SELECT * FROM sys.tables WHERE NAME = 'Afectiune')
BEGIN
	CREATE TABLE Afectiune(
		idAfectiune INT PRIMARY KEY IDENTITY(1,1),
		nume VARCHAR(20) UNIQUE, -- definirea de constrangeri
		cantitateSange INT NOT NULL, -- unitate: ml, definirea de constrangeri
	)
END

IF NOT EXISTS(SELECT * FROM sys.tables WHERE NAME = 'Spital')
BEGIN
	CREATE TABLE Spital(
		idSpital INT PRIMARY KEY IDENTITY(1,1),
		nume VARCHAR(50),
		oras VARCHAR(20),
		adresa VARCHAR(50),
	)
END

IF NOT EXISTS(SELECT * FROM sys.tables WHERE NAME = 'Test')
BEGIN
	CREATE TABLE Test(
		idDonator INT FOREIGN KEY REFERENCES Donator(idDonator) ON DELETE CASCADE ON UPDATE CASCADE,
		presiuneaSangelui INT,
		alcolemie INT,
		nivelHemoglobina INT,
		validitate BIT DEFAULT 0,  
	)
END

IF NOT EXISTS(SELECT * FROM sys.tables WHERE NAME = 'Pacient')
BEGIN
	CREATE TABLE Pacient
	(
		idPacient INT PRIMARY KEY IDENTITY(1,1),
		idPersoana INT FOREIGN KEY REFERENCES Persoana(idPersoana) ON DELETE CASCADE ON UPDATE CASCADE,
		idAfectiune INT FOREIGN KEY REFERENCES Afectiune(idAfectiune),
		idSpital INT FOREIGN KEY REFERENCES Spital(idSpital) ON DELETE CASCADE ON UPDATE CASCADE,
	)
END

IF NOT EXISTS(SELECT * FROM sys.tables WHERE NAME = 'BancaSange')
BEGIN
	CREATE TABLE BancaSange(
		idBancaSange INT PRIMARY KEY IDENTITY(1,1),
		idSpital INT FOREIGN KEY REFERENCES Spital(idSpital) ON DELETE CASCADE ON UPDATE CASCADE,
		denumire VARCHAR(20),
		adresa VARCHAR(50),
	)
END

IF NOT EXISTS(SELECT * FROM sys.tables WHERE NAME = 'DonatorBancaSange')
BEGIN
	CREATE TABLE DonatorBancaSange(
		idDonator INT FOREIGN KEY REFERENCES Donator(idDonator) ON DELETE CASCADE ON UPDATE CASCADE,
		idBancaSange INT FOREIGN KEY REFERENCES BancaSange(idBancaSange) ON DELETE CASCADE ON UPDATE CASCADE,
		PRIMARY KEY(idDonator, idBancaSange),
	)
END

IF NOT EXISTS(SELECT * FROM sys.tables WHERE NAME = 'RecipientSange')
BEGIN
	CREATE TABLE RecipientSange(
		idRecipientSange INT PRIMARY KEY IDENTITY(1,1),
		idPacient INT FOREIGN KEY REFERENCES Pacient(idPacient),
		idBancaSange INT FOREIGN KEY REFERENCES BancaSange(idBancaSange) ON DELETE CASCADE ON UPDATE CASCADE,
		idTipSange INT FOREIGN KEY REFERENCES TipSange(idTipSange),
		dataExpirare DATE NOT NULL,
	)
END

IF NOT EXISTS(SELECT * FROM sys.tables WHERE NAME = 'CerereSange')
BEGIN
	CREATE TABLE CerereSange(
		idCerereSange INT PRIMARY KEY IDENTITY(1,1),
		idPacient INT FOREIGN KEY REFERENCES Pacient(idPacient) ON DELETE CASCADE ON UPDATE CASCADE,
		idBancaSange INT FOREIGN KEY REFERENCES BancaSange(idBancaSange),
		idTipSange INT FOREIGN KEY REFERENCES TipSange(idTipSange),
		cantitate INT NOT NULL,
	)
END

-- popularea cu minim 5 inregistrari a fiecarui tabel creat
INSERT INTO TipSange(grupa, rh)
VALUES('A', '+'),
('B', '+'),
('AB', '+'),
('O', '+'),
('A', '-')

INSERT INTO Persoana(nume, prenume, sex, dataNasterii, idTipSange, greutate)
VALUES('Popescu', 'Ion', 'M', '1990-01-01', 1, 70),
('Ionescu', 'Maria', 'F', '1996-03-02', 2, 60),
('Vasilescu', 'Andrei', 'M', '1995-05-03', 3, 80),
('Murarasu', 'Vlad', 'M', '2002-09-17', 4, 50),
('Mihai', 'Andreea', 'F', '1998-11-11', 5, 65),
('Rossi', 'Luca', 'M', '1995-01-01', 1, 73),
('Bianchi', 'Giulia', 'F', '2001-03-02', 2, 60),
('Verdi', 'Giovanni', 'M', '1998-05-03', 3, 83),
('Gialli', 'Karina', 'F', '2002-06-13', 4, 56),
('Neri', 'Manuela', 'F', '2003-01-01', 5, 50)

INSERT INTO Donator(idPersoana, dataUltimeiDonari)
VALUES(6, '2019-01-01'),
(7, '2019-03-02'),
(8, '2019-05-03'),
(9, '2022-09-17'),
(10, '2019-11-11')

INSERT INTO Afectiune(nume, cantitateSange)
VALUES('anemie', 500),
('hemofilie', 1000),
('leucemie', 1500),
('sifilis', 2000),
('tuberculoza', 2500)

INSERT INTO Spital(nume, oras, adresa)
VALUES('Spitalul de Urgenta', 'Bucuresti', 'Str. Splaiul Independentei, nr. 169'),
('Spitalul Judetean', 'Brasov', 'Str. Mihai Viteazul, nr. 27'),
('Spitalul Militar', 'Bucuresti', 'Str. Ion Mihalache, nr. 1'),
('Spitalul de Boli Infectioase', 'Bucuresti', 'Str. Ion Creanga, nr. 60'),
('Spitalul de Copii', 'Bucuresti', 'Str. Ion Creanga, nr. 61')

INSERT INTO Test(idDonator, presiuneaSangelui, alcolemie, nivelHemoglobina, validitate)
VALUES(1, 120, 0, 120, 1),
(2, 130, 0, 130, 1),
(3, 140, 0, 140, 1),
(4, 150, 0, 150, 1),
(5, 160, 0, 160, 1)

INSERT INTO Pacient(idPersoana, idAfectiune, idSpital)
VALUES(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 4, 4),
(5, 5, 5)

INSERT INTO BancaSange(idSpital, denumire, adresa)
VALUES(1, 'Banca Sange 1', 'Str. Splaiul Independentei, nr. 169'),
(2, 'Banca Sange 2', 'Str. Mihai Viteazul, nr. 27'),
(3, 'Banca Sange 3', 'Str. Ion Mihalache, nr. 1'),
(4, 'Banca Sange 4', 'Str. Ion Creanga, nr. 60'),
(5, 'Banca Sange 5', 'Str. Ion Creanga, nr. 61')

INSERT INTO DonatorBancaSange(idDonator, idBancaSange)
VALUES(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5)

INSERT INTO RecipientSange(idPacient, idBancaSange, idTipSange, dataExpirare)
VALUES(1, 1, 1, '2023-01-03'),
(2, 2, 2, '2023-01-10'),
(3, 3, 3, '2023-01-15'),
(4, 4, 4, '2023-01-30'),
(5, 5, 5, '2023-02-03')

INSERT INTO CerereSange(idPacient, idBancaSange, idTipSange, cantitate)
VALUES(1, 1, 1, 500),
(2, 2, 2, 1000),
(3, 3, 3, 1500),
(4, 4, 4, 2000),
(5, 5, 5, 2500)

-- view-uri

CREATE VIEW view_DonatoriRhPozitiv AS
SELECT *
FROM Donator
WHERE idPersoana IN (SELECT idPersoana
					 FROM Persoana
					 WHERE idTipSange IN (SELECT idTipSange
										  FROM TipSange
										  WHERE rh = '+'))

CREATE VIEW view_DonatoriValizi AS
SELECT *
FROM Donator
WHERE idDonator IN (SELECT idDonator
					FROM Test
					WHERE validitate = 1)

CREATE VIEW view_RecipientiSangeValizi AS
SELECT *
FROM RecipientSange
WHERE dataExpirare > GETDATE()

-- functii

-- functie care verifica daca un donator este apt pentru donare si returneaza 1 daca da si 0 daca nu
CREATE FUNCTION fn_DonatorApt(@idDonator INT) RETURNS BIT
AS
BEGIN
	DECLARE @validitate BIT
	DECLARE @presiuneaSangelui INT
	DECLARE @alcolemie INT
	DECLARE @dataUltimeiDonari DATE
	DECLARE @greutate INT
	DECLARE @nivelHemoglobina INT
	DECLARE @varsta INT

	SELECT @presiuneaSangelui = presiuneaSangelui
	FROM Test
	WHERE idDonator = @idDonator

	SELECT @alcolemie = alcolemie
	FROM Test
	WHERE idDonator = @idDonator

	SELECT @dataUltimeiDonari = dataUltimeiDonari
	FROM Donator
	WHERE idDonator = @idDonator

	SELECT @greutate = greutate
	FROM Persoana
	WHERE idPersoana = (SELECT idPersoana
						FROM Donator
						WHERE idDonator = @idDonator)
	
	SELECT @nivelHemoglobina = nivelHemoglobina
	FROM Test
	WHERE idDonator = @idDonator

	SELECT @varsta = DATEDIFF(YEAR, dataNasterii, GETDATE())
	FROM Persoana
	WHERE idPersoana = (SELECT idPersoana
						FROM Donator
						WHERE idDonator = @idDonator)

	IF (@presiuneaSangelui < 180)
	AND (@alcolemie = 0) 
	AND (@dataUltimeiDonari < DATEADD(MONTH, -3, GETDATE()) OR @dataUltimeiDonari IS NULL)
	AND (@greutate BETWEEN 50 AND 150)
	AND (@nivelHemoglobina > 120)
	AND (@varsta BETWEEN 18 AND 65)
		SET @validitate = 1
	ELSE
		SET @validitate = 0
	RETURN @validitate
END

UPDATE Test SET validitate = dbo.fn_DonatorApt(idDonator)

-- functie care returneaza numarul de donatori apti
CREATE FUNCTION fn_NrDonatoriApti() RETURNS INT
AS
BEGIN
	DECLARE @nrDonatoriApti INT
	SELECT @nrDonatoriApti = COUNT(idDonator)
	FROM Donator
	WHERE idDonator IN (SELECT idDonator
						FROM Test
						WHERE validitate = 1)
	RETURN @nrDonatoriApti
END

-- exemplu de utilizare a functiei

SELECT dbo.fn_NrDonatoriApti()

-- triggere

CREATE TRIGGER tr_CerereSange
ON Pacient
AFTER INSERT
AS
BEGIN
	DECLARE @idPacient INT
	DECLARE @idBancaSange INT
	DECLARE @idTipSange INT
	DECLARE @cantitateSange INT

	SELECT @idPacient = idPacient
	FROM inserted

	SELECT @idBancaSange = idBancaSange
	FROM BancaSange
	WHERE idSpital = (SELECT idSpital
					  FROM Pacient
					  WHERE idPacient = @idPacient)

	SELECT @idTipSange = idTipSange
	FROM Persoana
	WHERE idPersoana = (SELECT idPersoana
						FROM Pacient
						WHERE idPacient = @idPacient)

	SELECT @cantitateSange = cantitateSange
	FROM Afectiune
	WHERE idAfectiune = (SELECT idAfectiune
						 FROM Pacient
						 WHERE idPacient = @idPacient)

	INSERT INTO CerereSange(idPacient, idBancaSange, idTipSange, cantitate)
	VALUES(@idPacient, @idBancaSange, @idTipSange, @cantitateSange)
END


-- rapoarte

SELECT *
FROM Donator
JOIN Persoana ON Donator.idPersoana = Persoana.idPersoana

SELECT *
FROM Pacient
JOIN Persoana ON Pacient.idPersoana = Persoana.idPersoana

SELECT *
FROM RecipientSange
RIGHT JOIN Pacient ON RecipientSange.idPacient = Pacient.idPacient
RIGHT JOIN Persoana ON Pacient.idPersoana = Persoana.idPersoana

SELECT p.nume, p.prenume, COUNT(*) AS NrRecipientPerPacient
FROM RecipientSange rs
INNER JOIN Pacient pac ON pac.idPacient = rs.idPacient
INNER JOIN Persoana p ON pac.idPersoana = p.idPersoana
GROUP BY p.nume, p.prenume
HAVING COUNT(*) > 0
ORDER BY NrRecipientPerPacient DESC

-- un raport cu pacientii care au grupa de sange A+ si concateneaza grupa cu rh ul pacientului
SELECT p.nume, p.prenume, CONCAT(t.grupa, t.rh) AS GrupaRh
FROM Pacient pac
INNER JOIN Persoana p ON pac.idPersoana = p.idPersoana
INNER JOIN TipSange t ON p.idTipSange = t.idTipSange
WHERE t.grupa = 'A' AND t.rh = '+'

-- indecsi
CREATE INDEX idx_Persoana ON Persoana(idPersoana, nume, prenume, dataNasterii, idTipSange)
CREATE INDEX idx_Donator ON Donator(idDonator, idPersoana, dataUltimeiDonari)
CREATE INDEX idx_Pacient ON Pacient(idPacient, idPersoana, idSpital, idAfectiune)

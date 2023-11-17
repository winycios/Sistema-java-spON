-- Arquivo de apoio, caso você queira criar tabelas como as aqui criadas para a API funcionar.
CREATE DATABASE SuperVisiON_Individual;

USE SuperVisiON_Individual;

CREATE TABLE tbOcorrencia(

idOcorrencia INT PRIMARY KEY AUTO_INCREMENT,
descricao VARCHAR(100),
apelidoComputador Varchar(30),
hora datetime
);

CREATE TABLE tbMonitoramento(

dataHora DATETIME PRIMARY KEY,
apelidoComputador VARCHAR(30),
cpuTemp INT,
cpuFreq INT,
gpuTemp INT,
gpuFreq INT,
redeLatencia INT,
disco INT,
ram INT
);

create user 'superOn'@'localhost' identified by '123';
grant all privileges on SuperVisiON_Individual.* to 'superOn'@'localhost';
flush privileges;
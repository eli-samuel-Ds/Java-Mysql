create database eventos;
use eventos;

create table eventos (

	id int auto_increment primary key,
    nombre varchar(50) not null,
    matricula int not null,
    descripcionEvento varchar(200)

);
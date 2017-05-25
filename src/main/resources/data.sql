insert into permission values (1, 'ADMINPAGE');
insert into permission values (2, 'EMPLOYEEPAGE');
insert into permission values (3, 'CLIENTPAGE');
insert into permission values (4, 'Cetvrta');

insert into role values (1, 'Prva rola');
insert into role values (2, 'Druga rola');
insert into role values (3, 'Treca rola');

insert into role_permissions values (1, 1);
insert into role_permissions values (1, 3);
insert into role_permissions values (3, 3);
insert into role_permissions values (2, 2);

insert into bank values (1, '123', '12345678', 'Prva banka', '12345678');
insert into bank values (2, '234', '21345678',  'Druga banka', '21345678');
insert into bank values (3, '345', '14345678' , 'Treca banka', '14345678');
insert into bank values (4, '456', '16345678' , 'Cetvrta banka', '16345678');
insert into bank values (5, '567', '12342678', 'Peta banka', '12342678');

insert into drzava values (1, 'Srbija', 'SRB');
insert into drzava values (2,'Makedonija', 'MKD' );
insert into drzava values (3, 'Crna Gora', 'MTN');
insert into drzava values (4, 'Bosna i Hercegovina', 'BIH');
insert into drzava values (5, 'Hrvatska', 'CRO');


insert into naseljeno_mesto values (1, 'Novi Sad', 'NS', '21000', 1);
insert into naseljeno_mesto values (2, 'Beograd', 'BG', '11000', 1);
insert into naseljeno_mesto values (3, 'Kragujevac', 'KG', '14300', 1);
insert into naseljeno_mesto values (4, 'Skoplje', 'SK', '98000', 2);
insert into naseljeno_mesto values (5, 'Prnjavor', 'PR', '43000', 4);

insert into subject values ('1');
insert into subject values ('2');
insert into subject values ('3');
insert into subject values ('4');
insert into subject values ('5');
insert into subject values ('6');
insert into subject values ('7');

insert into subject values ('8');
insert into subject values ('9');
insert into subject values ('10');
insert into subject values ('11');


insert into employee values ('Prvizaposleni', 'Prvizaposleni', '1', '1');
insert into employee values ('Drugizaposleni', 'Drugizaposleni', '2', '1');
insert into employee values ('Trecizaposleni', 'Trecizaposleni', '3', '1');
insert into employee values ('Cetvrtizaposleni', 'Cetvrtizaposleni', '4', '1');
insert into employee values ('Petizaposleni', 'Petizaposleni', '5', '1');
insert into employee values ('Sestizaposleni', 'Sestizaposleni', '6', '1');
insert into employee values ('Sedmizaposleni', 'Sedmizaposleni', '7', '1');

insert into employee values ('Osmizaposleni', 'Osmizaposleni', '8', '1');
insert into employee values ('Devetizaposleni', 'Devetizaposleni', '9', '1');
insert into employee values ('Desetizaposleni', 'Desetizaposleni', '10', '1');
insert into employee values ('Jedanaestizaposleni', 'Jedanaestizaposleni', '11', '1');

insert into user values ('1', TRUE, '2013-08-30 19:05:00', 'neki1@kkk.kkk', 'username1', '', 'username1', '1', '1', '1');
insert into user values ('2', TRUE, '2013-08-30 19:06:00', 'neki2@kkk.kkk', 'username2', '', 'username2', '1', '1', '2');
insert into user values ('3', TRUE, '2013-08-30 19:07:00', 'neki3@kkk.kkk', 'username3', '', 'username3', '1', '2', '3');
insert into user values ('4', TRUE, '2013-08-30 19:08:00', 'neki4@kkk.kkk', 'username4', '', 'username4', '1', '2', '4');
insert into user values ('5', TRUE, '2013-08-30 19:09:00', 'neki5@kkk.kkk', 'username5', '', 'username5', '1', '2', '5');
insert into user values ('6', TRUE, '2013-08-30 19:10:00', 'neki6@kkk.kkk', 'username6', '', 'username6', '1', '3', '6');
insert into user values ('7', TRUE, '2013-08-30 19:11:00', 'neki7@kkk.kkk', 'username7', '', 'username7', '1', '3', '7');

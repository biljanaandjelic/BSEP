insert into permission values ('1', 'INSERT_PERMISSION');
insert into permission values ('2', 'UPDATE_PERMISSION');
insert into permission values ('3', 'DELETE_PERMISSION');
insert into permission values ('4', 'FIND_ALL_PERMISSION');

insert into permission values ('5', 'INSERT_ROLE');
insert into permission values ('6', 'UPDATE_ROLE');
insert into permission values ('7', 'DELETE_ROLE');
insert into permission values ('8', 'FIND_ALL_ROLE');

insert into permission values ('9', 'INSERT_USER');
insert into permission values ('10', 'UPDATE_USER');
insert into permission values ('11', 'DELETE_USER');
insert into permission values ('12', 'FIND_ALL_USER');
insert into permission values ('13', 'PASSWORD_CHANGE_USER');

insert into permission values ('14', 'INSERT_STATE');
insert into permission values ('15', 'UPDATE_STATE');
insert into permission values ('16', 'DELETE_STATE');
insert into permission values ('17', 'FIND_ALL_STATE');
insert into permission values ('18', 'FILTER_STATE');

insert into permission values ('19', 'INSERT_CITY');
insert into permission values ('20', 'UPDATE_CITY');
insert into permission values ('21', 'DELETE_CITY');
insert into permission values ('22', 'FIND_ALL_CITY');
insert into permission values ('23', 'FILTER_CITY');
insert into permission values ('24', 'FIND_CITY_BY_STATE');

insert into permission values ('25', 'INSERT_ACTIVITY');
insert into permission values ('26', 'UPDATE_ACTIVITY');
insert into permission values ('27', 'DELETE_ACTIVITY');
insert into permission values ('28', 'FIND_ALL_ACTIVITY');
insert into permission values ('29', 'FILTER_ACTIVITY');

insert into permission values ('30', 'INSERT_VALUTE');
insert into permission values ('31', 'UPDATE_VALUTE');
insert into permission values ('32', 'DELETE_VALUTE');
insert into permission values ('33', 'FIND_ALL_VALUTE');
insert into permission values ('34', 'FILTER_VALUTE');


insert into permission values ('35', 'INSERT_ACCOUNT');
insert into permission values ('36', 'DEACTIVATE_ACCOUNT');
insert into permission values ('37', 'FIND_ALL_ACCOUNT');
insert into permission values ('38', 'FIND_ONE_ACCOUNT');
insert into permission values ('39', 'FIND_ACCOUNT_BY_OWNER');
insert into permission values ('40', 'FILTER_ACCOUNT');

insert into permission values ('41', 'FIND_ALL_DEACTIVATION');
insert into permission values ('42', 'FILTER_DEACTIVATION');
insert into permission values ('43', 'FILTER_DEACTIVATION_BY_ACCOUNT');

insert into permission values ('44', 'FIND_ALL_EMPLOYEE');

insert into permission values ('45', 'INSERT_PHYSICAL');
insert into permission values ('46', 'UPDATE_PHYSICAL');
insert into permission values ('47', 'DELETE_PHYSICAL');
insert into permission values ('48', 'FIND_ALL_PHYSICAL');
insert into permission values ('49', 'FIND_ALL_PHYSICAL_BY_CITY');
insert into permission values ('50', 'FILTER_PHYSICAL');

insert into permission values ('51', 'INSERT_LEGAL');
insert into permission values ('52', 'UPDATE_LEGAL');
insert into permission values ('53', 'DELETE_LEGAL');
insert into permission values ('54', 'FIND_ALL_LEGAL');
insert into permission values ('55', 'FIND_ALL_LEGAL_BY_CITY');
insert into permission values ('56', 'FILTER_LEGAL');

insert into permission values ('57', 'LOGOFF');





insert into role values (1, 'ADMINISTRATOR_BANK');
insert into role values (2, 'MANAGER');
insert into role values (3, 'COUNTER_OFFICER');
insert into role values (4, 'LEGAL');

insert into role_permissions values (1, 1);
insert into role_permissions values (1, 2);
insert into role_permissions values (1, 3);
insert into role_permissions values (1, 4);
insert into role_permissions values (1, 5);
insert into role_permissions values (1, 6);
insert into role_permissions values (1, 7);
insert into role_permissions values (1, 8);
insert into role_permissions values (1, 9);
insert into role_permissions values (1, 10);
insert into role_permissions values (1, 11);
insert into role_permissions values (1, 12);
insert into role_permissions values (1, 13);
insert into role_permissions values (1, 14);
insert into role_permissions values (1, 15);
insert into role_permissions values (1, 16);
insert into role_permissions values (1, 17);
insert into role_permissions values (1, 18);
insert into role_permissions values (1, 19);
insert into role_permissions values (1, 20);
insert into role_permissions values (1, 21);
insert into role_permissions values (1, 22);
insert into role_permissions values (1, 23);
insert into role_permissions values (1, 24);
insert into role_permissions values (1, 25);
insert into role_permissions values (1, 26);
insert into role_permissions values (1, 27);
insert into role_permissions values (1, 28);
insert into role_permissions values (1, 29);
insert into role_permissions values (1, 30);
insert into role_permissions values (1, 31);
insert into role_permissions values (1, 32);
insert into role_permissions values (1, 33);
insert into role_permissions values (1, 34);
insert into role_permissions values (1, 44);
insert into role_permissions values (1, 57);

insert into role_permissions values (2, 35);
insert into role_permissions values (2, 36);
insert into role_permissions values (2, 37);
insert into role_permissions values (2, 38);
insert into role_permissions values (2, 39);
insert into role_permissions values (2, 40);
insert into role_permissions values (2, 41);
insert into role_permissions values (2, 42);
insert into role_permissions values (2, 43);
insert into role_permissions values (2, 51);
insert into role_permissions values (2, 52);
insert into role_permissions values (2, 53);
insert into role_permissions values (2, 54);
insert into role_permissions values (2, 55);
insert into role_permissions values (2, 56);
insert into role_permissions values (2, 57);

insert into role_permissions values (3, 35);
insert into role_permissions values (3, 36);
insert into role_permissions values (3, 37);
insert into role_permissions values (3, 38);
insert into role_permissions values (3, 39);
insert into role_permissions values (3, 40);
insert into role_permissions values (3, 41);
insert into role_permissions values (3, 42);
insert into role_permissions values (3, 43);
insert into role_permissions values (3, 45);
insert into role_permissions values (3, 46);
insert into role_permissions values (3, 47);
insert into role_permissions values (3, 48);
insert into role_permissions values (3, 49);
insert into role_permissions values (3, 50);
insert into role_permissions values (3, 57);



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

insert into subject values ('12');
insert into subject values ('13');
insert into subject values ('14');
insert into subject values ('15');
insert into subject values ('16');



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
insert into user values ('8', TRUE, '2013-08-30 19:11:00', 'neki8@kkk.kkk', 'username8', '', 'username8', '2', '2', '8');
insert into user values ('9', TRUE, '2013-09-30 19:11:00', 'neki9@kkk.kkk', 'username9', '' ,'username9', '3', '3', '9');

insert into klijent values ('adresa', 'mail1@kkk.kkk', TRUE, 'Ime1', '4578445121255', 'Prezime1', '123456788', '12', '1', '1');
insert into klijent values ('adresa', 'mail2@kkk.kkk', TRUE, 'Ime2', '4578545121255', 'Prezime2', '123454788', '13', '1', '1');
insert into klijent values ('adresa', 'mail3@kkk.kkk', TRUE, 'Ime3', '4518545121255', 'Prezime3', '123452788', '14', '1', '1');
insert into klijent values ('Adresa1', 'ime3@prezime.com', TRUE, 'Ime', '0000000000000', 'Prezime', '123452788', '15', '2', '2');
insert into klijent values ('Adresa1', 'ime4@prezime.com', TRUE, 'Ime', '0000000000000', 'Prezime', '123452788', '16', '3', '3');

insert into racun values ('1', '123-1726533787929-93', '2017-04-30 19:05:00', '2013-02-15 19:05:00', '30000', FALSE, '1', '12');
insert into racun values ('2', '123-9338670954849-69', NULL, '2013-01-15 19:05:00', '20000', TRUE, '1', '12');
insert into racun values ('3', '123-8880775775674-13', null, '2013-05-10 19:05:00', '12000', TRUE, '1', '13');
insert into racun values ('4', '123-7184821064803-82', null, '2013-05-07 19:05:00', '12000', TRUE, '1', '13');
insert into racun values ('5', '234-6355718718680-9', null, '2013-05-16 19:05:00', '12000', TRUE, '2', '15');
insert into racun values ('6', '345-3990849817799-56', null,'2014-05-16 19:05:00','20000', TRUE,'3','16');
insert into zatvaranje_racuna values ('1', '2017-04-30 19:05:00', '123-7184821064803-82', '1');


insert into message values ('1', 'MT102');
insert into message values ('2', 'MT103');
insert into message values ('3', 'MT202');
insert into message values ('4', 'MT900');

insert into dnevno_stanje_racuna values ('1', '2017-05-27 00:00:00', '1000', '2000', '1000', '0', '1');
insert into dnevno_stanje_racuna values ('2', '2017-05-28 00:00:00', '1500', '1000', '0', '500', '1');
insert into dnevno_stanje_racuna values ('3', '2017-05-29 00:00:00', '500', '1000', '500', '0', '2');
insert into dnevno_stanje_racuna values ('4', '2017-05-30 00:00:00', '2000', '500', '0', '1500', '2');

insert into analitika_izvoda values ('1', '2017-05-27 00:00:00', '2017-05-27 00:00:00', '2017-05-27 00:00:00', '', TRUE, '300', '97', '97', '123456789012345', '123456789012345', '', '123-8880775775674-13', '123-1726533787929-93', 'DIN', 'T', '', '1');
insert into analitika_izvoda values ('2', '2017-05-27 00:00:00', '2017-05-27 00:00:00', '2017-05-27 00:00:00', '', FALSE, '700', '97', '97', '123456789012345', '123456789012345', '', '123-8880775775674-13', '123-1726533787929-93', 'DIN', 'T', '', '1');

insert into analitika_izvoda values ('3', '2017-05-28 00:00:00', '2017-05-28 00:00:00', '2017-05-28 00:00:00', '', FALSE, '800', '97', '97', '123456789012345', '123456789012345', '', '123-1726533787929-93', '123-8880775775674-13', 'DIN', 'K', '', '2');
insert into analitika_izvoda values ('4', '2017-05-28 00:00:00', '2017-05-28 00:00:00', '2017-05-28 00:00:00', '', FALSE, '300', '97', '97', '123456789012345', '123456789012345', '', '123-8880775775674-13', '123-1726533787929-93', 'DIN', 'T', '', '2');

insert into analitika_izvoda values ('5', '2017-05-29 00:00:00', '2017-05-29 00:00:00', '2017-05-29 00:00:00', '', FALSE, '250', '97', '97', '123456789012345', '123456789012345', '', '123-8880775775674-13', '123-9338670954849-69', 'DIN', 'T', '', '3');
insert into analitika_izvoda values ('6', '2017-05-29 00:00:00', '2017-05-29 00:00:00', '2017-05-29 00:00:00', '', FALSE, '250', '97', '97', '123456789012345', '123456789012345', '', '123-8880775775674-13', '123-9338670954849-69', 'DIN', 'T', '', '3');

insert into analitika_izvoda values ('7', '2017-05-30 00:00:00', '2017-05-30 00:00:00', '2017-05-30 00:00:00', '', TRUE, '1000', '97', '97', '123456789012345', '123456789012345', '', '123-9338670954849-69', '123-1726533787929-93', 'DIN', 'K', '', '4');
insert into analitika_izvoda values ('8', '2017-05-30 00:00:00', '2017-05-30 00:00:00', '2017-05-30 00:00:00', '', FALSE, '500', '97', '97', '123456789012345', '123456789012345', '', '123-9338670954849-69', '123-1726533787929-93', 'DIN', 'K', '', '4');


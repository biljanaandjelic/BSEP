
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
insert into permission values ('30', 'FIND_ONE_ACTIVITY');

insert into permission values ('31', 'INSERT_VALUTE');
insert into permission values ('32', 'UPDATE_VALUTE');
insert into permission values ('33', 'DELETE_VALUTE');
insert into permission values ('34', 'FIND_ALL_VALUTE');
insert into permission values ('35', 'FILTER_VALUTE');
insert into permission values ('36', 'FIND_ONE_VALUTE');
insert into permission values ('37', 'FIND_VALUTE_BY_CODE');

insert into permission values ('38', 'FIND_ALL_EMPLOYEE');

insert into permission values ('39', 'INSERT_MESSAGE');
insert into permission values ('40', 'UPDATE_MESSAGE');
insert into permission values ('41', 'DELETE_MESSAGE');
insert into permission values ('42', 'FIND_ALL_MESSAGE');
insert into permission values ('43', 'FIND_MESSAGE_BY_CODE');
insert into permission values ('44', 'FIND_ONE_MESSAGE');

insert into permission values ('45', 'GENERATE_CERTIFICATE_REQUEST');
insert into permission values ('46', 'GET_CERTIFICATE_REQUESTS');
insert into permission values ('47', 'MAKE_CERTIFICATE');
insert into permission values ('48', 'FIND_CERTIFICATE_BY_ALIAS');
insert into permission values ('49', 'REVOKE_CERTIFICATE');
insert into permission values ('50', 'PROCESS_REVOKE_REQUEST');
insert into permission values ('51', 'GET_REVOKE_REQUEST');
insert into permission values ('52', 'SAVE_REVOKE_REQUEST');

insert into permission values ('53', 'OCSP_RESPONSE');

insert into permission values ('54', 'GENERATE_CERTIFICATE');


insert into permission values ('55', 'INSERT_ACCOUNT');
insert into permission values ('56', 'DEACTIVATE_ACCOUNT');
insert into permission values ('57', 'FIND_ALL_ACCOUNT');
insert into permission values ('58', 'FIND_ONE_ACCOUNT');
insert into permission values ('59', 'FIND_ACCOUNT_BY_OWNER');
insert into permission values ('60', 'FILTER_ACCOUNT');

insert into permission values ('61', 'FIND_ALL_DEACTIVATION');
insert into permission values ('62', 'FILTER_DEACTIVATION');
insert into permission values ('63', 'FILTER_DEACTIVATION_BY_ACCOUNT');


insert into permission values ('64', 'INSERT_PHYSICAL');
insert into permission values ('65', 'UPDATE_PHYSICAL');
insert into permission values ('66', 'DELETE_PHYSICAL');
insert into permission values ('67', 'FIND_ALL_PHYSICAL');
insert into permission values ('68', 'FIND_ALL_PHYSICAL_BY_CITY');
insert into permission values ('69', 'FILTER_PHYSICAL');

insert into permission values ('70', 'INSERT_LEGAL');
insert into permission values ('71', 'UPDATE_LEGAL');
insert into permission values ('72', 'DELETE_LEGAL');
insert into permission values ('73', 'FIND_ALL_LEGAL');
insert into permission values ('74', 'FIND_ALL_LEGAL_BY_CITY');
insert into permission values ('75', 'FILTER_LEGAL');

insert into permission values ('76', 'FIND_ALL_DAILY_STATE');
insert into permission values ('77', 'FILTER_DAILY_STATE');
insert into permission values ('78', 'FIND_DAILY_STATE_BY_ACCOUNT');

insert into permission values ('79', 'FIND_ALL_ANALYTICS');
insert into permission values ('80', 'FILTER_ANALYTICS');
insert into permission values ('81', 'FIND_ANALYTICS_BY_DAILY_STATE');

insert into permission values ('82', 'IMPORT_FROM_XML');

insert into permission values ('83', 'FIND_ALL_INTERBANK_TRANSFER');
insert into permission values ('84', 'FILTER_INTERBANK_TRANSFER');

insert into permission values ('85', 'TRANSACTION_ITEMS');

insert into permission values ('86', 'GENERATE_REPORT');
insert into permission values ('87', 'EXPORT_INTERBANK_TRANSFER');
insert into permission values ('88', 'EXPORT_ACCOUNT_STATEMENT');

insert into permission values ('89', 'LOGOFF');

insert into permission values ('90', 'FIND_ALL_BANK');

insert into permission values ('91', 'OPEN_KEYSTORE');
insert into permission values ('92', 'CLOSE_KEYSTORE');
insert into permission values ('93', 'IMPORT_CERTIFICATE');
insert into permission values ('94', 'DOWNLOAD_CERTIFICATE');

insert into role values (1, 'ADMINISTRATOR_BANK');
insert into role values (2, 'MANAGER');
insert into role values (3, 'COUNTER_OFFICER');
insert into role values (4, 'LEGAL');
insert into role values (5, 'ADMINISTRATOR_CENTRAL');

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

insert into role_permissions values (1, 38);

insert into role_permissions values (1, 45);
insert into role_permissions values (1, 46);
insert into role_permissions values (1, 47);
insert into role_permissions values (1, 48);
insert into role_permissions values (1, 49);
insert into role_permissions values (1, 50);
insert into role_permissions values (1, 51);
insert into role_permissions values (1, 52);
insert into role_permissions values (1, 53);

insert into role_permissions values (1, 90);

insert into role_permissions values (1, 73);
insert into role_permissions values (1, 89);
insert into role_permissions values (1, 91);
insert into role_permissions values (1, 92);
insert into role_permissions values (1, 93);

insert into role_permissions values (1, 94);

insert into role_permissions values (2, 13);

insert into role_permissions values (2, 14);
insert into role_permissions values (2, 15);
insert into role_permissions values (2, 16);
insert into role_permissions values (2, 17);
insert into role_permissions values (2, 18);

insert into role_permissions values (1, 19);
insert into role_permissions values (1, 20);
insert into role_permissions values (1, 21);
insert into role_permissions values (2, 22);
insert into role_permissions values (2, 23);
insert into role_permissions values (2, 24);

insert into role_permissions values (1, 25);
insert into role_permissions values (1, 26);
insert into role_permissions values (2, 27);
insert into role_permissions values (2, 28);
insert into role_permissions values (2, 29);
insert into role_permissions values (2, 30);

insert into role_permissions values (2, 31);
insert into role_permissions values (2, 32);
insert into role_permissions values (2, 33);
insert into role_permissions values (2, 34);
insert into role_permissions values (2, 35);
insert into role_permissions values (2, 36);
insert into role_permissions values (2, 37);

insert into role_permissions values (2, 39);
insert into role_permissions values (2, 40);
insert into role_permissions values (2, 41);
insert into role_permissions values (2, 42);
insert into role_permissions values (2, 43);
insert into role_permissions values (2, 44);

insert into role_permissions values (2, 55);
insert into role_permissions values (2, 56);
insert into role_permissions values (2, 57);
insert into role_permissions values (2, 58);
insert into role_permissions values (2, 59);
insert into role_permissions values (2, 60);

insert into role_permissions values (2, 61);
insert into role_permissions values (2, 62);
insert into role_permissions values (2, 63);

insert into role_permissions values (2, 70);
insert into role_permissions values (2, 71);
insert into role_permissions values (2, 72);
insert into role_permissions values (2, 73);
insert into role_permissions values (2, 74);
insert into role_permissions values (2, 75);

insert into role_permissions values (2, 76);
insert into role_permissions values (2, 77);
insert into role_permissions values (2, 78);

insert into role_permissions values (2, 79);
insert into role_permissions values (2, 80);
insert into role_permissions values (2, 81);

insert into role_permissions values (2, 82);
insert into role_permissions values (2, 83);
insert into role_permissions values (2, 84);
insert into role_permissions values (2, 85);
insert into role_permissions values (2, 86);
insert into role_permissions values (2, 87);
insert into role_permissions values (2, 88);
insert into role_permissions values (2, 89);


insert into role_permissions values (3, 13);

insert into role_permissions values (3, 14);
insert into role_permissions values (3, 15);
insert into role_permissions values (3, 16);
insert into role_permissions values (3, 17);
insert into role_permissions values (3, 18);

insert into role_permissions values (3, 19);
insert into role_permissions values (3, 20);
insert into role_permissions values (3, 21);
insert into role_permissions values (3, 22);
insert into role_permissions values (3, 23);
insert into role_permissions values (3, 24);

insert into role_permissions values (3, 25);
insert into role_permissions values (3, 26);
insert into role_permissions values (3, 27);
insert into role_permissions values (3, 28);
insert into role_permissions values (3, 29);
insert into role_permissions values (3, 30);

insert into role_permissions values (3, 31);
insert into role_permissions values (3, 32);
insert into role_permissions values (3, 33);
insert into role_permissions values (3, 34);
insert into role_permissions values (3, 35);
insert into role_permissions values (3, 36);
insert into role_permissions values (3, 37);


insert into role_permissions values (3, 39);
insert into role_permissions values (3, 40);
insert into role_permissions values (3, 41);
insert into role_permissions values (3, 42);
insert into role_permissions values (3, 43);
insert into role_permissions values (3, 44);


insert into role_permissions values (3, 55);
insert into role_permissions values (3, 56);
insert into role_permissions values (3, 57);
insert into role_permissions values (3, 58);
insert into role_permissions values (3, 59);
insert into role_permissions values (3, 60);

insert into role_permissions values (3, 61);
insert into role_permissions values (3, 62);
insert into role_permissions values (3, 63);

insert into role_permissions values (3, 64);
insert into role_permissions values (3, 65);
insert into role_permissions values (3, 66);
insert into role_permissions values (3, 67);
insert into role_permissions values (3, 68);
insert into role_permissions values (3, 69);

insert into role_permissions values (3, 76);
insert into role_permissions values (3, 77);
insert into role_permissions values (3, 78);

insert into role_permissions values (3, 79);
insert into role_permissions values (3, 80);
insert into role_permissions values (3, 81);

insert into role_permissions values (3, 82);
insert into role_permissions values (3, 83);
insert into role_permissions values (3, 84);
insert into role_permissions values (3, 85);
insert into role_permissions values (3, 86);
insert into role_permissions values (3, 87);
insert into role_permissions values (3, 88);
insert into role_permissions values (3, 89);

insert into role_permissions values (4, 13);

insert into role_permissions values (4, 45);

insert into role_permissions values (4, 48);
insert into role_permissions values (4, 49);

insert into role_permissions values (4, 52);
insert into role_permissions values (4, 53);

insert into role_permissions values (4, 90);

insert into role_permissions values (4, 89);
insert into role_permissions values (4, 91);
insert into role_permissions values (4, 92);
insert into role_permissions values (4, 93);
insert into role_permissions values (4, 94);


insert into role_permissions values (5, 13);

insert into role_permissions values (5, 46);
insert into role_permissions values (5, 47);
insert into role_permissions values (5, 48);
insert into role_permissions values (5, 49);
insert into role_permissions values (5, 50);
insert into role_permissions values (5, 51);
insert into role_permissions values (5, 52);
insert into role_permissions values (5, 53);
insert into role_permissions values (5, 54);
insert into role_permissions values (5, 90);

insert into role_permissions values (5, 89);

insert into role_permissions values (5, 91);
insert into role_permissions values (5, 92);
insert into role_permissions values (5, 93);
insert into role_permissions values (5, 94);



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


insert into activity values ('1','DJEL1', 'Djelatnost1');
insert into activity values ('2','DJEL2', 'Djelatnost2');
insert into activity values ('3','DJEL3', 'Djelatnost3');
insert into activity values ('4','DJEL4', 'Djelatnost4');


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
insert into subject values ('17');

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

insert into employee values ('Mojzaposleni', 'Mojzaposleni', '12', '1');

insert into user values ('1', FALSE, '2017-06-04 00:05:00', 'neki1@kkk.kkk', 'username1', '', 'username1', '1', '1', '1');
insert into user values ('2', TRUE, '2013-08-30 19:06:00', 'neki2@kkk.kkk', 'username2', '', 'username2', '1', '1', '2');
insert into user values ('3', TRUE, '2013-08-30 19:07:00', 'neki3@kkk.kkk', 'username3', '', 'username3', '1', '2', '3');
insert into user values ('4', TRUE, '2013-08-30 19:08:00', 'neki4@kkk.kkk', 'username4', '', 'username4', '1', '2', '4');
--insert into user values ('5', TRUE, '2013-08-30 19:09:00', 'neki5@kkk.kkk', 'username5', '', 'username5', '1', '2', '5');
--insert into user values ('6', TRUE, '2013-08-30 19:10:00', 'neki6@kkk.kkk', 'username6', '', 'username6', '1', '3', '6');
--insert into user values ('7', TRUE, '2013-08-30 19:11:00', 'neki7@kkk.kkk', 'username7', '', 'username7', '1', '3', '7');
--insert into user values ('8', TRUE, '2013-08-30 19:11:00', 'neki8@kkk.kkk', 'username8', '', 'username8', '2', '2', '8');
--insert into user values ('9', TRUE, '2013-09-30 19:11:00', 'neki9@kkk.kkk', 'username9', '' ,'username9', '3', '3', '9');

insert into klijent values ('adresa', 'mail1@kkk.kkk', TRUE, 'Ime1', '4578445121255', 'Prezime1', '123456788', '12', '1', '1');
insert into klijent values ('adresa', 'mail2@kkk.kkk', TRUE, 'Ime2', '4578545121255', 'Prezime2', '123454788', '13', '1', '1');
insert into klijent values ('adresa', 'mail3@kkk.kkk', TRUE, 'Ime3', '4518545121255', 'Prezime3', '123452788', '14', '1', '1');
insert into klijent values ('Adresa1', 'ime3@prezime.com', TRUE, 'Ime', '0000000000000', 'Prezime', '123452788', '15', '2', '2');
insert into klijent values ('Adresa1', 'ime4@prezime.com', TRUE, 'Ime', '0000000000000', 'Prezime', '123452788', '16', '3', '3');

insert into klijent values ('Pravno', 'pravno@prezime.com', FALSE, 'Pravno', '6655111444477', 'Prezime', '123452728', '17', '1', '1');
insert into pravno_lice values ('9879879', 'Odobrilo', '9283746578', '17', '3');

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

insert into valuta values ('1', 'DIN', 'Dinar');
insert into valuta values ('2', 'BAM', 'Bosanska marka');
insert into valuta values ('3', 'ALL', 'Lek');
insert into valuta values ('4', 'USD', 'Americki dolar');
insert into valuta values ('5', 'EUR', 'Euro');
insert into valuta values ('6', 'JPY', 'YEN');

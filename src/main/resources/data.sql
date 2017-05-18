insert into bank values (1, '123', 'Prva banka', '12345678', '9998828');
insert into bank values (2, '234', 'Druga banka', '21345678', '99988188');
insert into bank values (3, '345', 'Treca banka', '14345678', '99988858');
insert into bank values (4, '456', 'Cetvrta banka', '16345678', '99984888');
insert into bank values (5, '567', 'Peta banka', '12342678', '89089');

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

insert into permission values (1, 'Prva');
insert into permission values (2, 'Druga');
insert into permission values (3, 'Treca');
insert into permission values (4, 'Cetvrta');

insert into role values (1, 'Prva rola');
insert into role values (2, 'Druga rola');
insert into role values (3, 'Treca rola');

insert into role_permissions values (1, 1);
insert into role_permissions values (1, 3);
insert into role_permissions values (3, 2);
insert into role_permissions values (2, 4);

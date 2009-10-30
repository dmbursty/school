-- Example commands for (re) creating the STUDENT-COURSE database
-- NOTE: insert commands should be modified to create appropriate
-- test data

connect to cs348

drop table course
create table course ( \
    cnum        varchar(8) not null, \
    cname       varchar(40) not null, \
    primary key (cnum) )

insert into course values ('CS348', 'Introduction to Databases')
insert into course values ('ECE457', 'Applied Artificial Intelligence')
insert into course values ('MATH239', 'Combinatorics')
insert into course values ('STAT206', 'Statistics for Software Engineers')
insert into course values ('SPCOM223', 'Public Speaking')
insert into course values ('THOR101', 'Norse Mythology')
insert into course values ('THOR102', 'Advanced Norse Mythology')

drop table professor
create table professor ( \
    pnum        integer not null, \
    pname       varchar(20) not null, \
    office      varchar(10) not null, \
    dept        varchar(30) not null, \
    primary key (pnum) )

insert into professor values (1, 'Weddell, Grant', 'DC3346', 'Computer Science')
insert into professor values (2, 'Ilyas, Ihab', 'DC3348', 'Computer Science')
insert into professor values (3, 'Andrew, Kennings', 'EIT2222', 'ECE')
insert into professor values (4, 'David, Jao', 'MC6789', 'Cominatorics and Optimization')
insert into professor values (5, 'Peter, Balka', 'MC5000', 'Statistics')
insert into professor values (6, 'John, Doe', 'ABCD', 'Philosophy')
insert into professor values (7, 'Raveendran, Gobaan', 'CS744', 'Insane')

drop table student
create table student ( \
    snum        integer not null, \
    sname       varchar(20) not null, \
    year        integer not null, \
    primary key (snum) )

insert into student values (1, 'Burstyn, Max', 3)
insert into student values (2, 'Flath, Nathan', 2)
insert into student values (3, 'Kim, Yubin', 3)
insert into student values (4, 'McDummy, Dummy', 2)
insert into student values (5, 'Raveendran, Gobaan', 2)
insert into student values (1337, 'Sucks, Gerald', 1)

drop table class
create table class ( \
    cnum        varchar(8) not null, \
    term        varchar(5) not null, \
    section     integer not null, \
    pnum        integer not null, \
    primary key (cnum, term, section), \
    foreign key (cnum) references course (cnum), \
    foreign key (pnum) references professor (pnum) )

insert into class values ('CS348', 'F2009', 1, 1)
insert into class values ('CS348', 'F2009', 2, 1)
insert into class values ('CS348', 'F2009', 3, 2)
insert into class values ('CS348', 'F2009', 4, 3)
insert into class values ('CS348', 'S2005', 1, 1)
insert into class values ('CS348', 'S2006', 1, 1)
insert into class values ('CS348', 'S2007', 1, 2)
insert into class values ('CS348', 'S2008', 1, 5)

insert into class values ('ECE457', 'F2009', 1, 3)

insert into class values ('MATH239', 'W2007', 1, 4)
insert into class values ('MATH239', 'F2009', 1, 6)

insert into class values ('STAT206', 'W2007', 1, 5)
insert into class values ('STAT206', 'W2007', 2, 6)

insert into class values ('SPCOM223', 'W2007', 1, 6)

drop table enrollment
create table enrollment ( \
    snum        integer not null, \
    cnum        varchar(8) not null, \
    term        varchar(5) not null, \
    section     integer not null, \
    primary key (snum, cnum, term, section), \
    foreign key (snum) references student (snum), \
    foreign key (cnum, term, section) references class (cnum, term, section) )

insert into enrollment values (1, 'CS348', 'F2009', 2)
insert into enrollment values (1, 'ECE457', 'F2009', 1)
insert into enrollment values (1, 'MATH239', 'W2007', 1)
insert into enrollment values (1, 'STAT206', 'W2007', 1)
insert into enrollment values (1, 'SPCOM223', 'W2007', 1)

insert into enrollment values (2, 'CS348', 'F2009', 1)
insert into enrollment values (2, 'MATH239', 'W2007', 1)
insert into enrollment values (2, 'STAT206', 'W2007', 1)

insert into enrollment values (3, 'CS348', 'F2009', 2)
insert into enrollment values (3, 'MATH239', 'W2007', 1)
insert into enrollment values (3, 'STAT206', 'W2007', 1)


insert into enrollment values (4, 'CS348', 'F2009', 1)
insert into enrollment values (4, 'CS348', 'F2009', 2)
insert into enrollment values (4, 'CS348', 'F2009', 3)
insert into enrollment values (4, 'CS348', 'F2009', 4)
insert into enrollment values (4, 'CS348', 'S2005', 1)
insert into enrollment values (4, 'CS348', 'S2006', 1)
insert into enrollment values (4, 'CS348', 'S2007', 1)
insert into enrollment values (4, 'CS348', 'S2008', 1)

insert into enrollment values (4, 'MATH239', 'W2007', 1)
insert into enrollment values (4, 'MATH239', 'F2009', 1)

insert into enrollment values (4, 'STAT206', 'W2007', 1)
insert into enrollment values (4, 'STAT206', 'W2007', 2)

insert into enrollment values (5, 'STAT206', 'W2007', 2)

drop table mark
create table mark ( \
    snum        integer not null, \
    cnum        varchar(8) not null, \
    term        varchar(5) not null, \
    section     integer not null, \
    grade       integer not null, \
    primary key (snum, cnum, term, section), \
    foreign key (snum, cnum, term, section) \
    references enrollment (snum, cnum, term, section) )

insert into mark values (1, 'MATH239', 'W2007', 1, 110)
insert into mark values (1, 'STAT206', 'W2007', 1, 110)
insert into mark values (1, 'SPCOM223', 'W2007', 1, 91)

insert into mark values (2, 'MATH239', 'W2007', 1, 10)
insert into mark values (2, 'STAT206', 'W2007', 1, 10)

insert into mark values (3, 'MATH239', 'W2007', 1, 110)
insert into mark values (3, 'STAT206', 'W2007', 1, 110)

insert into mark values (4, 'CS348', 'S2005', 1, 45)
insert into mark values (4, 'CS348', 'S2006', 1, 45)
insert into mark values (4, 'CS348', 'S2007', 1, 45)
insert into mark values (4, 'CS348', 'S2008', 1, 45)

insert into mark values (4, 'MATH239', 'W2007', 1, 45)

insert into mark values (4, 'STAT206', 'W2007', 1, 45)
insert into mark values (4, 'STAT206', 'W2007', 2, 45)

insert into mark values (5, 'STAT206', 'W2007', 2, 91)

drop table schedule
create table schedule ( \
    cnum        varchar(8) not null, \
    term        varchar(5) not null, \
    section     integer not null, \
    day         varchar(10) not null, \
    time        varchar(5) not null, \
    room        varchar(10) not null, \
    primary key (cnum, term, section, day, time), \
    foreign key (cnum, term, section) \
    references class (cnum, term, section) )

insert into schedule values ('CS348', 'F2009', 1, 'Tuesday', '10:00', 'MC4042')
insert into schedule values ('CS348', 'F2009', 1, 'Thursday', '10:00', 'MC4042')
insert into schedule values ('CS348', 'F2009', 2, 'Tuesday', '14:30', 'MC4042')
insert into schedule values ('CS348', 'F2009', 2, 'Thursday', '14:30', 'MC4042')
insert into schedule values ('CS348', 'F2009', 3, 'Tuesday', '11:30', 'MC4020')
insert into schedule values ('CS348', 'F2009', 3, 'Thursday', '11:30', 'MC4020')
insert into schedule values ('CS348', 'S2008', 1, 'Monday', '11:30', 'DC1350')
insert into schedule values ('CS348', 'S2007', 1, 'Wednesday', '11:30', 'DC1350')
insert into schedule values ('CS348', 'S2006', 1, 'Friday', '11:30', 'DC1350')
insert into schedule values ('MATH239', 'W2007', 1, 'Tuesday', '11:30', 'DC1350')
insert into schedule values ('ECE457', 'F2009', 1, 'Monday', '7:00', 'RCH306')

commit work

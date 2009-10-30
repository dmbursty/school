-- 1.
select distinct p.pnum, p.pname, p.dept \
from professor p, class c, schedule s \
where p.pnum = c.pnum \
  and c.cnum = s.cnum and c.term = s.term and c.section = s.section \
  and s.day = 'Tuesday' \
  and not exists ( select * from mark m where m.cnum = c.cnum \
                                          and m.term = c.term \
                                          and m.section = c.section )

-- 2.
select count(distinct p.pnum) as NumProfessors \
from professor p, class c \
where p.pnum = c.pnum \
  and c.cnum = 'CS348' \
  and exists ( select * from mark m where m.cnum = c.cnum \
                                      and m.term = c.term \
                                      and m.section = c.section )

-- 3.
select c.cnum, c.cname, m.grade \
from course c, class cl, enrollment e, mark m \
where c.cnum = cl.cnum \
  and e.cnum = cl.cnum and e.term = cl.term and e.section = cl.section \
  and e.cnum = m.cnum and e.term = m.term \
  and e.section = m.section and e.snum = m.snum \
  and m.snum = 1224

-- 4.
select * from student \
except \
select distinct s.* \
from professor p, class c, enrollment e, mark m, student s \
where p.pnum = c.pnum \
  and p.dept != 'Philosophy' \
  and e.cnum = c.cnum and e.term = c.term and e.section = c.section \
  and e.cnum = m.cnum and e.term = m.term \
  and e.section = m.section and e.snum = m.snum \
  and s.snum = m.snum \
  and m.grade < 92

-- 5.
select pnum, pname, dept \
from professor p \
except \
select distinct p.pnum, p.pname, p.dept \
from professor p, class c, schedule s \
where p.pnum = c.pnum \
  and c.cnum = s.cnum and c.term = s.term and c.section = s.section \
  and ( s.day = 'Monday' or s.day = 'Friday' ) \
  and not exists ( select * from mark m where m.cnum = c.cnum \
                                          and m.term = c.term \
                                          and m.section = c.section )

-- 6.
select c.cnum, c.term, c.section, p.pnum, p.pname \
from professor p, class c \
where p.pnum = c.pnum \
  and exists ( select * from mark m where m.cnum = c.cnum \
                                      and m.term = c.term \
                                      and m.section = c.section )

-- 7.
with Enrollments(cnum, enrollment) as ( \
  select e.cnum, count(e.snum) \
  from class cl, enrollment e \
  where e.cnum = cl.cnum and e.term = cl.term and e.section = cl.section \
  group by e.cnum \
  union \
  select c.cnum, 0 \
  from course c \
  where not exists ( select * from class where class.cnum = c.cnum) ) \
select * from Enrollments \
except \
select e1.cnum, e1.enrollment \
from Enrollments e1, Enrollments e2, Enrollments e3, Enrollments e4 \
where e1.enrollment > e2.enrollment and \
      e2.enrollment > e3.enrollment and \
      e3.enrollment > e4.enrollment

-- 8.
with Enrollments(pnum, pname, cnum, term, section, enrollment) as ( \
  select p.pnum, p.pname, c.cnum, c.term, c.section, count(s.snum) \
  from professor p, class c, enrollment e, student s \
  where p.pnum = c.pnum \
    and e.cnum = c.cnum and e.term = c.term and e.section = c.section \
    and e.snum = s.snum \
    and s.year = 2 \
    and not exists ( select * from mark m where m.cnum = c.cnum \
                                            and m.term = c.term \
                                            and m.section = c.section ) \
    group by p.pnum, p.pname, c.cnum, c.term, c.section) \
(select * from Enrollments \
 union \
 select p.pnum, p.pname, c.cnum, c.term, c.section, 0 as enrollment \
 from professor p, class c \
 where p.pnum = c.pnum \
   and not exists (select * from Enrollments e \
                     where c.cnum = e.cnum \
                       and c.term = e.term \
                       and c.section = e.section) \
   and not exists ( select * from mark m where m.cnum = c.cnum \
                                           and m.term = c.term \
                                           and m.section = c.section ) \
) order by pname, pnum, cnum, term, section

-- 9.
select p.pnum, p.pname, c.cnum, co.cname, c.term, c.section, \
       min(m.grade) as min, max(m.grade) as max \
from professor p, course co, class c, mark m \
where p.pnum = c.pnum \
  and co.cnum = c.cnum \
  and c.cnum = m.cnum and c.term = m.term and c.section = m.section \
  and p.dept = 'Statistics' \
  and exists ( select * from mark m where m.cnum = c.cnum \
                                      and m.term = c.term \
                                      and m.section = c.section ) \
  group by p.pnum, p.pname, c.cnum, co.cname, c.term, c.section

-- 10.
with Departments(dept) as ( \
  select distinct p.dept \
  from professor p, class c \
  where p.pnum = c.pnum \
    and exists ( select * from mark m where m.cnum = c.cnum \
                                        and m.term = c.term \
                                        and m.section = c.section ) \
  group by p.pnum, p.dept, c.term \
  having count(c.section) > 1) \
select (count(distinct d.dept) * 1.0) / count(distinct p.dept) as Percentage \
from Departments d, professor p

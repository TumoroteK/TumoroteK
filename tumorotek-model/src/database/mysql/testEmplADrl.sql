select format_adrl_position(1, 1);
-- A-A
select format_adrl_position(5, 1);
-- A-E
select format_adrl_position(10, 1);
-- A-J
select format_adrl_position(11, 1);
-- B-A
select format_adrl_position(20, 1);
-- B-J
select format_adrl_position(23, 1);
-- C-C
select format_adrl_position(59, 1);
-- F-I
select format_adrl_position(100, 1);
-- J-J

select format_adrl_position(1, 2);
-- 1
select format_adrl_position(5, 2);
-- 2
select format_adrl_position(10, 2);
-- 10
select format_adrl_position(11, 2);
-- 11
select format_adrl_position(20, 2);
-- 20
select format_adrl_position(23, 2);
-- 23
select format_adrl_position(59, 2);
-- 59
select format_adrl_position(100, 2);
-- 100

select format_adrl_position(1, 4);
-- 1-1
select format_adrl_position(5, 4);
-- 1-5
select format_adrl_position(10, 4);
-- 1-10
select format_adrl_position(11, 4);
-- 2-1
select format_adrl_position(20, 4);
-- 2-10
select format_adrl_position(23, 4);
-- 3-3
select format_adrl_position(59, 4);
-- 6-9
select format_adrl_position(100, 4);
-- 10-10
		
select format_adrl_position(1, 5);
-- 1-A
select format_adrl_position(5, 5);
-- 2-C
select format_adrl_position(10, 5);
-- 4-A
select format_adrl_position(11, 5);
-- 4-B
select format_adrl_position(23, 5);
-- 6-C
select format_adrl_position(59, 5);
-- 10-E
select format_adrl_position(67, 5);
-- 11-D
		
select format_adrl_position(1, 6);
-- A-1
select format_adrl_position(5, 6);
-- A-5
select format_adrl_position(10, 6);
-- B-1
select format_adrl_position(11, 6);
-- B-2
select format_adrl_position(23, 6);
-- C-5
select format_adrl_position(59, 6);
-- G-5
select format_adrl_position(81, 6);
-- I-9
	
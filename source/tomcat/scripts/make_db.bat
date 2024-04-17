set PGCLIENTENCODING=UTF8
psql -U postgres -h localhost -p 5432 -f test_pop.sql
psql -U postgres -h localhost -p 5432 -f test_pop2.sql

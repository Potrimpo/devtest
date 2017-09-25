
-- name: insert-or-update-person
-- query returns
-- * true if row is inserted,
-- * false if it is updated,
-- * nothing if neither update nor insert occurs
INSERT INTO person AS p (fname, lname, dob, phone)
VALUES (
  :fname,
  :lname,
  to_date(regexp_replace(:dob, '\\N', '', 'g'), ('YYYY-MM-DD')),
  :phone
)
ON CONFLICT ON CONSTRAINT unique_identifying_fields
  DO UPDATE
    SET phone = EXCLUDED.phone
    WHERE p.phone != EXCLUDED.phone
RETURNING TRUE;
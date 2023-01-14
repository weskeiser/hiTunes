UPDATE superhero
SET name = 'Dark Rent'
WHERE superhero_id = 1
RETURNING *;

DELETE FROM assistant
WHERE name like '__b%'
RETURNING assistant;

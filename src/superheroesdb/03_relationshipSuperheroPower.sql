CREATE TABLE superhero_link_power(
  superhero_id int REFERENCES superhero,
  power_id int REFERENCES "power",
  PRIMARY KEY (superhero_id, power_id)
);

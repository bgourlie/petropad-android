CREATE TABLE vehicle 
	(vehicle_id INTEGER PRIMARY KEY, 
	odometer_units INTEGER NOT NULL, 
	vehicle_name VARCHAR(255) NOT NULL UNIQUE);

CREATE TABLE entry 
	(entry_id INTEGER PRIMARY KEY AUTOINCREMENT, 
	timestamp INTEGER NOT NULL, 
	odometer_reading INTEGER NOT NULL, 
	distance_unit INTEGER NOT NULL DEFAULT 0, 
	fill_amount INTEGER NOT NULL, 
	price_per_unit INTEGER NOT NULL, 
	volume_unit INTEGER NOT NULL DEFAULT 0, 
	currency_type INTEGER NOT NULL DEFAULT 0, 
	vehicle_id INTEGER NOT NULL, 
FOREIGN KEY (vehicle_id)  REFERENCES vehicle(vehicle_id));
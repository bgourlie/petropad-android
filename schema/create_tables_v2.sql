CREATE TABLE vehicle 
	(vehicle_id INTEGER PRIMARY KEY AUTOINCREMENT, 
	odometer_units INTEGER NOT NULL, 
	vehicle_name VARCHAR(255) NOT NULL UNIQUE,
	make VARCHAR(64) NULL,
	model VARCHAR(64) NULL,
	year VARCHAR(4) NULL,
	vin VARCHAR(17) NULL,
	image BLOB NULL);

CREATE TABLE odometer_reading
	(odometer_reading_id INTEGER PRIMARY KEY AUTOINCREMENT,
	 odometer_reading INTEGER NOT NULL,
	 vehicle_id INTEGER NOT NULL,
	 timestamp INTEGER NOT NULL,
FOREIGN KEY (vehicle_id)  REFERENCES vehicle(vehicle_id));

CREATE TABLE trip
	(trip_id INTEGER PRIMARY KEY AUTOINCREMENT,
	label VARCHAR(255) NOT NULL,
	start_odometer_reading_id INTEGER NOT NULL,
	end_odometer_reading_id INTEGER NULL,
FOREIGN KEY (start_odometer_reading_id)  REFERENCES vehicle(odometer_reading_id)
FOREIGN KEY (end_odometer_reading_id)  REFERENCES vehicle(odometer_reading_id));

CREATE TABLE entry 
	(entry_id INTEGER PRIMARY KEY AUTOINCREMENT, 	 
	odometer_reading_id INTEGER NOT NULL, 
	distance_unit INTEGER NOT NULL DEFAULT 0, 
	fill_amount INTEGER NOT NULL, 
	price_per_unit INTEGER NOT NULL, 
	volume_unit INTEGER NOT NULL DEFAULT 0, 
	currency_type INTEGER NOT NULL DEFAULT 0,
	previous_entry_missed TINYINT NOT NULL DEFAULT 0,
	partial_fillup TINYINT NOT NULL DEFAULT 0,
	note VARCHAR(510) NULL, 
	trip_id INTEGER NULL,
FOREIGN KEY (odometer_reading_id)  REFERENCES vehicle(odometer_reading_id)
FOREIGN KEY (trip_id)  REFERENCES trip(trip_id));
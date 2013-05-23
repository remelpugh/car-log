DROP VIEW IF EXISTS vw_StatisticsTotals;
CREATE VIEW vw_StatisticsTotals AS
    SELECT VehicleId,
        MAX(OdometerReading) - MIN(OdometerReading) AS TotalDistance,
        SUM(FuelAmount) AS TotalFuel,
        SUM(TotalCost) AS TotalFuelCost,
        ( SELECT SUM(TotalCost)
          FROM FuelHistory
          WHERE VehicleId = t.VehicleId AND
                STRFTIME('%m', PurchaseDate / 1000, 'unixepoch') = STRFTIME('%m', 'now') ) AS TotalCurrentMonth,
        ( SELECT SUM(TotalCost)
          FROM FuelHistory
          WHERE VehicleId = t.VehicleId AND STRFTIME('%m', PurchaseDate / 1000, 'unixepoch') =
                                            STRFTIME('%m', 'now', '-1 month') ) AS TotalPreviousMonth,
        ( SELECT SUM(TotalCost)
          FROM FuelHistory
          WHERE VehicleId = t.VehicleId AND
                STRFTIME('%Y', PurchaseDate / 1000, 'unixepoch') = STRFTIME('%Y', 'now') ) AS TotalCurrentYear,
        ( SELECT SUM(TotalCost)
          FROM FuelHistory
          WHERE VehicleId = t.VehicleId AND STRFTIME('%Y', PurchaseDate / 1000, 'unixepoch') =
                                            STRFTIME('%Y', 'now', '-1 year') ) AS TotalPreviousYear
    FROM FuelHistory t
    GROUP BY VehicleId;

DROP VIEW IF EXISTS vw_StatisticsFillUps;
CREATE VIEW vw_StatisticsFillUps AS
    SELECT VehicleId,
        COUNT(_id) AS TotalFillUps,
        MAX(FuelAmount) AS MaxFuelAmount,
        MIN(FuelAmount) AS MinFuelAmount,
        MAX(TotalCost) AS MaxTotalCost,
        MIN(TotalCost) AS MinTotalCost,
        MAX(CostPerUnit) AS MaxCostPerUnit,
        MIN(CostPerUnit) AS MinCostPerUnit,
        MAX(( OdometerReading - IFNULL(( SELECT OdometerReading
                                         FROM FuelHistory
                                         WHERE VehicleId = t.VehicleId AND PurchaseDate < t.PurchaseDate
                                         ORDER BY PurchaseDate
                                             DESC
                                         LIMIT 1 ), 0) ) / FuelAmount) / 100 AS MaxCostPerMile,
        MIN(( OdometerReading - IFNULL(( SELECT OdometerReading
                                         FROM FuelHistory
                                         WHERE VehicleId = t.VehicleId AND PurchaseDate < t.PurchaseDate
                                         ORDER BY PurchaseDate
                                             DESC
                                         LIMIT 1 ), 0) ) / FuelAmount) / 100 AS MinCostPerMile
    FROM FuelHistory t
    GROUP BY VehicleId
    ORDER BY VehicleId, PurchaseDate
        DESC;

DROP VIEW IF EXISTS vw_StatisticsAverages;
CREATE VIEW vw_StatisticsAverages AS
    SELECT VehicleId,
        AVG(FuelAmount) AS AvgFuelAmount,
        AVG(TotalCost) AS AvgTotalCost,
        AVG(CostPerUnit) AS AvgCostPerUnit,
        AVG(( OdometerReading - IFNULL(( SELECT OdometerReading
                                         FROM FuelHistory
                                         WHERE VehicleId = t.VehicleId AND PurchaseDate < t.PurchaseDate
                                         ORDER BY PurchaseDate
                                             DESC
                                         LIMIT 1 ), 0) ) / FuelAmount) AS AvgMilesPerGallon,
        MAX(( OdometerReading - IFNULL(( SELECT OdometerReading
                                         FROM FuelHistory
                                         WHERE VehicleId = t.VehicleId AND PurchaseDate < t.PurchaseDate
                                         ORDER BY PurchaseDate
                                             DESC
                                         LIMIT 1 ), 0) ) / FuelAmount) AS MaxMilesPerGallon
    FROM FuelHistory t
    GROUP BY VehicleId
    ORDER BY VehicleId, PurchaseDate
        DESC;
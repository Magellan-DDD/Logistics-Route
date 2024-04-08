CREATE TABLE ROUTE
(
    ID VARCHAR2(40) NOT NULL PRIMARY KEY,
    STATUS VARCHAR2(40) NOT NULL,
    DISPATCHER_ID VARCHAR2(40) NOT NULL,
    DRIVER_ID VARCHAR2(40),
    VEHICLE_ID VARCHAR2(40),

    STARTED_DATE TIMESTAMP,
    ACTUAL_ARRIVAL_DATE TIMESTAMP,
    DEPARTURE_DATE TIMESTAMP NOT NULL,
    ARRIVAL_DATE TIMESTAMP NOT NULL,

    ZIP VARCHAR2(20),
    REGION VARCHAR2(125) NOT NULL,
    CITY VARCHAR2(125) NOT NULL,
    STREET VARCHAR2(125) NOT NULL,
    BUILDING VARCHAR2(125) NOT NULL
);

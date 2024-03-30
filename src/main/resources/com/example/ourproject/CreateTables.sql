CREATE TABLE Shop.Emp (
                          Emp_ID SERIAL PRIMARY KEY,
                          Firs_tName VARCHAR(15) NOT NULL,
                          Mid_Name VARCHAR(15),
                          Last_Name VARCHAR(15) NOT NULL,
                          UserName VARCHAR(20) UNIQUE NOT NULL,
                          "Password" VARCHAR(20) NOT NULL,
                          Gender Shop.GenderType NOT NULL,
                          Address VARCHAR(30) NOT NULL,
                          B_Date DATE NOT NULL,
                          Hired_Date DATE NOT NULL,
                          Earning_Per_T NUMERIC(10, 2) DEFAULT 0.0 NOT NULL,
                          SN INTEGER NOT NULL ,
                          ID CHAR(10) UNIQUE NOT NULL,
                          Phone CHAR(10) UNIQUE NOT NULL
);

CREATE TABLE shop.Payroll(
                             payroll_id SERIAL PRIMARY KEY,
                             "DATE" DATE DEFAULT CURRENT_DATE NOT NULL ,
                             Start_time time DEFAULT CURRENT_TIMESTAMP NOT NULL ,
                             end_time time DEFAULT CURRENT_TIMESTAMP + INTERVAL '8 hours' NOT NULL,
                             Bonus_Amount NUMERIC(10, 2) DEFAULT 0.0 NOT NULL ,
                             Payment_status shop.status DEFAULT 'P' NOT NULL,
                             EID INTEGER NOT NULL
);
CREATE TABLE shop.Section(
                             Section_number serial PRIMARY KEY,
                             Section_Name VARCHAR(20) UNIQUE NOT NULL,
                             Description VARCHAR(50),
                             Manager_ID INTEGER UNIQUE,
                             MGR_Start_Date DATE
);
CREATE TABLE shop.Product(
                             ProductID SERIAL PRIMARY KEY,
                             Product_Name VARCHAR(30) NOT NULL,
                             Description VARCHAR(50),
                             Quantity INTEGER DEFAULT 0 NOT NULL,
                             Price NUMERIC(8,2) DEFAULT 0.0 NOT NULL,
                             PSN INTEGER NOT NULL
);
CREATE TABLE shop.Item_Details
(
    Item_Details_ID SERIAL PRIMARY KEY,
    Quantity INTEGER DEFAULT 0 NOT NULL,
    Note VARCHAR(30),
    Order_ID INTEGER NOT NULL,
    PID INTEGER NOT NULL
);
CREATE TABLE Shop.Order(
                           Order_ID SERIAL PRIMARY KEY,
                           Order_Date DATE DEFAULT CURRENT_DATE NOT NULL,
                           Status shop.status NOT NULL,
                           Discount NUMERIC (5,2) DEFAULT 0.0 NOT NULL,
                           Type Shop.status not null,
                           OEmpID INTEGER NOT NULL
);
CREATE TABLE shop.Supplier(
                              SID SERIAL PRIMARY KEY,
                              Company_Name VARCHAR(30) NOT NULL,
                              Phone CHAR(10) UNIQUE NOT NULL,
                              Email VARCHAR(25),
                              Location VARCHAR(30)
);
CREATE TABLE Shop.Supplier_Order(
                                    Order_ID INTEGER NOT NULL,
                                    SID INTEGER NOT NULL,
                                    PRIMARY KEY (Order_ID,SID)
);
CREATE TABLE shop.customer(
                              CID SERIAL PRIMARY KEY,
                              FName CHAR(15) NOT NULL,
                              MName CHAR(15),
                              LName CHAR(15) NOT NULL,
                              Phone CHAR(10) NOT NULL,
                              ID_NUMBER INTEGER NOT NULL
);
CREATE TABLE shop.Customer_order(
                                    Order_ID INTEGER NOT NULL,
                                    CID INTEGER NOT NULL,
                                    PRIMARY KEY (Order_ID,CID)
);

ALTER TABLE Shop.Emp
    ADD CONSTRAINT fk_section_Number
        FOREIGN KEY (SN) REFERENCES shop.section(section_number)
            ON DELETE NO ACTION
            ON UPDATE CASCADE;

ALTER TABLE Shop.Payroll
    ADD CONSTRAINT fk_emp_id
        FOREIGN KEY (EID) REFERENCES shop.emp(emp_id)
            ON DELETE NO ACTION
            ON UPDATE CASCADE;

ALTER TABLE Shop.Section
    ADD CONSTRAINT fk_Manager_id
        FOREIGN KEY (Manager_ID) REFERENCES shop.emp(emp_id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

ALTER TABLE Shop.Product
    ADD CONSTRAINT fk_PSN_id
        FOREIGN KEY (PSN) REFERENCES shop.Section(Section_number)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE Shop.Item_Details
    ADD CONSTRAINT fk_PID_id
        FOREIGN KEY (PID) REFERENCES shop.Product(ProductID)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE Shop.Item_Details
    ADD CONSTRAINT fk_ORDER_id_in_itemD
        FOREIGN KEY (Order_ID) REFERENCES shop.Order(Order_ID)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE Shop.Order
    ADD CONSTRAINT fk_OEmp_id
        FOREIGN KEY (OEmpID) REFERENCES shop.Emp(emp_id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

ALTER TABLE Shop.Supplier_Order
    ADD CONSTRAINT fk_order_id_in_SO
        FOREIGN KEY (Order_ID) REFERENCES shop.Order(order_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE Shop.Supplier_Order
    ADD CONSTRAINT fk_Sid_in_SO
        FOREIGN KEY (SID) REFERENCES shop.Supplier(SID)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE Shop.Customer_order
    ADD CONSTRAINT fk_order_id_in_CO
        FOREIGN KEY (Order_ID) REFERENCES shop.Order(order_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE Shop.Customer_order
    ADD CONSTRAINT fk_Cid_in_CO
        FOREIGN KEY (CID) REFERENCES shop.Customer(CID)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

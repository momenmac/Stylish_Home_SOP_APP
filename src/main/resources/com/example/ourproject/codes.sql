-- Drop the existing sequence if it exists


DROP SEQUENCE shop.product_productid_seq CASCADE;


-- Create a new sequence starting from 6
CREATE SEQUENCE shop.product_productid_seq START 7;

-- Set the next value for the sequence
SELECT setval('shop.product_productid_seq', 7, false);

-- Set the sequence for the productid column in your table
ALTER TABLE shop.product ALTER COLUMN productid SET DEFAULT nextval('shop.product_productid_seq');


ALTER TABLE Shop.Emp
    ALTER COLUMN Hired_Date SET DEFAULT CURRENT_DATE;


CREATE OR REPLACE FUNCTION shop.updateEmployeeSection()
    RETURNS TRIGGER AS $$
BEGIN
    -- Update section_num for the new manager of the section
    IF NEW.manager_id IS NOT NULL THEN
        UPDATE shop.emp
        SET sn = NEW.section_number
        WHERE emp_id = NEW.manager_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER AssignManagerToSection
    AFTER INSERT OR UPDATE OF manager_id ON shop.section
    FOR EACH ROW
EXECUTE FUNCTION shop.updateEmployeeSection();

ALTER SEQUENCE shop.section_section_number_seq RESTART WITH 8;

-- Drop the existing sequence if it exists
DROP SEQUENCE IF EXISTS shop.section_section_number_seq CASCADE;

-- Create a new sequence starting from 6
CREATE SEQUENCE shop.section_section_number_seq
    START WITH 6;

-- Set the ownership of the sequence
ALTER SEQUENCE shop.section_section_number_seq OWNER TO momen;

-- Set the ownership to a specific table's column (if needed)
ALTER SEQUENCE shop.section_section_number_seq OWNED BY shop.section.section_number;

ALTER TABLE shop.section ALTER COLUMN section_number SET DEFAULT nextval('shop.section_section_number_seq');


CREATE OR REPLACE FUNCTION shop.update_mgr_start_date_to_null()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.manager_id IS NULL THEN
        NEW.mgr_start_date = NULL;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_mgr_start_date_trigger
    BEFORE INSERT OR UPDATE OF manager_id ON shop.section
    FOR EACH ROW
EXECUTE FUNCTION shop.update_mgr_start_date_to_null();

ALTER TABLE shop.supplier
    ADD CONSTRAINT unique_email UNIQUE (email);

ALTER TABLE shop.customer ADD COLUMN location varchar(50);

-- Change fname column to varchar(15)
ALTER TABLE shop.customer
    ALTER COLUMN fname TYPE varchar(15);

-- Change mname column to varchar(15)
ALTER TABLE shop.customer
    ALTER COLUMN mname TYPE varchar(15);

-- Change lname column to varchar(15)
ALTER TABLE shop.customer
    ALTER COLUMN lname TYPE varchar(15);

ALTER TABLE shop.item_details
    ADD COLUMN price numeric(8, 2) DEFAULT 0.0 NOT NULL;


CREATE OR REPLACE FUNCTION shop.update_product_quantity()
    RETURNS TRIGGER AS $$
BEGIN
    -- Fetch the purchase type ('S' or 'P') from the 'order' table
    DECLARE
        purchaseType CHAR;
    BEGIN
        SELECT type INTO purchaseType
        FROM shop."order"
        WHERE order_id = NEW.order_id;

        -- Check the purchase type and update product quantity accordingly
        IF purchaseType = 'S' THEN
            UPDATE shop.product
            SET quantity = quantity - NEW.quantity
            WHERE productid = NEW.pid;
        ELSIF purchaseType = 'P' THEN
            UPDATE shop.product
            SET quantity = quantity + NEW.quantity
            WHERE productid = NEW.pid;
        END IF;
    END;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger
CREATE TRIGGER update_product_quantity_trigger
    AFTER INSERT ON shop.item_details
    FOR EACH ROW
EXECUTE FUNCTION shop.update_product_quantity();


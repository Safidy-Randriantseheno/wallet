
-- Vérification et création du type ENUM "category_type"
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'category_type') THEN
        CREATE TYPE "category_type" AS ENUM ('credit', 'debit');
    END IF;
END
$$;

-- Création du type ENUM "name"
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'name') THEN
        CREATE TYPE name AS ENUM (
'Food and Drinks',
    'Groceries',
    'Coffee Shops',
    'Fast Food',
    'Online Shopping',
    'Pets',
    'Jewelry Accessories',
    'Gifts',
    'Electronics Accessories',
    'Children',
    'Home Garden',
    'Stationery',
    'Pharmacy Convenience',
    'Health Beauty',
    'Leisure Time',
    'Clothing Shoes',
    'Housing',
    'Property Insurance',
    'Utilities',
    'Maintenance Repairs',
    'Rent',
    'Mortgage',
    'Services',
    'Transportation',
    'Long Distance',
    'Taxi',
    'Public Transportation',
    'Business Travel',
    'Vehicle',
    'Vehicle Insurance',
    'Fuel',
    'Vehicle Maintenance',
    'Rental',
    'Rentals',
    'Parking',
    'Leisure',
    'Alcohol Tobacco',
    'Wellness Beauty',
    'Culture Sports Events',
    'Education Development',
    'Life Events',
    'Books Audio Subscriptions',
    'Hobbies',
    'Lottery Gambling',
    'Charity Organization',
    'Healthcare Doctor',
    'Sports Fitness',
    'TV Streaming',
    'Holidays Travel Hotels',
    'Multimedia Computers',
    'Internet',
    'Software Applications Games',
    'Postal Services',
    'Phone Mobile Phone',
    'Financial Expenses',
    'Family Allowances',
    'Fines',
    'Insurances',
    'Warning',
    'Charges Fees',
    'Loan Interest',
    'Taxes',
    'Investments',
    'Real Estate',
    'Collections',
    'Savings',
    'Financial Investments'
    'Vehicles Personal Property',
    'Income',
    'Family Allowances Income',
    'Gifts Income',
    'Checks Discounts',
    'Subsidies Donations',
    'Interests Dividends',
    'Lottery Gambling Income',
    'Loan Rental',
    'Refunds Tax Purchases',
    'Rental Income',
    'Salaries Invoices',
    'Sales',
    'Other',
    'Missing',
    'Unknown Expense',
    'Unknown Income'
        );
    END IF;
END
$$;

-- Insertions dans la table "category"
INSERT INTO "category" (id, name, type)
SELECT 'ca1', 'Food and Drinks', 'credit'
WHERE NOT EXISTS (
    SELECT 1 FROM "category" WHERE name = 'Food and Drinks' AND type = 'credit'
);

INSERT INTO "category" (id, name, type)
SELECT 'ca2', 'Groceries', 'debit'
WHERE NOT EXISTS (
    SELECT 1 FROM "category" WHERE name = 'Groceries' AND type = 'debit'
);

INSERT INTO "category" (id, name, type)
SELECT 'ca3', 'Pets', 'credit'
WHERE NOT EXISTS (
    SELECT 1 FROM "category" WHERE name = 'Groceries' AND type = 'credit'
);


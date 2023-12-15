DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'category_type') THEN
        CREATE TYPE "category_type" AS ENUM ('credit', 'debit');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'expense_category') THEN
            CREATE TYPE expense_category AS ENUM (
                'Food and drinks',
                    'Food',
                    'Bar, cafe',
                    'Restaurant, fast food',

                'Online purchases and stores',
                    'Pets',
                    'Jewelry, accessories',
                    'Gifts',
                    'Electronics, accessories',
                    'Children',
                    'Home, Garden',
                    'Stationery store',
                    'Pharmacy, convenience store',
                    'Health and beauty',
                    'Free time',
                    'Clothes shoes',

                'Accommodation',
                    'Property insurance',
                    'Energy, utilities',
                    'Maintenance, repairs',
                    'Rent',
                    'Mortgage loan',
                    'Services',

                'Transportation',
                    'Long distance',
                    'Taxi',
                    'Public transport',
                    'Business travel',

                'Vehicle',
                    'Vehicle insurance',
                    'Fuel',
                    'Vehicle maintenance',
                    'Rental',
                    'Rentals',
                    'Parking',

                'Hobbies',
                    'Alcohol, tobacco',
                    'Well-being, beauty',
                    'Culture, sporting events',
                    'Education, development',
                    'Life events',
                    'Books, audio, subscriptions',
                    'Hobbies',
                    'Lottery, games of chance',
                    'Charity, gifts',
                    'Healthcare, doctor',
                    'Sport, fitness',
                    'TV, streaming',
                    'Vacation, travel, hotels',

                'Multimedia, IT',
                    'Internet',
                    'Software, applications, games',
                    'Postal services',
                    'Telephone, mobile phone',

                'Financial expenses',
                    'Family allowances',
                    'Fines',
                    'Insurance',
                    'Warning',
                    'Charges, fees',
                    'Loan, interest',
                    'Taxes',

                'Investments',
                    'Property',
                    'Collections',
                    'Saving',
                    'Financial investments',
                    'Vehicles, personal property'
            );
END
$$;

CREATE TABLE IF NOT EXISTS "category" (
    id      VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
    name    name UNIQUE NOT NULL,
    type    category_type NOT NULL
);
 create index if not exists name_index on "category" (name);
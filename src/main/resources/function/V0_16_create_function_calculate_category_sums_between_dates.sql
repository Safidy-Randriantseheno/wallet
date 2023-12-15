CREATE OR REPLACE FUNCTION calculateCategorySumBetweenDates(
    account_id VARCHAR,
    start_date TIMESTAMP,
    end_date TIMESTAMP
) RETURNS TABLE (
    restaurant NUMERIC,
    salaire NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COALESCE(SUM(CASE WHEN c.name = 'Restaurant' THEN t.amount ELSE 0 END), 0) AS restaurant,
        COALESCE(SUM(CASE WHEN c.name = 'Salaire' THEN t.amount ELSE 0 END), 0) AS salaire
    FROM
        transaction t
    LEFT JOIN
        category c ON t.category_id = c.id
    WHERE
        t.account_id = calculateCategorySumBetweenDates.account_id
        AND t.transaction_date >= calculateCategorySumBetweenDates.start_date
        AND t.transaction_date <= calculateCategorySumBetweenDates.end_date;

    RETURN;
END;
$$ LANGUAGE plpgsql;

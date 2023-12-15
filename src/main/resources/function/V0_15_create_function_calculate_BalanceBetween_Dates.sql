CREATE OR REPLACE FUNCTION calculateBalanceBetweenDates(
    account_id VARCHAR,
    start_date TIMESTAMP,
    end_date TIMESTAMP
) RETURNS NUMERIC AS $$
DECLARE
    total_amount NUMERIC := 0;
BEGIN
    -- Calcule la somme des entrées d'argent (crédits) pendant la plage de dates
    SELECT COALESCE(SUM(amount), 0)
    INTO total_amount
    FROM "transaction"  -- Ajoutez le préfixe ici
    WHERE "transaction".account_id = calculateBalanceBetweenDates.account_id
        AND "transaction".transaction_date >= calculateBalanceBetweenDates.start_date
        AND "transaction".transaction_date <= calculateBalanceBetweenDates.end_date
        AND "transaction".amount > 0;

    -- Soustrait la somme des sorties d'argent (débits) pendant la plage de dates
    SELECT COALESCE(SUM(amount), 0)
    INTO total_amount
    FROM "transaction"  -- Ajoutez le préfixe ici
    WHERE "transaction".account_id = calculateBalanceBetweenDates.account_id
        AND "transaction".transaction_date >= calculateBalanceBetweenDates.start_date
        AND "transaction".transaction_date <= calculateBalanceBetweenDates.end_date
        AND "transaction".amount < 0;

    RETURN total_amount;
END;
$$ LANGUAGE plpgsql;

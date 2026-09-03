-- =====================================================================
-- IECS Enterprise: PostgreSQL Query Optimization & EXPLAIN ANALYZE Test
-- =====================================================================

-- 1. Switch to AR database
-- \c his_ar;

-- 2. Populate 250,000 mock application records (for benchmark testing)
DO $$
BEGIN
    FOR i IN 1..250000 LOOP
        INSERT INTO applications (citizen_id, plan_id, workflow_status, created_at, updated_at)
        VALUES (
            (random() * 15000)::INT,
            (random() * 5 + 1)::INT,
            CASE (i % 4)
                WHEN 0 THEN 'PENDING'
                WHEN 1 THEN 'IN_REVIEW'
                WHEN 2 THEN 'APPROVED'
                ELSE 'REJECTED'
            END,
            NOW() - (random() * 365 || ' days')::INTERVAL,
            NOW()
        );
    END LOOP;
END $$;

-- 3. TEST BEFORE INDEXING (Sequential Scan):
-- Expectation: Execution Time ~1,200ms - 1,500ms
EXPLAIN ANALYZE
SELECT * FROM applications 
WHERE citizen_id = 10429 AND workflow_status = 'APPROVED' 
ORDER BY created_at DESC;

-- 4. APPLY COMPOSITE B-TREE INDEX:
CREATE INDEX idx_app_citizen_status_date ON applications(citizen_id, workflow_status, created_at DESC);

-- 5. TEST AFTER INDEXING (Index Scan):
-- Expectation: Execution Time < 1ms (< 150ms target)
EXPLAIN ANALYZE
SELECT * FROM applications 
WHERE citizen_id = 10429 AND workflow_status = 'APPROVED' 
ORDER BY created_at DESC;

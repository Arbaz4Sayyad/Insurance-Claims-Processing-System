-- Seed Plans
INSERT INTO plans (name, description, active) VALUES ('SNAP', 'Supplemental Nutrition Assistance Program', true);
INSERT INTO plans (name, description, active) VALUES ('Medicaid', 'Health coverage for low income families', true);
INSERT INTO plans (name, description, active) VALUES ('TANF', 'Temporary Assistance for Needy Families', true);
INSERT INTO plans (name, description, active) VALUES ('GMA', 'General Medical Assistance', true);

-- Seed Rules for SNAP (Plan ID 1)
-- Eligible if total income < 2000 per household member
INSERT INTO rules (plan_id, condition_expression, benefit_amount, denial_reason) 
VALUES (1, 'totalIncome < (2000 * householdSize)', 350.0, 'Household income exceeds the limit for SNAP eligibility');

-- Seed Rules for Medicaid (Plan ID 2)
-- Eligible if under 18 or has children
INSERT INTO rules (plan_id, condition_expression, benefit_amount, denial_reason) 
VALUES (2, 'age < 18 or hasChildren == true', 0.0, 'Applicant must be under 18 or have dependents for this Medicaid plan');

-- Seed Rules for TANF (Plan ID 3)
-- Eligible if income < 1000 and adult
INSERT INTO rules (plan_id, condition_expression, benefit_amount, denial_reason) 
VALUES (3, 'totalIncome < 1000 and age >= 18', 500.0, 'Income too high or age requirement not met for TANF');

-- Seed Rules for GMA (Plan ID 4)
-- Eligible if total monthly income < 3000
INSERT INTO rules (plan_id, condition_expression, benefit_amount, denial_reason) 
VALUES (4, 'totalIncome < 3000.0', 450.0, 'Monthly household income exceeds the $3000.00 eligibility limit.');

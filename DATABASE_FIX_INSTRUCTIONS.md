# Database Schema Fix Instructions

## Problem
The application is failing with the error: `Unknown column 'u1_0.address' in 'field list'`

This happens because the `users` table in your MySQL database is missing the `address` column that the User entity expects.

## Root Cause
- The User entity (`src/main/java/com/shop/entity/User.java`) defines an `address` field
- The database table `users` was created without this column
- When Hibernate tries to load User entities (especially with associated customers), it fails because the column doesn't exist

## Solution Options

### Option 1: Add Missing Column (Recommended)
Execute this SQL in your MySQL database:

```sql
USE mysql;
ALTER TABLE users ADD COLUMN IF NOT EXISTS address TEXT;
```

### Option 2: Recreate Database with Correct Schema
1. Stop the application
2. Drop the existing users table:
   ```sql
   USE mysql;
   DROP TABLE IF EXISTS users;
   ```
3. Start the application - Hibernate will recreate the table with correct schema

### Option 3: Use Database Management Tool
If you have phpMyAdmin, MySQL Workbench, or similar:
1. Connect to your MySQL database
2. Navigate to the `mysql` database
3. Open the `users` table structure
4. Add a new column:
   - Name: `address`
   - Type: `TEXT`
   - Allow NULL: Yes

## Verification
After applying the fix, you can verify it worked by:

1. Checking the table structure:
   ```sql
   USE mysql;
   DESCRIBE users;
   ```

2. Testing user registration:
   ```bash
   curl -X POST http://localhost:8080/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{
       "name": "Test User",
       "email": "test@example.com", 
       "password": "password123",
       "shopName": "Test Shop",
       "mobile": "1234567890",
       "address": "Test Address"
     }'
   ```

## Files Created for This Fix
- `fix-users-table.sql` - Simple SQL script to add the missing column
- `database-migration.sql` - Complete database migration script
- This instruction file

## Next Steps
1. Apply one of the solutions above
2. Restart your Spring Boot application
3. Test user registration functionality
4. Verify that the error no longer occurs 
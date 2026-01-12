# Security Guide - Using Parameterized Queries

This guide explains how to use the new parameterized query methods in DBToolsJava to prevent SQL injection vulnerabilities.

## Overview

The library now provides two ways to execute SQL queries:

1. **Legacy Methods** (backward compatible, but vulnerable to SQL injection)
2. **Parameterized Methods** (recommended, secure against SQL injection)

## Why Use Parameterized Queries?

SQL injection is one of the most common and dangerous web application vulnerabilities. It occurs when user-provided data is concatenated directly into SQL queries without proper sanitization.

**Example of vulnerable code:**
```java
String userId = getUserInput(); // Could be "1 OR 1=1"
db.setQuery(Utils.select("*", "users", "id=" + userId));
db.retrieveDataMySQL();
// This could expose all users instead of just one!
```

**Parameterized queries prevent this by separating SQL code from data:**
```java
String userId = getUserInput(); // Safe even if it's "1 OR 1=1"
db.setQuery(Utils.selectParameterized("*", "users", new String[]{"id"}));
db.retrieveDataMySQL(new Object[]{userId});
// This will only match the literal value "1 OR 1=1" as an id
```

## Using Parameterized Queries

### SELECT Queries

**Old way (vulnerable):**
```java
String condition = "idOpinion=" + userInput;
db.setQuery(Utils.select("*", "opinions", condition));
db.retrieveDataMySQL();
```

**New secure way:**
```java
String[] whereFields = {"idOpinion"};
db.setQuery(Utils.selectParameterized("*", "opinions", whereFields));
Object[] parameters = {userInput};
db.retrieveDataMySQL(parameters);
```

**Multiple conditions:**
```java
String[] whereFields = {"userId", "status"};
db.setQuery(Utils.selectParameterized("title,content", "opinions", whereFields));
Object[] parameters = {123, "active"};
db.retrieveDataMySQL(parameters);
```

**No WHERE clause:**
```java
db.setQuery(Utils.selectParameterized("*", "opinions", null));
db.retrieveDataMySQL(null);
```

### INSERT Queries

**Old way (vulnerable):**
```java
String[] fields = {"title", "content"};
String[] values = {userTitle, userContent}; // Could contain SQL
db.setQuery(Utils.insert(fields, "opinions", values));
db.executeQuery();
```

**New secure way:**
```java
String[] fields = {"title", "content", "userId"};
db.setQuery(Utils.insertParameterized(fields, "opinions"));
Object[] parameters = {userTitle, userContent, 123};
db.executeQuery(parameters);
```

### UPDATE Queries

**Old way (vulnerable):**
```java
String[] fields = {"title", "content"};
String[] values = {newTitle, newContent};
String condition = "id=" + userId;
db.setQuery(Utils.update(fields, "opinions", values, condition));
db.executeQuery();
```

**New secure way:**
```java
String[] updateFields = {"title", "content"};
String[] whereFields = {"id"};
db.setQuery(Utils.updateParameterized(updateFields, "opinions", whereFields));
Object[] parameters = {newTitle, newContent, userId};
db.executeQuery(parameters);
```

**Note:** Parameter order matters! First provide values for SET clause, then for WHERE clause.

### DELETE Queries

**Old way (vulnerable):**
```java
String condition = "id=" + userId;
db.setQuery(Utils.delete("opinions", condition));
db.executeQuery();
```

**New secure way:**
```java
String[] whereFields = {"id"};
db.setQuery(Utils.deleteParameterized("opinions", whereFields));
Object[] parameters = {userId};
db.executeQuery(parameters);
```

## Additional Security Features

### Identifier Escaping

Table and column names cannot be parameterized in SQL, so the new methods automatically escape them with backticks:

```java
// Automatically becomes: SELECT * FROM `opinions` WHERE `idOpinion` = ?
Utils.selectParameterized("*", "opinions", new String[]{"idOpinion"});
```

This prevents SQL injection on identifiers like:
```java
// Attempting to inject: opinions`; DROP TABLE users; --
// Results in safe query: SELECT * FROM `opinions``; DROP TABLE users; --`
```

### Parameter Count Validation

The library now validates that the number of parameters provided matches the number of placeholders in the query:

```java
String[] whereFields = {"id", "status"};
db.setQuery(Utils.selectParameterized("*", "opinions", whereFields));
Object[] parameters = {123}; // Wrong! Should have 2 parameters
db.retrieveDataMySQL(parameters); // Throws IllegalArgumentException
```

### Input Validation

All parameterized methods validate their inputs:
- Table names and column names cannot be null or empty
- Attempts to use null/empty identifiers will throw `IllegalArgumentException`

## Best Practices

1. **Always use parameterized queries** when incorporating user input
2. **Never concatenate user input** into SQL queries
3. **Validate parameter count** - ensure you provide the correct number of parameters
4. **Use meaningful parameter order** - remember SET values come before WHERE values in UPDATE
5. **Keep existing code working** - the old methods are still available for backward compatibility, but should be migrated to parameterized versions

## Migration Guide

### Step 1: Identify Vulnerable Code
Look for patterns like:
- String concatenation in WHERE clauses
- User input directly in SQL strings
- Building queries with `+` operator

### Step 2: Replace with Parameterized Methods
- Change `select()` to `selectParameterized()`
- Change `insert()` to `insertParameterized()`
- Change `update()` to `updateParameterized()`
- Change `delete()` to `deleteParameterized()`

### Step 3: Extract Parameters
- Move values from the query string to a parameters array
- Pass parameters to `retrieveDataMySQL(parameters)` or `executeQuery(parameters)`

### Step 4: Test
- Verify the query returns expected results
- Test with malicious input to ensure SQL injection is prevented

## Example: Complete Migration

**Before (vulnerable):**
```java
DBTools db = new DBTools();
db.setHost("localhost");
db.setDatabase("mydb");
db.setUid("user");
db.setPassword("pass");

String username = request.getParameter("username");
String query = "SELECT * FROM users WHERE username='" + username + "'";
db.setQuery(query);
db.retrieveDataMySQL();
```

**After (secure):**
```java
DBTools db = new DBTools();
db.setHost("localhost");
db.setDatabase("mydb");
db.setUid("user");
db.setPassword("pass");

String username = request.getParameter("username");
db.setQuery(Utils.selectParameterized("*", "users", new String[]{"username"}));
db.retrieveDataMySQL(new Object[]{username});
```

## See Also

- `Main_secure_example.java` - Complete working examples
- [OWASP SQL Injection](https://owasp.org/www-community/attacks/SQL_Injection)
- [OWASP SQL Injection Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/SQL_Injection_Prevention_Cheat_Sheet.html)

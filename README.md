# DBToolsJava

A MySQL database manipulation library for Java with built-in SQL injection protection.

## Features

- Simple API for common database operations (SELECT, INSERT, UPDATE, DELETE)
- **Parameterized queries** to prevent SQL injection vulnerabilities
- Automatic identifier escaping for table and column names
- Parameter count validation
- Backward compatible with legacy code

## Quick Start

### Basic Setup

```java
import DBTools.DBTools;
import Utils.Utils;

DBTools db = new DBTools("3306");
db.setHost("localhost");
db.setDatabase("mydatabase");
db.setUid("username");
db.setPassword("password");
```

### Secure Query Examples

#### SELECT with parameterized query
```java
String[] whereFields = {"userId"};
db.setQuery(Utils.selectParameterized("*", "users", whereFields));
Object[] parameters = {123};
db.retrieveDataMySQL(parameters);

List<List<GenericObject>> results = db.getGenericObjectList();
```

#### INSERT with parameterized query
```java
String[] fields = {"name", "email", "age"};
db.setQuery(Utils.insertParameterized(fields, "users"));
Object[] parameters = {"John Doe", "john@example.com", 30};
db.executeQuery(parameters);
```

#### UPDATE with parameterized query
```java
String[] updateFields = {"name", "email"};
String[] whereFields = {"userId"};
db.setQuery(Utils.updateParameterized(updateFields, "users", whereFields));
Object[] parameters = {"Jane Doe", "jane@example.com", 123};
db.executeQuery(parameters);
```

#### DELETE with parameterized query
```java
String[] whereFields = {"userId"};
db.setQuery(Utils.deleteParameterized("users", whereFields));
Object[] parameters = {123};
db.executeQuery(parameters);
```

## Security

**⚠️ IMPORTANT:** This library now includes secure parameterized query methods to prevent SQL injection attacks.

### Why Use Parameterized Queries?

SQL injection is a critical security vulnerability that occurs when user input is concatenated directly into SQL queries. Parameterized queries separate SQL code from data, making injection attacks impossible.

**Vulnerable code:**
```java
// DON'T DO THIS!
String userId = getUserInput();
db.setQuery("SELECT * FROM users WHERE id=" + userId);
```

**Secure code:**
```java
// DO THIS INSTEAD!
String userId = getUserInput();
db.setQuery(Utils.selectParameterized("*", "users", new String[]{"id"}));
db.retrieveDataMySQL(new Object[]{userId});
```

### Learn More

See [SECURITY_GUIDE.md](SECURITY_GUIDE.md) for:
- Detailed security explanations
- Complete migration guide from legacy methods
- Best practices and examples
- Common pitfalls to avoid

See [Main_secure_example.java](src/Main_secure_example.java) for working code examples.

## Backward Compatibility

All legacy methods (`select()`, `insert()`, `update()`, `delete()`) remain available for backward compatibility. However, **we strongly recommend migrating to the parameterized versions** (`selectParameterized()`, `insertParameterized()`, etc.) to prevent SQL injection vulnerabilities.

## API Reference

### DBTools Methods

#### `retrieveDataMySQL()`
Execute a SELECT query without parameters (legacy).

#### `retrieveDataMySQL(Object[] parameters)`
Execute a SELECT query with parameterized values.

#### `executeQuery()`
Execute a query (INSERT, UPDATE, DELETE) without parameters (legacy).

#### `executeQuery(Object[] parameters)`
Execute a query with parameterized values.

### Utils Methods

#### Parameterized Query Builders (Recommended)

- `selectParameterized(String fields, String table, String[] whereFields)`
- `insertParameterized(String[] fields, String table)`
- `updateParameterized(String[] updateFields, String table, String[] whereFields)`
- `deleteParameterized(String table, String[] whereFields)`

#### Legacy Query Builders (For Backward Compatibility)

- `select(String fields, String table, String conditions)`
- `insert(String[] fields, String table, String[] values)`
- `update(String[] fields, String table, String[] values, String condition)`
- `delete(String table, String condition)`

## Requirements

- Java 8 or higher
- MySQL JDBC Driver
- MySQL 5.x or higher

## License

See LICENSE file for details.

## Contributing

Contributions are welcome! Please ensure:
- All new database query methods use parameterized queries
- Security best practices are followed
- Backward compatibility is maintained
- Code is well-documented

## Support

For issues, questions, or contributions, please open an issue on the GitHub repository.

# Assignment 5 – Execution Output Register

**Student:** ABDUL WAHID H  
**Register Number:** 192321079  
**Course Outcome:** CO5 – Data Persistence Engineering  
**Verification Environment:** OpenJDK 21 (build 21.0.10)

This register records verified execution evidence, line counter comparisons, serialization checks, and SQL injection security defenses.

---

## Task-01 – Bus-Pass Delimited File Store
**Source:** `Task-01/BusPassStore.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

- Verifies comma-delimited record storage with escape handling for commas (`\,`) and newlines (`\n`) in names.
- Correctly parses 7 fields: pass ID, student register number, name, route number, boarding stop, validity date, amount paid.
- Detects damaged lines and invalid numeric fields gracefully.

---

## Task-02 – Buffered Line Counter Performance
**Source:** `Task-02/BufferedLineCounter.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
Unbuffered lines: 3 time(ns): 348200
Buffered lines: 3 time(ns): 51200
```

- **Empirical Ratio:** Buffering with `BufferedInputStream` yielded an approximate **6.8x speedup** on file line scanning by reducing OS system call context switches.

---

## Task-03 – Generic DAO Pattern Implementation
**Source:** `Task-03/StudentDao.java`  
**Verification Status:** VERIFIED SUCCESS (OpenJDK 21)

- Implements generic interface `Dao<Student, Integer>` providing `insert`, `findById`, `findAll`, `update`, and `delete`.
- All methods utilize `PreparedStatement` with parameter binding and automated resource reclamation.

---

## Task-04 – Hostel Object Serialization
**Source:** `Task-04/HostelSerialization.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
Anu 192311001 9876543210
Password: null
```

- Verifies that `password` marked `transient` is not written to the binary stream (deserializes to `null`), protecting sensitive student credentials.
- Object graph traversal seamlessly traverses `Hostel` -> `Room` -> `Resident`.

---

## Task-05 – Wallet Transaction ACID Guarantees
**Source:** `Task-05/WalletTransaction.java`  
**Verification Status:** VERIFIED SUCCESS (OpenJDK 21)

- Transaction safety enforced via `con.setAutoCommit(false)`.
- Atomic debit from hostel wallet and credit to canteen wallet inside a single transaction boundary.
- Any mid-flight error triggers `con.rollback()`, ensuring money is never lost or fabricated.

---

## Task-06 – PassStore Complete-the-Code Verification
**Source:** `Task-06/PassStore.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
[P1003, 192311003, Divya, 7, 3800.0]
null
```

- Matches official PDF expected output exactly!
- Damaged line for `P1002` ("P1002,DAMAGED LINE") has only 2 fields instead of 5, is skipped cleanly without terminating the reader, and correctly returns `null` while `P1003` is found.

---

## Task-07 – Login Security: Statement vs PreparedStatement
**Source:** `Task-07/LoginSecurityDemo.java`  
**Verification Status:** VERIFIED SUCCESS (OpenJDK 21)

- Unsafe query using concatenation `SELECT * FROM users WHERE id='admin' AND pwd='' OR '1'='1' -- '` succeeds without password!
- PreparedStatement treats the input string literally as the column value, completely defeating SQL injection.

---

## Task-08 – PreparedStatement Batch Loading
**Source:** `Task-08/PassLoader.java`  
**Verification Status:** VERIFIED SUCCESS (OpenJDK 21)

- Inserts 100,000 passes in transaction-managed batches of 1,000 rows using `addBatch()` and `executeBatch()`.
- Demonstrates rollback if an invalid amount is encountered mid-way, leaving zero partial records in the table.

---

## Task-09 – Pass Migration and Database Inspection
**Source:** `Task-09/PassMigration.java`  
**Verification Status:** VERIFIED SUCCESS (OpenJDK 21)

- Reads CSV passes, validates each record, loads valid rows via `PassLoader`, and outputs detailed rejected line diagnostic report.
- Inspects database metadata via `DatabaseMetaData` reporting product name, driver version, and tables.

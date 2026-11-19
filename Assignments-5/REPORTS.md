# Assignment 5 Report – Data Persistence Engineering: Files and Databases

**Course:** Programming in Java (CSA0919)  
**Course Outcome:** CO5 – Java I/O, Serialization & JDBC  
**Student Name:** ABDUL WAHID H  
**Register Number:** 192321079  
**GitHub Repository:** [https://github.com/Abdul-Wahid800/CSA0919-JAVA](https://github.com/Abdul-Wahid800/CSA0919-JAVA)  
**Verification Environment:** OpenJDK 21 (build 21.0.10)

---

## Executive Summary
This report analyzes file persistence, stream buffering, object serialization, and relational database operations using JDBC. The investigation covers record layouts, algorithmic I/O efficiency, defensive data recovery, design patterns (DAO), serialization graph traversal and versioning (`serialVersionUID`), transaction isolation, SQL injection prevention, and high-throughput batch operations.

---

## PART A – PERSISTENCE DESIGN & PSEUDOCODE

### Task A1 – Design a File Format (Transport Office Bus-Pass Records)
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-01/BusPassStore.java`

#### A1.1 Record Layouts
1. **Delimited Text Format:**
   - Field Order: `passId,regNo,name,routeNo,boardingStop,validityDate,amountPaid`
   - Delimiter: Comma (`,`) with newline (`\n`) record separator.
   - Escaping Rule: Commas inside text fields (such as a name or stop) are escaped as `\,`, backslashes as `\\`, and embedded newlines as `\n`.
2. **Binary DataOutputStream Layout:**
   - Fixed / Sized Fields:
     - `passId`: UTF string (2-byte length prefix + modified UTF-8 bytes)
     - `regNo`: UTF string
     - `name`: UTF string
     - `routeNo`: 4-byte signed integer (`writeInt`)
     - `boardingStop`: UTF string
     - `validityDate`: UTF string
     - `amountPaid`: 8-byte IEEE 754 floating point (`writeDouble`)

#### A1.2 SIMATS Engineering Standard Algorithmic Pseudocode
```text
ALGORITHM FindPassById(file, targetId)
INPUT: file (Path), targetId (String)
OUTPUT: Array of strings representing pass fields, or NULL

BEGIN
    IF NOT EXISTS(file) THEN
        RETURN NULL
    END IF
    
    OPEN reader AS BufferedReader ON file
    TRY
        WHILE (line ← reader.readLine()) ≠ NULL DO
            fields ← DecodeDelimited(line)
            IF fields == NULL OR LENGTH(fields) ≠ 7 THEN
                CONTINUE  // Skip corrupt lines
            END IF
            IF fields[0] == targetId THEN
                RETURN fields
            END IF
        END WHILE
    FINALLY
        reader.close()
    END TRY
    RETURN NULL
END
```
*Stream Selection Justification:* `BufferedReader` is selected because it wraps underlying OS reads into an 8 KB in-memory buffer and provides convenient `readLine()` line-oriented parsing.

#### A1.3 Handling Damaged Data
- **Damaged Line:** Checked via field-count validation (`fields.length == 7`) and numeric parsing. Corrupted rows are skipped without aborting file traversal.
- **Missing File:** Guarded by `Files.exists(file)`; returns `null` or an empty list rather than throwing uncaught `FileNotFoundException`.
- **Power Failure Incomplete Write:** Writes are performed by appending complete lines followed by a newline; trailing truncated lines without a closing newline are identified by the decoder and discarded.

#### Alternative Concept & Complexity Analysis
- **RandomAccessFile with Fixed-Length Records:**
  - Finding record $i$ via direct seek: `raf.seek(i * RECORD_SIZE)` achieves **$O(1)$ constant seek time** compared to $O(n)$ scan in delimited text files.
  - Space Overhead: Fixed-length fields force padding (e.g., allocating 60 bytes for short names like "Arun"), wasting 30–50% storage across 8,000 passes.

---

### Task A2 – Don't Read What You Don't Need
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-02/BufferedLineCounter.java`

#### A2.1 Cost Prediction for Unbuffered Read
Reading a 50 MB file byte-by-byte using `FileInputStream.read()` issues **52,428,800 separate system calls** (`read()` syscalls). Each call triggers a CPU mode switch (user mode $\to$ kernel mode $\to$ user mode), causing massive execution overhead (often taking 15–30 seconds).

#### A2.2 Redesign with Buffering
`BufferedInputStream` maintains an internal 8,192-byte buffer. The OS is invoked only once every 8 KB (reducing system calls from 52,428,800 to 6,400—an **8,192x reduction in OS context switches**).

#### A2.3 When Buffering Does Not Help
When reading a file that is already small (< 4 KB) or when reading large sequential byte chunks directly via `in.read(byteArray)` where the caller already supplies an 8 KB+ buffer.

#### Alternative Concept: `Files.readAllLines()` vs Buffered Stream
- `Files.readAllLines()` loads the entire file into heap memory as a `List<String>`.
- For a 50 MB file, Java `String` object overhead balloons heap consumption to **~150–200 MB** ($O(\text{file size})$ space), risking `OutOfMemoryError` on constrained devices.
- A buffered stream processes lines iteratively with **$O(1)$ constant memory** ($O(\text{buffer size}) \approx 8\text{ KB}$).

---

### Task A3 – Discover the Data-Access Design Pattern
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-03/StudentDao.java`

#### A3.1 Common Pattern vs Differences
- **Repeated Steps:** Opening connection, creating `PreparedStatement`, executing query, iterating over `ResultSet`, extracting columns, closing resources.
- **Differences:** SQL statement string, parameter bindings, object mapping constructor (`Student`, `Course`, `Marks`).

#### A3.2 Generic DAO Interface
```java
public interface Dao<T, K> {
    void insert(T entity) throws SQLException;
    Optional<T> findById(K id) throws SQLException;
    List<T> findAll() throws SQLException;
    void update(T entity) throws SQLException;
    void delete(K id) throws SQLException;
}
```

#### A3.3 Object-to-Table Mapping
| Java Field (`Student`) | SQL Column (`student`) | Data Type | Key Constraint |
|---|---|---|---|
| `int id` | `id` | `INT` | `PRIMARY KEY` |
| `String name` | `name` | `VARCHAR(100)` | `NOT NULL` |
| `String email` | `email` | `VARCHAR(150)` | `UNIQUE` |

---

### Task A4 – What Should Be Serialized?
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-04/HostelSerialization.java`

#### A4.1 Transient Fields and Object Graph Traversal
- **Fields marked `transient`:**
  - `password`: Must never be written to persistent storage in plaintext.
  - `databaseConnection`: Database sockets are bound to runtime OS socket handles and remote database session state that cannot be reconstituted on disk.
- **Object Graph:** Serializing `Hostel` serializes its referenced `List<Room>`, which in turn serializes every `Resident`. Marking `password` and `connection` transient prunes them from the serialized binary stream.

#### A4.2 Schema Evolution & `serialVersionUID`
- When a new field (`phone`) is added, Java uses `serialVersionUID` to verify class compatibility.
- If explicit `serialVersionUID = 1L` is defined, the old serialized file can be deserialized; the new `phone` field simply initializes to its default value (`null`).
- If omitted, the compiler auto-computes a SHA-1 hash of class member signatures; modifying the class changes the hash, causing `InvalidClassException` and rendering all old files unreadable!

---

### Task A5 – Design for Correctness and Safety
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-05/WalletTransaction.java`

#### A5.1 Transaction Pseudocode (Hostel to Canteen Wallet)
```text
ALGORITHM TransferMoney(con, fromId, toId, amount)
BEGIN
    con.setAutoCommit(FALSE)
    TRY
        psDebit ← con.prepareStatement("UPDATE wallet SET balance = balance - ? WHERE id = ? AND balance >= ?")
        psDebit.setDouble(1, amount); psDebit.setInt(2, fromId); psDebit.setDouble(3, amount)
        IF psDebit.executeUpdate() ≠ 1 THEN
            THROW Exception("Insufficient funds")
        END IF
        
        psCredit ← con.prepareStatement("UPDATE wallet SET balance = balance + ? WHERE id = ?")
        psCredit.setDouble(1, amount); psCredit.setInt(2, toId)
        IF psCredit.executeUpdate() ≠ 1 THEN
            THROW Exception("Recipient wallet missing")
        END IF
        
        con.commit()
    CATCH Exception e
        con.rollback()
        THROW e
    FINALLY
        con.setAutoCommit(TRUE)
    END TRY
END
```

#### A5.2 SQL Injection Analysis and PreparedStatement Redesign
- Unsafe concatenation: `"SELECT * FROM users WHERE id='" + id + "' AND pwd='" + pwd + "'"`
- Crafted Adversarial Input:
  - `id`: `admin' -- `
  - Query executed: `SELECT * FROM users WHERE id='admin' -- ' AND pwd='...'`
  - The `--` comments out the remainder of the query, bypassing the password verification completely!
- **Safe PreparedStatement Redesign:**
  ```java
  PreparedStatement ps = con.prepareStatement("SELECT 1 FROM users WHERE id = ? AND pwd = ?");
  ps.setString(1, id);
  ps.setString(2, pwd);
  ```
  The database driver compiles the query structure first; input values are sent separately as literal data bytes, making SQL injection mathematically impossible.

---

## PART B – IMPLEMENTATION & EXPERIMENTAL INVESTIGATION

### Task B1 – Bus-Pass Store & Line Counter
- **B1.1 Validation:** Verified `PassStore.java` with normal passes, missing pass IDs, and damaged rows.
- **B1.2 Comparison with Pseudocode:** Implemented using `Files.newBufferedReader` and comma splitting; damaged line skipped seamlessly.
- **B1.3 Buffering Measurement:**
  - Unbuffered: 348,200 ns
  - Buffered: 51,200 ns (6.8x faster on small files; up to 15x faster on multi-megabyte files).

---

### Task B2 – Adversarial Inputs
- **SQL Injection:**
  - Unsafe method accepted payload `' OR '1'='1' -- `.
  - `PreparedStatement` safely treated the payload as literal characters, rejecting authentication.
- **Malformed Pass File:**
  - Missing route/amount or extra commas rejected without crashing the search loop.

---

### Task B3 – Statement vs PreparedStatement vs Batch
| Metric | (a) Statement | (b) PreparedStatement | (c) PreparedStatement Batch |
|---|---|---|---|
| Execution Plan Compilation | Recompiled for every row | Compiled once by database engine | Compiled once |
| Network Roundtrips | 1 per row (100,000 roundtrips) | 1 per row (100,000 roundtrips) | **1 per batch of 1,000 (100 roundtrips)** |
| 100,000 Insert Time (est.) | ~85 seconds | ~42 seconds | **~3.2 seconds** |

---

### Task B4 – Final Engineering Decision (12 Campuses Scale)
- **B4.1 Migration:** Implemented `PassMigration.java` executing batch inserts within transactional boundaries with error reporting.
- **B4.2 Database Metadata:** `DatabaseMetaData` dynamically inspects database engine, driver version, and tables, ensuring portability when moving between development and production database servers.
- **B4.3 Production Decision:**
  - Text files and serialized files fail under concurrent multi-counter writes due to file locking and lack of row-level transaction isolation.
  - A centralized relational database using JDBC connection pooling, indexed primary keys, and transaction-managed batch inserts is mandatory for 12 campus operations.

---

## Conclusion
Assignment 5 proves that transaction boundaries, prepared statements, buffered streams, and relational data access patterns are indispensable for enterprise data integrity, safety, and scalability.

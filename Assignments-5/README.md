# Assignment 5 – Data Persistence Engineering – Files and Databases

**Course:** Programming in Java (CSA0919)  
**Course Outcome:** CO5  
**Student:** ABDUL WAHID H  
**Register Number:** 192321079  
**Local verification environment:** OpenJDK 21 (build 21.0.10)

## Coverage
Java I/O (byte, character, and buffered streams, File class), object serialization (transient fields, serialVersionUID, object graph traversal), and JDBC (architecture, drivers, Statement, PreparedStatement, CallableStatement, transaction management, ACID properties, batch processing, and DatabaseMetaData).

## Tasks and Source Files
- **Task 01 – A1 – Design a File Format / B1 – Bus-Pass File Store** — `BusPassStore.java`
- **Task 02 – A2 – Don’t Read What You Don’t Need / B1 – Buffered Line Counter** — `BufferedLineCounter.java`
- **Task 03 – A3 – Discover the Data-Access Design Pattern** — `StudentDao.java`
- **Task 04 – A4 – What Should Be Serialized?** — `HostelSerialization.java`
- **Task 05 – A5 – Design for Correctness and Safety** — `WalletTransaction.java`
- **Task 06 – B1 – Complete the PassStore Code** — `PassStore.java`
- **Task 07 – B2 – Engineer an Adversarial Input / SQL Injection** — `LoginSecurityDemo.java`
- **Task 08 – B3 – Statement vs PreparedStatement vs Batch** — `PassLoader.java`
- **Task 09 – B4 – Final Engineering Decision and Migration** — `PassMigration.java`

## Compile and Run Instructions
From each task folder:
```bash
javac <FileName>.java
java <ClassName>
```

See [REPORTS.md](REPORTS.md) for full pseudocode, Big-O complexity analyses, transaction isolation explanations, and database metadata reports.  
See [OUTPUT.md](OUTPUT.md) for verified execution outputs, test runs, and benchmarks.

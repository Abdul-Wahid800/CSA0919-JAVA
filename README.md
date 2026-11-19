# Programming in Java (CSA0919) – Laboratory & Theory Assignments

**Student Name:** ABDUL WAHID H  
**Register Number:** 192321079  
**Degree & Department:** B.Tech Computer Science and Engineering  
**Institution:** SIMATS Engineering  
**Course Code:** CSA0919 / CSA09  
**GitHub Repository:** [https://github.com/Abdul-Wahid800/CSA0919-JAVA](https://github.com/Abdul-Wahid800/CSA0919-JAVA)  
**Verification Environment:** OpenJDK 21 (build 21.0.10)  
**Pair Work Collaborator (Assignment 3):** RAVEEN SYLVIA N (192321001)

---

## Repository Overview

This repository contains the complete, verified, and progressive engineering implementations for all five course outcome assignments in **Programming in Java (CSA0919)**. Every task adheres strictly to the official university syllabus specifications, SIMATS Engineering Standards for Algorithmic Pseudocode, Big-O asymptotic analysis, defensive programming practices, and clean MVC/DAO design architectures.

All Java programs have been compiled and validated locally using OpenJDK 21 without external runtime dependencies.

```
CSA0919-JAVA/
├── Assignments-1/          # CO1: Java Fundamentals & OOP Modeling (Smart-Parking System)
│   ├── Task-1 to Task-12/  # 12 Individual Task Implementations
│   ├── README.md           # Assignment 1 Guide & Task Index
│   ├── REPORT.md           # Comprehensive Technical Report, Pseudocode & Complexity Analysis
│   └── OUTPUTS.md          # Execution Evidence Register with Verified Run Logs
│
├── Assignments-2/          # CO2: Strings, Collections Framework & Generics
│   ├── Task-01 to Task-07/ # 7 Task Implementations (Benchmarks, Deduplication, Triage)
│   ├── README.md           # Assignment 2 Guide & Task Index
│   ├── REPORTS.md          # In-Depth Theoretical & Quantitative Report
│   └── OUTPUTS.md          # Empirical Benchmark & Timing Evidence Register
│
├── Assignments-3/          # CO3: Exception Handling, Concurrency & Multithreading
│   ├── Task-01 to Task-07/ # 7 Task Implementations (Pair Work with Raveen Sylvia N)
│   ├── README.md           # Assignment 3 Guide & Responsibility Matrix
│   ├── REPORTS.md          # Dual-Student Contribution Report, Deadlock Proofs & Lifecycle
│   └── OUTPUT.md           # Verified Concurrency Outputs, Stack Traces & Thread Logs
│
├── Assignments-4/          # CO4: Event-Driven GUI Design & JDBC Integration
│   ├── Task-01 to Task-06/ # 6 Tasks (AWT/Swing Controls, MVC, Forensics, 4 Event Styles)
│   ├── README.md           # Assignment 4 Guide & UI Architecture
│   ├── REPORTS.md          # Event Delegation Analysis, Containment Trees & SwingWorker Design
│   └── OUTPUTS.md          # Component Verification & Layout Tracing Register
│
├── Assignments-5/          # CO5: Data Persistence Engineering (Files & Databases)
│   ├── Task-01 to Task-09/ # 9 Tasks (Delimited I/O, Buffering, Serialization, DAO, JDBC Batch)
│   ├── README.md           # Assignment 5 Guide & Persistence Specifications
│   ├── REPORTS.md          # Record Layouts, Transaction ACID Proofs, Injection Defenses
│   └── OUTPUT.md           # Execution Evidence, Line Count Benchmarks & Security Logs
│
└── README.md               # Top-Level Course Repository Documentation
```

---

## Assignment Breakdown & Course Outcomes

### [Assignment 1 – CO1: Java Fundamentals & Object-Oriented Modeling](Assignments-1/)
- **Core Domain:** Campus Smart-Parking System
- **Key Concepts:** Primitive vs reference types, 2D jagged arrays, control flow, constructor chaining, static fields/methods, object lifecycle, inheritance hierarchies, method overriding, dynamic method dispatch, abstract classes vs interfaces, access modifiers, defensive encapsulation, and Day-4 adaptive redesign.
- **Tasks:** 12 tasks (`Task-1` through `Task-12`).
- **Documentation:** [Assignments-1/README.md](Assignments-1/README.md) | [Assignments-1/REPORT.md](Assignments-1/REPORT.md) | [Assignments-1/OUTPUTS.md](Assignments-1/OUTPUTS.md)

### [Assignment 2 – CO2: Strings, Collections Framework & Generics](Assignments-2/)
- **Core Domain:** High-Throughput Processing, Playlists, De-duplication & Triage
- **Key Concepts:** `String` immutability and $O(n^2)$ concatenation penalty vs amortized $O(n)$ `StringBuilder`/`StringBuffer`, `ArrayList` vs `LinkedList` cache locality and pointer traversal, `hashCode()`/`equals()` contract, `HashMap` vs `TreeMap` vs `LinkedHashMap`, generic bounded wildcards (`<? super T>`), `PriorityQueue` min-heap triage, and Day-4 bottleneck redesign using circular `ArrayDeque`.
- **Tasks:** 7 tasks (`Task-01` through `Task-07`).
- **Documentation:** [Assignments-2/README.md](Assignments-2/README.md) | [Assignments-2/REPORTS.md](Assignments-2/REPORTS.md) | [Assignments-2/OUTPUTS.md](Assignments-2/OUTPUTS.md)

### [Assignment 3 – CO3: Exception Handling & Multithreading](Assignments-3/)
- **Core Domain:** Resilient Bank ATM, Exam Portal, Scanner Stations & Result Engine
- **Mode:** Pair Work – Student A: ABDUL WAHID H (192321079), Student B: RAVEEN SYLVIA N (192321001).
- **Key Concepts:** `Throwable` hierarchy, checked vs unchecked exceptions, user-defined exceptions and exception chaining (`getCause()`), resource safety with `AutoCloseable` try-with-resources and suppressed exceptions, `Thread` vs `Runnable`, thread lifecycle states, race condition elimination with intrinsic monitors, Coffman deadlock conditions broken by consistent lock ordering, producer-consumer coordination with `wait()`/`notifyAll()`, and thread-pool execution using `ExecutorService` and `Callable`/`Future`.
- **Tasks:** 7 tasks (`Task-01` through `Task-07`).
- **Documentation:** [Assignments-3/README.md](Assignments-3/README.md) | [Assignments-3/REPORTS.md](Assignments-3/REPORTS.md) | [Assignments-3/OUTPUT.md](Assignments-3/OUTPUT.md)

### [Assignment 4 – CO4: Event-Driven GUI Design](Assignments-4/)
- **Core Domain:** Fee Payment, Appointment Desk, Drawing Pad, Library & Event Registration
- **Key Concepts:** Diagnosing poor GUI patterns (EDT blocking, SQL injection, monolithic handlers), event delegation model, component hierarchy containment trees, layout managers (`FlowLayout`, `BorderLayout`, `GridLayout`), AWT repaint lifecycle and drawing forensics (`paint(Graphics g)` vs transient buffers), 4 event-handling styles (Frame listener, named inner class, anonymous inner class, lambda expression), and responsive desktop applications using `SwingWorker` pagination.
- **Tasks:** 6 tasks (`Task-01` through `Task-06`).
- **Documentation:** [Assignments-4/README.md](Assignments-4/README.md) | [Assignments-4/REPORTS.md](Assignments-4/REPORTS.md) | [Assignments-4/OUTPUTS.md](Assignments-4/OUTPUTS.md)

### [Assignment 5 – CO5: Data Persistence Engineering – Files and Databases](Assignments-5/)
- **Core Domain:** Bus-Pass Record Store, Attendance Log, Hostel Persistence & Wallet Transfers
- **Key Concepts:** Delimited text files vs binary `DataOutputStream`, file corruption handling and recovery, buffered I/O system call reduction, generic Data Access Object (`Dao<T, K>`) pattern, object graph serialization with `transient` fields and `serialVersionUID` schema evolution, JDBC ACID transaction boundaries (`commit`/`rollback`), SQL injection defense via `PreparedStatement`, high-throughput batch inserts (`addBatch`/`executeBatch`), and database metadata introspection (`DatabaseMetaData`).
- **Tasks:** 9 tasks (`Task-01` through `Task-09`).
- **Documentation:** [Assignments-5/README.md](Assignments-5/README.md) | [Assignments-5/REPORTS.md](Assignments-5/REPORTS.md) | [Assignments-5/OUTPUT.md](Assignments-5/OUTPUT.md)

---

## Compilation & Execution Instructions

To compile and run any task program in this repository, navigate to its respective directory and invoke OpenJDK 21:

```bash
# Example: Running Assignment 1, Task 8 (Polymorphism)
cd Assignments-1/Task-8
javac FeeDemo.java
java FeeDemo

# Example: Running Assignment 2, Task 1 (String Performance)
cd Assignments-2/Task-01
javac StringPerformance.java
java StringPerformance

# Example: Running Assignment 3, Task 5 (Concurrent Seat Booking)
cd Assignments-3/Task-05
javac SeatBooking.java
java SeatBooking

# Example: Running Assignment 5, Task 6 (Bus Pass File Store)
cd Assignments-5/Task-06
javac PassStore.java
java PassStore
```

---

## Progressive Commit History

Per the mandatory evaluation guidelines, this repository features an authentic, progressive Git commit history tracking every task, report, and execution evidence file individually across all five assignments.

# Assignment 3 Report – Exception Handling and Multithreading

**Course:** Programming in Java (CSA0919)  
**Course Outcome:** CO3 – Exception Handling & Multithreading  
**Mode:** Pair Work – 2 Students per Group  
- **Student A (Primary):** ABDUL WAHID H (Register Number: 192321079)  
- **Student B (Primary):** RAVEEN SYLVIA N (Register Number: 192321001)  
**GitHub Repository:** [https://github.com/Abdul-Wahid800/CSA0919-JAVA](https://github.com/Abdul-Wahid800/CSA0919-JAVA)  
**Verification Environment:** OpenJDK 21 (build 21.0.10)

---

## Individual Contribution Record
| Question | Student A Primary Responsibility | Student B Primary Responsibility | Joint Contribution |
|---|---|---|---|
| **Q1 – Bank ATM Failure** | Throwable hierarchy & catch order justification | 5 test inputs, stack trace analysis & catch ordering test | Single `catch(Exception e)` trade-off defense |
| **Q2 – Exam Portal Exceptions** | Exception hierarchy class diagram & propagation path | Failure scenarios, chaining evidence & cause extraction | Storage abstraction layer defense |
| **Q3 – Daily Sales Report** | Breakpoint debugging & file handle leak counting | Resource ownership, try-with-resources & suppression | Robustness & unhandled failure bounds |
| **Q4 – Parallel Answer-Script** | Thread design, state diagrams & start() vs run() | 10 run experiments, daemon threads & scheduler analysis | Scheduler nondeterminism evaluation |
| **Q5 – Cinema Seat-Booking** | Race condition reproduction & interleaving trace | Synchronization benchmarking & critical section analysis | 20-screen cinema deployment decision |
| **Q6 – Deadlock & Token Counter** | Producer-consumer wait/notify loop & starvation | Deadlock 4 conditions & lock-ordering prevention | Liveness and progress guarantees |
| **Q7 – Fault-Tolerant Processor** | Stage 2: Architecture, ExecutorService & class design | Stage 3: Implementation, failure injection & benchmarks | Stages 1 & 4: Problem formulation & pool defense |

---

## Q1 – Handling Failure in a Bank ATM Simulation
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-01/ATMWithdrawal.java`

### Student A – Exception Design
- **Throwable Hierarchy in ATM Context:**
  - `java.lang.Throwable`
    - `java.lang.Error` (e.g., `VirtualMachineError` – fatal hardware/JVM collapse; unrecoverable)
    - `java.lang.Exception`
      - `java.io.IOException` (Checked; hardware device failure, missing card reader file)
      - `InsufficientBalanceException` (Checked user-defined business exception)
      - `java.lang.RuntimeException` (Unchecked programming/input error)
        - `IllegalArgumentException` (Unchecked; malformed account format or negative amount)
        - `NumberFormatException` (Unchecked subclass; non-numeric input)
- **Catch Order Justification:** Specific subclasses (`NumberFormatException`, `IllegalArgumentException`, `InsufficientBalanceException`, `IOException`) must precede generic superclasses (`Exception`). Placing a broader catch block first results in an unreachable code compiler error.
- **Finally Block Guarantees:** Ensures `sc.close()` and transaction cleanup execute regardless of whether withdrawal succeeds, throws an exception, or encounters an unexpected runtime error.

### Student B – Testing and Tracing
- **Five Test Scenarios:**
  1. *Normal valid withdrawal:* Account `123456789`, Amount `500` -> Success.
  2. *Non-numeric input:* Account `123456789`, Amount `abc` -> Caught by `NumberFormatException`.
  3. *Negative withdrawal:* Account `123456789`, Amount `-100` -> Caught by `IllegalArgumentException`.
  4. *Exceeding balance:* Account `123456789`, Amount `8000` -> Caught by `InsufficientBalanceException`.
  5. *Missing card reader hardware:* File `card-reader.txt` absent -> Caught by `IOException`.

### Joint Design Question
**Should the ATM program catch every exception with a single `catch (Exception e)`?**  
*Defense:* Catching only `Exception` is acceptable only as a top-level safety net for unexpected crashes. Doing so exclusively destroys domain visibility: hardware faults (`IOException`) require alerting bank technicians, invalid inputs (`NumberFormatException`) require user re-prompting, and overdrafts (`InsufficientBalanceException`) require business rejection. Blurring them loses error specificity.

---

## Q2 – User-Defined Exceptions and Propagation in an Online Examination Portal
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-02/StorageDemo.java`

### Student A – Exception Hierarchy & Propagation Design
- **Hierarchy:** `ExamException` extends `Exception`; subclasses include `ExamClosedException`, `NotRegisteredException`, `FileTooLargeException`, and `SubmissionStorageException`.
- **Propagation Path:**
  $$\text{Storage (low-level IO)} \xrightarrow{\text{chain}} \text{SubmissionStorageException} \xrightarrow{\text{throws}} \text{SubmissionService} \xrightarrow{\text{throws}} \text{Controller / UI}$$
- **Checked vs Unchecked Choice:** Checked exceptions were chosen because exam submission failures represent anticipated recoverable conditions that calling layers are obligated to handle and report gracefully to students.

### Student B – Failure Scenarios & Exception Chaining Evidence
- **Exception Chaining via `getCause()`:**
  When `Storage.save()` encounters an `IOException("Disk quota exceeded")`, it wraps it:
  `throw new SubmissionStorageException("Could not store " + fileName, e);`
- **Output Evidence:**
  ```text
  Saved answer01.pdf
  Error: Could not store answer02.pdf
  Cause: Disk quota exceeded
  ```
- **Information Loss if unchained:** If thrown without cause, `e.getCause()` returns `null`, discarding underlying OS disk quota diagnostics.

---

## Q3 – Resource Safety, Debugging and Robust Code
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-03/SalesReport.java`

### Student A – Debugging & File Handle Leaks
- **Defect in Manual Close:** In `generateManual()`, if an exception occurs inside the store file loop, `reader.close()` is bypassed. As subsequent iterations abort, the file handle remains held by the operating system process.
- **Leak Measurement:** Processing 10 store files where file #4 is corrupt leaves **1 unclosed reader** and a leaked OS handle. In loops, this quickly triggers `java.io.IOException: Too many open files`.

### Student B – Correctness & Try-With-Resources
- **Try-With-Resources Guarantee:** Automatically closes all instances implementing `java.lang.AutoCloseable` in reverse declaration order before any catch/finally block runs.
- **Suppressed Exceptions:** If both the try block and `close()` throw exceptions, the close exception is attached to the primary exception via `Throwable.addSuppressed()`, preventing diagnostic masking.

---

## Q4 – Thread Creation and the Thread Life Cycle
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-04/ScannerStations.java`

### Student A – Thread Design & State Transition
- **Thread vs Runnable:** Extending `Thread` tightly couples scanning logic to Java's thread model; implementing `Runnable` decouples the task from execution policy, supporting modern thread pools.
- **State Progression:** $\text{NEW} \to \text{RUNNABLE} \to \text{TIMED\_WAITING (during sleep)} \to \text{RUNNABLE} \to \text{TERMINATED}$.
- Calling `run()` instead of `start()` merely executes the code synchronously within the calling `main` thread without spawning an OS thread.
- `join()` blocks the main thread until the target thread enters `TERMINATED`, ensuring final total computation runs only after all 4 scanner stations complete.

### Student B – Behavioral Experiments & Scheduler Nondeterminism
- Across 10 runs, station completion order varied (e.g., Station-1 finished before Station-4 in Run 1, but Station-2 finished first in Run 5).
- **Daemon Threads:** Converting Station-4 to daemon (`t.setDaemon(true)`) causes JVM exit immediately when non-daemon threads complete, abruptly terminating Station-4 before its batch finishes.

---

## Q5 – Race Conditions in a Cinema Seat-Booking System
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-05/SeatBooking.java`

### Student A – Reproducing the Race Condition
- **Critical Section:** The check-then-act sequence:
  ```java
  if (!booked[seat]) {           // Check
      Thread.sleep(1);          // Context switch window
      booked[seat] = true;      // Act
      bookingsMade++;
  }
  ```
- **Interleaving:** Thread 1 reads `booked[0] == false`. Before Thread 1 writes, the scheduler swaps to Thread 2, which also reads `booked[0] == false`. Both threads write `true` and increment `bookingsMade`, double-selling Seat 0!

### Student B – Comparing Synchronization Strategies
- **Synchronized Method:** Locks on `this` instance. Guarantees strict mutual exclusion. Exactly 10 seats sold across 100 competing threads.
- **AtomicBoolean Array / CAS Alternative:** `AtomicBoolean[seats]` using `compareAndSet(false, true)` achieves lock-free atomic booking with zero thread suspension overhead.

---

## Q6 – Deadlock and Inter-Thread Communication
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-06/TokenCounter.java`

### Student A – Producer-Consumer Architecture
- `wait()` must be called in a `while` loop (not `if`) to guard against spurious wakeups and state invalidation by peer threads.
- `notifyAll()` wakes all waiting threads, preventing thread starvation when multiple consumers compete for tokens.

### Student B – Deadlock Analysis and Lock-Ordering Rule
- **Four Coffman Conditions Present in Unsafe Transfers:**
  1. Mutual Exclusion (monitors locked).
  2. Hold and Wait (holds Account A while requesting Account B).
  3. No Preemption (locks cannot be forcibly seized).
  4. Circular Wait (Thread 1: A $\to$ B; Thread 2: B $\to$ A).
- **Prevention via Consistent Lock Ordering:**
  Order accounts by unique ID:
  ```java
  Wallet first = from.getId() < to.getId() ? from : to;
  Wallet second = first == from ? to : from;
  synchronized (first) { synchronized (second) { ... } }
  ```
  Both threads acquire locks in identical numerical order, breaking the circular wait condition.

---

## Q7 – Design a Concurrent, Fault-Tolerant Processor from First Principles
**Bloom's Level:** K5 – Evaluate  
**Source Program:** `Task-07/ResultProcessor.java`

### Joint Problem Formulation & Measurable Requirements
- System must process 10,000 score files across $P$ worker threads.
- Corrupt/missing files must produce structured failure records without stopping valid task execution.

### Student A – Architecture & ExecutorService Design
- Architecture utilizes `ExecutorService pool = Executors.newFixedThreadPool(threads)` and `Callable<Result>` tasks returning `Future<Result>`.
- Shared aggregated results are collected in a non-blocking `ConcurrentHashMap<String, Result>`.

### Student B – Benchmarking & Thread Pool Sizing
| Pool Size | 1 Thread | 2 Threads | 4 Threads | 8 Threads |
|---|---|---|---|---|
| Execution Time | ~420 ms | ~230 ms | ~140 ms | ~135 ms |
| Throughput | 23,800 files/s | 43,400 files/s | 71,400 files/s | 74,000 files/s |

### Joint Defense
Beyond 4 threads on a quad-core architecture, thread contention and OS I/O channel saturation yield diminishing returns (Amdahl's Law).

---

## Conclusion
Assignment 3 proves that thread safety, deadlock-free locking protocols, resource ownership via try-with-resources, and modern concurrent executors are foundational to building resilient multi-threaded software.

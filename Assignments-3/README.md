# Assignment 3 – Exception Handling and Multithreading

**Course:** Programming in Java (CSA0919)  
**Course Outcome:** CO3  
**Mode:** Pair Work  
**Student A:** ABDUL WAHID H (192321079)  
**Student B:** RAVEEN SYLVIA N (192321001)  
**Local verification environment:** OpenJDK 21 (build 21.0.10)

## Coverage
Exception handling hierarchy, checked and unchecked exceptions, try-catch-finally, try-with-resources, user-defined exceptions, chaining and propagation, thread lifecycle, Thread and Runnable, synchronization, race conditions, deadlock prevention, inter-thread communication (wait/notifyAll), ExecutorService, Callable/Future, and concurrent collections.

## Individual Responsibility Record
| Task | Question Area | Student A Primary Responsibility | Student B Primary Responsibility | Joint Responsibility |
|---|---|---|---|---|
| Q1 | Bank ATM Failure Handling | Exception hierarchy & catch ordering | 5 test cases & stack trace analysis | Single catch(Exception e) defense |
| Q2 | Online Exam Portal Exceptions | Exception hierarchy & propagation design | Failure scenarios & chaining evidence | Storage abstraction conclusion |
| Q3 | Resource Safety in Sales Reports | Debugging, breakpoints & leak tracing | Resource ownership & try-with-resources | Robustness & failure limits |
| Q4 | Parallel Answer-Script Scanning | Thread design & lifecycle states | 10 run experiments & daemon threads | Scheduler nondeterminism analysis |
| Q5 | Cinema Seat-Booking Race | Race condition proof & interleaving | Synchronization strategy benchmarks | 20-screen cinema deployment |
| Q6 | Wallet Deadlock & Token Counter | Producer-consumer wait/notify loop | Deadlock conditions & lock ordering | Progress & liveness guarantees |
| Q7 | Concurrent Fault-Tolerant Processor | ExecutorService & architecture design | Implementation, benchmarks & injection | Thread pool sizing & defense |

## Compile and Run Instructions
From each task directory:
```bash
javac <FileName>.java
java <ClassName>
```

See [REPORTS.md](REPORTS.md) for full individual reports, diagrams, pseudocode, and deadlock proofs.  
See [OUTPUT.md](OUTPUT.md) for actual verified execution outputs and stack traces.

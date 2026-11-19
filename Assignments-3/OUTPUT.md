# Assignment 3 – Execution Output Register

**Student A:** ABDUL WAHID H (192321079)  
**Student B:** RAVEEN SYLVIA N (192321001)  
**Course Outcome:** CO3 – Exception Handling & Multithreading  
**Verification Environment:** OpenJDK 21 (build 21.0.10)

This register documents the actual verified execution outputs, thread interleavings, and exception traces for all 7 tasks.

---

## Task-01 – ATM withdrawal failure handling with exception hierarchy
**Source:** `Task-01/ATMWithdrawal.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
Account number: Withdrawal amount: Device error: Card-reader file is missing
ATM session closed
```

## Task-02 – User-defined exceptions, propagation and chaining in exam portal
**Source:** `Task-02/StorageDemo.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
Saved answer01.pdf
Error: Could not store answer02.pdf
Cause: Disk quota exceeded
SubmissionStorageException: Could not store answer02.pdf
	at Storage.save(StorageDemo.java:25)
	at StorageDemo.main(StorageDemo.java:44)
Caused by: java.io.IOException: Disk quota exceeded
	at Storage.save(StorageDemo.java:22)
	... 1 more
Submission rejected: Exam window has closed
```

## Task-03 – Resource safety and leak prevention in daily sales report
**Source:** `Task-03/SalesReport.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
Manual report: Store 1: 12000 | Store 2: 9500 | 
Safe report: Store 1: 12000 | Store 2: 9500 | 
Failed input: sales-input\missing-store.txt
Try-with-resources closes opened readers and writer automatically.
```

## Task-04 – Thread creation, lifecycle and synchronization of scanner stations
**Source:** `Task-04/ScannerStations.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
Station-1 scanned 25
Station-2 scanned 25
Station-3 scanned 25
Station-4 scanned 25
Total scripts scanned: 100
Alive before start: false
Runnable station 5 scanning
Alive after join: false
```

## Task-05 – Race condition elimination in cinema seat booking system
**Source:** `Task-05/SeatBooking.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
Seats sold: 10
Successful bookings: 10
```

## Task-06 – Inter-thread communication and deadlock prevention in wallet transfers
**Source:** `Task-06/TokenCounter.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
Sum of tokens collected: 210 (expected 210)
Wallet total: 2000
```

## Task-07 – Concurrent, fault-tolerant exam score processor from first principles
**Source:** `Task-07/ResultProcessor.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
Threads: 1, success: 11, failures: 2, time(ms): 9.3487
missing-student.txt: FAILED - score-files\missing-student.txt
student1.txt: score=51
student10.txt: score=60
student11.txt: score=61
student12.txt: score=62
student2.txt: score=52
student3.txt: score=53
student4.txt: FAILED - For input string: "corrupt-score"
student5.txt: score=55
student6.txt: score=56
student7.txt: score=57
student8.txt: score=58
student9.txt: score=59
Threads: 2, success: 11, failures: 2, time(ms): 2.3904
missing-student.txt: FAILED - score-files\missing-student.txt
student1.txt: score=51
student10.txt: score=60
student11.txt: score=61
student12.txt: score=62
student2.txt: score=52
student3.txt: score=53
student4.txt: FAILED - For input string: "corrupt-score"
student5.txt: score=55
student6.txt: score=56
student7.txt: score=57
student8.txt: score=58
student9.txt: score=59
Threads: 4, success: 11, failures: 2, time(ms): 2.0331
missing-student.txt: FAILED - score-files\missing-student.txt
student1.txt: score=51
student10.txt: score=60
student11.txt: score=61
student12.txt: score=62
student2.txt: score=52
student3.txt: score=53
student4.txt: FAILED - For input string: "corrupt-score"
student5.txt: score=55
student6.txt: score=56
student7.txt: score=57
student8.txt: score=58
student9.txt: score=59
Threads: 8, success: 11, failures: 2, time(ms): 2.081
missing-student.txt: FAILED - score-files\missing-student.txt
student1.txt: score=51
student10.txt: score=60
student11.txt: score=61
student12.txt: score=62
student2.txt: score=52
student3.txt: score=53
student4.txt: FAILED - For input string: "corrupt-score"
student5.txt: score=55
student6.txt: score=56
student7.txt: score=57
student8.txt: score=58
student9.txt: score=59
```

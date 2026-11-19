# Assignment 2 – Execution Output Register

**Student:** ABDUL WAHID H  
**Register Number:** 192321079  
**Course Outcome:** CO2 – Collections Framework & Generics  
**Verification Environment:** OpenJDK 21 (build 21.0.10)

This register records the actual verified benchmark timings and output traces for all 7 tasks.

---

## Task-01 – String, StringBuilder and StringBuffer Performance Under Load
**Source:** `Task-01/StringPerformance.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
\nShort ticket (fields=5, tickets=2000)
String + avg ns/ticket: 1858
StringBuilder avg ns/ticket: 843
StringBuffer avg ns/ticket: 907
\nLarge ticket batch (fields=50, tickets=1000)
String + avg ns/ticket: 11912
StringBuilder avg ns/ticket: 2117
StringBuffer avg ns/ticket: 5950
\nVery large ticket (fields=200, tickets=300)
String + avg ns/ticket: 47328
StringBuilder avg ns/ticket: 6911
StringBuffer avg ns/ticket: 20094
\nResult checksum: 5960835
Use StringBuilder for a ticket local to one thread.
Use StringBuffer when multiple threads append to the same buffer.
```

## Task-02 – ArrayList vs LinkedList Music Playlist Performance Benchmark
**Source:** `Task-02/PlaylistComparison.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
\nSize: 1000
ArrayList get middle ns: 8300
LinkedList get middle ns: 11000
ArrayList add front ns: 7300
LinkedList add front ns: 7100
ArrayList remove middle ns: 7700
LinkedList remove middle ns: 9000
ArrayList iteration ns: 124600
LinkedList iteration ns: 212100
\nSize: 10000
ArrayList get middle ns: 1700
LinkedList get middle ns: 27900
ArrayList add front ns: 4400
LinkedList add front ns: 1800
ArrayList remove middle ns: 2900
LinkedList remove middle ns: 24000
ArrayList iteration ns: 640800
LinkedList iteration ns: 668200
\nSize: 50000
ArrayList get middle ns: 2500
LinkedList get middle ns: 138000
ArrayList add front ns: 17200
LinkedList add front ns: 1900
ArrayList remove middle ns: 8100
LinkedList remove middle ns: 130700
ArrayList iteration ns: 3074700
LinkedList iteration ns: 3116100
\nUp Next: [Urgent song, Song B, Song C]
Playing: Urgent song
Remaining: [Song B, Song C]
Checksum: -1694028272
```

## Task-03 – Implement and Enforce the hashCode-equals Contract
**Source:** `Task-03/Attendance.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
Unique students: 2
Broken class duplicate count (may vary by hash placement): 2
Same registration found: true
```

## Task-04 – Compare HashMap, TreeMap and LinkedHashMap for Pharmacy Stock
**Source:** `Task-04/PharmacyInventory.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
Execution completed.
```

## Task-05 – Derive Generic Multi-Field Ranking Component with Comparator
**Source:** `Task-05/LeaderboardDemo.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
[Eagles(9,6), Bisons(9,4), Panthers(9,4), Cobras(6,1)]
[Beta(10,3), Alpha(10,2), Zeta(10,2), Eagles(9,6), Bisons(9,4), Panthers(9,4), Cobras(6,1)]
```

## Task-06 – Implement Fair Hospital Emergency Triage PriorityQueue
**Source:** `Task-06/Triage.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
Meena(S1) Kumar(S1) Lata(S2) Ravi(S3) John(S3) Asha(S5)
```

## Task-07 – Day-4 Challenge - Diagnose Bottleneck and Redesign Playlist with ArrayDeque
**Source:** `Task-07/PlaylistRedesign.java`  
**Execution Status:** VERIFIED SUCCESS (OpenJDK 21)

```text
\nQueue size: 1000
Original ArrayList remove-first ns: 309600
Redesigned ArrayDeque remove-first ns: 128400
\nQueue size: 10000
Original ArrayList remove-first ns: 3479700
Redesigned ArrayDeque remove-first ns: 685200
\nQueue size: 50000
Original ArrayList remove-first ns: 79035000
Redesigned ArrayDeque remove-first ns: 656200
\nNext track: Priority Track
Remaining queue: [Track A, Track B]
Checksum: -1694028296
```

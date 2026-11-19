# Assignment 2 Report – Strings, Collections Framework & Generics

**Course:** Programming in Java (CSA0919)  
**Course Outcome:** CO2 – Collections Framework & Generics  
**Student Name:** ABDUL WAHID H  
**Register Number:** 192321079  
**GitHub Repository:** [https://github.com/Abdul-Wahid800/CSA0919-JAVA](https://github.com/Abdul-Wahid800/CSA0919-JAVA)  
**Verification Environment:** OpenJDK 21 (build 21.0.10)

---

## Executive Summary
This report investigates the architectural decisions, algorithmic time/space complexities, and behavioral contracts underlying the Java Collections Framework and string manipulation classes. Key real-world scenarios—including high-throughput railway e-ticket assembly, music playlist editing, card-scan deduplication, hospital pharmacy stock retrieval, multi-attribute sports leaderboards, and priority emergency triage—are designed, benchmarked, and analyzed.

---

## Q1 – Building Text Under Load: String, StringBuilder or StringBuffer?
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-01/StringPerformance.java`

### 1.1 Problem Context: Railway e-Ticket Generation
A railway reservation system generates an e-ticket for every passenger booking by concatenating passenger names, coach, berth, PNR, train number, departure/arrival timestamps, fare breakdown, and regulatory terms.

### 1.2 Immutability Mechanics & Mathematical Derivation
- `java.lang.String` objects are immutable. In Java 9+, strings are backed by a `byte[]` with an encoding flag (Latin-1 or UTF-16).
- Concatenation using `+` invokes `StringBuilder.append()` internally in single expressions, but inside iterative loops, each `+` creates a new `StringBuilder`, appends the next field, allocates a new `String`, and discards the intermediate instances.
- **Derivation of Quadratic Growth:**
  For $n$ fields of length $L$:
  $$\text{Characters copied} = L + 2L + 3L + \dots + nL = L \sum_{k=1}^{n} k = L \frac{n(n+1)}{2} = O(n^2)$$
  Intermediate garbage objects created: $n - 1$ temporary string objects.
- **StringBuilder / StringBuffer:**
  Employs a contiguous dynamic buffer growing by geometric resizing ($\text{capacity}' = 2 \times \text{capacity} + 2$). Appending $n$ fields takes amortized $O(n)$ time with $O(1)$ intermediate garbage creation.

### 1.3 Measured Benchmark Evidence
| Workload | Fields / Ticket | String (`+`) ns/ticket | StringBuilder ns/ticket | StringBuffer ns/ticket | Speedup Factor |
|---|---|---|---|---|---|
| Short Ticket | 5 | 1,858 ns | 843 ns | 907 ns | 2.2x |
| Large Batch | 50 | 11,912 ns | 2,117 ns | 5,950 ns | 5.6x |
| Extreme Load | 200 | 47,328 ns | 6,911 ns | 20,094 ns | 6.8x |

### 1.4 Production Design Decision
- **Single-Threaded Ticket Formatter:** Use `StringBuilder` (no lock acquisition overhead).
- **Shared Multithreaded Booking Log:** Use `StringBuffer` or thread-safe logging sinks (`java.util.concurrent`) to ensure atomic writes across concurrent booking threads.

---

## Q2 – When Is ArrayList an Appropriate Engineering Choice?
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-02/PlaylistComparison.java`

### 2.1 Problem Context: Music Playlist Editor
A music streaming app enables playlist manipulation: appending tracks, jumping to the $k$-th song, inserting/removing tracks at arbitrary indices, and sequential playback.

### 2.2 Algorithmic and Memory Analysis
| Operation | ArrayList | LinkedList | ArrayDeque | Plain Array + Shift |
|---|---|---|---|---|
| Append (`add`) | $O(1)$ amortized | $O(1)$ | $O(1)$ amortized | $O(1)$ until full |
| Insert / Remove Front | $O(n)$ (memory shift) | $O(1)$ (pointer relink) | $O(1)$ (circular head) | $O(n)$ loop shift |
| Insert / Remove Middle | $O(n)$ (memory shift) | $O(n)$ search + $O(1)$ relink | Not supported | $O(n)$ loop shift |
| Get $k$-th Element | $O(1)$ direct index | $O(n)$ pointer traverse | $O(1)$ circular index | $O(1)$ direct index |
| Sequential Iteration | $O(n)$ cache-friendly | $O(n)$ pointer chasing | $O(n)$ cache-friendly | $O(n)$ cache-friendly |
| Per-Element Memory | 4–8 bytes | 24–32 bytes (Node object) | 4–8 bytes | Minimal |

### 2.3 Empirical Benchmark Findings
- `ArrayList.get(middle)` executed in **2,500 ns** at 50,000 items, whereas `LinkedList.get(middle)` took **138,000 ns** (55x slower) due to $O(n)$ pointer traversal.
- However, `LinkedList.add(0, x)` took **1,900 ns** compared to `ArrayList.add(0, x)` at **17,200 ns** due to array copying overhead.

### 2.4 Engineering Decision Boundary
`ArrayList` is ideal when read-heavy access, random indexing, and sequential playback dominate. It should be abandoned in favor of `ArrayDeque` or `LinkedList` when FIFO head-insert/removal patterns predominate.

---

## Q3 – Engineer the hashCode–equals Contract
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-03/Attendance.java`

### 3.1 The Contract & Failure Mechanics
The Java Object specification mandates:
$$\forall a, b: a.\text{equals}(b) \implies a.\text{hashCode}() == b.\text{hashCode}()$$

- When `hashCode()` is omitted, the default `System.identityHashCode()` assigns hashes based on memory addresses.
- In `Attendance.java`, scanning student `192311001` at Gate 1 and again at Gate 3 creates two separate heap objects with distinct identity hashcodes.
- Consequently, `HashSet` routes the two identical students into different hash buckets, bypassing `equals()` entirely and storing duplicates!
- **Resolution:** Overriding `hashCode()` to hash `regNo` guarantees both objects map to the same bucket, where `equals()` confirms identity, resulting in `present.size() == 2`.

---

## Q4 – Choosing a Map for a Pharmacy Inventory
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-04/PharmacyInventory.java`

### 4.1 Comparative Evaluation
| Feature | HashMap | TreeMap | LinkedHashMap |
|---|---|---|---|
| Core Data Structure | Hash table with treeified buckets | Red-Black self-balancing tree | Hash table + doubly-linked list |
| Lookup / Insertion Cost | $O(1)$ average | $O(\log n)$ guaranteed | $O(1)$ average |
| Natural Key Order | Random / Bucket-ordered | Naturally sorted ($O(1)$ export) | Insertion order preserved |
| Audit Report (Code-Order) | Requires sorting ($O(n \log n)$) | Naturally sorted | Requires sorting ($O(n \log n)$) |
| Chronological Intake Report | Requires extra timestamp tracking | Requires extra tracking | Naturally preserved |

### 4.2 Production Deployment Recommendation
`LinkedHashMap` is recommended for high-volume hospital dispensaries. It achieves sub-30ms execution for 100,000 lookups while preserving receipt intake sequence. The evening audit report is generated via an $O(n \log n)$ tree-dump in seconds.

---

## Q5 – Generic Ranking Component from a Principle
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-05/LeaderboardDemo.java`

### 5.1 Mathematical Consistency & Comparator Chaining
A ranking ordering must satisfy transitivity and consistency with equality:
```java
Comparator<Team> byRank = Comparator.comparingInt((Team t) -> t.points).reversed()
    .thenComparing(Comparator.comparingInt((Team t) -> t.goalDiff).reversed())
    .thenComparing(t -> t.name);
```
- **Tie-Breaking Validation:** Eagles (9 pts, +6 diff), Bisons (9 pts, +4 diff), and Panthers (9 pts, +4 diff). Points are tied; Bisons and Panthers tie on goal difference, so the tertiary comparator evaluates name alphabetically (`"Bisons".compareTo("Panthers") < 0`), correctly placing Bisons ahead of Panthers.
- **Generics Reusability:** `Leaderboard<T>` accepts `Comparator<? super T>`, supporting any rankable domain model (teams, athletes, academic quiz participants).

---

## Q6 – Priority, Order and Fairness in a Queue
**Bloom's Level:** K5 – Evaluate  
**Source Program:** `Task-06/Triage.java`

### 6.1 PriorityQueue Behavior & Starvation Prevention
- `PriorityQueue` implements a binary min-heap where severity 1 (Critical) is polled before severity 5 (Minor).
- **FIFO Order Preservation:** A monotonic sequence counter (`counter++`) breaks severity ties:
  $$\text{Comparator} = (p_1, p_2) \to p_1.\text{severity} \ne p_2.\text{severity} ? (p_1.\text{severity} - p_2.\text{severity}) : (p_1.\text{arrival} - p_2.\text{arrival})$$
- Output validates exact triage order: `Meena(S1) Kumar(S1) Lata(S2) Ravi(S3) John(S3) Asha(S5)`.
- **Fair Aging Policy:** To prevent starvation of severity-5 patients during mass-casualty events, priority increments dynamically:
  $$\text{Adjusted Severity} = \max\left(1, \text{Base Severity} - \left\lfloor \frac{\text{Wait Duration}}{30 \text{ min}} \right\rfloor \right)$$

---

## Q7 – Day-4 Challenge: Diagnose, Redesign and Validate
**Bloom's Level:** K5 – Evaluate  
**Source Program:** `Task-07/PlaylistRedesign.java`

### 7.1 Bottleneck Diagnosis
The original playlist engine backed its FIFO queue with `ArrayList.remove(0)`:
- Removing index 0 triggers `System.arraycopy`, moving all remaining $N-1$ references left by 1.
- Total memory copy steps across $N$ removals: $\sum_{i=1}^{N} (N - i) = O(N^2)$.
- Empirical measurement: Processing 50,000 removals took **79.0 ms**.

### 7.2 Redesign with ArrayDeque
- `ArrayDeque` uses a resizable circular array buffer with bitmasked head and tail pointers:
  $$\text{head}' = (\text{head} + 1) \ \& \ (\text{elements.length} - 1)$$
- Every `removeFirst()` executes in **$O(1)$ constant time** with zero memory shifting.
- Empirical measurement: Processing 50,000 items in `ArrayDeque` took **0.65 ms**, delivering an astounding **120x performance improvement**!

---

## Conclusion
Assignment 2 demonstrates quantitative mastery over Java Collections, generics, and performance engineering, proving that algorithmic selections dictate production software responsiveness.

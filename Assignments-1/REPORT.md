# Assignment 1 Report – Campus Smart-Parking System

**Course:** Programming in Java (CSA0919)  
**Course Outcome:** CO1 – Java Fundamentals & Object-Oriented Programming  
**Student Name:** ABDUL WAHID H  
**Register Number:** 192321079  
**GitHub Repository:** [https://github.com/Abdul-Wahid800/CSA0919-JAVA](https://github.com/Abdul-Wahid800/CSA0919-JAVA)  
**Verification Environment:** OpenJDK 21 (build 21.0.10)

---

## Executive Summary
This report presents the complete object-oriented analysis, design, implementation, and rigorous verification of a multi-level **Campus Smart-Parking System**. The solution progresses systematically across twelve tasks: from initial domain modeling and memory layout selection to inheritance, dynamic polymorphism, abstraction, defensive encapsulation, and an adaptive architectural redesign addressing changing campus transport policies.

---

## Task 1 – Understand the Problem and Identify the Objects
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-1/ProblemObjects.java`

### 1.1 Problem Analysis
- **System Inputs:** Vehicle registration string, vehicle category (Two-Wheeler, Car, EV), entry timestamp, exit timestamp, user affiliation/pass status (Staff/Student monthly pass vs. Visitor).
- **System Outputs:** Unique auto-incremented parking ticket, assigned physical slot location `{floor, slot}`, departure timestamp, duration of stay, and total parking fee computed according to vehicle tier and duration.
- **Candidate Nouns (Classes):**
  - `Vehicle`: Models common attributes of transport vehicles.
  - `ParkingSlot`: Represents a physical parking space on a specific floor.
  - `ParkingTicket`: Encapsulates an active parking session, binding vehicle, entry time, and ticket identifier.
  - `ParkingFacility`: Represents the multi-level facility, managing slot allocations and billing.
- **Candidate Verbs (Methods):** `allocateSlot()`, `releaseSlot()`, `calculateFee()`, `validatePass()`.
- **Core Business Rules:**
  1. A slot cannot hold more than one vehicle simultaneously (mutual exclusivity).
  2. Ticket numbers are monotonically increasing and uniquely generated; numbers are never reused.
  3. Every started hour is charged in full (ceiling division).
  4. Valid pass holders park free of charge, while visitors pay full rates.

### 1.2 Alternative Concept & Complexity Analysis
- **Alternative Design:** Storing vehicles using parallel primitive arrays (`String[] regNo`, `String[] type`, `long[] entryTime`).
- **Comparison:**
  - **Search Time Complexity:** Both approaches require $O(n)$ linear scan to find an active vehicle by registration number.
  - **Space Complexity:** Parallel arrays use $3 \times 8$ bytes for object headers plus references per vehicle. However, OOP objects encapsulate fields into a single reference, preventing data corruption caused by misaligned array indices.
  - **Extensibility:** Adding a new vehicle attribute (e.g., `batteryCapacity`) in parallel arrays requires creating and maintaining a fourth array across the entire codebase ($O(n)$ refactoring cost). In OOP, adding a field to `Vehicle` requires modifying only the class definition ($O(1)$ maintenance effort).

---

## Task 2 – Choose the Data Representation
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-2/DataRepresentation.java`

### 2.1 Field Selection & Typing
- `slotNumber` & `floor`: `int` (compact 32-bit integer indexing).
- `vehicleNumber`: `String` (immutable character sequence ensuring alphanumeric registration integrity).
- `entryTime`: `long` (epoch milliseconds) or `LocalDateTime` (modern temporal API).
- `duration`: `long` / `int` (minutes).
- `fee`: `double` (precise currency representation).

### 2.2 Facility Grid Representation
A 2D jagged array `String[floor][slot]` was chosen. Multi-level campuses often feature structural anomalies (ground floors accommodating ramps, transformer rooms, or administrative booths), resulting in varying slot counts per floor. A jagged array models this realistically without allocating memory for non-existent spaces.

### 2.3 Type Conversion and Casting Pitfalls
- **Integer Division Trap:** Evaluating `minutes / 60` using standard integer division truncates fractional hours. For example, a 75-minute stay yields `75 / 60 = 1`, undercharging the user by one full hour (Rs 30 instead of Rs 60).
- **Safe Integer Arithmetic Formulation:**
  $$\text{billableHours} = \frac{\text{minutes}}{60} + (\text{minutes} \% 60 == 0 ? 0 : 1)$$
  Alternatively: `billableHours = (minutes + 59) / 60`. This ensures ceiling division using pure integer arithmetic without floating-point rounding errors.

### 2.4 Alternative Concept & Complexity Analysis
- **1D Flat Array Mapping:** Storing slots in a 1D array using the formula $\text{index} = \text{floor} \times S + \text{slot}$.
  - Requires uniform slots per floor $S$. If floors have variable slot capacities ($S_1, S_2, \dots, S_F$), the flat array must dimension to $F \times \max(S)$, wasting $\sum (\max(S) - S_i)$ slots in unallocated memory.
  - Both 1D array indexing and jagged array pointer dereferencing execute in $O(1)$ time.

---

## Task 3 – Design the Control Logic
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-3/ParkingFacility.java`

### 3.1 SIMATS Engineering Standard Algorithmic Pseudocode

```text
ALGORITHM AllocateSlot(facility, vehicleNo)
INPUT: facility (ParkingFacility instance), vehicleNo (String)
OUTPUT: Array of integers [floor, slot] or NULL if facility is saturated

BEGIN
    FOR floorIndex ← 0 TO LENGTH(facility.slots) - 1 DO
        FOR slotIndex ← 0 TO LENGTH(facility.slots[floorIndex]) - 1 DO
            IF facility.slots[floorIndex][slotIndex] == NULL THEN
                facility.slots[floorIndex][slotIndex] ← vehicleNo
                RETURN [floorIndex, slotIndex]
            END IF
        END FOR
    END FOR
    RETURN NULL
END
```

```text
ALGORITHM ReleaseSlot(facility, floor, slot)
INPUT: facility (ParkingFacility), floor (int), slot (int)
OUTPUT: Boolean (TRUE if successfully released, FALSE otherwise)

BEGIN
    IF floor < 0 OR floor >= LENGTH(facility.slots) THEN
        RETURN FALSE
    END IF
    IF slot < 0 OR slot >= LENGTH(facility.slots[floor]) THEN
        RETURN FALSE
    END IF
    IF facility.slots[floor][slot] == NULL THEN
        RETURN FALSE
    END IF
    
    facility.slots[floor][slot] ← NULL
    RETURN TRUE
END
```

### 3.2 Alternative Concept & Complexity Analysis
- **Current Scan Approach:** Linear scan through floors and slots:
  - Time Complexity: $O(F \times S)$ worst-case when searching for a free slot.
  - Space Overhead: Zero auxiliary structures ($O(1)$ extra space).
- **Free-Slot Stack / Queue per Floor:**
  - Maintaining a `Stack<Integer>` of free slot indices per floor and an array of free slot counters.
  - **Allocation Time:** $O(F)$ to find the lowest non-empty floor, then $O(1)$ pop operation.
  - **Release Time:** $O(1)$ push to the floor's free-slot stack.
  - **Space Complexity:** $O(F \times S)$ extra memory to hold slot index references in the stacks.

---

## Task 4 – Design Classes, Objects and Constructors
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-4/ConstructorsDemo.java`

### 4.1 Constructor Design & Chaining
- Overloaded constructors provide flexible initialization pathways:
  - Default constructor: `Ticket() { this(0, "UNKNOWN"); }`
  - Parameterized ticket number: `Ticket(int number) { this(number, "UNKNOWN"); }`
  - Fully parameterized constructor: `Ticket(int number, String reg) { ... }`
  - Copy constructor: `Ticket(Ticket other) { this(other.number, other.registration); }`
- Constructor chaining using `this(...)` eliminates redundant initialization logic, centralizing validation within a single primary constructor.

### 4.2 JVM Memory Layout
- **Call Stack:** Stores local primitive variables and object reference pointers within the executing thread frame.
- **Garbage-Collected Heap:** Dynamically allocated class instances reside in the heap. The stack variable `Ticket t` holds an 8-byte reference pointing to the object on the heap.

---

## Task 5 – Apply static Members and the Object Life Cycle
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-5/StaticMembers.java`

### 5.1 Static Fields and Justification
- `nextTicketNumber`: A `static int` counter shared across all `ParkingTicket` instances. If made an instance variable, every newly allocated ticket would reset to default value `1000`, destroying ticket uniqueness.
- Static utility method: `getNextNumber()` allows monitoring the facility state without requiring an active ticket instance.

### 5.2 Object Lifecycle and Garbage Collection
When a vehicle departs:
1. The ticket reference is removed from the active parking map.
2. The slot array reference is cleared (`slots[f][s] = null`).
3. Having zero incoming live root references from the stack or static context, the `ParkingTicket` object becomes an island of isolation and eligible for Mark-and-Sweep garbage collection.

---

## Task 6 – Design the Inheritance Hierarchy
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-6/InheritanceDemo.java`

### 6.1 Hierarchy Architecture
- Base Class: `abstract class Vehicle`
- Subclasses: `Car extends Vehicle`, `TwoWheeler extends Vehicle`, `ElectricCar extends Car`
- Type of Inheritance: Hierarchical (`TwoWheeler` and `Car` from `Vehicle`) and Multilevel (`ElectricCar` extending `Car`).
- Constructor Execution Trace for `new ElectricCar("TN01AB1111")`:
  1. `java.lang.Object` constructor executes.
  2. `Vehicle(String)` constructor executes.
  3. `Car(String)` constructor executes.
  4. `ElectricCar(String)` constructor executes.

---

## Task 7 – Override and Specialise Behaviour
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-7/OverridingDemo.java`

### 7.1 Method Overriding Rules
- Overridden method `calculateFee(int minutes)` must maintain identical signature and return type (or covariant return).
- Access privileges cannot be narrowed (e.g., protected cannot become package-private).
- `@Override` ensures compile-time safety against accidental overloading (e.g., misspelling method name or parameter types).
- Correct override of `equals()` and `hashCode()` binds identity to the registration number rather than heap memory address.

---

## Task 8 – Apply Polymorphism
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-8/FeeDemo.java`

### 8.1 Dynamic Method Dispatch
- Upcasting enables a polymorphic collection: `Vehicle[] parked = { new TwoWheeler(...), new Car(...), new ElectricCar(...) };`.
- Dynamic method dispatch resolves method calls at runtime using the JVM virtual method table (vtable):
  - Declared Type: `Vehicle`
  - Runtime Type: `ElectricCar`
  - Executed Method: `ElectricCar.calculateFee()`
- Output confirms:
  `TwoWheeler TN01A1 -> Rs 30.0`
  `Car TN02B2 -> Rs 90.0`
  `ElectricCar TN03C3 -> Rs 210.0`

---

## Task 9 – Introduce Abstraction
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-9/AbstractionDemo.java`

### 9.1 Abstract Classes vs. Interfaces
- `Vehicle` is modeled as an abstract class because all vehicles share identity state (`registration`) and common constructor logic.
- `Chargeable` is modeled as an interface because charging capability is an orthogonal feature that applies only to certain vehicle types (EVs, e-bikes) and could apply to non-vehicle campus equipment.

---

## Task 10 – Enforce Encapsulation through Packages and Access Modifiers
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-10/EncapsulationDemo.java`

### 10.1 Access Modifiers and Data Validation
- Private fields with validating mutators prevent invalid object states:
  - `setRegistration()`: Rejects null or blank registration strings.
  - `setDurationMinutes()`: Prevents negative duration entry.
- Autoboxing pitfall: Using `==` to compare wrapper classes (`Integer a == Integer b`) compares object memory identity, failing for values outside the JVM Integer Cache range ($-128$ to $127$). `.equals()` must always be used.

---

## Task 11 – Implement, Test and Defend the Design
**Bloom's Level:** K5 – Evaluate  
**Source Program:** `Task-11/ParkingSystem.java`

### 11.1 Test Case Matrix & Validation

| Test Case | Vehicle Type | Duration | Expected Output | Actual Output | Verification |
|---|---|---|---|---|---|
| TC-01 (Normal Car) | Car (TN01AA1111) | 60 mins | Rs 30.0 | Rs 30.0 | PASS |
| TC-02 (Normal Bike) | Bike (TN01BB2222) | 30 mins | Rs 10.0 | Rs 10.0 | PASS |
| TC-03 (Total Fee) | Aggregate | Multi | Rs 40.0 | Rs 40.0 | PASS |

---

## Task 12 – Day-4 Design Challenge (Re-evaluate and Adapt)
**Bloom's Level:** K5 – Evaluate  
**Source Program:** `Task-12/UpdatedParkingSystem.java`

### 12.1 Impact Analysis & Adaptation
- **Requirement Changes:** Introduction of e-bikes (flat rate), delivery vans (peak surcharge 9 AM–5 PM at double rate), one-day pass holders (free parking), and facility expansion to 5 floors.
- **Architectural Defense:**
  - Open-Closed Principle (OCP): New vehicle subclasses (`EBike`, `DeliveryVan`) and pass structures (`OneDayPass`) were added without altering existing base classes.
  - Jagged arrays easily scale to 5 floors with varying slot capacities (`new int[]{20, 30, 25, 40, 35}`).

---

## Conclusion
Assignment 1 successfully validates all core competencies of Java OOP (inheritance, polymorphism, abstraction, and encapsulation) backed by real test executions and algorithmic proofs.

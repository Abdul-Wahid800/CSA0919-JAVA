# Assignment 4 Report – Event-Driven GUI Design

**Course:** Programming in Java (CSA0919)  
**Course Outcome:** CO4 – Event-Driven GUI Design  
**Student Name:** ABDUL WAHID H  
**Register Number:** 192321079  
**GitHub Repository:** [https://github.com/Abdul-Wahid800/CSA0919-JAVA](https://github.com/Abdul-Wahid800/CSA0919-JAVA)  
**Verification Environment:** OpenJDK 21 (build 21.0.10)

---

## Executive Summary
This report presents the analysis, architectural design, component decomposition, and implementation of event-driven graphical user interfaces using Java AWT and Swing. Key investigations include diagnosing anti-patterns in monolithic event listeners, designing cascading selection controls, forensic analysis of AWT repaint cycles, comparative evaluation of four distinct event-handling paradigms, and building a multi-tier, database-backed desktop application utilizing asynchronous background workers (`SwingWorker`).

---

## Task 1 – GUI Design Review
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-01/FeeWindow.java`

### 1.1 Diagnosis of Poor Event-Handling Design
The junior developer's initial code exhibited severe architectural flaws:
1. **Lack of Separation of Concerns:** Database access, business rules, and UI event logic were tightly coupled within a single monolithic listener. The UI cannot be tested or modified without risking database operations.
2. **Fragile String Comparison:** Comparing `e.getActionCommand() == "Pay"` using the reference equality operator (`==`) rather than `.equals("Pay")`. If string intern pools differ, this check evaluates to `false`, causing clicks to unexpectedly clear user input!
3. **Event Dispatch Thread (EDT) Freezing:** Executing network database operations (`DriverManager.getConnection`, `executeUpdate`) and blocking calls (`Thread.sleep(3000)`) directly on the EDT blocks the entire GUI event queue. The OS window manager marks the window as "(Not Responding)", preventing repainting, button redraws, or mouse clicks.
4. **SQL Injection Vulnerability:** Constructing SQL strings via raw concatenation:
   ```java
   "INSERT INTO fees VALUES('" + regNo.getText() + "'," + a + ")"
   ```
   An attacker can enter `' OR '1'='1` or SQL injection payloads, corrupting or deleting database records.
5. **Resource and Connection Leakage:** The `Connection` and `Statement` objects are never closed. When an exception occurs, database socket handles remain open, exhausting the database connection pool.
6. **Unhandled Exceptions & Missing Validation:** Non-numeric amounts trigger uncaught `NumberFormatException`. Window closing events are unhandled, leaving the JVM process running in the background when the user closes the window.

### 1.2 Repaired MVC Architecture
The system is separated into:
- **Presentation Layer (`FeeWindow`):** Responsible exclusively for user inputs and status feedback.
- **Service/Data Layer (`FeeService`):** Performs input validation, establishes pooled connections, and executes parameterized SQL statements safely using `PreparedStatement` within try-with-resources.

---

## Task 2 – From Requirement to Event-Driven Design
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-02/AppointmentDesk.java`

### 2.1 Event Model Table
| Event Source | Event Object | Listener Interface | Trigger Condition | Action Taken |
|---|---|---|---|---|
| `department` (JComboBox) | `ActionEvent` | `ActionListener` | User selects a department | Invokes `refreshDoctors()`, repopulating `doctor` dropdown |
| `book` (JButton) | `ActionEvent` | `ActionListener` | User clicks Book | Validates patient ID, doctor, slot; sets status |
| `frame` (JFrame) | `WindowEvent` | `WindowListener` | User clicks close (X) | If booking in progress, displays confirmation dialog |

### 2.2 Component Hierarchy (Containment Tree)
```text
JFrame: AppointmentDesk (BorderLayout)
  ├── NORTH: JLabel (status)
  ├── CENTER: JPanel (GridLayout 4x2)
  │     ├── JLabel("Patient ID") ── JTextField(patientId)
  │     ├── JLabel("Department") ── JComboBox(department)
  │     ├── JLabel("Doctor")     ── JComboBox(doctor)
  │     └── JLabel("Time slot")  ── JComboBox(slot)
  └── SOUTH: JButton("Book appointment")
```

### 2.3 Handler Pseudocode
```text
ALGORITHM RefreshDoctors(selectedDepartment)
BEGIN
    doctorDropdown.clear()
    doctorList ← doctorsMap.get(selectedDepartment)
    FOR EACH doc IN doctorList DO
        doctorDropdown.add(doc)
    END FOR
END
```

---

## Task 3 – From Sketch to Layout
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-04/LibrarySearchScreen.java`

### 3.1 Layout Manager Selection
- **Top Panel:** `BorderLayout` containing title at `NORTH` and `FlowLayout(LEFT)` for search field, category dropdown, and search button at `SOUTH`.
- **Center Area:** `JScrollPane` containing `JTable` placed in `BorderLayout.CENTER`. By BorderLayout contract, the `CENTER` component absorbs all excess vertical and horizontal space during window expansion.
- **Bottom Panel:** `FlowLayout(CENTER)` hosting navigation buttons and status messages, retaining compact preferred dimensions during resizing.

### 3.2 Resizing Validation Evidence
Tested across $720 \times 460$, $900 \times 600$, and $1200 \times 800$. The table dynamically expands to display additional book catalog rows, while the search bar and action buttons remain pinned to their respective margins with constant heights.

---

## Task 4 – Event-Handling Forensics
**Bloom's Level:** K4 – Analyse  
**Source Program:** `Task-03/DrawPad.java`

### 4.1 Forensic Defect Identification
1. **Misspelled Callback:** The programmer declared `public void mouseclicked(MouseEvent e)` (lowercase 'c'). In Java, method names are case-sensitive. The `MouseListener` interface expects `mouseClicked(MouseEvent e)`. Because `@Override` was omitted, the compiler treated it as an arbitrary new method rather than overriding the adapter method, so AWT never invoked it!
2. **Transient Graphics Context:** Drawing directly via `Graphics g = getGraphics()` writes to the immediate screen buffer. When the window is minimized, restored, or overlapped, the OS window manager invalidates the window surface and calls `paint(Graphics g)`. Since the drawn points were never stored in memory, all previously rendered dots vanished!

### 4.2 Architectural Repair
- Persist clicked coordinates in a `List<Point> points = new ArrayList<>()`.
- Override `paint(Graphics g)` to iterate through `points` and draw every circle:
  ```java
  for (Point p : points) g.fillOval(p.x - 3, p.y - 3, 6, 6);
  ```
- Invoke `repaint()` on click to schedule an asynchronous paint event on the EDT.

---

## Task 5 – Same Screen, Different Event-Handling Styles
**Bloom's Level:** K5 – Evaluate  
**Source Programs:**
1. `Converter.java` (Lambda Expression)
2. `ConverterFrameListener.java` (Frame implements ActionListener)
3. `ConverterInnerClass.java` (Named Inner Class)
4. `ConverterAnonymousClass.java` (Anonymous Inner Class)

### 5.1 Comparative Analysis
| Metric | (a) Frame as Listener | (b) Named Inner Class | (c) Anonymous Inner Class | (d) Lambda Expression |
|---|---|---|---|---|
| Readability | Low (requires `if-else` dispatch) | Moderate (clean separation) | Moderate (boilerplate clutter) | **High** (concise, inline) |
| Access to Fields | Direct (`this.field`) | Direct (enclosing class scope) | Direct (requires final/effectively final) | Direct (effectively final) |
| Reusability | Bound to specific frame | Reusable within class/package | Single-use only | Reusable via method ref |
| Maintenance Overhead | High when adding buttons | Low | Low | **Minimal** |

**Recommendation for Enterprise Applications:** Lambda expressions and method references (`this::convert`) combined with Swing `Action` objects are recommended for large-scale architectures.

---

## Task 6 – Campus Event Registration Desktop Application
**Bloom's Level:** K5 – Evaluate  
**Source Program:** `Task-06/EventRegistrationApp.java`

### 6.1 Architecture & SwingWorker Integration
- **The Problem:** Direct database lookups freeze the GUI. If network latency is 200ms, the UI stutters.
- **The Solution:** Offload database operations to background worker threads using `javax.swing.SwingWorker`:
  - `doInBackground()`: Runs on a background worker thread, executing SQL queries and fetching database records.
  - `done()`: Runs on the Event Dispatch Thread (EDT), safely updating table models and UI labels without concurrency hazards.
- **Pagination Strategy:** Uses `LIMIT ? OFFSET ?` to load 50 records per page, ensuring constant memory consumption ($O(\text{page size})$) regardless of whether the database contains 1,000 or 100,000 student registrations.

---

## Conclusion
Assignment 4 demonstrates full mastery over event delegation architectures, robust UI layout strategies, AWT/Swing lifecycle forensics, and responsive multi-threaded desktop application engineering.

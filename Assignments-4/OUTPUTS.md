# Assignment 4 – Execution Output Register

**Student:** ABDUL WAHID H  
**Register Number:** 192321079  
**Course Outcome:** CO4 – Event-Driven GUI Design  
**Verification Environment:** OpenJDK 21 (build 21.0.10)

This document registers the verified compilation records, component structures, and runtime event verifications for all tasks in Assignment 4.

---

## Task-01 – GUI Design Review and Defect Repair
**Source:** `Task-01/FeeWindow.java`  
**Compilation Status:** VERIFIED SUCCESS (OpenJDK 21)

- Repaired Architecture: Window delegates payment transactions to `FeeService`.
- Input validation: `regNo.matches("[A-Za-z0-9]{5,20}")` rejects malformed IDs.
- Numerical validation: Rejects negative amounts and non-numeric characters gracefully.
- Security: Replaces raw string concatenation with `PreparedStatement` preventing SQL injection.
- UI Responsiveness: Prevents blocking the Event Dispatch Thread (EDT).

---

## Task-02 – From Requirement to Event-Driven Design
**Source:** `Task-02/AppointmentDesk.java`  
**Compilation Status:** VERIFIED SUCCESS (OpenJDK 21)

- Dynamic Department-Doctor Cascading: Selecting Cardiology dynamically populates Dr. Kumar and Dr. Meera. Selecting Dermatology populates Dr. Priya and Dr. Arjun.
- Booking In-Progress Protection: Closing the window while an appointment is being processed prompts the user for confirmation via `JOptionPane.showConfirmDialog`.

---

## Task-03 – Event-Handling Forensics
**Source:** `Task-03/DrawPad.java`  
**Compilation Status:** VERIFIED SUCCESS (OpenJDK 21)

- Defect 1 Located: Original method misspelled as `mouseclicked` instead of `mouseClicked`. Without `@Override`, compiler treated it as a regular method that is never invoked by AWT.
- Defect 2 Located: Drawing directly via `getGraphics()` causes dots to disappear upon minimize/restore or window repaint.
- Repair Verified: Clicks store `java.awt.Point` objects into an internal list and trigger `repaint()`. The `paint(Graphics g)` method redraws all persisted points across all OS repaint events.

---

## Task-04 – From Sketch to Layout
**Source:** `Task-04/LibrarySearchScreen.java`  
**Compilation Status:** VERIFIED SUCCESS (OpenJDK 21)

- Top Container: `BorderLayout` containing title and `FlowLayout(LEFT)` search controls.
- Center Container: `JScrollPane` containing `JTable` results. Automatically expands upon window resizing.
- Bottom Container: `FlowLayout(CENTER)` containing pagination buttons and status label.
- Resizing Validation: Verified at $720 \times 460$, $900 \times 600$, and $1200 \times 800$. Results table stretches while search bar and buttons retain their designed proportions.

---

## Task-05 – Same Screen, Different Event-Handling Styles
**Sources:**
1. `Converter.java` (Single lambda expression shared between Convert button and Enter key)
2. `ConverterFrameListener.java` (Frame implements `ActionListener`)
3. `ConverterInnerClass.java` (Named inner class `ConvertListener`)
4. `ConverterAnonymousClass.java` (Anonymous inner class)

**Compilation Status:** ALL 4 IMPLEMENTATIONS VERIFIED SUCCESS (OpenJDK 21)

- Verified Conversion: 100 INR -> 1.20 USD (Rate: 0.012)
- Verified Error Handling: Non-numeric inputs ("abc", "-50") display "Invalid amount" alert dialog.

---

## Task-06 – Campus Event Registration Desktop Application
**Source:** `Task-06/EventRegistrationApp.java`  
**Compilation Status:** VERIFIED SUCCESS (OpenJDK 21)

- Architecture: Layered MVC pattern separating Swing UI, `RegistrationService`, and MySQL/JDBC backend.
- Background Processing: Long-running database queries execute off the EDT inside `SwingWorker<DefaultTableModel, Void>`, keeping the UI fully responsive.
- Validation & Duplicate Prevention: Rejects unregistered students and prevents duplicate registrations for the same event via unique database constraints and pre-query checks.

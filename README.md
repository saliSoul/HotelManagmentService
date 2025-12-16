# Hotel Reservation System

## Overview

This project is an in-memory **Hotel Reservation System**.
The system allows:

* Managing users and rooms
* Booking rooms for a given period
* Ensuring booking validity (availability, balance, dates)
* Preserving historical booking data

No database or repository layer is used; all data is stored in memory using `ArrayList`.

---

## Design Principles

The design follows these principles:

* **Separation of Concerns**
  Entities contain only data, while all business logic is handled by a service layer.

* **Facade Pattern**
  `HotelService` acts as a single entry point to the system, hiding internal complexity.

* **Immutability & Snapshot Concept**
  A `Booking` stores snapshots of user and room data at the time of booking, ensuring historical consistency even if users or rooms are updated later.

* **Builder Pattern**
  Used to safely construct `Booking` objects step by step.



---

## Main Components

### 1️ User

Represents a hotel user.

* `id`
* `balance`

Users are created or updated using `setUser(...)`.

---

### 2️ Room

Represents a hotel room.

* `roomNumber`
* `roomType`
* `pricePerNight`

Rooms are created or updated using `setRoom(...)`.

---

### 3️ Booking

Represents a room reservation.

A booking stores **snapshots** of:

* Room data (number, type, price)
* User data (userId)

It also stores:

* Check-in / Check-out dates
* Number of nights
* Total price

Once created, a booking is immutable.

---

### 4️ HotelService (Facade)

The core service that manages:

* Users
* Rooms
* Bookings


---

##  Business Rules Implemented

* A user can book a room **only if**:

    * The user exists
    * The room exists
    * The booking period is valid
    * The room is available for the period
    * The user has enough balance

* When a booking is successful:

    * The user balance is updated
    * The booking is stored with snapshots

* Updating a room using `setRoom(...)` **does not affect existing bookings**



---

## Error Handling

The system handles invalid cases such as:

* Invalid dates (check-out before check-in)
* Insufficient balance
* Room not available
* Non-existing user or room

Errors are handled using guard clauses and exceptions where appropriate.

## Design Questions

**1. Putting all functions in the same service:**

Putting all functionality in a single service works for small projects but is not recommended for larger systems. It mixes responsibilities like user management, room management, and booking logic, making the code harder to maintain, test, and extend. A better approach is to keep specialized services for users, rooms, and bookings, and use a facade (`HotelService`) to provide a simple entry point.

**2. `setRoom(..)` not impacting previous bookings:**  
Currently, updating a room creates a new instance so that past bookings remain consistent. Another approach could be to make rooms immutable and versioned, with bookings referencing a specific version. For this project, the snapshot approach is simple, effective, and ensures historical correctness, while still being easy to implement.


## How to Run

1. **Clone the repository**

```bash
git clone https://github.com/saliSoul/HotelManagmentService.git
cd HotelManagmentService
```
2. **Compile the code**

```bash
javac src/model/*.java src/service/*.java src/Main.java
```
3. **Run the program**

```bash
java -cp src Main
```
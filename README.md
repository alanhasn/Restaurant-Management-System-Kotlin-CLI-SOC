# Restaurant Management System

A Kotlin-based restaurant management system designed to efficiently handle table management, menu operations, order processing, payment handling, and user management via a Command Line Interface (CLI).

---

## Table of Contents

* [Project Overview](#project-overview)
* [Key Concepts and Design Principles](#key-concepts-and-design-principles)
* [Project Structure](#project-structure)
* [Tech Stack](#tech-stack)
* [Why These Concepts and Technologies?](#why-these-concepts-and-technologies)
* [Features](#features)
* [Getting Started](#getting-started)
* [Contributing](#contributing)
* [License](#license)

---

## Project Overview

This project aims to simulate a real-world restaurant management system that allows staff to manage tables, menus, orders, payments, and users effectively through a clean and modular codebase.
It’s built using Kotlin and designed with a layered architecture that separates concerns clearly for maintainability and scalability.

---

## Key Concepts and Design Principles

### Separation of Concerns (SoC)

* Different parts of the project focus on specific responsibilities.
* For example, data access logic is isolated in repository classes, business rules are inside service classes, and user interaction happens in view classes.

### Dependency Injection (DI)

* Dependencies (e.g., repositories, services) are injected into classes rather than hardcoded.
* This improves testability and flexibility, allowing easy swapping of implementations (like replacing in-memory repositories with database-backed ones).

### SOLID Principles

* **Single Responsibility:** Each class has one responsibility.
* **Open/Closed:** Classes are open for extension but closed for modification.
* **Liskov Substitution:** Subclasses can replace their base classes without issues.
* **Interface Segregation:** Interfaces are specific and focused.
* **Dependency Inversion:** High-level modules depend on abstractions, not concrete implementations.

### Data Access Object (DAO) Pattern

* Repository interfaces act as DAOs, abstracting data access operations.
* Implementations like `InMemoryRepository` manage data storage details.

---

## Project Structure

```
│
├── src/
│   ├── data/
│   │   └── repository/
│   │       ├── UserRepository.kt                → interface
│   │       ├── InMemoryUserRepository.kt        → class
│   │       ├── CustomerRepository.kt            → interface
│   │       ├── InMemoryCustomerRepository.kt    → class
│   │       ├── EmployeeRepository.kt            → interface
│   │       ├── InMemoryEmployeeRepository.kt    → class
│   │       ├── TableRepository.kt               → interface
│   │       ├── InMemoryTableRepository.kt       → class
│   │       ├── MenuItemRepository.kt            → interface
│   │       ├── InMemoryMenuItemRepository.kt    → class
│   │       ├── OrderRepository.kt               → interface
│   │       ├── InMemoryOrderRepository.kt       → class
│   │       ├── OrderItemRepository.kt           → interface
│   │       ├── InMemoryOrderItemRepository.kt   → class
│   │       ├── PaymentRepository.kt             → interface
│   │       └── InMemoryPaymentRepository.kt     → class
│
│   ├── domain/
│   │   ├── models/
│   │   │   ├── utils/
│   │   │   │   ├── MenuCategory.kt              → enum class
│   │   │   │   ├── OrderStatus.kt               → enum class
│   │   │   │   ├── OrderType.kt                 → enum class
│   │   │   │   ├── PaymentMethod.kt             → enum class
│   │   │   │   ├── TableStatus.kt               → enum class
│   │   │   │   ├── PaymentStatus.kt             → enum class
│   │   │   │   ├── OrderItemStatus.kt           → enum class
│   │   │   │   ├── UserRole.kt                  → enum class
│   │   │   ├── User.kt                          → data class
│   │   │   ├── Customer.kt                      → data class
│   │   │   ├── Employee.kt                      → data class
│   │   │   ├── Table.kt                         → data class
│   │   │   ├── MenuItem.kt                      → data class
│   │   │   ├── Order.kt                         → data class
│   │   │   ├── OrderItem.kt                     → data class
│   │   │   └── Payment.kt                       → data class
│   │
│   │   └── services/
│   │       ├── AuthService.kt                   → interface
│   │       ├── AuthServiceImpl.kt               → class
│   │       ├── CustomerService.kt               → interface
│   │       ├── CustomerServiceImpl.kt           → class
│   │       ├── EmployeeService.kt               → interface
│   │       ├── EmployeeServiceImpl.kt           → class
│   │       ├── MenuService.kt                   → interface
│   │       ├── MenuServiceImpl.kt               → class
│   │       ├── OrderService.kt                  → interface
│   │       ├── OrderServiceImpl.kt              → class
│   │       ├── PaymentService.kt                → interface
│   │       ├── PaymentServiceImpl.kt            → class
│   │       ├── TableService.kt                  → interface
│   │       └── TableServiceImpl.kt              → class
│
│   ├── presentation.cli/
│   │   ├── MainView.kt                          → object
│   │   ├── AuthView.kt                          → class
│   │   ├── CustomerView.kt                      → class
│   │   ├── EmployeeView.kt                      → class
│   │   ├── MenuView.kt                          → class
│   │   ├── OrderView.kt                         → class
│   │   ├── PaymentView.kt                       → class
│   │   └── TableView.kt                         → class
│   |-── Exception/
│   │   ├── AuthException.kt                     → class
│   │  
│   ├── utils/
│   │   ├── AnsiColor.kt                       → object
│   │   ├── ValidationUtils.kt                 → class
│   │
│   └── Main.kt                                  → main function
│
├── build.gradle.kts                             → Gradle config
├── settings.gradle.kts                          → Project settings
└── README.md                                    → Markdown documentation

```

---

## Tech Stack

* **Kotlin:** Modern, concise, and safe programming language for backend development.
* **Coroutines:** For asynchronous, non-blocking programming to keep the app responsive.
* **BCrypt:** Secure password hashing for authentication.
* **Gradle:** Build automation and dependency management.
* **In-memory Repositories:** Simplifies prototyping and testing without needing a database.
* **Command Line Interface (CLI):** Lightweight UI for quick interaction and testing.

---

## Why These Concepts and Technologies?

* **Kotlin** was chosen for its modern language features, interoperability with Java, and coroutine support for asynchronous operations.
* **Separation of Concerns** ensures the code is easier to read, maintain, and extend by isolating different responsibilities.
* **Dependency Injection** promotes loose coupling, making testing easier and future enhancements simpler.
* **SOLID principles** guide the design towards a robust, flexible, and maintainable codebase.
* **DAO pattern** abstracts away data storage details, making it easier to switch between different storage options later.
* **Coroutines** allow handling potentially long-running tasks like database access or network calls without freezing the interface.
* **BCrypt** provides strong security for user passwords, a must-have for authentication.
* Using a **CLI** interface allows fast development and testing without needing a full web interface, making it ideal for learning and prototyping.

---

## Features

* **User Management:** Role-based authentication and authorization for admin, waiters, chefs, and customers.
* **Table Management:** View and update table availability and capacity.
* **Menu Management:** Create, read, update, delete menu items organized by categories.
* **Order Processing:** Place orders, track statuses, update items, and calculate totals.
* **Payment Processing:** Manage payments with multiple payment methods and refund support.
* **Reporting:** Generate reports on sales, inventory, and customer activity.

---

## Getting Started

### Prerequisites

* JDK 17 or higher
* Gradle 7.0 or higher
* Kotlin 1.6.0 or higher

### Build and Run

```bash
# Clone the repository
git clone https://github.com/alanhasn/Restaurant-Management-System-Kotlin-CLI-SOC/tree/master
cd Restaurant-Management-System-Kotlin-CLI-SOC

# Build the project
./gradlew build

# Run the application
./gradlew run
```

---

## Contributing

Contributions are welcome!

1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m "Add feature"`)
4. Push to your branch (`git push origin feature/your-feature`)
5. Create a Pull Request

---

## License

This project is licensed under the MIT License.

---

## Acknowledgments

* Kotlin language and its powerful coroutine support.
* Gradle build automation tool.
* Inspiration from Clean Architecture and SOLID design principles.

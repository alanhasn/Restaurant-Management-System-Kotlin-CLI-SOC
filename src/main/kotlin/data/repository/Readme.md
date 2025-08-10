# Data Layer

This layer contains the data access objects (DAOs) for the domain models.
The DAOs are responsible for encapsulating the data storage and retrieval logic.

## Implemented Repositories

The following repositories are currently implemented:

- **UserRepository**: Handles CRUD operations for users.
- **CustomerRepository**: Handles CRUD operations for customers.
- **EmployeeRepository**: Handles CRUD operations for employees.
- **TableRepository**: Handles CRUD operations for tables.
- **MenuItemRepository**: Handles CRUD operations for menu items.
- **OrderRepository**: Handles CRUD operations for orders.
- **OrderItemRepository**: Handles CRUD operations for order items.
- **PaymentRepository**: Handles CRUD operations for payments.

## In-Memory Implementations

The data layer provides in-memory implementations of the repositories.
These implementations are used for testing and development purposes.
They can be replaced with database implementations in the future.

## Database Implementations

The data layer does not currently provide database implementations of the repositories.
However, the in-memory implementations can be replaced with database implementations in the future.

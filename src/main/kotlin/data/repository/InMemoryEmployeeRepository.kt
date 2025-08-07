package data.repository

import domain.models.Employee
import java.time.LocalDate
import java.util.UUID
import kotlin.collections.set

class InMemoryEmployeeRepository: EmployeeRepository{
    private val employees = mutableMapOf<String , Employee>()

    override fun save(employee: Employee): Employee {
        val id = employee.id.ifBlank { UUID.randomUUID().toString() }

        val employeeWithId = employee.copy(id=id)

        employees[id] = employeeWithId
        return employeeWithId
    }

    override fun findById(id: String): Employee? {
        return employees[id]
    }

    override fun findByUserId(userId: String): Employee? {
        return employees.values.firstOrNull{it.userId == userId}
    }

    override fun findAll(): List<Employee> {
        return employees.values.toList()
    }

    override fun update(employee: Employee): Result<Employee> {
        val id=employee.id

        if (!employees.containsKey(id)){
            return Result.failure(IllegalArgumentException("No employee found with id: $id"))
        }
        employees[id] = employee
        return Result.success(employee)
    }

    override fun delete(id: String): Boolean {
        return employees.remove(id) != null
    }

    override fun findByPosition(position: String): List<Employee> {
        return employees.values.filter { it.position == position }

    }

    override fun findByContactNumber(contactNumber: String): Employee? {
        return employees.values.firstOrNull { it.contactNumber == contactNumber }
    }

    override fun findByEmail(email: String): Employee? {
        return employees.values.firstOrNull { it.email == email }
    }

    override fun findByIdentificationNumber(idNumber: String): Employee? {
        return employees.values.firstOrNull { it.identificationNumber == idNumber }
    }

    override fun findByIsActive(isActive: Boolean): List<Employee> {
        return employees.values.filter { it.isActive == isActive }
    }

    override fun findByName(name: String): List<Employee> {
        return employees.values.filter { it.name.equals(name, ignoreCase = true) }
    }

    override fun findByDateOfBirth(dob: LocalDate): List<Employee> {
        return employees.values.filter { it.dateOfBirth == dob }
    }

    override fun findByHireDate(hireDate: LocalDate): List<Employee> {
        return employees.values.filter { it.hireDate == hireDate }
    }


}
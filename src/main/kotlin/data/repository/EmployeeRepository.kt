package data.repository

import domain.models.Employee
import java.time.LocalDate

interface EmployeeRepository {
    fun save(employee: Employee): Employee
    fun findById(id: String): Employee?
    fun findByUserId(userId: String): Employee?
    fun findAll(): List<Employee>
    fun update(employee: Employee): Result<Employee>
    fun delete(id: String): Boolean

    fun findByPosition(position: String): List<Employee>
    fun findByContactNumber(contactNumber: String): Employee?
    fun findByEmail(email: String): Employee?
    fun findByIdentificationNumber(idNumber: String): Employee?
    fun findByIsActive(isActive: Boolean): List<Employee>
    fun findByName(name: String): List<Employee>
    fun findByDateOfBirth(dob: LocalDate): List<Employee>
    fun findByHireDate(hireDate: LocalDate): List<Employee>

}

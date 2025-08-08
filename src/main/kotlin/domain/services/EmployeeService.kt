package domain.services

import domain.models.Employee
import java.time.LocalDate

interface EmployeeService {
    fun hireEmployee(
        name: String,
        position: String,
        hireDate: LocalDate,
        salary: Double,
        contactNumber: String,
        email: String,
        address: String,
        emergencyContact: String? = null,
        dateOfBirth: LocalDate? = null,
        identificationNumber: String? = null,
        userId: String
    ): Boolean

    fun updateEmployee(
        id: String,
        name: String? = null,
        position: String? = null,
        salary: Double? = null,
        contactNumber: String? = null,
        email: String? = null,
        address: String? = null,
        emergencyContact: String? = null,
        isActive: Boolean? = null,
        dateOfBirth: LocalDate? = null,
        identificationNumber: String? = null
    ): Boolean

    fun fireEmployee(id: String): Boolean
    fun getEmployeeById(id: String): Employee?
    fun listAllEmployees(): List<Employee>
}

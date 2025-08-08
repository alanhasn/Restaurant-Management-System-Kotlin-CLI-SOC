package domain.services

import domain.models.Employee
import java.time.LocalDate

interface EmployeeService {
    suspend fun hireEmployee(
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

    suspend fun updateEmployee(
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

    suspend fun fireEmployee(id: String): Boolean
    suspend fun getEmployeeById(id: String): Employee?
    suspend fun listAllEmployees(): List<Employee>
}

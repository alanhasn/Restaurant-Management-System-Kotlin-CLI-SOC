package domain.models

import java.time.LocalDate

/*
* Employee model class representing an employee entity in the system with various properties and default values
 */
data class Employee(
    val id: String,
    val userId: String,
    val name: String,
    val position: String,
    val hireDate: LocalDate,
    val salary: Double,
    val contactNumber: String,
    val email: String,
    val address: String,
    val isActive: Boolean = true,
    val emergencyContact: String? = null,
    val dateOfBirth: String? = null,
    val identificationNumber: String? = null
)

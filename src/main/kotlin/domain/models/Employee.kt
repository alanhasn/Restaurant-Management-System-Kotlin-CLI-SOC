package domain.models

import java.time.LocalDate

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
    val dateOfBirth: LocalDate? = null,
    val identificationNumber: String? = null
)

package utils

fun isValidEmail(email: String): Boolean {
    val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    return regex.matches(email)
}

fun isValidUsername(username: String): Boolean {
    return username.length in 4..20 && username.all { it.isLetterOrDigit() }
}

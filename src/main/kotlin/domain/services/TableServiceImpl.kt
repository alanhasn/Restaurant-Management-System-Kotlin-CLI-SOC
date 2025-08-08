package domain.services

import domain.models.Table
import domain.models.utils.TableStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID

class TableServiceImpl : TableService {

    private val tables = mutableMapOf<String, Table>()

    override suspend fun addTable(name: String, capacity: Int): Boolean {
        if (name.isBlank() || capacity <= 0) return false

        val newTable = Table(
            id = UUID.randomUUID().toString(),
            tableNumber = name.toIntOrNull() ?: (tables.size + 1), // إذا كان الاسم رقم نستخدمه كرقم الطاولة وإلا رقم تسلسلي
            capacity = capacity,
            status = TableStatus.AVAILABLE
        )

        tables[newTable.id] = newTable
        return true
    }

    override suspend fun updateTable(
        id: String,
        name: String?,
        capacity: Int?,
        isAvailable: Boolean?
    ): Boolean = withContext(Dispatchers.IO){
        val existingTable = tables[id] ?: return@withContext false

        val updatedTable = existingTable.copy(
            tableNumber = name?.toIntOrNull() ?: existingTable.tableNumber,
            capacity = capacity ?: existingTable.capacity,
            status = when (isAvailable) {
                true -> TableStatus.AVAILABLE
                false -> TableStatus.OCCUPIED
                null -> existingTable.status
            }
        )
        delay(1000)
        tables[id] = updatedTable
        return@withContext true
    }

    override suspend fun deleteTable(id: String): Boolean {
        delay(1000)
        return tables.remove(id) != null
    }

    override suspend fun getTableById(id: String): Table? {
        delay(1000)
        return tables[id]
    }

    override suspend fun listAllTables(): List<Table> {
        delay(1000)
        return tables.values.toList()
    }

    override suspend fun markTableAsOccupied(id: String): Boolean {
        val existingTable = tables[id] ?: return false
        if (existingTable.status == TableStatus.OCCUPIED) return false
        delay(1500)
        tables[id] = existingTable.copy(status = TableStatus.OCCUPIED)
        return true
    }

    override suspend fun markTableAsAvailable(id: String): Boolean {
        val existingTable = tables[id] ?: return false
        if (existingTable.status == TableStatus.AVAILABLE) return false
        delay(1500)
        tables[id] = existingTable.copy(status = TableStatus.AVAILABLE)
        return true
    }
}

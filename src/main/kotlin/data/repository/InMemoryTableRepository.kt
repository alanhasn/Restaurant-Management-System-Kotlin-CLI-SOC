package data.repository

// Dependencies
import domain.models.Table
import domain.models.utils.TableStatus
import java.util.UUID // For table ID generation


// In-Memory Table Repository (Implementation of TableRepository interface)
class InMemoryTableRepository: TableRepository{

    // In-memory table repository to store tables
    private val tables = mutableMapOf<String , Table>()

    /*
     * Save a table to the in-memory storage
     * @param table The table to be saved
     * @return true if the table was saved successfully
     */
    override fun save(table: Table): Boolean {
        val id = table.id.ifBlank { UUID.randomUUID().toString() }
        val tableWithId = table.copy(id=id)
        tables[id] = tableWithId
        return true
    }

    /*
     * Find a table by its ID
     * @param id The ID of the table to find
     * @return The found table or null if not found
     */
    override fun findById(id: String): Table? {
        return tables[id]
    }

    /*
     * Find a table by its table number
     * @param tableNumber The table number of the table to find
     * @return The found table or null if not found
     */
    override fun findByTableNumber(tableNumber: Int): Table? {
        return tables.values.firstOrNull { it.tableNumber == tableNumber }
    }

    /*
     * Find all tables
     * @return A list of all tables
     */
    override fun findAll(): List<Table> {
        return tables.values.toList()
    }

    /*
     * Find tables by their status
     * @param status The status of the tables to find
     * @return A list of tables with the specified status
     */
    override fun findByStatus(status: TableStatus): List<Table> {
        return tables.values.filter { it.status == status}
    }

    /*
     * Update a table in the in-memory storage
     * @param table The table to be updated
     * @return true if the table was updated successfully
     */
    override fun update(table: Table): Boolean {
        val id = table.id
        if (!tables.containsKey(id)){
            return false
        }
        tables[id] = table
        return true
    }

    /*
     * Delete a table by its ID
     * @param id The ID of the table to delete
     * @return true if the table was deleted successfully
     */
    override fun delete(id: String): Boolean {
        return tables.remove(id) != null
    }
}
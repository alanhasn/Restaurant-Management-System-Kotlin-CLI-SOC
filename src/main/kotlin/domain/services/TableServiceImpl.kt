package domain.services

// Dependencies
import data.repository.TableRepository
import domain.models.Table
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Table Service Implementation
class TableServiceImpl(
    private val tableRepository: TableRepository
) : TableService {

    /*
     * Adds a new table to the repository.
     *
     * @param table The table to be added.
     * @return True if the table was successfully added, false otherwise.
     */
    override suspend fun addTable(table: Table): Boolean = withContext(Dispatchers.IO) {
        tableRepository.save(table)
    }

    /*
     * Updates an existing table in the repository.
     * @param id The ID of the table to be updated.
     * @param table The updated table data.
     * @return True if the table was successfully updated, false otherwise.
     */
    override suspend fun updateTable(id: String, table: Table): Boolean {
        return withContext(Dispatchers.IO) {
            tableRepository.update(table)
        }
    }

    /*
     * Deletes a table from the repository.
     * @param id The ID of the table to be deleted.
     * @return True if the table was successfully deleted, false otherwise.
     */
    override suspend fun deleteTable(id: String): Boolean = withContext(Dispatchers.IO) {
        tableRepository.delete(id)
    }

    /*
     * Retrieves a table from the repository by its ID.
     * @param id The ID of the table to retrieve.
     * @return The table object if found, null otherwise.
     */
    override suspend fun getTableById(id: String): Table? = withContext(Dispatchers.IO) {
        tableRepository.findById(id)
    }

    /*
     * Retrieves all tables from the repository.
     * @return A list of all tables in the repository.
     */
    override suspend fun listAllTables(): List<Table> = withContext(Dispatchers.IO) {
        tableRepository.findAll()
    }

}

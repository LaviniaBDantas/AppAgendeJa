package com.example.trabalhofinal.data_layer
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Database(entities = [Disciplina::class, Nota::class], version = 1)
abstract class TrabFinalDatabase : RoomDatabase() {
    abstract fun disciplinaDao(): DisciplinaDao
    abstract fun notaDao(): NotaDao
}

@Entity(tableName = "disciplina")
data class Disciplina(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val favoritada: Boolean
)

@Entity(tableName = "nota", foreignKeys = [ForeignKey(entity = Disciplina::class, parentColumns = ["id"], childColumns = ["idDisciplina"],
    onDelete = ForeignKey.CASCADE
)])
data class Nota(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val texto: String,
    val dataCriacao: Long = System.currentTimeMillis(),
    val idDisciplina: Int,
)

data class DisciplinaComNotas(
    @Embedded val disciplina: Disciplina,

    @Relation(
        parentColumn = "id",           // ID da disciplina
        entityColumn = "idDisciplina"  // Campo na tabela Nota que aponta para Disciplina
    )
    val notas: List<Nota>             // Lista de notas ligadas a essa disciplina
)

@Dao
interface DisciplinaDao {
    @Query("SELECT * FROM Disciplina ORDER BY favoritada DESC")
    fun getAllItems(): Flow<List<DisciplinaComNotas>>

    @Insert
    suspend fun insert(disciplina: Disciplina)

    @Query("UPDATE Disciplina SET favoritada = :favoritado WHERE id = :id")
    suspend fun updateFavoritado(id: Int, favoritado: Boolean)

    //
    @Query("DELETE FROM disciplina WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM disciplina WHERE id = :disciplinaId")
    fun getNotasByDisciplinaId(disciplinaId: Int): Flow<List<DisciplinaComNotas>>

    @Query("SELECT nome FROM disciplina WHERE id = :disciplinaId")
    fun getNomeDisciplina(disciplinaId: Int): Flow<String>

}

@Dao
interface NotaDao {
    @Query("SELECT * FROM Nota")
    fun getAllItems(): Flow<List<Nota>>

    @Insert
    suspend fun insert(nota: Nota)

//    @Update
//    suspend fun update(nota: Nota)

    @Query("DELETE FROM Nota WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM Nota")
    suspend fun deleteAllNotes()
}

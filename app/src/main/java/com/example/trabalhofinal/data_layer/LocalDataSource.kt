package com.example.aula17.data_layer

import androidx.room.ColumnInfo
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
import androidx.room.Transaction
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
    @Query("SELECT * FROM Disciplina")
    fun getAllItems(): Flow<List<DisciplinaComNotas>>
    @Insert
    suspend fun insert(disciplina: Disciplina)
//    @Update
//    suspend fun update(item: Item)
//
    @Query("DELETE FROM disciplina WHERE id = :id")
    suspend fun deleteById(id: Int)
}

@Dao
interface NotaDao {
    @Query("SELECT * FROM Nota")
    fun getAllItems(): Flow<List<Nota>>
    /*@Insert
    suspend fun insert(item: Item)
    @Update
    suspend fun update(item: Item)
    /* ... */
    @Query("DELETE FROM item WHERE id = :id")
    suspend fun deleteById(id: Int)*/
}

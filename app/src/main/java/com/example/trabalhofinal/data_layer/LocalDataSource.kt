package com.example.aula17.data_layer

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Database(entities = [Paciente::class, Medico::class, Consulta::class], version = 1)
abstract class TrabFinalDatabase : RoomDatabase() {
    abstract fun pacienteDao(): PacienteDao
    abstract fun medicoDao(): MedicoDao
    abstract fun consultaDao(): ConsultaDao
}

@Entity
data class Paciente(
    @PrimaryKey(autoGenerate = true)
    val cpf: Int = 0,
    val nome: String,
    val endereco: String,
    val telefone: String,
    val foto: String
)

@Entity
data class Medico(
    @PrimaryKey(autoGenerate = true)
    val crm: Int = 0,
    val nome: String,
    val endereco: String,
    val telefone: String,
    val foto: String,
    val especialidade: String
)

@Entity(tableName = "Consulta",foreignKeys = [ForeignKey(
    entity = Paciente::class,
    parentColumns = ["cpf"],
    childColumns = ["cpf_paciente"]
),ForeignKey(
    entity = Medico::class,
    parentColumns = ["crm"],
    childColumns = ["crm_medico"])
]
)
data class Consulta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cpf_paciente: Int,
    val crm_medico: Int,
    val data: String,
    val hora: String,
)
@Dao
interface PacienteDao {
    @Query("SELECT * FROM Paciente")
    fun getAllItems(): Flow<List<Paciente>>
    @Insert
    suspend fun insert(paciente: Paciente)
//    @Update
//    suspend fun update(item: Item)
//
//    @Query("DELETE FROM item WHERE id = :id")
//    suspend fun deleteById(id: Int)
}

@Dao
interface MedicoDao {
    @Query("SELECT * FROM Medico")
    fun getAllItems(): Flow<List<Medico>>
    /*@Insert
    suspend fun insert(item: Item)
    @Update
    suspend fun update(item: Item)
    /* ... */
    @Query("DELETE FROM item WHERE id = :id")
    suspend fun deleteById(id: Int)*/
}

@Dao
interface ConsultaDao {
    @Query("SELECT * FROM Consulta")
    fun getAllItems(): Flow<List<Consulta>>
    /*@Insert
    suspend fun insert(item: Item)
    @Update
    suspend fun update(item: Item)
    /* ... */
    @Query("DELETE FROM item WHERE id = :id")
    suspend fun deleteById(id: Int)*/
}
package com.denkiri.poscashier.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.poscashier.models.order.TotalAmount
@Dao
interface TotalAmountDao {
    @Query("SELECT *FROM TotalAmount LIMIT 1")
    fun getTotalAmount(): LiveData<TotalAmount>
    @Query("SELECT * FROM TotalAmount  LIMIT 1")
    fun fetch(): TotalAmount
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTotalAmount(model: TotalAmount )
    @Delete
    fun deleteTotalAmount(model: TotalAmount )
    @Query("DELETE FROM TotalAmount ")
    fun delete()
}
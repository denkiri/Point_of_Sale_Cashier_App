package com.denkiri.poscashier.storage.daos
import androidx.room.*
import com.denkiri.poscashier.models.order.Order
@Dao
interface OrderDao {
    @Query("SELECT  * FROM `Order`")
    fun getAll():List<Order>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(model: List<Order>)
    @Query("DELETE FROM `Order`")
    fun delete()
}
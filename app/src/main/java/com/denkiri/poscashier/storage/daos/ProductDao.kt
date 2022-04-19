package com.denkiri.poscashier.storage.daos
import androidx.lifecycle.LiveData
import androidx.room.*
import com.denkiri.poscashier.models.product.Product
@Dao
interface ProductDao {
    @Query("SELECT * FROM Product ORDER BY Product.productName")
    fun getAll():List<Product>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(model: List<Product>)
    @Query("DELETE FROM Product")
    fun delete()
}
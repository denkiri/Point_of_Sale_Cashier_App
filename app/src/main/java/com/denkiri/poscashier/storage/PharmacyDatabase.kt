package com.denkiri.poscashier.storage
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.denkiri.poscashier.models.oauth.Profile
import com.denkiri.poscashier.models.order.Order
import com.denkiri.poscashier.models.order.TotalAmount
import com.denkiri.poscashier.models.product.Product
import com.denkiri.poscashier.storage.daos.OrderDao
import com.denkiri.poscashier.storage.daos.ProductDao
import com.denkiri.poscashier.storage.daos.ProfileDao
import com.denkiri.poscashier.storage.daos.TotalAmountDao

@Database(entities = [Profile::class,Product::class,Order::class,TotalAmount::class],version = 1,exportSchema = false)
 abstract class PharmacyDatabase :RoomDatabase() {
     companion object{
         private lateinit var INSTANCE:PharmacyDatabase
         fun getDatabase(context: Context):PharmacyDatabase?{
             synchronized(PharmacyDatabase::class.java){
                 INSTANCE = Room.databaseBuilder(context.applicationContext,
                     PharmacyDatabase::class.java,"pos_database"
                 )
                     .fallbackToDestructiveMigration()
                     .allowMainThreadQueries()
                     .build()
             }
             return INSTANCE
         }}
    abstract fun profileDao(): ProfileDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao():OrderDao
    abstract fun totalAmountDao():TotalAmountDao




 }

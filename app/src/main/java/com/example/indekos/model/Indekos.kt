package com.example.indekos.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "indekos")
@Parcelize
data class Indekos(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "indekosId")
    val indekosId: Int = 0,

    @ColumnInfo(name = "userId")
    val userId: Int,

    @ColumnInfo(name = "name_indekos")
    val name_indekos: String?,

    @ColumnInfo(name = "harga")
    val harga: String?,

    @ColumnInfo(name = "jumlah_bedroom")
    val jumlah_bedroom: String?,

    @ColumnInfo(name = "jumlah_cupboard")
    val jumlah_cupboard: String?,

    @ColumnInfo(name = "jumlah_kitchen")
    val jumlah_kitchen: String?,

    @ColumnInfo(name = "latitude_indekos")
    val latitude_indekos: Double?,

    @ColumnInfo(name = "longitude_indekos")
    val longitude_indekos: Double?,

    @ColumnInfo(name = "alamat")
    val alamat: String?,

    @ColumnInfo(name = "kota")
    val kota: String?,

    @ColumnInfo(name = "provinsi")
    val provinsi: String?,

    @ColumnInfo(name = "photosUrl")
    val photoUrl: List<String>?,

    @ColumnInfo(name = "photoBannerUrl")
    val photoBannerUrl:String?,

) : Parcelable
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
    var indekosId: Int = 0,

    @ColumnInfo(name = "name_indekos")
    var name_indekos: String? = null,

    @ColumnInfo(name = "harga")
    var harga: String? = null,

    @ColumnInfo(name = "jumlah_bedroom")
    var jumlah_bedroom: String? = null,

    @ColumnInfo(name = "jumlah_cupboard")
    var jumlah_cupboard: String? = null,

    @ColumnInfo(name = "jumlah_kitchen")
    var jumlah_kitchen: String? = null,

    @ColumnInfo(name = "latitude_indekos")
    var latitude_indekos: String? = null,

    @ColumnInfo(name = "longitude_indekos")
    var longitude_indekos: String? = null,

    @ColumnInfo(name = "photoUrl")
    var photoUrl: String? = null,

    @ColumnInfo(name = "photoBannerUrl")
    var photoBannerUrl:String? = null,
) : Parcelable

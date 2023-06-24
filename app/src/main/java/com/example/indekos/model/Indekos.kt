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
    var name_indekos: String,

    @ColumnInfo(name = "harga")
    var harga: String,

    @ColumnInfo(name = "jumlah_bedroom")
    var jumlah_bedroom: String,

    @ColumnInfo(name = "jumlah_cupboard")
    var jumlah_cupboard: String,

    @ColumnInfo(name = "jumlah_kitchen")
    var jumlah_kitchen: String,

    @ColumnInfo(name = "latitude_indekos")
    var latitude_indekos: String,

    @ColumnInfo(name = "longitude_indekos")
    var longitude_indekos: String,

    @ColumnInfo(name = "photosUrl")
    var photoUrl: List<String>,

    @ColumnInfo(name = "photoBannerUrl")
    var photoBannerUrl:String,
) : Parcelable

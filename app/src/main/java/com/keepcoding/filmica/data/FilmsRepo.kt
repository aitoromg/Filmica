package com.keepcoding.filmica.data

import android.arch.persistence.room.Room
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object FilmsRepo {

    private val films: MutableList<Film> = mutableListOf()

    @Volatile
    private var db: AppDatabase? = null

    private fun getDbInstance(context: Context): AppDatabase {
        if (db == null) {

            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "filmica-db"
            ).build()
        }

        return db as AppDatabase
    }

    fun findFilmById(id: String): Film? {
        return films.find { film -> film.id == id }
    }

    fun saveFilm(
        context: Context,
        film: Film,
        callbackSuccess: (Film) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().insertFilm(film)
            }

            async.await()
            callbackSuccess.invoke(film)
        }
    }

    fun watchlist(
        context: Context,
        callbackSuccess: (List<Film>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().getFilms()
            }

            val films: List<Film> = async.await()
            callbackSuccess.invoke(films)
        }
    }

    fun deleteFilm(
        context: Context,
        film: Film,
        callbackSuccess: (Film) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().deleteFilm(film)
            }
            async.await()
            callbackSuccess.invoke(film)
        }
    }

    fun addNewFilm(newFilms: List<Film>) {
        newFilms.map {film ->
            if (!films.contains(film)) {
                films.add(film)
            }
        }
    }

    fun dummyFilms(): List<Film> {
        return (0..9).map { i ->
            Film(
                title = "Film $i",
                overview = "Overview $i",
                genre = "Genre $i",
                voteRating = i.toDouble(),
                release = "200$i-0$i-0$i"
            )
        }
    }

}
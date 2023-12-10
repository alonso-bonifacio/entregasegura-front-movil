package idat.gomez.entregasegura;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DaoSesion {

    @Query("SELECT * FROM sesion WHERE uuid =:uuid")
    Sesion buscarSesion(String uuid);

    @Insert
    void insertarSesion(Sesion sesion);

    @Query("DELETE FROM sesion WHERE uuid =:uuid")
    void eliminarSesion(String uuid);
}

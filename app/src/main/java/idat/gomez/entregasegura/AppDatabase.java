package idat.gomez.entregasegura;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {Sesion.class},
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoSesion daoSesion();
}

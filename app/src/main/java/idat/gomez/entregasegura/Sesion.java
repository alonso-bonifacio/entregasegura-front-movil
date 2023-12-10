package idat.gomez.entregasegura;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Sesion {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "uuid")
    public String uuid;

    public Sesion(@NotNull String uuid) {
        this.uuid = uuid;
    }
}

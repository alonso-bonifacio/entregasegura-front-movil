package idat.gomez.entregasegura;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {
    @GET
    Call<Usuario> getByUser(@Url @NotNull String url);
}

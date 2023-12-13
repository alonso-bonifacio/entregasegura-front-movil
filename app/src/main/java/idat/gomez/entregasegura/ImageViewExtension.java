package idat.gomez.entregasegura;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ImageViewExtension {
    public static final void fromUrl(@NotNull ImageView profileFoto, String url) {
        Picasso.get().load(url).into(profileFoto);
    }
}

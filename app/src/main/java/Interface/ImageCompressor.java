package Interface;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

public interface ImageCompressor {

    Bitmap compressBitmap(Context context, Uri imageUri);
}

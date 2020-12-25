package come.thing.ranker.gallery;

import android.net.Uri;

public class GalleryItem {
    Uri uri;
    boolean selected = false;

    public GalleryItem(Uri uri) {
        this.uri = uri;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Uri getUri() {
        return uri;
    }
}

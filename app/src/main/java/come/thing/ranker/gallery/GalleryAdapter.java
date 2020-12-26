package come.thing.ranker.gallery;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import come.thing.ranker.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private final List<GalleryItem> localDataSet;
    private final ImageButton button;
    private int numSelected = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public GalleryAdapter(List<GalleryItem> dataSet, ImageButton button) {
        localDataSet = dataSet;
        this.button = button;
        button.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.gallery_image_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        GalleryItem item = localDataSet.get(position);
        ImageView view = viewHolder.getImageView();
        view.setImageURI(item.getUri());
        final int TINT = Color.argb(135, 123, 227, 238);
        view.setColorFilter(item.isSelected() ? TINT : Color.TRANSPARENT);
        view.setOnClickListener(view1 -> {
            item.setSelected(!item.isSelected());
            view.setColorFilter(item.isSelected() ? TINT : Color.TRANSPARENT);
            numSelected += item.isSelected() ? 1 : -1;
            if (numSelected > 0)
                button.setVisibility(View.VISIBLE);
            else
                button.setVisibility(View.GONE);
        });

    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public List<Integer> getSelectedIndicesReverse() {
        List<Integer> list = new ArrayList<>();
        for (int i = getItemCount() - 1; i >= 0; i--)
            if (localDataSet.get(i).isSelected()) list.add(i);
        return list;
    }
}
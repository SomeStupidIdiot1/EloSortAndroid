package come.thing.ranker.ranking;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import come.thing.ranker.R;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    private final List<Uri> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            textView = view.findViewById(R.id.textView2);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public RankingAdapter(List<Uri> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ranking_image_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Uri item = localDataSet.get(position);
        ImageView view = viewHolder.getImageView();
        TextView textView = viewHolder.getTextView();
        textView.setText(String.valueOf(position + 1));
        view.setImageURI(item);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
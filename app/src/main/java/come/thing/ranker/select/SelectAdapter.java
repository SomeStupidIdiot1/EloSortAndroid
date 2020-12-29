package come.thing.ranker.select;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import come.thing.ranker.PlayActivity;
import come.thing.ranker.R;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {

    private final File[] localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button button;
        private final Context context;

        public ViewHolder(View view) {
            super(view);
            button = view.findViewById(R.id.button13);
            context = view.getContext();
        }

        public Button getButton() {
            return button;
        }

        public Context getContext() {
            return context;
        }
    }

    public SelectAdapter(File[] dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.select_button_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Button button = viewHolder.getButton();
        button.setText(localDataSet[position].getName());
        Context context = viewHolder.getContext();
        button.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlayActivity.class);
            intent.setData(Uri.fromFile(localDataSet[position]));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
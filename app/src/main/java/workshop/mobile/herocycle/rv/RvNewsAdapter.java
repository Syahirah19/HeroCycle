package workshop.mobile.herocycle.rv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import workshop.mobile.herocycle.R;
import workshop.mobile.herocycle.model.RvNews;

public class RvNewsAdapter extends RecyclerView.Adapter<RvNewsAdapter.RvNewsHolder> {
    private Context context;
    private final ArrayList<RvNews> item;

    public RvNewsAdapter(Context context, ArrayList<RvNews> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public RvNewsAdapter.RvNewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rv_news, parent, false);
        return new RvNewsAdapter.RvNewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvNewsAdapter.RvNewsHolder holder, int position) {
        RvNews currentNew = item.get(position);
        holder.title.setText(currentNew.getTitle());
        holder.text.setText(currentNew.getText());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class RvNewsHolder extends RecyclerView.ViewHolder{

        TextView title,text;

        public RvNewsHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);

        }
    }
}

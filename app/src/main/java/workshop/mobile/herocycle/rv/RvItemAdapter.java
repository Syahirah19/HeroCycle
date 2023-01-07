package workshop.mobile.herocycle.rv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import workshop.mobile.herocycle.R;
import workshop.mobile.herocycle.model.RvItem;

public class RvItemAdapter extends RecyclerView.Adapter<RvItemAdapter.RvItemHolder> {
    private Context context;
    private final ArrayList<RvItem> item;

    public RvItemAdapter(Context context, ArrayList<RvItem> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public RvItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rv_item, parent, false);
        return new RvItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvItemHolder holder, int position) {
        RvItem currentItem = item.get(position);
        holder.textView.setText(currentItem.getText());
        holder.imageView.setImageResource(currentItem.getIcon());

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class RvItemHolder extends  RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public RvItemHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
package workshop.mobile.herocycle.rv;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import workshop.mobile.herocycle.News_1;
import workshop.mobile.herocycle.News_2;
import workshop.mobile.herocycle.News_3;
import workshop.mobile.herocycle.News_4;
import workshop.mobile.herocycle.News_5;
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
        RvNews currentNews = item.get(position);
        holder.title.setText(currentNews.getTitle());
        holder.imageView.setImageResource(currentNews.getImage());

        holder.linearLayout.setOnClickListener(view -> {

            Intent intent = null;

            if (holder.getLayoutPosition() == 0){
                intent = new Intent(view.getContext(), News_1.class);
            } else if (holder.getLayoutPosition() == 1){
                intent = new Intent(view.getContext(), News_2.class);
            } else if (holder.getLayoutPosition() == 2){
                intent = new Intent(view.getContext(), News_3.class);
            } else if (holder.getLayoutPosition() == 3){
                intent = new Intent(view.getContext(), News_4.class);
            } else if (holder.getLayoutPosition() == 4){
                intent = new Intent(view.getContext(), News_5.class);
            }

            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class RvNewsHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView imageView;
        LinearLayout linearLayout;

        public RvNewsHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
            linearLayout = itemView.findViewById(R.id.linearlayout);

        }
    }
}

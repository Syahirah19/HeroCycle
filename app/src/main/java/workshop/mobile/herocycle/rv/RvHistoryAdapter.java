package workshop.mobile.herocycle.rv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import workshop.mobile.herocycle.R;
import workshop.mobile.herocycle.model.RvHistory;

public class RvHistoryAdapter extends RecyclerView.Adapter<RvHistoryAdapter.RvHistoryHolder> {
    private Context context;
    private final ArrayList<RvHistory> item;

    public RvHistoryAdapter(Context context, ArrayList<RvHistory> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public RvHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rv_history,parent,false);
        return new RvHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvHistoryHolder holder, int position) {
        RvHistory currentItem = item.get(position);

        holder.txtReqDate.setText(currentItem.getReqDate());
        holder.txtColDate.setText(currentItem.getColDate());
        holder.txtColPrice.setText(currentItem.getCollPrice());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class RvHistoryHolder extends RecyclerView.ViewHolder{
        TextView txtReqDate, txtColDate, txtColPrice;

        public RvHistoryHolder(@NonNull View itemView) {
            super(itemView);

            txtReqDate = itemView.findViewById(R.id.txtReqDate);
            txtColDate = itemView.findViewById(R.id.txtColDate);
            txtColPrice = itemView.findViewById(R.id.txtColPrice);
        }
    }

}

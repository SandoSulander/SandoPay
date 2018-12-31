package store.catsocket.sandopay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {
    private List<AccountTransaction> accountTransactions = new ArrayList<>();

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, parent, false);
        return new TransactionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        AccountTransaction currentAccountTransaction = accountTransactions.get(position);
        holder.textviewFrom.setText("From: "+currentAccountTransaction.getFromAccountNumber());
        holder.textviewTo.setText("To: "+currentAccountTransaction.getToAccountNumber());
        holder.textviewDate.setText("Time: "+currentAccountTransaction.getDateString());
        holder.textviewMessage.setText("Message: " + currentAccountTransaction.getMessage());
        holder.textviewTransaction.setText(String.valueOf((currentAccountTransaction.getTransaction()))+"€");

    }

    @Override
    public int getItemCount() {
        return accountTransactions.size();
    }

    public void setAccountTransactions(List<AccountTransaction> accountTransactions) {
        this.accountTransactions = accountTransactions;
        notifyDataSetChanged();
    }

    class TransactionHolder extends RecyclerView.ViewHolder{

        private TextView textviewFrom;
        private TextView textviewTo;
        private TextView textviewDate;
        private TextView textviewMessage;
        private TextView textviewTransaction;

        public TransactionHolder(View itemView){
            super(itemView);
            textviewFrom = itemView.findViewById(R.id.text_view_from);
            textviewTo = itemView.findViewById(R.id.text_view_to);
            textviewDate = itemView.findViewById(R.id.text_view_date);
            textviewMessage = itemView.findViewById(R.id.text_view_message);
            textviewTransaction = itemView.findViewById(R.id.text_view_transaction);
        }
    }
}

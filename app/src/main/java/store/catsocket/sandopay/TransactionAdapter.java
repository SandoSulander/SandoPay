package store.catsocket.sandopay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/* This Class works as An Adapter, which is an object that acts as a bridge between an AdapterView and the underlying data for that view.
   It provides access to the data items. The Adapter is also responsible for making a View for each item in the data set.
   This adapter extends RecyclerView.Adapter, which is a base class for presenting List data in a RecyclerView, including computing diffs between Lists on a background thread.
   In other words, it acts as a convenience wrapper around AsyncListDiffer that implements Adapter's common default behavior for item access and counting.
   Furthermore, while using a LiveData<List> is an easy way to provide data to the adapter, it isn't required - we can use submitList(List) instead,
   when ever new lists are available. Finally, to get the list comparison to work properly, this class also utilizes DiffUtil.
   It is a utility class that can calculate the difference between two lists and output a list of update operations that converts the first list into the second one.
   It can be used to calculate updates for a RecyclerView Adapter. */


public class TransactionAdapter extends ListAdapter<AccountTransaction, TransactionAdapter.TransactionHolder> {

    public TransactionAdapter() {
        super(DIFF_CALLBACK);
    }
    // Check if the item hardcoded id is the same
    private static final DiffUtil.ItemCallback<AccountTransaction> DIFF_CALLBACK = new DiffUtil.ItemCallback<AccountTransaction>() {
        @Override
        public boolean areItemsTheSame(@NonNull AccountTransaction oldItem, @NonNull AccountTransaction newItem) {
            return oldItem.getId() == newItem.getId();
        }

        // Check if the adapter has to update the item or not -- Execute only if everything is the same.
        @Override
        public boolean areContentsTheSame(@NonNull AccountTransaction oldItem, @NonNull AccountTransaction newItem) {
            return  oldItem.getAccountId() == newItem.getAccountId() &&
                    oldItem.getTransaction() == newItem.getTransaction() &&
                    oldItem.getUserId() == newItem.getUserId() &&
                    oldItem.getDateString().equals(newItem.getDateString())&&
                    oldItem.getFromAccountNumber().equals(newItem.getFromAccountNumber()) &&
                    oldItem.getToAccountNumber().equals(newItem.getToAccountNumber()) &&
                    oldItem.getMessage().equals(newItem.getMessage());}
    };


    /* Inflate the correct Item LayOut */
    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, parent, false);
        return new TransactionHolder(itemView);
    }

    /* Set the correct position of the item and the correct content into the Item */
    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        AccountTransaction currentAccountTransaction = getItem(position);
        holder.textViewFrom.setText("From: "+currentAccountTransaction.getFromAccountNumber());
        holder.textViewTo.setText("To: "+currentAccountTransaction.getToAccountNumber());
        holder.textViewDate.setText("Time: "+currentAccountTransaction.getDateString());
        holder.textViewMessage.setText("Message: " + currentAccountTransaction.getMessage());
        holder.textViewTransaction.setText(String.valueOf((currentAccountTransaction.getTransaction()))+"€");

    }

    public AccountTransaction getTransactionAt(int position) {
        return getItem(position);
    }

    /* This class describes an item view and metadata about its place within the RecyclerView. */

    class TransactionHolder extends RecyclerView.ViewHolder{

        private TextView textViewFrom;
        private TextView textViewTo;
        private TextView textViewDate;
        private TextView textViewMessage;
        private TextView textViewTransaction;

        public TransactionHolder(View itemView){
            super(itemView);
            textViewFrom = itemView.findViewById(R.id.text_view_from);
            textViewTo = itemView.findViewById(R.id.text_view_to);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewMessage = itemView.findViewById(R.id.text_view_message);
            textViewTransaction = itemView.findViewById(R.id.text_view_transaction);
        }
    }
}

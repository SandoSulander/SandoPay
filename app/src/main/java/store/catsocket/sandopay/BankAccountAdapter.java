package store.catsocket.sandopay;

import android.annotation.SuppressLint;
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

public class BankAccountAdapter extends ListAdapter<DebitAccount, BankAccountAdapter.BankAccountHolder> {
    private OnItemClickListener debitAccountListener; //TODO Credit Account listener?

    public BankAccountAdapter() {
        super(DIFF_CALLBACK);
    }

    // Check if the item hardcoded id is the same
    private static final DiffUtil.ItemCallback<DebitAccount> DIFF_CALLBACK = new DiffUtil.ItemCallback<DebitAccount>() {
        @Override
        public boolean areItemsTheSame(@NonNull DebitAccount oldItem, @NonNull DebitAccount newItem) {
            return oldItem.getId() == newItem.getId();
        }

        // Check if the adapter has to update the item or not -- Execute only if everything is the same.
        @Override
        public boolean areContentsTheSame(@NonNull DebitAccount oldItem, @NonNull DebitAccount newItem) {
            return oldItem.getAccountNumber().equals(newItem.getAccountNumber()) &&
                    oldItem.getBalance() == newItem.getBalance();
        }
    };

    /* Inflate the correct Item LayOut */
    @NonNull
    @Override
    public BankAccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_item, parent, false);
        return new BankAccountHolder(itemView);
    }

    /* Set the correct position of the item and the correct content into the Item */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BankAccountHolder holder, int position) {
        DebitAccount currentDebitAccount = getItem(position);
        holder.textViewAccountNumber.setText("Account number: " + currentDebitAccount.getAccountNumber());
        holder.textViewBlance.setText("Balance: " + String.valueOf(currentDebitAccount.getBalance()) + "€");
    }


    public DebitAccount getDebitAccountAt(int position) {
        return getItem(position);
    }

    /* This class describes an item view and metadata about its place within the RecyclerView. */
    class BankAccountHolder extends RecyclerView.ViewHolder {

        private TextView textViewAccountNumber;
        private TextView textViewBlance;
        private TextView textViewCredit; //TODO - When CreditAccount created

        public BankAccountHolder(View itemView) {
            super(itemView);
            textViewAccountNumber = itemView.findViewById(R.id.text_view_account_number);
            textViewBlance = itemView.findViewById(R.id.text_view_balance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (debitAccountListener != null && position != RecyclerView.NO_POSITION) {
                        debitAccountListener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DebitAccount debitAccount);
    }

    public void setOnItemClickListener(OnItemClickListener debitAccountListener) {
        this.debitAccountListener = debitAccountListener;
    }

}

package com.example.prm392_su25.Adapter.PaymentHistory;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_su25.Model.Payment.PaymentHistory;
import com.example.prm392_su25.R;

import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.PaymentViewHolder> {

    private List<PaymentHistory> paymentList;

    public PaymentHistoryAdapter(List<PaymentHistory> paymentList) {
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_history, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        PaymentHistory payment = paymentList.get(position);
        holder.tvOrderId.setText("Mã đơn: #" + payment.orderId);
        holder.tvDate.setText("Ngày: " + payment.paymentDate.substring(0, 10));
        holder.tvAmount.setText("Tổng tiền: " + payment.amount + "₫");
        holder.tvStatus.setText("Trạng thái: " + payment.paymentStatus);

        // Nested RecyclerView
        ProductItemAdapter itemAdapter = new ProductItemAdapter(payment.items);
        holder.rvItems.setAdapter(itemAdapter);
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvDate, tvAmount, tvStatus;
        RecyclerView rvItems;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            rvItems = itemView.findViewById(R.id.rvProducts);
            rvItems.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}

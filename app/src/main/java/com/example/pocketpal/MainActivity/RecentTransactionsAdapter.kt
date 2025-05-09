package com.example.pocketpal.MainActivity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketpal.database.Expense
import com.example.pocketpal.databinding.ItemExpenseBinding

class RecentTransactionsAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecentTransactionsAdapter.RecentTransactionsViewHolder>() {

    private var recentTransactions : List<Expense> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Expense>){
        this.recentTransactions = list
        notifyDataSetChanged()
    }

    inner class RecentTransactionsViewHolder(val bind: ItemExpenseBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentTransactionsViewHolder {
        val bind = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentTransactionsViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return recentTransactions.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecentTransactionsViewHolder, position: Int) {
        val currentRecentTransaction = recentTransactions[position]
        holder.bind.apply {
            spentAmount.text = "-₹${currentRecentTransaction.amount}"
            val categoryImageName = currentRecentTransaction.category?.lowercase()
            val imageResource = context.resources.getIdentifier(
                "$categoryImageName",
                "drawable",
                context.packageName
            )
            ivCategory.setImageResource(imageResource)
            tvCategory.text = currentRecentTransaction.category
            val accountTypeImageName = when (currentRecentTransaction.paymentMode) {
                1 -> "card"
                2 -> "cash"
                3 -> "qr_code"
                else -> ""
            }
            val accountTypeImageResource = context.resources.getIdentifier(
                accountTypeImageName,
                "drawable",
                context.packageName
            )
            ivAccountType.setImageResource(accountTypeImageResource)
            val accountTypeText = when (currentRecentTransaction.paymentMode) {
                1 -> "Card"
                2 -> "Cash"
                3 -> "Upi"
                else -> ""
            }
            tvAccountType.text = accountTypeText

            itemRecords.isClickable = false
            itemRecords.isFocusable = false
        }
    }
}
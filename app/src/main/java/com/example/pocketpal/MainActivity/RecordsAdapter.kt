package com.example.pocketpal.MainActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketpal.database.Expense
import com.example.pocketpal.databinding.ItemExpenseBinding

class RecordsAdapter(
    private val context: Context,
    private val navController: NavController,
    private val onClickListener: (Expense) -> Unit
) : RecyclerView.Adapter<RecordsAdapter.RecordsViewHolder>() {

    private var expenseDetails: List<Expense> = emptyList()

    fun submitList(list: List<Expense>) {
        this.expenseDetails = list
        notifyDataSetChanged()
    }

    inner class RecordsViewHolder(val bind: ItemExpenseBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordsViewHolder {
        val bind = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordsViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return expenseDetails.size
    }

    override fun onBindViewHolder(holder: RecordsViewHolder, position: Int) {
        val currentExpense = expenseDetails[position]
        holder.bind.apply {
            spentAmount.text = "-₹${currentExpense.amount}"
            val categoryImageName = currentExpense.category?.lowercase()
            val imageResource = context.resources.getIdentifier(
                "$categoryImageName",
                "drawable",
                context.packageName
            )
            ivCategory.setImageResource(imageResource)
            tvCategory.text = currentExpense.category
            val accountTypeImageName = when (currentExpense.paymentMode) {
                1 -> "card"
                2 -> "cash"
                3 -> "qr_code"
                else -> ""
            }
            val accountTypeImageResource = context.resources.getIdentifier(
                "$accountTypeImageName",
                "drawable",
                context.packageName
            )
            ivAccountType.setImageResource(accountTypeImageResource)
            val accountTypeText = when (currentExpense.paymentMode) {
                1 -> "Card"
                2 -> "Cash"
                3 -> "Upi"
                else -> ""
            }
            tvAccountType.text = accountTypeText

            itemRecords.setOnClickListener {
                onClickListener(currentExpense)
            }
        }
    }

}
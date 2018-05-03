package com.guna.firebasedatabase

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.employee.view.*

class EmployeeAdapter(options: FirestoreRecyclerOptions<Employee>) : FirestoreRecyclerAdapter<Employee, EmployeeAdapter.ViewHolder>(options) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Employee) {
        holder.bind(model)
    }

    private val TAG: String = "EmployeeAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee, parent, false));
    }

    override fun onDataChanged() {
        super.onDataChanged()
        Log.v(TAG, "onDataChanged")
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: Employee) {
            itemView.textName.text = model.getName()
            itemView.textEmail.text = model.getEmail()
            itemView.textPhone.text = model.getPhoneNumber()
        }

    }
}
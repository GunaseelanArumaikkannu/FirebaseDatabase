package com.guna.firebasedatabase

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainFragment : Fragment() {

    private lateinit var mFirestore: FirebaseFirestore
    private var mQuery: Query? = null
    private val TAG: String = "MainFragment"
    val paint = Paint()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFirestore = FirebaseFirestore.getInstance();

        // Get employees
        /*mFirestore.collection("employee").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    Log.d(TAG, document.id + " => " + document.data)
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.exception)
            }
        }*/

        val query = mFirestore.collection("Employees")
        usersList.setHasFixedSize(true)
        usersList.layoutManager = activity?.let { LinearLayoutManager(it) }

        val options = FirestoreRecyclerOptions.Builder<Employee>()
                .setQuery(query, Employee::class.java)
                .setLifecycleOwner(this)
                .build()

        val adapter = EmployeeAdapter(options)
        usersList.adapter = adapter

        addSwipe(adapter, query)
    }

    private fun addSwipe(adapter: EmployeeAdapter, query: CollectionReference) {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                val employee = adapter.getItem(viewHolder!!.adapterPosition)
                val id = adapter.snapshots.getSnapshot(viewHolder.adapterPosition).id
                if (direction == ItemTouchHelper.LEFT) {
                    query.document(id).delete()
                    usersList.adapter.notifyDataSetChanged()
                } else if (direction == ItemTouchHelper.RIGHT) {
                    val intent = Intent(activity, AddEmployeeActivity::class.java);
                    intent.putExtra("Employee", employee)
                    intent.putExtra("Id", id)
                    startActivity(intent)
                }
            }

            override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                if (dX > 0) {
                    paint.color = Color.parseColor("#009688")
                    val rect = Rect(viewHolder!!.itemView!!.left, viewHolder.itemView.top, dX.toInt(), viewHolder.itemView.bottom)
                    c?.drawRect(rect, paint)
                } else if (dX < 0) {
                    paint.color = Color.parseColor("#FF9800")
                    val rect = Rect(viewHolder!!.itemView.right.plus(dX).toInt(), viewHolder.itemView.top, viewHolder.itemView.right, viewHolder.itemView.bottom)
                    c?.drawRect(rect, paint)
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(usersList)
    }
}

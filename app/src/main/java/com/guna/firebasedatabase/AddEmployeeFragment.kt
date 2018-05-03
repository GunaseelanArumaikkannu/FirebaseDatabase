package com.guna.firebasedatabase

import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_add_employee.*
import com.google.firebase.firestore.CollectionReference


/**
 * A placeholder fragment containing a simple view.
 */
class AddEmployeeFragment : Fragment() {

    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var employees: CollectionReference
    private val TAG = "AddEmployeeFragment"
    private var docName = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFirestore = FirebaseFirestore.getInstance();
        employees = mFirestore.collection("Employees")

        //Create employee using hashmap
        /*val employee = HashMap<String, Any>()
        employee.put("name", "Guna")
        employee.put("phoneNumber", "1144778855")
        employee.put("email", "Guna@gmail.com")*/

        //Add a new document with a generated ID
        /* mFirestore.collection("employees")
                 .add(employee)
                 .addOnSuccessListener({ documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id) })
                 .addOnFailureListener({ e -> Log.w(TAG, "Error adding document", e) })*/

        val employee = activity!!.intent.getParcelableExtra<Employee>("Employee")
        if (employee != null) {
            docName = activity!!.intent.getStringExtra("Id")
            btnSubmit.setText("Save Employee")
            etName.setText(employee.getName())
            etPhone.setText(employee.getPhoneNumber())
            etEmail.setText(employee.getEmail())
        }
        btnSubmit.setOnClickListener({
            save()
        })
    }

    private fun save() {
        val name = etName.text.toString()
        val phone = etPhone.text.toString()
        val email = etEmail.text.toString()
        if (!name.isEmpty()) {
            val employee = Employee(name, phone, email)
            if (docName.isEmpty()) {
                docName = name
            }
            employees.document(docName).set(employee).addOnSuccessListener { activity?.finish() }.addOnFailureListener({ e -> Log.w(TAG, "Error adding document", e) })
        } else {
            etName.error = "Name is required"
        }
    }
}

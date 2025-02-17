package com.example.nativeshop.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nativeshop.Model.CategoryModel
import com.example.nativeshop.Model.ItemModel
import com.example.nativeshop.Model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel: ViewModel()  {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _banner = MutableLiveData<List<SliderModel>>()
    private val _categories = MutableLiveData<List<CategoryModel>>()
    private val _recommended = MutableLiveData<List<ItemModel>>()

    val banners: LiveData<List<SliderModel>> = _banner
    val categories: LiveData<List<CategoryModel>> = _categories
    val recommended: LiveData<List<ItemModel>> = _recommended

    fun fetchBanners() {
        val firebaseRef = firebaseDatabase.getReference("Banner")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _banner.value = lists
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun fetchCategories() {
        val firebaseRef = firebaseDatabase.getReference("Category")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<CategoryModel>()
                for (childrenSnapshot in snapshot.children) {
                    val list = childrenSnapshot.getValue(CategoryModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _categories.value = lists
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun fetchRecommended() {
        val firebaseRef = firebaseDatabase.getReference("Items")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemModel>()
                for (childrenSnapshot in snapshot.children) {
                    val list = childrenSnapshot.getValue(ItemModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _recommended.value = lists
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
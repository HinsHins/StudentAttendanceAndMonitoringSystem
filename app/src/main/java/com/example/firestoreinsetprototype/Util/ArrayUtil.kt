package com.example.firestoreinsetprototype.Util

import com.example.firestoreinsetprototype.Model.Model
import com.google.firebase.firestore.QueryDocumentSnapshot

object ArrayUtil {
    fun hasItem(arr: ArrayList<Model>, item: Model): Model? {
        for(index in 0 until arr.size){
            if(item.id == arr[index].id)
                return arr[index]
        }
        return null
    }

    fun hasItem(arr: ArrayList<QueryDocumentSnapshot>, item: QueryDocumentSnapshot): QueryDocumentSnapshot? {
        for(index in 0 until arr.size){
            if(item.id == arr[index].id)
                return arr[index]
        }
        return null
    }

}
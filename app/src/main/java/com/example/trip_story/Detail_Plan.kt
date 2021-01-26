package com.example.trip_story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trip_story.model.ContentDTO
import com.example.trip_story.model.DetailDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail__plan.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_detail.view.*
import kotlinx.android.synthetic.main.item_home.view.*
import kotlinx.android.synthetic.main.item_home.view.detailviewitem_imageview_content

class Detail_Plan : AppCompatActivity() {
    var firestore: FirebaseFirestore? = null //db에 연결하기 위함
    var uid = FirebaseAuth.getInstance().currentUser?.uid   //transaction을 위해 uid를 받아옴
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail__plan)

        toolbar_back.setOnClickListener{
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }

        var adapter = Detail_Plan_Adatper()
        detail_recyclerview.adapter=adapter
        detail_recyclerview.layoutManager = LinearLayoutManager(this)
    }
    inner class Detail_Plan_Adatper : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var detailDTOs : ArrayList<DetailDTO> = arrayListOf()
        var detailUidList : ArrayList<String> = arrayListOf()

        init {
            //db에 접근을 하여 data를 받아올 수 있는 쿼리를 만듬
            firestore?.collection("ImageFolder2")?.orderBy("timestamp")
                //시간순으로 받아올 수 있도록 timeastamp
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    detailDTOs.clear() //array를 초기화
                    detailUidList.clear()

                    if (querySnapshot == null) return@addSnapshotListener
                    //snapshot에 넘어오는 데이터를 하나하나 읽어줌
                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(DetailDTO::class.java)
                        detailDTOs.add(item!!)
                        detailUidList.add(snapshot.id)
                    }
                    notifyDataSetChanged() //값이 변하면 새로고침 됨
                }
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(p0.context).inflate(R.layout.item_detail, p0, false)
            return DetailViewHolder(view)
        }
        //일종의 약속.. recylcerview는 viewholder를 꼭 써야함
        inner class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return detailDTOs.size
        }

        //서버에서 넘어온 데이터들을 mapping
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewholder = (holder as DetailViewHolder).itemView

            Glide.with(holder.itemView.context).load(detailDTOs!![position].imageUrl)
                .into(viewholder.todo_img)

            viewholder.todo_title.text = detailDTOs!![position].trip_name

            viewholder.todo_day.text = detailDTOs!![position].timestamp.toString()

            }
    }
}
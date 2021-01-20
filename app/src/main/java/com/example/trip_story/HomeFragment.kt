package com.example.trip_story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.navigation.UserFragment
import com.example.trip_story.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_home.*
import kotlinx.android.synthetic.main.item_home.view.*

class HomeFragment : Fragment() {
    var firestore: FirebaseFirestore? = null //db에 연결하기 위함
    var uid = FirebaseAuth.getInstance().currentUser?.uid   //transaction을 위해 uid를 받아옴
    private lateinit var myContext: Context
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myContext = requireContext()
//        detailviewitem_detail_plan.setOnClickListener{
//            val intent = Intent(myContext,Detail_Plan::class.java)
//            startActivity(intent)
//        }

        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_home, container, false)
        firestore = FirebaseFirestore.getInstance() //초기화

        view.home_recycler_view.adapter = DetailViewRecyclerViewAdapter()
        view.home_recycler_view.layoutManager = LinearLayoutManager(activity)
        return view
    }

    inner class DetailViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var contentDTOs: ArrayList<ContentDTO> = arrayListOf() //contentDTO를 담기 위해
        var contentUidList: ArrayList<String> = arrayListOf()  //uid를 담기 위해

        init {
            //db에 접근을 하여 data를 받아올 수 있는 쿼리를 만듬
            firestore?.collection("images")?.orderBy("timestamp")
                    //시간순으로 받아올 수 있도록 timeastamp
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    contentDTOs.clear() //array를 초기화
                    contentUidList.clear()

                    if (querySnapshot == null) return@addSnapshotListener
                    //snapshot에 넘어오는 데이터를 하나하나 읽어줌
                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(ContentDTO::class.java)
                        contentDTOs.add(item!!)
                        contentUidList.add(snapshot.id)
                    }
                    notifyDataSetChanged() //값이 변하면 새로고침 됨
                }
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(p0.context).inflate(R.layout.item_home, p0, false)
            return CustomViewHolder(view)
        }
        //일종의 약속.. recylcerview는 viewholder를 꼭 써야함
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return contentDTOs.size
        }
        //서버에서 넘어온 데이터들을 mapping
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewholder = (holder as CustomViewHolder).itemView

            //UserId
            viewholder.detailviewitem_profile_text.text = contentDTOs!![position].userId

            //Image
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl)
                .into(viewholder.detailviewitem_imageview_content)

            //explain
            viewholder.detailviewitem_explain_textview.text = contentDTOs!![position].explain

            //name
            viewholder.detailviewitem_name.text = contentDTOs!![position].trip_name

            //time
            viewholder.detailviewitem_time.text = contentDTOs!![position].timestamp.toString()

            //likes
            viewholder.detailviewitem_favoritecounter_textview.text =
                "Likes " + contentDTOs!![position].favoriteCount

            //Profilesimages
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl)
                .into(viewholder.detailviewitem_profile_image)

            //like버튼에 이벤트를 달아줌
            viewholder.detailviewitem_favorite_imageview.setOnClickListener() {
                favoriteEvent(position)
            }

            //하트가 색칠되거나 비어있는 코드
            if (contentDTOs!![position].favorities.containsKey(uid)) {
                viewholder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite)
            } else {
                viewholder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite_border)
            }

            //프로필 이미지를 클릭했을 때 상대방 USER정보로 이동하는 코드
            viewholder.detailviewitem_profile_image.setOnClickListener {
                var fragment = UserFragment()
                var bundle = Bundle()
                bundle.putString("destinationUid", contentDTOs[position].uid) //선택된 uid값을 받아옴
                bundle.putString("userId", contentDTOs[position].userId) //이메일 값을 받아옴
                fragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_layout, fragment)?.commit()
            }
            viewholder.detailviewitem_detail_plan.setOnClickListener{
                var intent = Intent(myContext,Detail_Plan::class.java)
                startActivity(intent)
            }
        }
            /*
            //말풍선을 클릭하면 commentactivity가 나오게끔
            viewholder.detailviewitem_comment_imageview.setOnClickListener{v->
                var intent = Intent(v.context,CommentActivity::class.java)
                intent.putExtra("contentUid",contentUidList[position])
                intent.putExtra("destinationUid",contentDTOs[position].uid)
                startActivity(intent)
            }
        }
        }
             */
        fun favoriteEvent(position : Int){
            var tsDoc = firestore?.collection("images")?.document(contentUidList[position])
            //내가 선택한 컨탠츠의 uid를 받아와서 좋아요를 해주는 이벤트를 만들어줌
            //데이터를 입력하기 위해 transaction필요
            firestore?.runTransaction{transaction ->


                var contentDTO = transaction.get(tsDoc!!).toObject(ContentDTO::class.java) //ts의 데이터를 contentdto로 캐스팅

                if(contentDTO!!.favorities.containsKey(uid)){
                    //좋아요 버튼이 이미 클릭되어 있을 경우
                    contentDTO?.favoriteCount = contentDTO?.favoriteCount -1
                    contentDTO?.favorities.remove(uid)
                }else{
                    //좋아요 버튼이 클릭되어 있지 않을 경우
                    contentDTO?.favoriteCount = contentDTO?.favoriteCount +1
                    contentDTO?.favorities[uid!!] = true //uid값을 추가
                 //favoriteAlarm(contentDTOs[position].uid!!) //카운터가 올라갈때 알람
                }
                transaction.set(tsDoc,contentDTO)//ts를 서버로 돌려줌
            }
        }
            /*
        //좋아요버튼을 눌렀을때 알림 이벤트
        fun favoriteAlarm(destinationUid : String){
            var alarmDTO = AlarmDTO()
            alarmDTO.destinationUid = destinationUid
            alarmDTO.userId = FirebaseAuth.getInstance().currentUser?.email
            alarmDTO.uid = FirebaseAuth.getInstance().currentUser?.uid
            alarmDTO.kind = 0
            alarmDTO.timestamp = System.currentTimeMillis()
            FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)
        }
        */
}
}


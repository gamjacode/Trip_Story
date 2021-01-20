package com.example.myapplication.navigation

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.trip_story.HomeActivity
import com.example.trip_story.Login
import com.example.trip_story.R
import com.example.trip_story.model.ContentDTO
import com.example.trip_story.model.FollowDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_user.view.*

class UserFragment : Fragment() {
    var fragmentView: View? = null
    var firestore: FirebaseFirestore? = null
    var uid: String? = null
    var auth: FirebaseAuth? = null
    var currentUserUid: String? = null //이 값을 비교해서 계정인지 상대방의 계정인지 판단하는 역할

    companion object {
        var PICK_PROFILE_FROM_ALBUM = 10
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView =
            LayoutInflater.from(activity).inflate(R.layout.fragment_user, container, false)
        uid = arguments?.getString("destinationUid")
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        currentUserUid = auth?.currentUser?.uid

        if (uid == currentUserUid) {
            //MyPage(로그아웃)
            fragmentView?.account_btn_follow?.text = getString(R.string.signout) //LOGOUT으로 세팅
            fragmentView?.account_btn_follow?.setOnClickListener {
                activity?.finish()
                startActivity(Intent(activity, Login::class.java))
                auth?.signOut()
            }
        } else {
            //OtherUserPage
            fragmentView?.account_btn_follow?.text = getString(R.string.follow) //FOLLOW로 세팅
            var mainactivity = (activity as HomeActivity)
            mainactivity?.toolbar_username?.text = arguments?.getString("userId") //누구의 페이지인지 표시
            mainactivity?.toolbar_btn_back?.setOnClickListener { //뒤로가기 설정
                mainactivity.bottom_navigation.selectedItemId = R.id.bottom_menu_home
            }
         //   mainactivity?.toolbar_title_image?.visibility = View.GONE
            mainactivity?.toolbar_username?.visibility = View.VISIBLE
            mainactivity?.toolbar_btn_back?.visibility = View.VISIBLE

            fragmentView?.account_btn_follow?.setOnClickListener {
                requestFollow()
            }

        }

        fragmentView?.account_recyclerview?.adapter = UserFragmentRecyclerViewAdapter()
        fragmentView?.account_recyclerview?.layoutManager = GridLayoutManager(activity!!, 3)

        //프로필 사진 추가 내용
        fragmentView?.account_iv_profile?.setOnClickListener {
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            activity?.startActivityForResult(photoPickerIntent, PICK_PROFILE_FROM_ALBUM)
        }
       // getProfileImage()
        getFollowerAndFollowing()
        return fragmentView
    }

    //화면에 follow값을 바꿔주는
    fun getFollowerAndFollowing(){
        firestore?.collection("users")?.document(uid!!)?.addSnapshotListener{documentSnapshot, firebaseFirestoreException ->
            if(documentSnapshot == null) return@addSnapshotListener
            var followDTO = documentSnapshot.toObject(FollowDTO::class.java)
            if(followDTO?.followingCount != null){
                fragmentView?.account_tv_following_count?.text = followDTO?.followingCount?.toString()
            }
            if(followDTO?.followerCount != null){
                fragmentView?.account_tv_follower_count?.text = followDTO?.followerCount?.toString()

                //내가 follower를 하고 있으면 버튼이 변화되는 코드
                if(followDTO?.followers?.containsKey(currentUserUid!!)){
                    fragmentView?.account_btn_follow?.text = getString(R.string.follow_cancel)
                    fragmentView?.account_btn_follow?.background?.setColorFilter(ContextCompat.getColor(activity!!,R.color.colorLightGray),PorterDuff.Mode.MULTIPLY)
                }else{
                    if(uid != currentUserUid){
                        fragmentView?.account_btn_follow?.text = getString(R.string.follow)
                        fragmentView?.account_btn_follow?.background?.colorFilter = null
                    }
                }
            }
        }
    }

    fun requestFollow() {
        //나의 계정에는 상대방 누구를 follow하는지
        var tsDocFollowing = firestore?.collection("users")?.document(currentUserUid!!)
        firestore?.runTransaction { transaction ->
            var followDTO = transaction.get(tsDocFollowing!!).toObject(FollowDTO::class.java)
            if (followDTO == null) {
                followDTO = FollowDTO()
                followDTO!!.followingCount = 1
                followDTO!!.followers[uid!!] = true //중복 following을 방지하기 위해 상대방 uid를 넣어줌

                transaction.set(tsDocFollowing, followDTO) //data가 db에 담김
                return@runTransaction
            }
            if (followDTO.followings.containsKey(uid)) {  //follow한 상태
                followDTO?.followingCount = followDTO?.followingCount - 1
                followDTO?.followers?.remove(uid)       //상대방 uid를 제거
            } else {                                      //follow를 하지 않은 상태
                followDTO?.followingCount = followDTO?.followingCount + 1
                followDTO?.followers[uid!!] = true      //상대방 uid를 추가
            }
            transaction.set(tsDocFollowing, followDTO)   //db로 저장
            return@runTransaction
        }

        //내가 팔로잉한 상대방 계정에 접근하는 코드
        var tsDocFollwer = firestore?.collection("users")?.document(uid!!) //상대방 uid를 넣어줌
        firestore?.runTransaction { transaction ->
            var followDTO = transaction.get(tsDocFollwer!!).toObject(FollowDTO::class.java)
            if (followDTO == null) {
                followDTO = FollowDTO()
                followDTO!!.followerCount = 1
                followDTO!!.followers[currentUserUid!!] = true  //상대방 계정에 나의 uid를 넣어줌
               // followerAlarm(uid!!)

                transaction.set(tsDocFollwer, followDTO!!)
                return@runTransaction

            }
            if (followDTO!!.followers.containsKey(currentUserUid)) {
                followDTO!!.followerCount = followDTO!!.followerCount - 1
                followDTO!!.followers.remove(currentUserUid!!)
            } else {
                followDTO!!.followerCount = followDTO!!.followerCount + 1
                followDTO!!.followers[currentUserUid!!] = true
              //  followerAlarm(uid!!)
            }
            transaction.set(tsDocFollwer, followDTO!!)
            return@runTransaction
        }
    }
    //팔로우를 했을때 알림이 뜨는 이벤트
    /*
    fun followerAlarm(destinationUid : String){
        var alarmDTO = AlarmDTO()
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = auth?.currentUser?.email
        alarmDTO.uid = auth?.currentUser?.uid
        alarmDTO.kind = 2
        alarmDTO.timestamp = System.currentTimeMillis()
        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)
    }
*/
    //storage에 올린 프로필 이미지를 보여주는
    /*
    fun getProfileImage() {
        firestore?.collection("profileImages")?.document(uid!!)
            ?.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot == null) return@addSnapshotListener //null일 경우 바로 리턴
                if (documentSnapshot.data != null) {
                    var uri = documentSnapshot?.data!!["image"]  //image주소를 받아옴
                    Glide.with(activity!!).load(uri).apply(RequestOptions().circleCrop())
                        .into(fragmentView?.account_iv_profile!!) //받아온 이미지주소를 보여줌
                    //이미지를 원형으로
                }

            }
            */


    //adapter
    inner class UserFragmentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var contentDTOs: ArrayList<ContentDTO> = arrayListOf()

        init {                                           //내가 올린 이미지만 킬 수 있도록
            firestore?.collection("images")?.whereEqualTo("uid", uid)
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (querySnapshot == null) return@addSnapshotListener        //null일 경우 바로 종료

                    //Get data
                    for (snapshot in querySnapshot.documents) {
                        contentDTOs.add(snapshot.toObject(ContentDTO::class.java)!!)
                    }
                    fragmentView?.account_tv_post_count?.text =
                        contentDTOs.size.toString() //text를 size로 매핑
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var width = resources.displayMetrics.widthPixels / 3  //화면의 폭의 3분의 1 값을 가져옴

            var imageview = ImageView(parent.context)
            imageview.layoutParams = LinearLayoutCompat.LayoutParams(width, width)
            return CustomViewHolder(imageview)
        }

        inner class CustomViewHolder(var imageView: ImageView) :
            RecyclerView.ViewHolder(imageView) {

        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var imageview = (holder as CustomViewHolder).imageView
            Glide.with(holder.itemView.context).load(contentDTOs[position].imageUrl).apply(
                RequestOptions().centerCrop()
            ).into(imageview)
        }

    }
}
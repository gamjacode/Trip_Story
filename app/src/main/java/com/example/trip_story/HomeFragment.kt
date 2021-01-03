package com.example.trip_story

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trip_story.adapter.HomeAdatper
import com.example.trip_story.model.Data
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {
    private lateinit var adapter: HomeAdatper
    private lateinit var myContext:Context

    var Datas = arrayListOf<Data>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myContext = requireContext()
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        Datas = arrayListOf<Data>(
            Data("홍천","7/9-AM 10:00","yeosoo"),
            Data("남이섬","7/10-PM 02:00","namysum"),
            Data("여수","7/10-PM 04:00","yeosoo")
        )

        val layoutManager = LinearLayoutManager(myContext)
        adapter = HomeAdatper(myContext,Datas)
        view.home_recycler_view.adapter = adapter
        view.home_recycler_view.layoutManager = layoutManager
        view.home_recycler_view.setHasFixedSize(true)
        return view
    }
}
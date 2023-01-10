package com.example.firebaseuploadpicture.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseuploadpicture.R
import com.example.firebaseuploadpicture.model.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerrow.view.*

class HaberRecyclerAdapter(val postList: ArrayList<Post>):RecyclerView.Adapter<HaberRecyclerAdapter.PostHolder>()
{
            class PostHolder(itemView: View):RecyclerView.ViewHolder(itemView)
            {

            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
            val inflater=LayoutInflater.from(parent.context)
                val view=inflater.inflate(R.layout.recyclerrow,parent,false)
                return  PostHolder(view)
            }

            override fun onBindViewHolder(holder: PostHolder, position: Int) {
                holder.itemView.kullaniciText.text=postList[position].kullaniciName
                holder.itemView.emailText.text=postList[position].kullaniciEmail
                Picasso.get().load(postList[position].gorsel).into(holder.itemView.recycler_row_imageview)

            }

            override fun getItemCount(): Int {
             return   postList.size
            }
}
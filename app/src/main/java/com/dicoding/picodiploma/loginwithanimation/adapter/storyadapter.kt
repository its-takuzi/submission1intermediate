package com.dicoding.picodiploma.loginwithanimation.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ListItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class storyadapter() : ListAdapter<ListStoryItem, storyadapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val list = ArrayList<ListStoryItem>()

    fun setOnClickCallBack(onItemClickCallBack: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallBack
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }
    inner class MyViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(story : ListStoryItem?){
            story.let {
                binding.apply {
                    Glide.with(itemView).load(story?.photoUrl).into(pictureStory)
                    titleStory.text = story?.name

                    val dateFormat =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val date = dateFormat.parse(story?.createdAt.toString())
                    val formattedDate =
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
                    dateStory.text = formattedDate
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): storyadapter.MyViewHolder {
      val binding =
          ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(story!!)
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}


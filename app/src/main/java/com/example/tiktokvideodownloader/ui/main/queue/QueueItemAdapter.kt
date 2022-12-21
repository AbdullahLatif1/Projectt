package com.example.tiktokvideodownloader.ui.main.queue

import android.annotation.SuppressLint
import android.content.Context
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktokvideodownloader.R
import com.example.tiktokvideodownloader.data.model.VideoState
import com.example.tiktokvideodownloader.ui.shared.inflate
import com.example.tiktokvideodownloader.ui.shared.loadUri

class QueueItemAdapter(
    private val itemClicked: (path: String) -> Unit,
    private val urlClicked: (url: String) -> Unit,
    private val context: Context
) :
    ListAdapter<VideoState, QueueItemAdapter.DownloadActionsViewHolder>(DiffUtilItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadActionsViewHolder =
        DownloadActionsViewHolder(parent)

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: DownloadActionsViewHolder, position: Int) {
       // val item1 = holder.bindingAdapterPosition
        val item = getItem(position)
        holder.urlView.text = item.url
        val statusTextRes = when (item) {
            is VideoState.InPending -> R.string.status_pending
            is VideoState.Downloaded -> R.string.status_finished
            is VideoState.InProcess -> R.string.status_pending
        }
        holder.statusView.isInvisible = item is VideoState.InProcess
        holder.progress.isVisible = item is VideoState.InProcess
        if (item is VideoState.Downloaded) {
            holder.itemView.setOnClickListener {
                itemClicked(item.videoDownloaded.uri)
            }
            holder.itemView.isEnabled = true
        } else {
            holder.itemView.isEnabled = false
        }
        holder.urlView.setOnClickListener {
            urlClicked(item.url)
        }
        holder.thumbNailView1.setOnClickListener {
        val popupMenu = PopupMenu(context,holder.thumbNailView1)
            popupMenu.inflate(R.menu.threedots)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

                when (item!!.itemId) {
                    R.id.delete -> {
                        //currentList.toMutableList()
                        //currentList.remove(item.itemId.toString())
                      //  currentList.indexOf(position)
                        currentList.removeAt(holder.bindingAdapterPosition)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, currentList.size)
                        /*holder.cardView.removeAllViews()
                        holder.itemView.visibility = View.GONE
                      notifyDataSetChanged()*/

                    }
                    R.id.share -> {
                    }
                    R.id.play -> {
                    }
                }

                true
            })

            popupMenu.show()

        }
        holder.statusView.setText(statusTextRes)
        when (item) {
            is VideoState.InProcess,
            is VideoState.InPending -> holder.thumbNailView.setImageResource(R.drawable.ic_twotone_image)
            is VideoState.Downloaded ->
                holder.thumbNailView.loadUri(item.videoDownloaded.uri)
        }
    }

    fun swap(from: VideoState, to: VideoState) {
        val mutable = currentList.toMutableList()
        val fromIndex = currentList.indexOf(from)
        val toIndex = currentList.indexOf(to)
        mutable.removeAt(fromIndex)
        mutable.add(fromIndex, to)
        mutable.removeAt(toIndex)
        mutable.add(toIndex, from)
        submitList(mutable)
    }

    fun delete() {
        submitList(emptyList())
    }

    class DownloadActionsViewHolder(parent: ViewGroup) : MovingItemCallback,
        RecyclerView.ViewHolder(parent.inflate(R.layout.item_downloaded)) {
        val cardView: CardView = itemView as CardView
        val content: ConstraintLayout = itemView.findViewById(R.id.content)
        val urlView: TextView = itemView.findViewById(R.id.url)
        val statusView: TextView = itemView.findViewById(R.id.status)
        val thumbNailView: ImageView = itemView.findViewById(R.id.thumbnail)
        val thumbNailView1: ImageView = itemView.findViewById(R.id.thumbnail1)
        val progress: ProgressBar = itemView.findViewById(R.id.progress)

        override fun onMovingEnd() {
            cardView.isEnabled = false
            cardView.isPressed = false
            content.isPressed = false
        }

        override fun onMovingStart() {
            cardView.isEnabled = true
            cardView.isPressed = true
            content.isPressed = true
        }
    }

    class DiffUtilItemCallback : DiffUtil.ItemCallback<VideoState>() {
        override fun areItemsTheSame(oldItem: VideoState, newItem: VideoState): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: VideoState, newItem: VideoState): Boolean =
            oldItem == newItem

        override fun getChangePayload(oldItem: VideoState, newItem: VideoState): Any? =
            this

    }
}
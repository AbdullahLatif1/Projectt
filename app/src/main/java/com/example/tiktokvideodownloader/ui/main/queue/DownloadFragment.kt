package com.example.tiktokvideodownloader.ui.main.queue

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktokvideodownloader.R
import com.example.tiktokvideodownloader.data.model.VideoState
import com.example.tiktokvideodownloader.di.provideViewModels
import com.example.tiktokvideodownloader.ui.main.rvadapter.AdmobNativeAdAdapter
import com.google.android.material.snackbar.Snackbar

class DownloadFragment : Fragment() {
    private val viewModel by provideViewModels<QueueViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_download, container, false)
        val recycler = view.findViewById<RecyclerView>(R.id.recycler)
        recyclerViewSetup(recycler)
        navigationSetup()
        return view
    }
    companion object {

        fun newInstance(): DownloadFragment = DownloadFragment()
        fun createBrowserIntent(url: String): Intent =
            Intent(Intent.ACTION_VIEW, Uri.parse(url))

        fun createGalleryIntent(uri: String): Intent =
            Intent(Intent.ACTION_VIEW, uri.toUri())
    }
    private fun recyclerViewSetup(recycler: RecyclerView) {
        val adapter = QueueItemAdapter(
            itemClicked = viewModel::onItemClicked,
            urlClicked = viewModel::onUrlClicked,
            requireContext()
        )
        recycler.adapter = adapter




        val callback = VideoStateItemTouchHelper(
            whichItem = { adapter.currentList.getOrNull(it.bindingAdapterPosition) },
            onDeleteElement = viewModel::onElementDeleted,
            onUIMoveElement = adapter::swap,
            onMoveElement = viewModel::onElementMoved
        )
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(recycler)


        viewModel.downloads.observe(viewLifecycleOwner) { videoStates ->
            callback.dragEnabled = videoStates.none { it is VideoState.InProcess }

            adapter.submitList(videoStates, Runnable {
                val indexToScrollTo = videoStates.indexOfFirst { it is VideoState.InProcess }
                    .takeIf { it != -1 } ?: return@Runnable
                recycler.smoothScrollToPosition(indexToScrollTo)
            })
        }
    }
    fun navigationSetup() {
        viewModel.navigationEvent.observe(viewLifecycleOwner) {
            val intent = when (val data = it.item) {
                is QueueViewModel.NavigationEvent.OpenBrowser -> {
                   createBrowserIntent(data.url)
                }
                is QueueViewModel.NavigationEvent.OpenGallery ->
                   createGalleryIntent(data.uri)
                null -> return@observe
            }
            try {
                startActivity(intent)
            } catch(activityNotFoundException: ActivityNotFoundException) {
                val anchor = activity?.findViewById<CoordinatorLayout>(R.id.snack_bar_anchor) ?: return@observe
                Snackbar.make(anchor, R.string.could_not_open, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}
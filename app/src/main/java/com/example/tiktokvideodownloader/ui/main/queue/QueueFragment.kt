package com.example.tiktokvideodownloader.ui.main.queue

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
import com.anjlab.android.iab.v3.PurchaseInfo
import com.example.tiktokvideodownloader.R
import com.example.tiktokvideodownloader.di.provideViewModels
import com.google.android.material.navigation.NavigationView


class QueueFragment : Fragment(R.layout.fragment_queue) {

    private val viewModel by provideViewModels<QueueViewModel>()
    var fragmentTransaction: FragmentTransaction? = null
    var toolbar: Toolbar? = null
    var navigationView: NavigationView? = null
    var drawerToggle: ActionBarDrawerToggle? = null
    var drawerLayout: DrawerLayout? = null
    private lateinit var bp: BillingProcessor
    private lateinit var purchaseInfo: PurchaseInfo
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val saveUrlCta = view.findViewById<Button>(R.id.save_cta)
        val saveUrlCta1 = view.findViewById<Button>(R.id.paste)
        toolbar = view.findViewById(R.id.toolBar12)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        fragmentTransaction = (activity as AppCompatActivity?)!!.supportFragmentManager.beginTransaction()
        navigationView = view.findViewById(R.id.nav_view)
        drawerLayout = view.findViewById(R.id.drawerlayout)
        drawerToggle =
            ActionBarDrawerToggle(requireContext() as Activity?, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout?.addDrawerListener(drawerToggle!!)
        drawerToggle!!.syncState()
        navigationView?.bringToFront()
        //navigationView?.setNavigationItemSelectedListener(context!!)
       // navigationView?.bringToFront()
        val myClipboard = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        if(!myClipboard.hasPrimaryClip()){
          saveUrlCta1.isEnabled = false
        }

        val input = view.findViewById<EditText>(R.id.download_url_input)
        input.doAfterTextChanged {
            saveUrlCta.isEnabled = it?.isNotBlank() == true
        }
        bp.initialize()
        saveUrlCta1.setOnClickListener {
           /* val abc: ClipData = myClipboard.primaryClip!!
            val item = abc.getItemAt(0)

            val text = item.text.toString()
            input.setText(text)*/
        }
        saveUrlCta.setOnClickListener {
            viewModel.onSaveClicked(input.text?.toString().orEmpty())
            input.setText("")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // The action bar home/up action should open or close the drawer.
        when (item.itemId) {
            R.id.home123 -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                val Body = "Download this app"
                val Sub = "https://play.google.com/store/apps/details?id=com.successapp.qr.barcodescanner&hl=en&gl=US"
                intent.putExtra(Intent.EXTRA_TEXT, Body)
                intent.putExtra(Intent.EXTRA_TEXT, Sub)
                startActivity(Intent.createChooser(intent, "Share Using"))
            }
            R.id.scan -> {
                val url = "http://kingsappsstudio.blogspot.com/2018/01/privacy-policy-kings-apps-studio.html"
                val uri2 = Uri.parse(url)
                val intent1 = Intent(Intent.ACTION_VIEW, uri2)
                startActivity(intent1)
            }

        }
        drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }



    companion object {


        fun newInstance(): QueueFragment = QueueFragment()


    }
}
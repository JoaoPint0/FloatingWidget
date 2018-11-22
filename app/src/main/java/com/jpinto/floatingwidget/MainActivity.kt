package com.jpinto.floatingwidget

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.floating_view.view.*






class MainActivity : AppCompatActivity() {

    private val SYSTEM_ALERT_WINDOW_PERMISSION = 2084

    private val mWindowManager: WindowManager by lazy { getSystemService(Context.WINDOW_SERVICE) as WindowManager}
    private val mFloatingView: View by lazy { LayoutInflater.from(this).inflate(R.layout.floating_view, null) }

    private val viewmodel: NotificationsViewModel by lazy { ViewModelProviders.of(this).get(NotificationsViewModel::class.java) }
    private val adapter : NotificationsAdapter by lazy { NotificationsAdapter(viewmodel)}

    private var isShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewmodel.getNotifications().observe(this, Observer {

            if (it.isNotEmpty()){
                if(!isShowing) showNotifications()
                adapter.setNotifications(it)
            }else{
                if(isShowing) hideNotifications()
            }
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askPermission()
        }

        buttonCreateWidget.setOnClickListener{

            viewmodel.loadNotifications(listOf("Ola", "Mundo", "eu" , "vou", "te", "conhecer"))
        }

        buttonCreateDialog.setOnClickListener {

            val alertDialog: AlertDialog = this.let { activity ->

                val builder = AlertDialog.Builder(activity)
                builder.apply {
                    setPositiveButton("ok") { _, _ ->
                        // User clicked OK button
                    }
                    setNegativeButton("cancel") { _, _ ->
                        // User cancelled the dialog
                    }
                }
                builder.create()
            }
            alertDialog.show()
        }

    }

    private fun askPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION)
    }

    private fun showNotifications(){

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP

        mWindowManager.addView(mFloatingView, params)
        isShowing = true

        mFloatingView.notications_list.adapter = adapter
        mFloatingView.notications_list.layoutManager = LinearLayoutManager(this)

        val swipeController = NotificationSwipeController(adapter)
        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(mFloatingView.notications_list)

        mFloatingView.layoutCollapsed.setOnTouchListener(View.OnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> return@OnTouchListener true

                MotionEvent.ACTION_UP -> {

                    mFloatingView.layoutCollapsed.visibility = View.GONE
                    mFloatingView.layoutExpanded.visibility = View.VISIBLE
                    return@OnTouchListener true
                }

                MotionEvent.ACTION_MOVE ->
                    return@OnTouchListener true
            }
            false
        })

        mFloatingView.layoutExpanded.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> return@OnTouchListener true

                MotionEvent.ACTION_UP -> {

                    mFloatingView.layoutCollapsed.visibility = View.VISIBLE
                    mFloatingView.layoutExpanded.visibility = View.GONE
                    return@OnTouchListener true
                }

                MotionEvent.ACTION_MOVE ->
                    return@OnTouchListener true
            }
            false
        })

        mFloatingView.clear_list.setOnClickListener {

            viewmodel.loadNotifications(emptyList())
        }
    }

    private fun hideNotifications(){

        mFloatingView.layoutCollapsed.visibility = View.VISIBLE
        mFloatingView.layoutExpanded.visibility = View.GONE

        mWindowManager.removeView(mFloatingView)
        isShowing = false
    }
}

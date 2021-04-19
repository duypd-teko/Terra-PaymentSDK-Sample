package com.example.myapplication

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import vn.teko.android.payment.ui.PaymentActivity
import vn.teko.android.payment.ui.data.request.PaymentUIRequestBuilder
import vn.teko.android.payment.ui.data.request.PaymentV2Request
import vn.teko.android.payment.ui.data.result.PaymentResult
import vn.teko.android.payment.ui.util.extension.payWith

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

internal fun Context?.showMessageDialog(
    title: String,
    message: String,
    positiveButtonTitle: Int,
    positiveButtonAction: (DialogInterface, Int) -> Unit,
    cancelable: Boolean = true
): AlertDialog? {
    return if (this != null) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonTitle, positiveButtonAction)
            .setCancelable(cancelable)
            .show()
    } else null
}
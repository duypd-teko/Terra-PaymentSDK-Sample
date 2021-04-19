package com.example.myapplication

import android.app.Application
import vn.teko.android.payment.manager.TerraPayment
import vn.teko.android.payment.v2.IPaymentGateway
import vn.teko.android.payment.v2.PaymentGateway
import vn.teko.terra.core.android.terra.TerraApp

class MyApplication : Application() {

    lateinit var paymentGateway: IPaymentGateway

    companion object {

        lateinit var shared: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        shared = this
        TerraApp.initializeApp(this)
        paymentGateway = TerraPayment.getInstance(this, TerraApp.getInstance())
    }
}
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import vn.teko.android.payment.v2.IPaymentGateway
import java.lang.Exception

import vn.teko.android.payment.ui.PaymentActivity
import vn.teko.android.payment.ui.data.request.*
import vn.teko.android.payment.ui.data.result.PaymentResult
import vn.teko.android.payment.ui.util.extension.payWith

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PayViaVNPayGatewayFragment : Fragment() {

    private val paymentGateway: IPaymentGateway
        get() = MyApplication.shared.paymentGateway

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pay_vnpay_gateway, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnPayVnPayQR).setOnClickListener {
            payDirectWithVNPayQR()
        }

        view.findViewById<Button>(R.id.btnPayATM).setOnClickListener {
            payDirectWithATM()
        }

        view.findViewById<Button>(R.id.btnPayCredit).setOnClickListener {
            payDirectWithCredit()
        }

        view.findViewById<Button>(R.id.btnPayMobileBanking).setOnClickListener {
            payDirectWithMobileBanking()
        }
        view.findViewById<Button>(R.id.btnPayATMWithBankCode).setOnClickListener {
            payDirectWithATMWithCode()
        }
    }


    private fun payDirectWithVNPayQR() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                // set type specific for method online and passing params based on method selected
                // Refer to _root_ide_package_.vn.teko.android.payment.ui.data.request.PaymentMainMethodRequest to see all available online method
                .setMainMethod(PaymentMainMethodRequest.VNPayGatewayQR(amount = 10000))
            paymentGateway.payWith(this, builder.build())
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun payDirectWithATM() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                // set type specific for method online and passing params based on method selected
                // Refer to _root_ide_package_.vn.teko.android.payment.ui.data.request.PaymentMainMethodRequest to see all available online method
                .setMainMethod(PaymentMainMethodRequest.ATMBank(amount = 10000))
            paymentGateway.payWith(this, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun payDirectWithCredit() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                // set type specific for method online and passing params based on method selected
                // Refer to _root_ide_package_.vn.teko.android.payment.ui.data.request.PaymentMainMethodRequest to see all available online method
                .setMainMethod(PaymentMainMethodRequest.CreditCard(amount = 10000))
            paymentGateway.payWith(this, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun payDirectWithMobileBanking() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                // set type specific for method online and passing params based on method selected
                // Refer to _root_ide_package_.vn.teko.android.payment.ui.data.request.PaymentMainMethodRequest to see all available online method
                .setMainMethod(PaymentMainMethodRequest.MobileBanking(amount = 10000))
            paymentGateway.payWith(this, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun payDirectWithATMWithCode() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                // set type specific for method online and passing params based on method selected
                // Refer to _root_ide_package_.vn.teko.android.payment.ui.data.request.PaymentMainMethodRequest to see all available online method
                .setMainMethod(PaymentMainMethodRequest.ATMBank(amount = 10000, route = VnPayGatewayRoute.Bank("ncb")))
            paymentGateway.payWith(this, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PaymentActivity.RC_PAYMENT -> {
                when (resultCode) {
                    PaymentActivity.RESULT_CANCELED -> {
                        Toast.makeText(requireContext(), "Payment is canceled", Toast.LENGTH_SHORT)
                            .show()
                    }
                    PaymentActivity.RESULT_FAILED -> {
                        val result =
                            data?.getParcelableExtra<PaymentResult>(PaymentActivity.PAYMENT_RESULT_KEY)
                        val extraMessage = result
                            ?.transactions
                            ?.map { "\n${it.methodCode} - ${it.amount} - isSuccess(${it.isSuccess}) - error: (${it.error?.code} - ${it.error?.message})\n" }
                            ?.toString()

                        requireContext().showMessageDialog(
                            "Thông báo",
                            "Thanh toán thất bại Order Code ${result?.order?.orderCode} \n Amount: ${result?.order?.amount}.\n Error: ${result?.error}.\n$extraMessage",
                            R.string.Ok,
                            { _, _ -> })
                    }
                    PaymentActivity.RESULT_SUCCEEDED -> {
                        val result =
                            data?.getParcelableExtra<PaymentResult>(PaymentActivity.PAYMENT_RESULT_KEY)
                        //TODO: get more detail from reuslt
                        requireContext().showMessageDialog(
                            "Thông báo",
                            "Thanh toán thành công",
                            R.string.Ok,
                            { _, _ -> })
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }

    }
}
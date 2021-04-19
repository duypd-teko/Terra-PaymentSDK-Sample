package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import vn.teko.android.payment.ui.data.request.PaymentUIRequestBuilder
import vn.teko.android.payment.ui.data.request.PaymentV2Request
import vn.teko.android.payment.v2.IPaymentGateway
import java.lang.Exception

import vn.teko.android.payment.ui.PaymentActivity
import vn.teko.android.payment.ui.data.request.LoyaltyMethodRequest
import vn.teko.android.payment.ui.data.request.PaymentOnlineMethodRequest
import vn.teko.android.payment.ui.data.result.PaymentResult
import vn.teko.android.payment.ui.singlepayment.vnpayewallet.VNPayEWalletCustomer
import vn.teko.android.payment.ui.util.extension.payWith

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val paymentGateway: IPaymentGateway
        get() = MyApplication.shared.paymentGateway

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnOpenMethods1).setOnClickListener {
            openAvailableMethodOption1()
        }

        view.findViewById<Button>(R.id.btnOpenMethods2).setOnClickListener {
            openAvailableMethodOption2()
        }

        view.findViewById<Button>(R.id.btnPayWithSingleOnline).setOnClickListener {
            payDirectWithOnlineMethodOnly()
        }

        view.findViewById<Button>(R.id.btnPayWithSingleLoyalty).setOnClickListener {
            payDirectWithLoyaltyMethodOnly()
        }
        view.findViewById<Button>(R.id.btnPayWithMultiple).setOnClickListener {
            payDirectWithLoyaltyAndOnlineMethod()
        }
        view.findViewById<Button>(R.id.btnPayWithMultiple).setOnClickListener {
            payDirectWithLoyaltyAndOnlineMethod()
        }
        view.findViewById<Button>(R.id.btnPayWithVNPayEWallet).setOnClickListener {
            payDirectWithVnPayEwalletMethod()
        }
    }

    private fun openAvailableMethodOption1() {
        try {
            // set order info to payment including orderCode and amount
            val builder = PaymentUIRequestBuilder().setOrderConfig(
                PaymentV2Request.Order(
                    "AXXXXTTT_TEST_222",
                    10000
                )
            )
            paymentGateway.payWith(this, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openAvailableMethodOption2() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                // set type all for method online and passing amount
                .setOnlineMethod(PaymentOnlineMethodRequest.All(amount = 1000))
            paymentGateway.payWith(this, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun payDirectWithOnlineMethodOnly() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                // set type specific for method online and passing params based on method selected
                // Refer to PaymentOnlineMethodRequest to see all available online method
                .setOnlineMethod(PaymentOnlineMethodRequest.VNPayGatewayQR(amount = 1000))
            paymentGateway.payWith(this, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun payDirectWithLoyaltyMethodOnly() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                // set loyalty method and passing params
                .setLoyaltyMethod(LoyaltyMethodRequest(points = 1000, amount = 1000))
                // For loyalty you must be pass userId to payment
                // other way you can passing using  as below
                // paymentGateway.config.apply {
                //            client.userId = "yourUserId"
                //     }

                .setClientConfig(clientConfig = PaymentV2Request.ClientConfig(userId = "yourUserId"))
            paymentGateway.payWith(this, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun payDirectWithLoyaltyAndOnlineMethod() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                // set loyalty method and passing params
                .setLoyaltyMethod(LoyaltyMethodRequest(points = 1000, amount = 1000))
                .setOnlineMethod(PaymentOnlineMethodRequest.VNPayGatewayQR(amount = 1000))

                .setClientConfig(clientConfig = PaymentV2Request.ClientConfig(userId = "yourUserId"))
            paymentGateway.payWith(this, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun payDirectWithVnPayEwalletMethod() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                // set loyalty method and passing params
                .setOnlineMethod(
                    PaymentOnlineMethodRequest.VNPayEWallet(
                        amount = 1000,
                        config = VNPayEWalletCustomer(
                            phone = "0999999998",
                            name = "Nguyen",
                            email = "email@email.com"
                        )
                    )
                )
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
                            "Thanh toán thất bại Order Code ${result?.order?.amount} \n Amount: ${result?.order?.amount}.\n $extraMessage",
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
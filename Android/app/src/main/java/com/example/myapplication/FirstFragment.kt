package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import vn.teko.android.payment.v2.IPaymentGateway

import vn.teko.android.payment.ui.PaymentActivity
import vn.teko.android.payment.ui.data.request.*
import vn.teko.android.payment.ui.data.result.PaymentResult
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


        view.findViewById<Button>(R.id.btnOpenMethods2).setOnClickListener {
            openAvailableMethodOption()
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

        view.findViewById<Button>(R.id.btnPayWithVNPayGateway).setOnClickListener {
            findNavController().navigate(R.id.payThroughVNPayGateway)
        }
    }

    private fun openAvailableMethodOption() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                .setOrderAmount(amount = 1000)
                // set type all for method online and passing amount
                .setMainMethod(PaymentMainMethodRequest.All(amount = 1000))
                .setOptions(
                    ExtraOptions(
                        shouldShowPaymentResultScreen = true
                    )
                )
            paymentGateway.payWith(this, builder.build())
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun payDirectWithOnlineMethodOnly() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                .setOrderAmount(amount = 1000)
                // set type specific for method online and passing params based on method selected
                // Refer to _root_ide_package_.vn.teko.android.payment.ui.data.request.PaymentMainMethodRequest to see all available online method
                .setMainMethod(PaymentMainMethodRequest.VNPayGatewayQR(amount = 1000))
            paymentGateway.payWith(this, builder.build())
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun payDirectWithLoyaltyMethodOnly() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                .setOrderAmount(amount = 1000)
                // set loyalty method and passing params
                .setLoyaltyMethod(LoyaltyMethodRequest(points = 1000, amount = 1000))
            paymentGateway.payWith(this, builder.build())
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun payDirectWithLoyaltyAndOnlineMethod() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                .setOrderAmount(amount = 1000)
                // set loyalty method and passing params
                .setLoyaltyMethod(LoyaltyMethodRequest(points = 1000, amount = 1000))
                .setMainMethod(PaymentMainMethodRequest.VNPayGatewayQR(amount = 1000))

            paymentGateway.payWith(this, builder.build())
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    private fun payDirectWithVnPayEwalletMethod() {
        try {
            // set order info to payment including orderCode
            val builder = PaymentUIRequestBuilder()
                .setOrderCode(orderCode = "YourOrderCode")
                .setOrderAmount(amount = 1000)
                // set loyalty method and passing params
                .setMainMethod(
                    PaymentMainMethodRequest.VNPayEWallet(
                        amount = 1000
                    )
                )

                // must be pass config for VNPayEWallet
                .setMetadata(
                    PaymentV2Request.Metadata(
                        methods = arrayListOf(
                            PaymentV2Request.Metadata.MethodConfig.VNPayEWallet(
                                partnerId = "0999999998",
                                phone = "0999999998",
                                name = "Nguyen",
                                email = "email@email.com"
                            )
                        )
                    )
                )
            paymentGateway.payWith(this, builder.build())
        } catch (e: Throwable) {
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
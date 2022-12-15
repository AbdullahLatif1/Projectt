package com.example.tiktokvideodownloader.ui.main

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.android.billingclient.api.*
import com.android.billingclient.api.QueryProductDetailsParams.Product
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo
import com.example.tiktokvideodownloader.R
import kotlinx.coroutines.runBlocking
import okhttp3.internal.toImmutableList

class MainActivity12 : AppCompatActivity() {

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->

            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    sharedPreferences.edit().putBoolean("ads",true)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {

                sharedPreferences.edit().putBoolean("ads",false)

            } else {
                sharedPreferences.edit().putBoolean("ads",false)
            }

        }

    private lateinit var billingClient : BillingClient
    private var list:MutableList<Product> = arrayListOf()
    private var listProducts:List<ProductDetails> = arrayListOf()
    private lateinit var billingFlowParams: BillingFlowParams
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main12)

        sharedPreferences = getSharedPreferences("Settings",Context.MODE_PRIVATE)

        runBlocking {
            billingClient = BillingClient.newBuilder(this@MainActivity12)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build()
        }


        runBlocking {

            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                        Toast.makeText(this@MainActivity12,"Connected",Toast.LENGTH_LONG).show()
                    }
                }
                override fun onBillingServiceDisconnected() {

                }
            })

        }.let {


            list.add(Product.newBuilder()
                .setProductId("product_id_example")
                .setProductType(BillingClient.ProductType.INAPP)
                .build())

            val queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                    .setProductList(
                        list.toImmutableList()).build()


            billingClient.queryProductDetailsAsync(queryProductDetailsParams) {
                    _,
                    productDetailsList ->

                listProducts = productDetailsList

                if (listProducts.isEmpty()){
                    Toast.makeText(this@MainActivity12,"Wrong Product ID",Toast.LENGTH_LONG).show()
                    return@queryProductDetailsAsync
                }

                val productDetailsParamsList = listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(listProducts[0])
                        .build()
                )
                billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()
            }

        }

        findViewById<Button>(R.id.buyNow).setOnClickListener {

            if (sharedPreferences.getBoolean("ads",false)){
                Toast.makeText(this@MainActivity12,"Already Purchased",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            if (this::billingClient.isInitialized){
                val billingResult = billingClient.launchBillingFlow(this, billingFlowParams)
            }


        }

    }


}
















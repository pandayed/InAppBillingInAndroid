package com.edpub.googlesubscription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.android.billingclient.api.*

//https://www.youtube.com/watch?v=oOgI03ctPNo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val skuList = arrayListOf<String>("android.test.purchased")

        val purchasesUpdatedListener = PurchasesUpdatedListener{
            billingResult, mutableList ->

        }

        val billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases().build()

        val bBuySubs = findViewById<Button>(R.id.bBuySubs)
        bBuySubs.setOnClickListener{
            billingClient.startConnection(object : BillingClientStateListener{
                override fun onBillingServiceDisconnected() {

                }

                override fun onBillingSetupFinished(billlingResult: BillingResult) {
                    if(billlingResult.responseCode == BillingClient.BillingResponseCode.OK){
                        val params = SkuDetailsParams.newBuilder()
                        params.setSkusList(skuList)
                            .setType(BillingClient.SkuType.INAPP)
                        billingClient.querySkuDetailsAsync(params.build()){
                            billlingResult, skuDetailsList ->
                            for(skuDetails in skuDetailsList!!){
                                val flowPurchase = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetails)
                                    .build()
                                val responseCode = billingClient.launchBillingFlow(this@MainActivity, flowPurchase).responseCode
                            }
                        }
                    }
                }
            } )
        }

    }
}
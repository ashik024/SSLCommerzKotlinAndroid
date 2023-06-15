package com.example.sslcommerzandroidkotlin

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCAdditionalInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class MainActivity : AppCompatActivity() , SSLCTransactionResponseListener {

    lateinit var amount: EditText
    lateinit var pay: Button
    private lateinit var alert: AlertDialog
    var transactionId: String? = null
    var userId:Int?=null

    private var sslCommerzInitialization: SSLCommerzInitialization? = null
    private var additionalInitializer: SSLCAdditionalInitializer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        amount=findViewById(R.id.amount)
        pay=findViewById(R.id.payBtn)


        pay.setOnClickListener {

            if (amount.text.toString().trim().isBlank()||amount.text.toString().trim().isEmpty()){

                Toast.makeText(this,"please enter payable amount.",Toast.LENGTH_LONG).show()
                return@setOnClickListener

            }else{

                startTransaction(amount.text.toString().toDouble())
                return@setOnClickListener

            }
        }

    }

    private fun startTransaction(totalAmount: Double) {

        userId= (0..100000000000).random().toInt()
        transactionId= Build.VERSION.SDK+userId+System.currentTimeMillis()

        sslCommerzInitialization= SSLCommerzInitialization(
            "********",
            "********",
            totalAmount,
            SSLCCurrencyType.BDT,
            transactionId,
            "yourProductType",
//            SSLCSdkType.LIVE
            SSLCSdkType.TESTBOX
        )

        additionalInitializer = SSLCAdditionalInitializer()
        additionalInitializer!!.valueA = "Value Option 1"
        additionalInitializer!!.valueB = "Value Option 1"
        additionalInitializer!!.valueC = "Value Option 1"
        additionalInitializer!!.valueD = "Value Option 1"
        IntegrateSSLCommerz
            .getInstance(this)
            .addSSLCommerzInitialization(sslCommerzInitialization)
            .addAdditionalInitializer(additionalInitializer)
            .buildApiCall(this)


    }



    override fun transactionSuccess(transInfo: SSLCTransactionInfoModel?) {

        Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()

        Log.d(TAG, "transactionSuccess: "+transInfo?.amount)
        Log.d(TAG, "transactionSuccess: "+transInfo?.tranId)
        Log.d(TAG, "transactionSuccess: "+transInfo?.currencyType)
        Log.d(TAG, "transactionSuccess: "+transInfo?.tranDate)
        Log.d(TAG, "transactionSuccess: "+transInfo?.valueB)


    }
    override fun transactionFail(failMsg: String?) {

        Toast.makeText(this,"Failed: "+failMsg,Toast.LENGTH_LONG).show()
    }
    override fun closed(closeMsg: String?) {

        Toast.makeText(this,"Closed: "+closeMsg,Toast.LENGTH_LONG).show()
    }

    fun Long.toFormattedDateForSslInvoice(): String{
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Dhaka"))
        val cal = Calendar.getInstance(TimeZone.getDefault())
        cal.timeInMillis = this
        val dateGMT = cal.time
        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
        return sdf.format(dateGMT)
    }
    override fun toString(): String {
        return super<AppCompatActivity>.toString()
    }
    fun convertObjectToList(obj: Any): List<*>? {
        var list: List<*>? = ArrayList<Any>()
        if (obj.javaClass.isArray) {
            list = Arrays.asList(*obj as Array<Any?>)
        } else if (obj is Collection<*>) {
            list = ArrayList(obj)
        }
        return list
    }
}
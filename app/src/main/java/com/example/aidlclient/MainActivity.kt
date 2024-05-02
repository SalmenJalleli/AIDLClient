package com.example.aidlclient

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aidlclient.databinding.ActivityMainBinding
import de.eso.bootcamp.tuner.IMyRadioTunerAidlInterface

class MainActivity : AppCompatActivity() {
  private lateinit var iADILRadioTunerService: IMyRadioTunerAidlInterface
  private lateinit var binding : ActivityMainBinding
  private val tag: String = "MainActivity"
  private val mConnection =
      object: ServiceConnection {
          /**
           * Called when a connection to the Service has been established, with
           * the [android.os.IBinder] of the communication channel to the
           * Service.
           *
           *
           * **Note:** If the system has started to bind your
           * client app to a service, it's possible that your app will never receive
           * this callback. Your app won't receive a callback if there's an issue with
           * the service, such as the service crashing while being created.
           *
           * @param name The concrete component name of the service that has
           * been connected.
           *
           * @param service The IBinder of the Service's communication channel,
           * which you can now make calls on.
           */
          override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
              iADILRadioTunerService = IMyRadioTunerAidlInterface.Stub.asInterface(service)
              Log.i(tag,"connected to service $iADILRadioTunerService")
              Log.i(tag,"remote config service connected!")
          }

          /**
           * Called when a connection to the Service has been lost.  This typically
           * happens when the process hosting the service has crashed or been killed.
           * This does *not* remove the ServiceConnection itself -- this
           * binding to the service will remain active, and you will receive a call
           * to [.onServiceConnected] when the Service is next running.
           *
           * @param name The concrete component name of the service whose
           * connection has been lost.
           **/
          override fun onServiceDisconnected(name: ComponentName?) {
              //iADILRadioTunerService = null
              Log.i(tag,"Remote service config disconnected!")
          }

          override fun onNullBinding(name: ComponentName?) {
              super.onNullBinding(name)
              Log.i(tag,"onNullBinding")
          }
      }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    val intent= Intent("AidlRadioTuner")
        .setPackage("de.eso.bootcamp.tuner")
    //mConnection.onServiceConnected()
    val bindingValue = bindService(intent,mConnection, BIND_AUTO_CREATE)
    Log.i(tag,"Service connection state:$bindingValue")
    binding.bntStartPlayback.setOnClickListener{
        try {
            Toast.makeText(this, iADILRadioTunerService?.startPlayback().toString(),Toast.LENGTH_SHORT).show()
        } catch (e: RemoteException){
            Log.i(tag,"couldn't start radio service!")
        }
    }
    binding.bntStopPlayback.setOnClickListener{
        try {
            Toast.makeText(this, iADILRadioTunerService?.stopPlayback().toString(),Toast.LENGTH_SHORT).show()
        } catch (e: RemoteException){
            Log.i(tag,"couldn't stop radio service!")
        }
    }
      binding.bntConnect.setOnClickListener{
          try {
              when(iADILRadioTunerService?.playbackIsRunning()){
                  true -> Toast.makeText(this,"Radio is running!",Toast.LENGTH_SHORT).show()
                  false -> Toast.makeText(this,"Radio is stopped!",Toast.LENGTH_SHORT).show()
                  else -> {}
              }
          } catch (e: RemoteException) {
              Log.i(tag,"couldn't check radio state!")
          }
      }
  }
}


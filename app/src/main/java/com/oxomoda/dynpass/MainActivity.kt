package com.oxomoda.dynpass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.oxomoda.dynpass.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var listener:Custom_UDP_Listener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater) // para acceder los views, reemplaza findViewByID()
        val view = binding.root
        setContentView(view)

        /**
         * dentro de la corrutina corre un loop infinito que queda a la espera de recepcion
         * de mensajes UDP
         */
        GlobalScope.launch(Dispatchers.IO) {
            listener = Custom_UDP_Listener()
            listener.Listen();
        }

    }


    inner class Custom_UDP_Listener:UDP_Listener(){

        /**
         * en caso de detectar el DynPass desde OxoModa se pone la clave aleatoria de 4 digitos
         * recivida en el textView correspondiente
         */
        override fun OnDynPassReceive(pass: String?) {
            runOnUiThread {
                Toast.makeText(applicationContext, pass, Toast.LENGTH_SHORT).show()
                if(pass == null) binding.txvDynPass.setText("- - - -")
                else binding.txvDynPass.setText(pass)
            }
        }
    }
}
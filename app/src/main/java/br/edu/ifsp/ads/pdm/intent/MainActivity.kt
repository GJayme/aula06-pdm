package br.edu.ifsp.ads.pdm.intent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.ifsp.ads.pdm.intent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var outraActivityResultLauncher: ActivityResultLauncher<Intent>

    companion object {
        val PARAMETRO = "PARAMETRO"
//        val OUTRA_ACTIVITY_REQUEST_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        supportActionBar?.title = "Tratando Intents"
        supportActionBar?.subtitle = "Principais tipos"

        outraActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.getStringExtra(PARAMETRO)?.let {
                    activityMainBinding.retornoTv.text = it
                }
            }

        }

        Log.v("${getString(R.string.app_name)}/${localClassName}", "onCreate: Início CC")
    }

    override fun onStart() {
        super.onStart()
        Log.v("${getString(R.string.app_name)}/${localClassName}", "onStart: Início CV")
    }

    override fun onResume() {
        super.onResume()
        Log.v("${getString(R.string.app_name)}/${localClassName}", "onResume: Início CCP")
    }

    override fun onPause() {
        super.onPause()
        Log.v("${getString(R.string.app_name)}/${localClassName}", "onPause: Fim CPP")
    }

    override fun onStop() {
        super.onStop()
        Log.v("${getString(R.string.app_name)}/${localClassName}", "onStop: Fim CV")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("${getString(R.string.app_name)}/${localClassName}", "onDestroy: Fim CC")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when (item.itemId) {
            R.id.outraActivityMi -> {
                //Abrir outra activity
                val outraActivityIntent = Intent(this, OutraActivity::class.java)
                outraActivityIntent.putExtra(PARAMETRO, activityMainBinding.parametroEt.text.toString())

                //Passagem usando Bundle
//                val parametrosBundle: Bundle = Bundle()
//                parametrosBundle.putString(PARAMETRO, activityMainBinding.parametroEt.text.toString())
//                outraActivityIntent.putExtras(parametrosBundle)

//                startActivity(outraActivityIntent)
//                startActivityForResult(outraActivityIntent, OUTRA_ACTIVITY_REQUEST_CODE)
                outraActivityResultLauncher.launch(outraActivityIntent)
                true
            }
           R.id.viewMi -> {
               //Abrir navegador
               true
           }
           R.id.callMi -> {
               //Fazer uma chamada
               true
           }
           R.id.dialMi -> {
               //Abrir o discador
               true
           }
           R.id.pickMi -> {
               //Pegar uma imagem
               true
           }
           R.id.chooserMi -> {
               //Abrir App
               true
           }
            else -> {
                false
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == OUTRA_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
//            data?.getStringExtra(PARAMETRO).let {
//                activityMainBinding.retornoTv.text = it
//            }
//        }
//    }
}
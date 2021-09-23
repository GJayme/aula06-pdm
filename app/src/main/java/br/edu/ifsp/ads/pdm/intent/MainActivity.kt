package br.edu.ifsp.ads.pdm.intent

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.ifsp.ads.pdm.intent.databinding.ActivityMainBinding
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var outraActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var requisicaoPermissaoActivityResultLauncher: ActivityResultLauncher<String>
    private lateinit var selecionarImagemActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var escolherApplicativoActivityResultLauncher: ActivityResultLauncher<Intent>

    companion object {
        val PARAMETRO = "PARAMETRO"
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

        requisicaoPermissaoActivityResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { concedida ->
            if (!concedida) {
                //requisitar permissão
                requisitarPermissaoLigacao()
            } else {
                chamarTelefone()
            }
        }

        selecionarImagemActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            visualizarImagem(resultado)
        }

        escolherApplicativoActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            visualizarImagem(resultado)
        }

        Log.v("${getString(R.string.app_name)}/${localClassName}", "onCreate: Início CC")
    }

    private fun visualizarImagem(resultado: ActivityResult) {
        if (resultado.resultCode == RESULT_OK) {
            val visualizarImagemIntent = Intent(ACTION_VIEW)
            visualizarImagemIntent.data = resultado.data?.data
            startActivity(visualizarImagemIntent)
        }
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

                outraActivityResultLauncher.launch(outraActivityIntent)
                true
            }
           R.id.viewMi -> {
               //Abrir navegador
               //Url precisa conter http ou https
               var url: String = activityMainBinding.parametroEt.text.toString().let {
                   if (!it.lowercase().contains("http[s]?".toRegex())) "http://${it}" else it
               }
               val siteIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
               startActivity(siteIntent)
               true
           }
           R.id.callMi -> {
               //Fazer uma chamada
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   if (checkSelfPermission(CALL_PHONE) != PERMISSION_GRANTED) {
                       //requisito a permissão para o usuário
                        requisitarPermissaoLigacao()
                   } else {
                       //chama o discador
                       chamarTelefone()
                   }
               } else {
                   //chama o discador
                   chamarTelefone()
               }
               true
           }
           R.id.dialMi -> {
               //Abrir o discador
               val discadorIntent = Intent(ACTION_DIAL)
               discadorIntent.data = Uri.parse("tel: ${activityMainBinding.parametroEt.text}")
               startActivity(discadorIntent)
               true
           }
           R.id.pickMi -> {
               //Pegar uma imagem
               selecionarImagemActivityResultLauncher.launch(prepararImagemIntent())
               true
           }
           R.id.chooserMi -> {
               //Abrir lista de aplicativos
               val escolherActivityIntent = Intent(ACTION_CHOOSER)
               escolherActivityIntent.putExtra(EXTRA_INTENT, prepararImagemIntent())
               escolherActivityIntent.putExtra(EXTRA_TITLE, "Escolha um aplicativo")
               escolherApplicativoActivityResultLauncher.launch(escolherActivityIntent)
               true
           }
            else -> {
                false
            }
        }
    }

    private fun prepararImagemIntent(): Intent {
        val pegarImagemIntent = Intent(ACTION_PICK)
        val diretorio = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path
        pegarImagemIntent.setDataAndType(Uri.parse(diretorio), "image/*")
        return pegarImagemIntent
    }

    private fun requisitarPermissaoLigacao() {
        requisicaoPermissaoActivityResultLauncher.launch(CALL_PHONE)
    }


    private fun chamarTelefone() {
        val chamarIntent = Intent()
        chamarIntent.action = Intent.ACTION_CALL
        chamarIntent.data = Uri.parse("tel: ${activityMainBinding.parametroEt.text}")
        startActivity(chamarIntent)
    }
}
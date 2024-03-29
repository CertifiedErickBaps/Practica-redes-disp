package mx.itesm.practica_redes_erick

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import kotlinx.android.synthetic.main.fragment_descarga_imagen.*
import kotlinx.android.synthetic.main.fragment_descarga_texto.*

class MenuPrincipalActiv : AppCompatActivity() {

    lateinit var dialogo: ProgressDialog
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_texto, R.id.nav_imagen, R.id.nav_libros),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    private fun descargarTexto(view: View?) {

        mostrarDialogoEspera()
        AndroidNetworking.get("https://www.gutenberg.org/cache/epub/2000/pg2000.txt")
            .build()
            .getAsString(object: StringRequestListener {
                override fun onResponse(response: String?) {
                    tvTexto.text = response
                    dialogo.dismiss()

                }

                override fun onError(anError: ANError?) {
                    tvTexto.text = "Error en la descarga"
                    dialogo.dismiss()
                }

            })
    }

    private fun descargarImagen(view: View?) {

        AndroidNetworking.get("https://upload.wikimedia.org/wikipedia/commons/d/dd/Big_%26_Small_Pumkins.JPG").build().getAsBitmap(object :
            BitmapRequestListener {
            override fun onResponse(response: Bitmap?) {
                imgDescarga.setImageBitmap(response)
            }

            override fun onError(anError: ANError?) {
            }
        })

    }

    override fun onStart() {
        super.onStart()
        AndroidNetworking.initialize(baseContext)
        //Boton
        btnDescargarTexto.setOnClickListener { view: View? ->
            descargarTexto(view)
        }

        //TextView
        tvTexto.movementMethod = ScrollingMovementMethod()

        btnDescargarImagen.setOnClickListener{ view ->  descargarImagen(view)}

    }



    private fun mostrarDialogoEspera() {
        this.dialogo = ProgressDialog(this)
        dialogo.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialogo.setMessage("Descargando")
        dialogo.isIndeterminate = true
        dialogo.setCanceledOnTouchOutside(false)
        dialogo.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

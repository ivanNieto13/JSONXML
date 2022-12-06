package dgtic.unam.jsonxml

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import dgtic.unam.jsonxml.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var volleyAPI: VolleyAPI
    private lateinit var url: String
    private val ipPuerto = "192.168.0.2:8080"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        volleyAPI = VolleyAPI(this)
        binding.btnSearch.setOnClickListener {
            binding.tvOutText.text = ""
            url = "https://www.google.com/search?q" + URLEncoder.encode(
                binding.etSearchText.text.toString(),
                "UTF-8"
            )
            buscar()
        }
        binding.btnRestXML.setOnClickListener {
            studentXML()
        }
        binding.btnRestJSON.setOnClickListener {
            studentJSON()
        }
        binding.btnRestJSONId.setOnClickListener {
            studentJSONId()
        }
        binding.btnRestJSONAdd.setOnClickListener {
            studentJSONAdd()
        }
        binding.btnRestJSONDelete.setOnClickListener {
            studentJSONDelete()
        }
    }

    private fun buscar() {
        var stringRequest = object : StringRequest(Method.GET, url, Response.Listener { response ->
            binding.tvOutText.text = response
        }, Response.ErrorListener { error ->
            println(error.toString())
            binding.tvOutText.text = "No se encuentra la información"
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] =
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"
                return headers
            }
        }
        volleyAPI.add(stringRequest)
    }

    private fun studentXML() {
        val urlXML = "http://$ipPuerto/estudiantesXML"
        val stringRequest =
            object : StringRequest(Method.GET, urlXML, Response.Listener { response ->
                binding.tvOutText.text = response
            }, Response.ErrorListener { error ->
                println(error.toString())
                binding.tvOutText.text = "No se encuentra la información"
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] =
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"
                    return headers
                }
            }
        volleyAPI.add(stringRequest)
    }

    private fun studentJSON() {
        val urlJSON = "http://$ipPuerto/estudiantesJSON"
        var cadena = ""
        val jsonRequest =
            object : JsonArrayRequest(urlJSON, Response.Listener<JSONArray> { response ->
                (0 until response.length()).forEach {
                    val estudiante = response.getJSONObject(it)
                    val materia = estudiante.getJSONArray("materias")
                    cadena += estudiante.get("cuenta").toString() + "<"
                    (0 until materia.length()).forEach {
                        val datos = materia.getJSONObject(it)
                        cadena += datos.get("nombre").toString() + "**" + datos.get("creditos")
                            .toString() + "---"
                    }
                    cadena += "> \n"
                }
                binding.tvOutText.text = cadena
            }, Response.ErrorListener { error ->
                println(error.toString())
                binding.tvOutText.text = "No se encuentra la información"
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] =
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"
                    return headers
                }
            }
        volleyAPI.add(jsonRequest)
    }

    private fun studentJSONId() {
        val urlJSON = "http://$ipPuerto/id/" + binding.etSearchText.text.toString()
        var cadena = ""
        val jsonRequest = object : JsonObjectRequest(
            Method.GET, urlJSON, null,
            Response.Listener<JSONObject> { response ->
                binding.tvOutText.text = response.get("cuenta").toString() + "---" + response.get("nombre").toString() + "\n"
            },
            Response.ErrorListener { error ->
                println(error.toString())
                binding.tvOutText.text = "No se encuentra la información"
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] =
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"
                return headers
            }
        }
        volleyAPI.add(jsonRequest)
    }

    private fun studentJSONAdd() {
        val urlJSON = "http://$ipPuerto/agregarestudiante"
        var cadena = ""
        val jsonRequest =
            object : JsonArrayRequest(urlJSON, Response.Listener<JSONArray> { response ->
                (0 until response.length()).forEach {
                    val estudiante = response.getJSONObject(it)
                    val materia = estudiante.getJSONArray("materias")
                    cadena += estudiante.get("cuenta").toString() + "<"
                    (0 until materia.length()).forEach {
                        val datos = materia.getJSONObject(it)
                        cadena += datos.get("nombre").toString() + "**" + datos.get("creditos")
                            .toString() + "---"
                    }
                    cadena += "> \n"
                }
                binding.tvOutText.text = cadena
            }, Response.ErrorListener { error ->
                println(error.toString())
                binding.tvOutText.text = "No se encuentra la información"
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] =
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"
                    return headers
                }

                override fun getBody(): ByteArray {
                    val estudiante = JSONObject()
                    estudiante.put("cuenta", "A000")
                    estudiante.put("nombre", "Android")
                    estudiante.put("edad", "200")
                    val materias = JSONArray()
                    val itemMaterias = JSONObject()
                    itemMaterias.put("id", "1")
                    itemMaterias.put("nombre", "Nueva materia")
                    itemMaterias.put("creditos", "100")
                    materias.put(itemMaterias)
                    estudiante.put("materias", materias)
                    return estudiante.toString().toByteArray(Charsets.UTF_8)
                }

                override fun getMethod(): Int {
                    return Method.POST
                }
            }
        volleyAPI.add(jsonRequest)
    }

    private fun studentJSONDelete() {
        val urlJSON = "http://$ipPuerto/borrarestudiante/" + binding.etSearchText.text.toString()
        var cadena = ""
        val jsonRequest =
            object : JsonArrayRequest(urlJSON, Response.Listener<JSONArray> { response ->
                (0 until response.length()).forEach {
                    val estudiante = response.getJSONObject(it)
                    val materia = estudiante.getJSONArray("materias")
                    cadena += estudiante.get("cuenta").toString() + "<"
                    (0 until materia.length()).forEach {
                        val datos = materia.getJSONObject(it)
                        cadena += datos.get("nombre").toString() + "**" + datos.get("creditos")
                            .toString() + "---"
                    }
                    cadena += "> \n"
                }
                binding.tvOutText.text = cadena
            }, Response.ErrorListener { error ->
                println(error.toString())
                binding.tvOutText.text = "No se encuentra la información"
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] =
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"
                    return headers
                }

                override fun getBody(): ByteArray {
                    val estudiante = JSONObject()
                    estudiante.put("cuenta", "A000")
                    estudiante.put("nombre", "Android")
                    estudiante.put("edad", "200")
                    val materias = JSONArray()
                    val itemMaterias = JSONObject()
                    itemMaterias.put("id", "1")
                    itemMaterias.put("nombre", "Nueva materia")
                    itemMaterias.put("creditos", "100")
                    materias.put(itemMaterias)
                    estudiante.put("materias", materias)
                    return estudiante.toString().toByteArray(Charsets.UTF_8)
                }

                override fun getMethod(): Int {
                    return Method.DELETE
                }
            }
        volleyAPI.add(jsonRequest)
    }

}
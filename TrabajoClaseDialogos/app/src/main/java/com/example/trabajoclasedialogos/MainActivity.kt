package com.example.trabajoclasedialogos

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.trabajoclasedialogos.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var nombreUsuario: String = ""
    private var horaSeleccionada: String = ""
    private var fechaSeleccionada: String = ""
    private var numeroAsignaturasSeleccionadas: Int = 0
    private var notaMedia: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            mostrarCuadroDialogoHora()
        }
    }
    private fun mostrarCuadroDialogoHora() {
        val calendar = Calendar.getInstance()
        val hora = calendar.get(Calendar.HOUR)
        val minuto = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
            val horaFormato12 = if (hourOfDay > 12) hourOfDay - 12 else hourOfDay
            horaSeleccionada = "$horaFormato12:$minute ${if (hourOfDay >= 12) "PM" else "AM"}"
            mostrarCuadroDialogoFecha()
        }, hora, minuto, false)

        timePickerDialog.show()
    }
    private fun mostrarCuadroDialogoFecha() {
        val calendar = Calendar.getInstance()
        val año = calendar.get(Calendar.YEAR)
        val mes = calendar.get(Calendar.MONTH)
        val dia = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            fechaSeleccionada = "$dayOfMonth/${month + 1}/$year"
            mostrarCuadroDialogoPersonalizadoNombre()
        }, año, mes, dia)

        datePickerDialog.show()
    }
    private fun mostrarCuadroDialogoPersonalizadoNombre() {
        val customDialogView = layoutInflater.inflate(R.layout.custom_dialog_nombre, null)
        val editTextNombre = customDialogView.findViewById<EditText>(R.id.editTextNombre)

        AlertDialog.Builder(this)
            .setView(customDialogView)
            .setPositiveButton("OK") { _, _ ->
                if (editTextNombre.text.isNotEmpty()) {
                    nombreUsuario = editTextNombre.text.toString()
                    mostrarCuadroDialogoConfirmacion()
                } else {
                }
            }
            .create()
            .show()
    }
    private fun mostrarCuadroDialogoConfirmacion() {
        AlertDialog.Builder(this)
            .setMessage("Buenos días $nombreUsuario, vas a registrar una respuesta el $fechaSeleccionada a las $horaSeleccionada. ¿Estás seguro que quieres continuar?")
            .setPositiveButton("Sí") { _, _ ->
                mostrarCuadroDialogoSeleccionMultiple()
            }
            .setNegativeButton("No", null)
            .show()
    }
    private fun mostrarCuadroDialogoSeleccionMultiple() {
        val asignaturas = arrayOf("PMDM", "DI", "AD","SGE","EIE","ING")
        val asignaturasSeleccionadas = BooleanArray(asignaturas.size)
        AlertDialog.Builder(this)
            .setMultiChoiceItems(asignaturas, asignaturasSeleccionadas) { _, which, isChecked ->
                asignaturasSeleccionadas[which] = isChecked
            }
            .setPositiveButton("OK") { _, _ ->
                val asignaturasElegidas = asignaturas.filterIndexed { index, _ -> asignaturasSeleccionadas[index] }
                numeroAsignaturasSeleccionadas = asignaturasElegidas.size
                mostrarCuadroDialogoNotaMedia()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    @SuppressLint("MissingInflatedId")
    private fun mostrarCuadroDialogoNotaMedia() {
        val customDialogNotaView = layoutInflater.inflate(R.layout.custom_dialog_nota, null)
        val editTextNota = customDialogNotaView.findViewById<EditText>(R.id.editTextNota)

        AlertDialog.Builder(this)
            .setView(customDialogNotaView)
            .setPositiveButton("OK") { _, _ ->
                try {
                    notaMedia = editTextNota.text.toString().toDouble()
                    mostrarCuadroDialogoResumen()
                } catch (e: NumberFormatException) {
                }
            }
            .create()
            .show()
    }
    private fun mostrarCuadroDialogoResumen() {
        AlertDialog.Builder(this)
            .setMessage("Resumen:\n- Nombre y Apellidos: $nombreUsuario\n- Hora: $horaSeleccionada\n- Fecha: $fechaSeleccionada\n- Número de asignaturas a evaluar: $numeroAsignaturasSeleccionadas\n- Media obtenida: $notaMedia")
            .setPositiveButton("Aceptar", null)
            .show()
    }

}



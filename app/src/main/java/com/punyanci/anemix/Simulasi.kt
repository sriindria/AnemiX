package com.punyanci.anemix

import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class Simulasi : AppCompatActivity() {

    private lateinit var interpreter: Interpreter
    private val mModelPath = "anemiatypes.tflite"

    private lateinit var resultText: TextView
    private lateinit var definitionText: TextView
    private lateinit var causesText: TextView
    private lateinit var tipsText: TextView
    private lateinit var editWBC: EditText
    private lateinit var editLYMp: EditText
    private lateinit var editNEUTp: EditText
    private lateinit var editLYMn: EditText
    private lateinit var editNEUTn: EditText
    private lateinit var editRBC: EditText
    private lateinit var editHGB: EditText
    private lateinit var editHCT: EditText
    private lateinit var editMCV: EditText
    private lateinit var editMCH: EditText
    private lateinit var editMCHC: EditText
    private lateinit var editPLT: EditText
    private lateinit var editPDW: EditText
    private lateinit var editPCT: EditText
    private lateinit var checkButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_simulasi)

        resultText = findViewById(R.id.txtResult)
        definitionText = findViewById(R.id.txtDefinition)
        causesText = findViewById(R.id.txtCauses)
        tipsText = findViewById(R.id.txtTips)
        editWBC = findViewById(R.id.editWBC)
        editLYMp = findViewById(R.id.editLYMp)
        editNEUTp = findViewById(R.id.editNEUTp)
        editLYMn = findViewById(R.id.editLYMn)
        editNEUTn = findViewById(R.id.editNEUTn)
        editRBC = findViewById(R.id.editRBC)
        editHGB = findViewById(R.id.editHGB)
        editHCT = findViewById(R.id.editHCT)
        editMCV = findViewById(R.id.editMCV)
        editMCH = findViewById(R.id.editMCH)
        editMCHC = findViewById(R.id.editMCHC)
        editPLT = findViewById(R.id.editPLT)
        editPDW = findViewById(R.id.editPDW)
        editPCT = findViewById(R.id.editPCT)
        checkButton = findViewById(R.id.btnCheck)

        checkButton.setOnClickListener {
            if (validateInputs()) {
                var result = doInference(
                    editWBC.text.toString(),
                    editLYMp.text.toString(),
                    editNEUTp.text.toString(),
                    editLYMn.text.toString(),
                    editNEUTn.text.toString(),
                    editRBC.text.toString(),
                    editHGB.text.toString(),
                    editHCT.text.toString(),
                    editMCV.text.toString(),
                    editMCH.text.toString(),
                    editMCHC.text.toString(),
                    editPLT.text.toString(),
                    editPDW.text.toString(),
                    editPCT.text.toString()
                )
                runOnUiThread {
                    val anemiaInfo = getAnemiaInfo(result)
                    resultText.text = anemiaInfo.diagnosis
                    definitionText.text = anemiaInfo.definition
                    causesText.text = anemiaInfo.causes
                    tipsText.text = anemiaInfo.tips
                    definitionText.visibility = TextView.VISIBLE
                    causesText.visibility = TextView.VISIBLE
                    tipsText.visibility = TextView.VISIBLE
                }
            } else {
                Toast.makeText(this, "Silakan isi semua kolom", Toast.LENGTH_SHORT).show()
            }
        }
        initInterpreter()
        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this@Simulasi, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateInputs(): Boolean {
        return editWBC.text.isNotEmpty() &&
                editLYMp.text.isNotEmpty() &&
                editNEUTp.text.isNotEmpty() &&
                editLYMn.text.isNotEmpty() &&
                editNEUTn.text.isNotEmpty() &&
                editRBC.text.isNotEmpty() &&
                editHGB.text.isNotEmpty() &&
                editHCT.text.isNotEmpty() &&
                editMCV.text.isNotEmpty() &&
                editMCH.text.isNotEmpty() &&
                editMCHC.text.isNotEmpty() &&
                editPLT.text.isNotEmpty() &&
                editPDW.text.isNotEmpty() &&
                editPCT.text.isNotEmpty()
    }

    private fun doInference(
        input1: String,
        input2: String,
        input3: String,
        input4: String,
        input5: String,
        input6: String,
        input7: String,
        input8: String,
        input9: String,
        input10: String,
        input11: String,
        input12: String,
        input13: String,
        input14: String
    ): Int {
        val inputVal = FloatArray(14)
        inputVal[0] = input1.toFloat()
        inputVal[1] = input2.toFloat()
        inputVal[2] = input3.toFloat()
        inputVal[3] = input4.toFloat()
        inputVal[4] = input5.toFloat()
        inputVal[5] = input6.toFloat()
        inputVal[6] = input7.toFloat()
        inputVal[7] = input8.toFloat()
        inputVal[8] = input9.toFloat()
        inputVal[9] = input10.toFloat()
        inputVal[10] = input11.toFloat()
        inputVal[11] = input12.toFloat()
        inputVal[12] = input13.toFloat()
        inputVal[13] = input14.toFloat()
        val output = Array(1) { FloatArray(9) }
        interpreter.run(inputVal, output)

        Log.e("result", (output[0].toList() + " ").toString())

        return output[0].indexOfFirst { it == output[0].maxOrNull() }
    }

    private fun initInterpreter() {
        val options = Interpreter.Options()
        options.setNumThreads(5)
        options.setUseNNAPI(true)
        interpreter = Interpreter(loadModelFile(assets, mModelPath), options)
    }

    private fun loadModelFile(assetsManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetsManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    data class AnemiaInfo(
        val diagnosis: String,
        val definition: String,
        val causes: String,
        val tips: String
    )

    private fun getAnemiaInfo(result: Int): AnemiaInfo {
        return when (result) {
            0 -> AnemiaInfo(
                "Healthy",
                "Tidak ada anemia yang terdeteksi.",
                "Tidak ada penyebab.",
                "Pertahankan gaya hidup sehat."
            )
            1 -> AnemiaInfo(
                "Iron deficiency anemia",
                "Definisi: \nAnemia defisiensi besi adalah kondisi di mana tubuh kekurangan zat besi yang diperlukan untuk memproduksi hemoglobin, protein dalam sel darah merah yang membawa oksigen ke seluruh tubuh.",
                "Penyebab: \n- Asupan zat besi yang tidak cukup dari makanan.\n- Kehilangan darah yang berlebihan (misalnya, karena menstruasi berat atau perdarahan gastrointestinal). \n- Ketidakmampuan tubuh menyerap zat besi dengan baik (seperti pada penyakit celiac atau setelah operasi lambung).",
                "Tips: \n- Konsumsi makanan kaya zat besi seperti daging merah, bayam, dan kacang-kacangan. \n- Minum suplemen zat besi jika dianjurkan oleh dokter. \n- Makan makanan kaya vitamin C untuk meningkatkan penyerapan zat besi."
            )
            2 -> AnemiaInfo(
                "Leukemia",
                "Definisi: \nLeukemia adalah kanker darah dan sumsum tulang yang ditandai dengan produksi sel darah putih abnormal yang tidak terkontrol.",
                "Penyebab: \nPenyebab pasti tidak diketahui, tetapi faktor genetik dan lingkungan (seperti paparan radiasi atau bahan kimia tertentu) dapat berperan.",
                "Tips: \n- Menghindari paparan bahan kimia berbahaya dan radiasi.\n- Menjalani pemeriksaan kesehatan secara teratur untuk mendeteksi tanda-tanda awal. \n- Konsultasi dengan ahli hematologi, terapi sesuai diagnosis."
            )
            3 -> AnemiaInfo(
                "Leukemia with thrombocytopenia",
                "Definisi: \nKondisi di mana leukemia disertai dengan trombositopenia, yaitu rendahnya jumlah trombosit dalam darah, yang berfungsi dalam proses pembekuan darah.",
                "Penyebab: \n- Sel leukemia yang berlebihan dapat mengganggu produksi normal sel darah, termasuk trombosit. \n- Pengobatan leukemia seperti kemoterapi juga dapat menyebabkan trombositopenia.",
                "Tips: \n- Monitor jumlah trombosit secara teratur.\n" +
                        "- Hindari aktivitas yang dapat menyebabkan perdarahan atau memar.\n" +
                        "- Konsultasi dengan dokter tentang pengobatan yang dapat membantu meningkatkan jumlah trombosit."
            )
            4 -> AnemiaInfo(
                "Macrocytic anemia",
                "Definisi: \nAnemia makrositik adalah anemia yang ditandai dengan sel darah merah yang lebih besar dari normal.",
                "Penyebab: \n- Kekurangan vitamin B12 atau folat.\n" +
                        "- Gangguan sumsum tulang seperti sindrom myelodysplastic.\n" +
                        "- Alkoholisme kronis atau penggunaan obat tertentu.",
                "Tips: \n- Konsumsi makanan kaya vitamin B12 (seperti daging, telur, produk susu) dan folat (seperti sayuran hijau, buah-buahan, kacang-kacangan).\n" +
                        "- Hindari alkohol berlebihan.\n" +
                        "- Konsultasi dengan dokter mengenai suplemen vitamin jika diperlukan."
            )
            5 -> AnemiaInfo(
                "Normocytic hypochromic anemia",
                "Definisi: \nAnemia normositik hipokromik adalah anemia dengan sel darah merah berukuran normal tetapi memiliki kadar hemoglobin yang rendah.",
                "Penyebab: \n- Penyakit kronis seperti gagal ginjal atau penyakit autoimun.\n" +
                        "- Kehilangan darah kronis.\n" +
                        "- Infeksi atau peradangan kronis.",
                "Tips: \n- Tangani penyakit atau kondisi mendasar yang menyebabkan anemia.\n" +
                        "- Konsumsi diet seimbang dan cukup zat besi.\n" +
                        "- Berkonsultasi dengan dokter mengenai pengobatan yang tepat."
            )
            6 -> AnemiaInfo(
                "Normocytic normochromic anemia",
                "Definisi: \nAnemia normositik normokromik adalah anemia dengan sel darah merah yang berukuran dan berwarna normal tetapi jumlahnya rendah.",
                "Penyebab: \n- Penyakit kronis seperti penyakit ginjal atau kanker.\n" +
                        "- Kehilangan darah akut.\n" +
                        "- Gangguan sumsum tulang.",
                "Tips: \n- Identifikasi dan obati kondisi mendasar.\n" +
                        "- Konsumsi diet seimbang.\n" +
                        "- Berkonsultasi dengan dokter untuk penanganan lebih lanjut."
            )
            7 -> AnemiaInfo(
                "Other microcytic anemia",
                "Definisi: \nAnemia mikrositik lainnya adalah jenis anemia dengan sel darah merah yang lebih kecil dari normal, tetapi bukan disebabkan oleh kekurangan zat besi.",
                "Penyebab: \n- Anemia thalassemia.\n" +
                        "- Anemia penyakit kronis.",
                "Tips: \n- Lakukan pemeriksaan untuk menentukan penyebab spesifik anemia.\n" +
                        "- Mengikuti diet yang direkomendasikan dokter.\n" +
                        "- Mengikuti pengobatan dan perawatan sesuai dengan penyebab spesifik."
            )
            else -> AnemiaInfo(
                "Thrombocytopenia",
                "Definisi: \nTrombositopenia adalah kondisi di mana jumlah trombosit dalam darah lebih rendah dari normal, yang meningkatkan risiko perdarahan.",
                "Penyebab: \n- Penyakit autoimun seperti idiopathic thrombocytopenic purpura (ITP).\n" +
                        "- Infeksi virus (seperti dengue atau hepatitis).\n" +
                        "- Pengaruh obat-obatan tertentu.",
                "Tips: \n- Hindari obat yang dapat memperburuk kondisi, seperti aspirin atau ibuprofen.\n" +
                        "- Konsultasi dengan dokter tentang pengobatan yang tepat.\n" +
                        "- Monitor jumlah trombosit secara teratur dan hindari aktivitas yang berisiko menyebabkan cedera atau perdarahan."
            )
        }
    }
}

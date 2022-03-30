package com.project.worcul

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.worcul.databinding.ActivityWriteBinding
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


class WriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteBinding
    private lateinit var database: DatabaseReference
    private lateinit var LinkModel: ArrayList<FirebaseMessageDataFile>
    lateinit var ImageUri: Uri
    var JsonFile = "https://api.ocr.space/Parse/Image"
    private val PICK_FROM_GALLERY = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.database.reference

        binding.close.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            val personPhoto: Uri? = acct.photoUrl
            val personEmail = acct.email

            if (personEmail.isNullOrEmpty()) {
                Toast.makeText(this, "no profile", Toast.LENGTH_SHORT).show()
            } else {
                val imageView = binding.profile2
                Glide.with(this).load(personPhoto).centerCrop().into(imageView)
            }
        }

        val profileName = intent.getStringExtra("link").toString().trim()
        val description = intent.getStringExtra("description").toString().trim()

        if (profileName.equals("null") && description.equals("null")) {

        }else if(description.equals("null")){
            binding.text.setText("$profileName")
        }
        else {
            binding.text.setText("$profileName \n$description\n")

        }
        binding.button.setOnClickListener {
            val messageOfUser = binding.text.text.toString()

            
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            val tsLong = System.currentTimeMillis() / 1000
            if (acct != null) {
                val personName = acct.displayName
                writeNewUser("$personName", uid, messageOfUser, tsLong )
                finish()
            } else {
                writeNewUser("praveen", uid, messageOfUser, tsLong)
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.addImage.setOnClickListener {
            selectImage()
        }

        binding.GenerateLink.setOnClickListener(View.OnClickListener {
//            uploadToFirebase()
            GenerateLink()
        })
    }

    private fun GenerateLink() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Generating link fo txt....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, "https://pastebin.com/api/api_post.php", Response.Listener { response ->

                Log.d("here", response)
                try {
                    val parsedResult = response.toString()
                    binding.text.append(parsedResult)
                    if (progressDialog.isShowing) progressDialog.dismiss()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this,
                        "$e", Toast.LENGTH_LONG
                    ).show()
                    if (progressDialog.isShowing) progressDialog.dismiss()
                }


            }, Response.ErrorListener { error ->
                Toast.makeText(
                    this,
                    "$error", Toast.LENGTH_LONG
                ).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }) {
            override fun getParams(): Map<String, String> {
                var text = binding.TextFromImage.text.toString().trim()
                val params1: HashMap<String, String> = HashMap()
                params1["api_dev_key"] = "md2bZWU1UklGavEWO4ukjigUqs8TfxR2"
                params1["api_paste_code"] = "$text"
                params1["api_option"] = "paste"

                return params1
            }

//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["api_dev_key"] = "md2bZWU1UklGavEWO4ukjigUqs8TfxR2"
//                return params
//            }
        }
        val rq: RequestQueue = Volley.newRequestQueue(this)
        rq.add(stringRequest)
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            ImageUri = data?.data!!
            binding.Image.setImageURI(ImageUri)

            val imageStream: InputStream? = contentResolver.openInputStream(ImageUri)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            val encodedImage: String = encodeImage(selectedImage).toString().trim()

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("uploading file....")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, JsonFile, Response.Listener { response ->

                    Log.d("here", response)
                    try {
                        val parsedResult = JSONObject(response)
                        val arrayResult = parsedResult.getJSONArray("ParsedResults")
                        val TextOverlay = arrayResult.getJSONObject(0)
                        val ParsedText = TextOverlay.getString("ParsedText").toString()
                        binding.TextFromImage.setText("$ParsedText")
                        Log.d("1", "$parsedResult")
                        Log.d("2", "$arrayResult")
                        Log.d("3", "$TextOverlay")
                        Log.d("4", "$ParsedText")
                        if (progressDialog.isShowing) progressDialog.dismiss()


//                        for (i in 0..jsonArray.length() - 1) {
//                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
//                            val doc_ph_number = jsonObject.getString("ph_no_doctor")
//                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            "$e", Toast.LENGTH_LONG
                        ).show()
                        if (progressDialog.isShowing) progressDialog.dismiss()
                    }


                }, Response.ErrorListener { error ->
                    Toast.makeText(
                        this,
                        "$error", Toast.LENGTH_LONG
                    ).show()
                    if (progressDialog.isShowing) progressDialog.dismiss()
                }) {
                override fun getParams(): Map<String, String> {
                    val url = "http://dl.a9t9.com/blog/ocr-online/screenshot.jpg"
                    val params1: HashMap<String, String> = HashMap()
                    params1["base64Image"] = "data:image/jpeg;base64,$encodedImage"

                    return params1
                }

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["apikey"] = "K83815613088957"
                    return params
                }
            }
            val rq: RequestQueue = Volley.newRequestQueue(this)
            rq.add(stringRequest)

        }
    }

    private fun encodeImage(bm: Bitmap): String {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("uploading file....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 30, baos)
        val b = baos.toByteArray()
//        return Base64.encodeToString(b, Base64.DEFAULT)
        if (progressDialog.isShowing) progressDialog.dismiss()
        return android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT)

    }


    /*   private fun uploadToFirebase() {

           val progressDialog = ProgressDialog(this)
           progressDialog.setMessage("uploading file....")
           progressDialog.setCancelable(false)
           progressDialog.show()

           val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
           val now = Date()
           val fileName = formatter.format(now)
           val storageReference = FirebaseStorage.getInstance().getReference("Images/$fileName")
           val link = storageReference.getDownloadUrl().toString();
           Toast.makeText(this, "$link", Toast.LENGTH_SHORT).show()
           Log.d("this", "$link")

           storageReference.putFile(ImageUri).addOnSuccessListener {
               binding.Image.setImageURI(null)
               Toast.makeText(this, "successfully uploaded", Toast.LENGTH_SHORT).show()

               if (progressDialog.isShowing) progressDialog.dismiss()

           }.addOnFailureListener {
               if (progressDialog.isShowing) progressDialog.dismiss()
               Toast.makeText(this, "not uploaded", Toast.LENGTH_SHORT).show()

           }
       } */

    private fun writeNewUser(nameOfUser: String, UID: String, messageOfUser: String, Timestamp: Long) {

        val Message1 = FirebaseMessageDataFile(nameOfUser, messageOfUser, Timestamp)
        val uniqueId = UUID.randomUUID().toString();

        database.child("Users").child("messages").child(UID).child("$UID$nameOfUser$uniqueId")
            .setValue(Message1)
        database.child("messages").child("$UID$nameOfUser$uniqueId").setValue(Message1)

    }
}

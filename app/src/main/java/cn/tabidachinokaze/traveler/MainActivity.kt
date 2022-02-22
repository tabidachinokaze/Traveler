package cn.tabidachinokaze.traveler

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.tabidachinokaze.traveler.adapter.DataAdapter
import cn.tabidachinokaze.traveler.adapter.data.EmptyItem
import cn.tabidachinokaze.traveler.adapter.data.FileItem
import cn.tabidachinokaze.traveler.adapter.data.HeaderItem
import cn.tabidachinokaze.traveler.adapter.data.NotPermissionItem
import cn.tabidachinokaze.traveler.adapter.holder.NotPermissionHolder
import com.google.android.material.color.DynamicColors

class MainActivity : AppCompatActivity(), NotPermissionHolder.Listener {
    private val gamesData = mutableListOf<cn.tabidachinokaze.traveler.adapter.data.ItemType>()
    private val iconMaps = mutableMapOf<String, Drawable>()
    private val dataAdapter = DataAdapter(this, gamesData, iconMaps)
    private val packageNames =
        arrayOf(Util.YUANSHEN_OFFICIAL, Util.YUANSHEN_BILIBILI, Util.GENSHIN_IMPACT)
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("QueryPermissionsNeeded", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DynamicColors.applyIfAvailable(this)

        packageManager.getInstalledPackages(0).forEach { packageInfo ->
            if (packageNames.contains(packageInfo.packageName)) {
                iconMaps[packageInfo.packageName] =
                    packageManager.getApplicationIcon(packageInfo.applicationInfo)
            }
        }

        findViewById<RecyclerView>(R.id.recycler_view).apply {
            addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            )
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = dataAdapter
        }

        updateAdapter()

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val toast = Toast(this)
                toast.setText(R.string.permission_obtained_failed)
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        val uri = result.data?.data
                        val flags = result.data?.flags
                        if (uri != null && flags != null) {
                            if (uri.toString() == Util.ANDROID_DATA_URI) {
                                contentResolver.takePersistableUriPermission(
                                    uri,
                                    flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                                )
                                updateAdapter()
                                toast.setText(R.string.permission_obtained_successfully)
                            }
                        }
                    }
                }
                toast.show()
            }
    }

    override fun requestPermission() {
        activityResultLauncher.launch(Util.requestIntent("Android/data"))
    }

    private fun isUriGrant(uri: String): Boolean {
        contentResolver.persistedUriPermissions.forEach {
            if (it.isReadPermission && it.uri.toString() == uri) {
                return true
            }
        }
        return false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateAdapter() {
        gamesData.clear()
        if (!isUriGrant(Util.ANDROID_DATA_URI)) {
            gamesData.add(NotPermissionItem(getString(R.string.not_permission)))
        } else {
            val documentFile = DocumentFile.fromTreeUri(
                this@MainActivity,
                Uri.parse(Util.ANDROID_DATA_URI)
            )
            documentFile?.listFiles()?.forEach {
                if (packageNames.contains(it.name)) {
                    gamesData.add(FileItem(it))
                }
            }
            if (gamesData.isEmpty()) {
                gamesData.add(EmptyItem(getString(R.string.not_game)))
            } else {
                gamesData.add(0, HeaderItem(getString(R.string.game_data), gamesData.size))
            }
        }
        dataAdapter.notifyDataSetChanged()
    }
}
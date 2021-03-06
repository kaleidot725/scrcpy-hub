package model.repository

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.entity.Setting
import java.io.File

class SettingRepository(private val root: String) {
    fun get(): Setting {
        return load()
    }

    fun update(setting: Setting) {
        createDir()
        write(setting)
    }

    private fun write(setting: Setting) {
        try {
            File(createFilePath()).outputStream().apply {
                this.write(Json.encodeToString(setting).toByteArray())
                this.close()
            }
        } catch (e: Exception) {
            return
        }
    }

    private fun load(): Setting {
        return try {
            val content = File(createFilePath()).readText()
            Json.decodeFromString(string = content)
        } catch (e: Exception) {
            Setting()
        }
    }

    private fun createDir() {
        try {
            val file = File(createDirPath())
            if (!file.exists()) {
                val b = file.mkdir()
                print(b)
            }
        } catch (e: Exception) {
            return
        }
    }

    private fun createDirPath(): String {
        val home = System.getProperty("user.home")
        return "$home$root"
    }

    private fun createFilePath(): String {
        val dir = createDirPath()
        return "$dir$SETTING_FILE_NAME"
    }

    companion object {
        private const val SETTING_FILE_NAME = "setting.json"
    }
}
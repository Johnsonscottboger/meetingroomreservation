package com.aning.meetingroomreservation.controller

import com.aning.meetingroomreservation.annotation.AllowAnonymous
import com.aning.meetingroomreservation.service.IUserImportService
import com.aning.meetingroomreservation.viewmodel.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.util.ResourceUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.net.URLDecoder
import java.nio.file.Path
import java.util.*

/**
 * 文件管理控制器
 */
@Controller
@RequestMapping("/file")
public class FileController {
    @Autowired
    private lateinit var _parseService: IUserImportService

    /**
     * 上传文件
     */
    @ResponseBody
    @PostMapping("/upload")
    public fun upload(file: MultipartFile): Json {
        val operation = "上传文件"
        return try {
            var fileName = file.originalFilename
            val extension = fileName?.substring(fileName.lastIndexOf('.'))
            fileName = UUID.randomUUID().toString() + extension
            val dest = File(getCurrentPath(), "static/file/$fileName")
            if (!dest.parentFile.exists())
                dest.parentFile.mkdir()
            file.transferTo(dest)
            Json.succ(operation)
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }

    /**
     * 导入文件
     */
    @ResponseBody
    @PostMapping("/import")
    public fun import(file: MultipartFile): Json {
        val operation = "导入文件"
        var tempFile: File? = null
        return try {
            var fileName = file.originalFilename
            val extension = fileName?.substring(fileName.lastIndexOf('.'))
            fileName = UUID.randomUUID().toString() + extension
            val dest = File(getCurrentPath(), "static/temp/$fileName")
            tempFile = dest
            if (!dest.parentFile.exists())
                dest.parentFile.mkdir()
            file.transferTo(dest)
            val users = this._parseService.parse(dest)
            if (users.isEmpty())
                Json.fail(operation, message = "未找到用户")
            else
                Json.succ(operation, data = users)
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        } finally {
            tempFile?.delete()
        }
    }

    /**
     * 获取当前路径
     * @return 当前路径
     */
    private fun getCurrentPath(): String {
        val path = File(ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX).path)
        return URLDecoder.decode(path.absolutePath, "UTF-8")
    }
}
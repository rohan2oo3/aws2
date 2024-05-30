package com.cabapp.cabappspringboot.driver.controller

import com.cabapp.cabappspringboot.driver.service.S3Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.HttpURLConnection

@RestController
class S3Contoller {
    @Autowired
    private val s3Service: S3Service? = null

    @PostMapping("upload")
    fun upload(@RequestParam("file") file: MultipartFile?): String? {
        return s3Service!!.saveFile(file)
    }

    @GetMapping("download/{filename}")
    fun download(@PathVariable("filename") filename: String): ResponseEntity<ByteArray> {
        val headers = HttpHeaders()
        headers.add("Content-type", MediaType.ALL_VALUE)
        headers.add("Content-Disposition", "attachment; filename=$filename")
        val bytes = s3Service!!.downloadFile(filename)
        return ResponseEntity.status(HttpURLConnection.HTTP_OK).headers(headers).body(bytes)
    }


    @DeleteMapping("{filename}")
    fun deleteFile(@PathVariable("filename") filename: String?): String? {
        return s3Service!!.deleteFile(filename)
    }

    @get:GetMapping("list")
    val allFiles: List<String?>?
        get() = s3Service!!.listAllFiles()
}
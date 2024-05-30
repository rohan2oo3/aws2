package com.cabapp.cabappspringboot.driver.service

import org.springframework.web.multipart.MultipartFile

interface FileServiceImpl {
    fun saveFile(file: MultipartFile?): String?

    fun saveFile1(file: MultipartFile?): String?

    fun saveFile2(file: MultipartFile?): String?

    fun downloadFile(filename: String?): ByteArray?


    fun deleteFile(filename: String?): String?


    fun listAllFiles(): List<String?>?
}
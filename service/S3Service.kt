package com.cabapp.cabappspringboot.driver.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ListObjectsV2Request
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.amazonaws.util.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.stream.Collectors

@Service
class S3Service(private val s3: AmazonS3) : FileServiceImpl {
    @Value("\${bucketName}")
    private val bucketName: String? = null

    override fun saveFile(file: MultipartFile?): String? {
        val originalFilename = file!!.originalFilename
        val folderName = "driver/rc"
        val key = "$folderName/$originalFilename"

        var count = 0
        val maxTries = 3
        while (true) {
            try {
                val file1 = convertMultiPartToFile(file)
                val putObjectResult = s3.putObject(bucketName, key, file1)
                return putObjectResult.contentMd5
            } catch (e: IOException) {
                if (++count == maxTries) throw RuntimeException(e)
            }
        }
    }

    override fun saveFile1(file: MultipartFile?): String? {
        val originalFilename = file!!.originalFilename
        val folderName = "driver/license"
        val key = "$folderName/$originalFilename"

        var count = 0
        val maxTries = 3
        while (true) {
            try {
                val file1 = convertMultiPartToFile(file)
                val putObjectResult = s3.putObject(bucketName, key, file1)
                return putObjectResult.contentMd5
            } catch (e: IOException) {
                if (++count == maxTries) throw RuntimeException(e)
            }
        }
    }

    override fun saveFile2(file: MultipartFile?): String? {
        val originalFilename = file!!.originalFilename
        val folderName = "driver/dp"
        val key = "$folderName/$originalFilename"

        var count = 0
        val maxTries = 3
        while (true) {
            try {
                val file1 = convertMultiPartToFile(file)
                val putObjectResult = s3.putObject(bucketName, key, file1)
                return putObjectResult.contentMd5
            } catch (e: IOException) {
                if (++count == maxTries) throw RuntimeException(e)
            }
        }
    }

    override fun downloadFile(filename: String?): ByteArray? {
        val folderName = "driver/rc" // Specify the folder path here
        val key = "$folderName/$filename"

        val `object` = s3.getObject(bucketName, key)
        val objectContent = `object`.objectContent
        try {
            return IOUtils.toByteArray(objectContent)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun deleteFile(filename: String?): String? {
        val folderName = "driver/rc" // Specify the folder path here
        val key = "$folderName/$filename"

        s3.deleteObject(bucketName, key)
        return "File deleted"
    }

    override fun listAllFiles(): List<String?>? {
        val folderName = "driver/rc"
        val listObjectsV2Request = ListObjectsV2Request()
            .withBucketName(bucketName)
            .withPrefix("$folderName/")

        val listObjectsV2Result = s3.listObjectsV2(listObjectsV2Request)
        return listObjectsV2Result.objectSummaries.stream()
            .map { obj: S3ObjectSummary -> obj.key }
            .collect(Collectors.toList())
    }

    @Throws(IOException::class)
    private fun convertMultiPartToFile(file: MultipartFile?): File {
        val convFile = File(file!!.originalFilename)
        FileOutputStream(convFile).use { fos ->
            fos.write(file.bytes)
        }
        return convFile
    }
}
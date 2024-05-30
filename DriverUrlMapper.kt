package com.cabapp.cabappspringboot.driver
import com.cabapp.cabappspringboot.driver.service.S3Service
import com.cabapp.cabappspringboot.shared.Shared.pol1
import com.cabapp.cabappspringboot.shared.Shared.pol2
import com.cabapp.cabappspringboot.shared.Shared.pos1
import com.cabapp.cabappspringboot.shared.Shared.pos2
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("driver/")
class DriverUrlMapper {
    @Autowired
    private val s3Service: S3Service? = null

    @Autowired
    lateinit var driverAccountDetailsObj: DriverAccountDetails

    @CrossOrigin(origins = [pol1,pol2,pos1,pos2])
    @PostMapping("/update-details/")
    fun updateDetails(@RequestBody payload: String?, header: HttpServletRequest) = payload?.let {driverAccountDetailsObj.updateDetails(it, header)}

    @CrossOrigin(origins = [pol1,pol2,pos1,pos2])
    @PostMapping("view-details/")
    fun viewDetails(@RequestBody payload: String?, header: HttpServletRequest) = payload?.let {driverAccountDetailsObj.viewDetails(it, header)}

    @CrossOrigin(origins = [pol1, pol2, pos1, pos2])
    @PostMapping("/upload-rc")
    fun uploadRC(@RequestParam("file") file: MultipartFile?): String? {
        return s3Service!!.saveFile(file)
    }

    @CrossOrigin(origins = [pol1, pol2, pos1, pos2])
    @PostMapping("/upload-lc")
    fun uploadLicense(@RequestParam("file") file: MultipartFile?): String? {
        return s3Service!!.saveFile1(file)
    }

    @CrossOrigin(origins = [pol1, pol2, pos1, pos2])
    @PostMapping("/upload-dp")
    fun uploadDp(@RequestParam("file") file: MultipartFile?): String? {
        return s3Service!!.saveFile2(file)
    }
}
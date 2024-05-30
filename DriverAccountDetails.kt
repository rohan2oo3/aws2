package com.cabapp.cabappspringboot.driver

import com.cabapp.cabappspringboot.accounts.DriverAccountId1st
import com.cabapp.cabappspringboot.accounts.DriverAccountPh1st
import com.cabapp.cabappspringboot.driver.service.S3Service
import com.cabapp.cabappspringboot.shared.Encryption
import com.cabapp.cabappspringboot.shared.Session
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.datastax.oss.driver.api.querybuilder.select.Select
import com.google.gson.Gson
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Service
@RestController
class DriverAccountDetails(private val s3Service: S3Service) : Session() {
    fun updateDetails(@RequestBody payload: String, header: HttpServletRequest): String? {
        val gson = Gson()
        val token = header.getHeader("token")
        val tokenDecoded = Encryption.jwtDecrypt(token)
        val driverId = gson.fromJson(tokenDecoded, DriverAccountPh1st::class.java).driver_id

        val accountId1st: DriverAccountId1st = gson.fromJson(payload, DriverAccountId1st::class.java)
        println(accountId1st.state)
        val insertQuery = QueryBuilder.insertInto("driveraccountid1st")
            .value("driver_id", QueryBuilder.bindMarker())
            .value("address", QueryBuilder.bindMarker())
            .value("current_commission_rates", QueryBuilder.bindMarker())
            .value("dl_file_url", QueryBuilder.bindMarker())
            .value("dl_number", QueryBuilder.bindMarker())
            .value("dp_url", QueryBuilder.bindMarker())
            .value("email", QueryBuilder.bindMarker())
            .value("name", QueryBuilder.bindMarker())
            .value("phone", QueryBuilder.bindMarker())
            .value("rc_file_url", QueryBuilder.bindMarker())
            .value("rc_number", QueryBuilder.bindMarker())
            .value("state", QueryBuilder.bindMarker())
            .value("time", QueryBuilder.bindMarker())
            .value("vehicle_type", QueryBuilder.bindMarker())

        val insertDriverId1stSmt = sess!!.prepare(insertQuery.build()).bind(
            driverId,
            accountId1st.address,
            accountId1st.current_commission_rates,
            accountId1st.dl_file_url,
            accountId1st.dl_number,
            accountId1st.dp_url,
            accountId1st.email,
            accountId1st.name,
            accountId1st.phone,
            accountId1st.rc_file_url,
            accountId1st.rc_number,
            accountId1st.state,
            accountId1st.time,
            accountId1st.vehicle_type
        )
        sess!!.execute(insertDriverId1stSmt)
        return "{\"type\":\"success\"}"
    }

    fun viewDetails(@RequestBody payload: String, header: HttpServletRequest): String? {
        val gson = Gson()
        val token = header.getHeader("token")
        val tokenDecoded = Encryption.jwtDecrypt(token)
        val driverId = gson.fromJson(tokenDecoded, DriverAccountPh1st::class.java).driver_id
        val select: Select = QueryBuilder.selectFrom("driveraccountid1st").all()
            .whereColumn("driver_id").isEqualTo(QueryBuilder.bindMarker())
        val preparedStatement = sess!!.prepare(select.build()).bind(driverId)
        val resultSet = sess!!.execute(preparedStatement)
        val result: MutableList<DriverAccountId1st> = ArrayList()
        resultSet.forEach { x ->
            result.add(
                DriverAccountId1st(
                    x.getString("driver_id"),
                    x.getString("time"),
                    x.getString("phone"),
                    x.getString("name"),
                    x.getString("email"),
                    x.getString("address"),
                    x.getString("state"),
                    x.getString("vehicle_type"),
                    x.getString("rc_number"),
                    x.getString("dl_number"),
                    x.getString("rc_file_url"),
                    x.getString("dl_file_url"),
                    x.getString("dp_url"),
                    x.getString("current_commission_rates"),
                    x.getString("referral1coupon"),
                    x.getString("referral2coupon"),
                    x.getString("referredtoid1"),
                    x.getString("referredtoid2"),
                    x.getString("referredfromid")
                )
            )
        }
        result[0].driver_id = ""
        result[0].referredtoId1 = ""
        result[0].referredtoId2 = ""

        return gson.toJson(result[0])
    }

    fun uploadRC(file: MultipartFile, header: HttpServletRequest): String {
        val token = header.getHeader("token")
        val tokenDecoded = Encryption.jwtDecrypt(token)
        val driverId = Gson().fromJson(tokenDecoded, DriverAccountPh1st::class.java).driver_id
        val folder = "driver/rc/$driverId"

        val fileUrl = s3Service.saveFile(file)

        return "{\"fileUrl\":\"$fileUrl\"}"
    }

    fun uploadLicense(file: MultipartFile, header: HttpServletRequest): String {
        val token = header.getHeader("token")
        val tokenDecoded = Encryption.jwtDecrypt(token)
        val driverId = Gson().fromJson(tokenDecoded, DriverAccountPh1st::class.java).driver_id
        val folder = "driver/rc/$driverId"

        val fileUrl = s3Service.saveFile1(file)

        return "{\"fileUrl\":\"$fileUrl\"}"
    }

    fun uploadDp(file: MultipartFile, header: HttpServletRequest): String {
        val token = header.getHeader("token")
        val tokenDecoded = Encryption.jwtDecrypt(token)
        val driverId = Gson().fromJson(tokenDecoded, DriverAccountPh1st::class.java).driver_id
        val folder = "driver/rc/$driverId"

        val fileUrl = s3Service.saveFile2(file)

        return "{\"fileUrl\":\"$fileUrl\"}"
    }
}
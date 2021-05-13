package com.fiuba.seedyfiuba.login.framework.requestmanager.datasources

import com.fiuba.seedyfiuba.commons.Result
import com.fiuba.seedyfiuba.commons.SeedyFiubaError
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.ErrorBodyDto
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

private const val KEY_ERROR_MESSAGE: String = "message"
private const val KEY_ERROR_ERROR: String = "error"
private const val KEY_ERROR_STATUS: String = "status"

abstract class RemoteBaseDataSource {
	protected suspend fun <T : Any> getResult(call: suspend () -> Response<T>): Result<T> {
		return try {
			val response = call()
			if (response.isSuccessful) {
				response.body()?.let { Result.Success(it) }
					?: throw NoSuchFieldException("Null response body")
			} else {
				error(response.code(), response.errorBody()?.string() ?: "")
			}
		} catch (e: Exception) {
			error(e.message ?: e.toString())
		}
	}

	private fun <T : Any> error(errorCode: Int, errorStr: String): Result<T> {
		return try {
			val jsonError = JSONObject(errorStr)
			val errorBodyDto = ErrorBodyDto(
				errorStr,
				jsonError.getString(KEY_ERROR_ERROR),
				errorCode
			)
			Result.Error(SeedyFiubaError.getError(errorBodyDto))
		} catch (exception: JSONException) {
			Result.Error(SeedyFiubaError.Unknown(Throwable("Unknown networking error: $errorCode")))
		}
	}
}

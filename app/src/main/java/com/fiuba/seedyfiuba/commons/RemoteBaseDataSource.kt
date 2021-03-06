package com.fiuba.seedyfiuba.commons

import android.content.Context
import android.content.Intent
import com.fiuba.seedyfiuba.MainActivity
import com.fiuba.seedyfiuba.login.LoginContainer
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.ErrorBodyDto
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

private const val KEY_ERROR_MESSAGE: String = "message"
private const val KEY_ERROR_ERROR: String = "error"
private const val KEY_ERROR_STATUS: String = "status"

abstract class RemoteBaseDataSource(private val context: Context) {
	protected suspend fun <T : Any> getResult(call: suspend () -> Response<T>): Result<T> {
		return try {
			val response = call()
			if (response.isSuccessful) {
				response.body()?.let { Result.Success(it) }
					?: throw NoSuchFieldException("Null response body")
			} else {
				if(response.code() == 401){
					LoginContainer.logoutUseCase.invoke()
					val intent = Intent(context, MainActivity::class.java)
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent)
				}
				error(response.code(), response.errorBody()?.string() ?: "")
			}
		} catch (e: Exception) {
			error(e.message ?: e.toString())
		}
	}

	protected suspend fun getResultUnit(call: suspend () -> Response<Unit>): Result<Unit> {
		return try {
			val response = call()
			if (response.isSuccessful) {
				Result.Success(Unit)
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

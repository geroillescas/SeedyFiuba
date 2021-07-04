package com.fiuba.seedyfiuba.commons

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.fiuba.seedyfiuba.login.framework.requestmanager.dto.ErrorBodyDto

private const val INTERNAL = "INTERNAL"

sealed class Result<out T : Any?> {
	data class Success<out T : Any?>(val data: T) : Result<T>()
	data class Error(val exception: SeedyFiubaError) : Result<Nothing>()
}

sealed class SeedyFiubaError {
	/**
	 * Map API error responses by backend error code to a specific local exception to handle it.
	 */
	companion object {
		fun getError(errorBodyDto: ErrorBodyDto): SeedyFiubaError {
			return UnknownSeedyFiubaApiException(
				errorBodyDto.status,
				Exception("${errorBodyDto.error}: ${errorBodyDto.message}")
			)
		}

		fun getError(exception: Exception): SeedyFiubaError {
			return UnknownSeedyFiubaApiException(
				0,
				exception
			)
		}
	}


	data class Unknown(val exception: Throwable) : SeedyFiubaError()
	data class UnAuthorized(val exception: Throwable) : SeedyFiubaError()
	data class UnknownSeedyFiubaApiException(val code: Int, val exception: Throwable) :
		SeedyFiubaError()
}

/**
 * Applies the transformation to Result.Success data type, if is Result.Error returns the same
 */
inline fun <T : Any, R : Any> Result<T>.map(transform: (T) -> R): Result<R> =
	when (this) {
		is Result.Success -> Result.Success(
			transform(this.data)
		)
		is Result.Error -> Result.Error(
			this.exception
		)
	}

/**
 * Get an intent with the application package
 * @param context the context to get the application package
 *
 * @return an intent with the application package
 */
fun getSafeIntent(context: Context): Intent {
	return Intent(Intent.ACTION_VIEW).setPackage(context.packageName).putExtra(INTERNAL, true)
}

/**
 * Get an intent with the application package
 * @param context the context to get the application package to set it by calling [.setPackage].
 * @param uri the Uri of the data this intent is now targeting. It is a shortcut to [.setData].
 *
 * @return an intent with the application package and the given data.
 */
fun getSafeIntent(context: Context, uri: Uri): Intent {
	return getSafeIntent(context).setData(uri)
}


package com.death.goplan.utils

class ApiException(val statusCode: Int, message: String) : Exception(message)
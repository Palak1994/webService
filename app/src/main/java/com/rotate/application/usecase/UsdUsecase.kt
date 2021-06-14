package com.rotate.application.usecase

class UsdUsecase(private val mAssetDetailRepository: ApiRepository) {
    suspend fun execute(format: String, assetId: String) =
            mAssetDetailRepository.fetchData(format, assetId)
}
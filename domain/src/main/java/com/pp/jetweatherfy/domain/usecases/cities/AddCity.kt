/*
 * Copyright 2021 Paulo Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pp.jetweatherfy.domain.usecases.cities

import com.pp.jetweatherfy.domain.base.FlowUseCase
import com.pp.jetweatherfy.domain.base.Result
import com.pp.jetweatherfy.domain.di.IoDispatcher
import com.pp.jetweatherfy.domain.repositories.cities.ICityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddCity @Inject constructor(
    private val citiesRepository: ICityRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String, Unit>(dispatcher) {

    override fun execute(parameters: String): Flow<Result<Unit>> {
        return flow {
            emit(Result.Loading)
            emit(Result.Success(citiesRepository.addCity(parameters)))
        }
    }
}

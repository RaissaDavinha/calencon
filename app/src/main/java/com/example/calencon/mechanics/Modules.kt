package com.example.calencon.mechanics

import com.example.calencon.data.remote.WebServiceClient
import com.example.calencon.presentation.near_events.NearEventsInterface
import com.example.calencon.presentation.near_events.presenter.NearEventsPresenter
import org.koin.dsl.module

val presenterModule = module {
//    single {(view: NearEventsInterface) -> NearEventsPresenter(get(), view) }
}

val webServiceModules = module {
    single { WebServiceClient() }
}

val appModules =
    listOf(presenterModule, webServiceModules)
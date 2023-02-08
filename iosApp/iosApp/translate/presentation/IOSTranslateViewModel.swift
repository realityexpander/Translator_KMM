//
//  IOSTranslateViewModel.swift
//  iosApp

import Foundation
import shared

extension TranslateScreen {
    @MainActor class TranslateViewModelIOSImpl: ObservableObject {
        private var historyRepo: IHistoryRepository
        private var translateUseCase: TranslateUseCase
        private let viewModel: TranslateViewModel
        
        @Published var state: TranslateState = TranslateState(
            fromText: "",
            toText: nil,
            isTranslating: false,
            fromLanguage: UiLanguage(language: .english, imageName: "english"),
            toLanguage: UiLanguage(language: .german, imageName: "german"),
            isChoosingFromLanguage: false,
            isChoosingToLanguage: false,
            error: nil,
            history: []
        )
        private var observeStateHandle: DisposableHandle?
        
        init(historyRepo: IHistoryRepository, translateUseCase: TranslateUseCase) {
            self.historyRepo = historyRepo
            self.translateUseCase = translateUseCase
            self.viewModel = TranslateViewModel(
                translate: translateUseCase,
                historyRepo: historyRepo,
                coroutineScope: nil,
                savedState: nil
            )
        }
        
        func onEvent(event: TranslateEvent) {
            self.viewModel.onEvent(event: event)
        }
        
        func startObserving() {
            observeStateHandle = viewModel.state.subscribe(
                onCollect: { state in
                    
                    // similar to kotlin:
                    // state?.let { state -> this.state = state }
                    
                    if let state = state {
                        self.state = state
                    }
                }
            )
        }
        
        func dispose() {
            observeStateHandle?.dispose()
        }
    }
}

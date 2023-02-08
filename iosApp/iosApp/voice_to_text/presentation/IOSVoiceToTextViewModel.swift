//
//  IOSVoiceToTextViewModel.swift
//  iosApp
//
//

import Foundation
import shared
import Combine

@MainActor class IOSVoiceToTextViewModel: ObservableObject {
    private var parser: any IVoiceToTextParser
    private let languageCode: String
    
    private let viewModel: VoiceToTextViewModel
    @Published var state = VoiceToTextState(
        powerRatios: [],
        spokenText: "",
        isRecordPermissionGranted: false,
        recordError: nil,
        displayState: nil
    )
    private var handle: DisposableHandle?
    
    init(parser: IVoiceToTextParser, languageCode: String) {
        self.parser = parser
        self.languageCode = languageCode
        self.viewModel = VoiceToTextViewModel(parser: parser, coroutineScope: nil)
        self.viewModel.onEvent(event: VoiceToTextEvent.Reset())
        self.viewModel.onEvent(event: VoiceToTextEvent.PermissionResult(
            isGranted: true,
            isPermanentlyDeclined: false)
        )
    }
    
    func onEvent(event: VoiceToTextEvent) {
        viewModel.onEvent(event: event)
    }
    
    func startObserving() {
        handle = viewModel.state.subscribe { [weak self] state in
            if let state {
                self?.state = state
            }
        }
    }
    
    func dispose() {
        handle?.dispose()
        onEvent(event: VoiceToTextEvent.Reset())
    }
}

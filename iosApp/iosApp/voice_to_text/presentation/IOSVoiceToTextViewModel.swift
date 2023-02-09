//
//  IOSVoiceToTextViewModel.swift
//  iosApp
//
//  Wrapper around VoiceToTextViewModel to make it compatible with SwiftUI

import Foundation
import shared
import Combine

@MainActor class IOSVoiceToTextViewModel: ObservableObject {
    private var vttProcessor: any IVoiceToTextProcessor
    private let languageCode: String
    
    private let viewModel: VoiceToTextViewModel
    @Published var state = VoiceToTextState(
        powerRatios: [],
        spokenText: "",
        isRecordPermissionGranted: false,
        recordError: nil,
        displayState: nil
    )
    private var observeStateJobHandle: DisposableHandle?
    
    init(vttProcessor: IVoiceToTextProcessor, languageCode: String) {
        self.vttProcessor = vttProcessor
        self.languageCode = languageCode
        self.viewModel = VoiceToTextViewModel(vttProcessor: vttProcessor, coroutineScope: nil)
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
        observeStateJobHandle = viewModel.state.subscribe { [weak self] state in
            if let state {
                self?.state = state
            }
        }
    }
    
    func dispose() {
        observeStateJobHandle?.dispose()
        onEvent(event: VoiceToTextEvent.Reset())
    }
}

//
//  VoiceToTextScreen.swift
//  iosApp
//
//

import SwiftUI
import shared

struct VoiceToTextScreen: View {
    private let onResult: (String) -> Void
    
    @ObservedObject var viewModel: IOSVoiceToTextViewModel
    private let vttProcessor: any IVoiceToTextProcessor
    private let languageCode: String
    
    @Environment(\.presentationMode) var presentation // holds state to "pop" back
    
    init(
        onResult: @escaping (String) -> Void,
        vttProcessor: any IVoiceToTextProcessor,
        languageCode: String
    ) {
        self.onResult = onResult
        self.vttProcessor = vttProcessor
        self.languageCode = languageCode
        self.viewModel = IOSVoiceToTextViewModel(vttProcessor: vttProcessor, languageCode: languageCode)
    }
    
    var body: some View {
        VStack {
            Spacer()
            
            mainView
            
            Spacer()
            
            HStack {
                Spacer()
                VoiceRecorderButton(
                    displayState: viewModel.state.displayState ?? .waitingToListen,
                    onClick: {
                        if viewModel.state.displayState != .resultVisible {
                            // No results showing, so record new audio.
                            viewModel.onEvent(event: VoiceToTextEvent.Reset())
                            viewModel.onEvent(event: VoiceToTextEvent.ToggleRecording(languageCode: languageCode))
                        } else {
                            // Return the Recognized Text results.
                            onResult(viewModel.state.spokenText)
                            self.presentation.wrappedValue.dismiss()  // Pops back
                        }
                    }
                )
                
                // Re-record a new Audio for recognition.
                if viewModel.state.displayState == .resultVisible {
                    Button(action: {
                        viewModel.onEvent(event: VoiceToTextEvent.ToggleRecording(languageCode: languageCode))
                    }) {
                        Image(systemName: "arrow.clockwise")
                            .foregroundColor(.lightBlue)
                    }
                }
                Spacer()
            }
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
        .background(Color.background)
    }
    
    var mainView: some View {
        if let displayState = viewModel.state.displayState { // does a nil check
            switch displayState {
                case .waitingToListen:
                    return AnyView(  // "re-types" the enclosed View as a Generic View (return type is View)
                        Text("Click record and start talking.")
                            .font(.title2)
                    )
                case .resultVisible:
                    return AnyView(
                        Text(viewModel.state.spokenText)
                            .font(.title2)
                    )
                case .error:
                    return AnyView(
                        Text(viewModel.state.recordError ?? "Unknown error")
                            .font(.title2)
                            .foregroundColor(.red)
                    )
                case .listeningActive:
                    return AnyView(
                        VoiceRecorderDisplay(
                            powerRatios: viewModel.state.powerRatios.map {
                                Double(truncating: $0)
                            }
                        )
                        .frame(maxHeight: 100)
                        .padding()
                    )
                default: return AnyView(EmptyView())
            }
        } else {
            return AnyView(EmptyView())
        }
    }
}

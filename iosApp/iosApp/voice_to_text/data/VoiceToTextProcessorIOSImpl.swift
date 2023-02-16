//
//  VoiceToTextParserIOSImpl.swift
//  iosApp
//
//

import Foundation
import shared
import Speech
import Combine

class VoiceToTextProcessorIOSImpl: IVoiceToTextProcessor, ObservableObject {
    
    private let _state = IOSMutableStateFlow(
        initialValue: VoiceToTextProcessorState(
            result: "",
            error: nil,
            powerRatio: 0.0,
            isRecognizerListening: false
        )
    )
    var state: CommonStateFlow<VoiceToTextProcessorState> { _state }
    
    private var micObserver = MicrophonePowerObserver()
    var micPowerRatio: Published<Double>.Publisher { // Observes the micObserver.micPowerRatio -> normal var
        micObserver.$micPowerRatio  // this is a Double
    }
    private var micPowerCancellable: AnyCancellable?
    
    private var recognizer: SFSpeechRecognizer?
    private var audioEngine: AVAudioEngine?
    private var inputNode: AVAudioInputNode?
    private var audioBufferRequest: SFSpeechAudioBufferRecognitionRequest?
    private var recognitionTask: SFSpeechRecognitionTask?
    private var audioSession: AVAudioSession?

    ///////////////////////////////////////////////////////////////////////
    ////////////////// IVoiceToTextProcessor Implementation //////////////////

    func startListening(languageCode: String) {
        // Clear errors>
        updateState(error: nil)
        
        // Setup Speech Recognizer
        let chosenLocale = Locale.init(identifier: languageCode)
        let supportedLocale = SFSpeechRecognizer.supportedLocales().contains(chosenLocale)
            ? chosenLocale : Locale.init(identifier: "en-US")
        self.recognizer = SFSpeechRecognizer(locale: supportedLocale)
        
        guard recognizer?.isAvailable == true else {
            updateState(error: "Speech recognizer is not available")
            return
        }
        
        audioSession = AVAudioSession.sharedInstance()
        do {
            try audioSession?.setActive(true)
            try audioSession?.setPreferredInputNumberOfChannels(1)
        } catch {
            updateState(error: "Audio cant be set active")
            return
        }
        print("audioSession?.availableInputs: \(String(describing: audioSession?.availableInputs))")
        print("audioSession?.inputNumberOfChannels: \(String(describing: audioSession?.inputNumberOfChannels))")
        print("audioSession?.currentRoute.inputs: \(String(describing: audioSession?.currentRoute.inputs))")
        
        
        self.requestPermissions { [weak self] in
            self?.audioBufferRequest = SFSpeechAudioBufferRecognitionRequest()
            guard let audioBufferRequest = self?.audioBufferRequest else {
                self?.updateState(error: "Audio buffer request error")
                return
            }
            
            self?.audioEngine = AVAudioEngine()
            self?.audioEngine?.reset()
            self?.inputNode = self?.audioEngine?.inputNode
            
            // Setup recording to be saved in the `audioBufferRequest`
            let recordingFormat = self?.inputNode?
                .outputFormat(forBus: 0) // 0 because we have only one input
            //             .inputFormat(forBus: 0)
            

            func audioInputIsBusy(recordingFormat: AVAudioFormat) -> Bool {
                guard recordingFormat.sampleRate == 0 || recordingFormat.channelCount == 0 else {
                    return false
                }
                return true
            }
            
            guard !audioInputIsBusy(recordingFormat: recordingFormat!) else {
                self?.updateState(error: "Audio Input Is Busy")
                return
            }
            
            self?.recognitionTask = self?.recognizer?
                .recognitionTask(with: audioBufferRequest) { [weak self] (result, error) in
                    guard let result = result else {
                        self?.updateState(error: error?.localizedDescription)
                        return
                    }
                    
                    if result.isFinal {
                        self?.updateState(result: result.bestTranscription.formattedString)
                    }
                }
            
            self?.inputNode?.installTap(
                onBus: 0,
                bufferSize: 1024,
                format: recordingFormat
            ) { buffer, _ in
                self?.audioBufferRequest?.append(buffer) // `buffer` gets added to the `audioBufferRequest`
            }
            
            self?.audioEngine?.prepare()
            
            do {
                try self?.audioSession?
                    .setCategory(
                        .playAndRecord,
                        mode: .spokenAudio,   // Allow other apps to pause for audio prompts.
                        options: [.duckOthers, .defaultToSpeaker]  // Turn down the volume of other apps while recording.
                    )
                try self?.audioSession?
                    .setActive(
                        true,
                        options: .notifyOthersOnDeactivation  // let other apps turn their volume back to normal.
                    )
                
                self?.micObserver.startObserving()
                try self?.audioEngine?.start()
                self?.updateState(isRecognizerListening: true)
                
                // Observe the micPowerRatio changes and update the ViewModel state for "Audio Levels" UI
                self?.micPowerCancellable =  // If still observing...
                    self?.micPowerRatio      // Listen for changes to `micPowerRatio`...
                        .sink { [weak self] ratio in
                            self?.updateState(powerRatio: ratio)  // Update the current audio level
                        }
            } catch {
                self?.updateState(
                    error: error.localizedDescription,
                    isRecognizerListening: false
                )
            }
        }
    }
    
    func stopListening() {
        self.updateState(isRecognizerListening: false)
        
        recognitionTask?.finish()
        recognitionTask = nil
        
        // Turn off mic audio levels monitoring
        micPowerCancellable = nil
        micObserver.release()
        
        // End the recording
        audioBufferRequest?.endAudio()
        audioBufferRequest = nil
        
        // Stop the Audio Engine
        audioEngine?.stop()
        
        // Remove the input tap
        inputNode?.removeTap(onBus: 0)
        
        try? audioSession?.setActive(false)  // `try?` means ignore result if it fails, requires `try` bc a `throw` is possibly called.
        audioSession = nil
    }

    func cancel() {
        // Not needed on iOS
    }

    func reset() {
        self.stopListening()
        _state.value = VoiceToTextProcessorState(
            result: "",
            error: nil,
            powerRatio: 0.0,
            isRecognizerListening: false
        )
    }

    ////////////////////////////////////////////////////////////////////
    /////////////////// iOS OS Private Methods /////////////////////////
    
    private func requestPermissions(onGranted: @escaping () -> Void) {  // @escaping means execution may come back from a different thread
        audioSession?.requestRecordPermission { [weak self] isPermissionGranted in
            guard isPermissionGranted else {
                self?.updateState(error: "You need to grant permission to record your voice.")
                self?.stopListening()
                return
            }
            
            SFSpeechRecognizer.requestAuthorization { [weak self] status in
                DispatchQueue.main.async {  // runs this on the main thread, similar to main dispatcher
                    guard status == .authorized else {
                        self?.updateState(error: "You need to grant permission to transcribe audio.")
                        self?.stopListening()
                        return
                    }
                    onGranted()
                }
            }
        }
    }


    // Simulates the `_state.update {}` method like MutableStateFlow in Kotlin
    private func updateState(
        result: String? = nil,
        error: String? = nil,
        powerRatio: CGFloat? = nil,
        isRecognizerListening: Bool? = nil
    ) {
        let currentState = _state.value
    
        // Replicate the `.copy(xxx=yyy)` feature in Kotlin
        _state.value = VoiceToTextProcessorState(
            result: result ?? currentState?.result ?? "",
            error: error ?? currentState?.error,
            powerRatio: Float(powerRatio ?? CGFloat(currentState?.powerRatio ?? 0.0)),
            isRecognizerListening: isRecognizerListening ?? currentState?.isRecognizerListening ?? false
        )
    }
    
}

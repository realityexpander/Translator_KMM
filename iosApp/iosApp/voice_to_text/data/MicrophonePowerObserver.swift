//
//  MicrophonePowerObserver.swift
//  iosApp
//
//

import Foundation
import shared
import Speech
import Combine

class MicrophonePowerObserver: ObservableObject {
    private var cancellable: AnyCancellable? = nil  // Use to cancel the micPowerRatio Publishing Timer
    private var audioRecorder: AVAudioRecorder? = nil
    
    @Published private(set) var micPowerRatio = 0.0
    
    private let powerRatioEmissionsPerSecond = 20.0
    
    func startObserving() {
        do {
            // Setup recorder
            let recorderSettings: [String: Any] = [   // [type:type] sets up a map
                AVFormatIDKey: NSNumber(value: kAudioFormatAppleLossless),
                AVNumberOfChannelsKey: 1
            ]
            
            // Send recording into THE VOID (/dev/null) because we dont care about the audio file data,
            //   we are only using the "audio amplitude" levels for the meter on the UI.
            let recorder = try AVAudioRecorder(
                url: URL(fileURLWithPath: "/dev/null", isDirectory: true),
                settings: recorderSettings
            )
            recorder.isMeteringEnabled = true
            recorder.record()
            self.audioRecorder = recorder
            
            self.cancellable = Timer.publish(
                every: 1.0 / powerRatioEmissionsPerSecond,
                tolerance: 1.0 / powerRatioEmissionsPerSecond,
                on: .main,   // Where to run, like Dispatchers in Kotlin.
                in: .common  // Similar to Thread pools in Android.
            )
            .autoconnect()  // eliminates the need for the caller to need to call `.connect()`
            .sink { [weak self] _ in // sink is same as .collect(), on a different thread. (weak means self could ne set to nil
                recorder.updateMeters()
                
                // Get normalized average audio loudness level
                let powerOffset = recorder.averagePower(forChannel: 0)
                if powerOffset < -50 {
                    self?.micPowerRatio = 0.0
                } else {
                    let normalizedOffset = CGFloat(50 + powerOffset) / 50
                    self?.micPowerRatio = normalizedOffset
                }
            }
        } catch {
            print("An error occurred when observing microphone power: \(error.localizedDescription)")
        }
    }
    
    func release() {
        cancellable = nil
        
        audioRecorder?.stop()
        audioRecorder = nil
        
        micPowerRatio = 0.0
    }
}

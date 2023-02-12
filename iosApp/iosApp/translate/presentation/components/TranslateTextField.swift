//
//  TranslateTextField.swift
//  iosApp
//

import SwiftUI
import shared
import UniformTypeIdentifiers

struct TranslateTextField: View {
    @Binding var fromText: String
    let toText: String?
    let isTranslating: Bool
    let fromLanguage: UiLanguage
    let toLanguage: UiLanguage
    let onTranslateEvent: (TranslateEvent) -> Void
    
    var body: some View {
        if toText == nil || isTranslating {
            QueryTextField(
                fromText: $fromText,
                isTranslating: isTranslating,
                onTranslateEvent: onTranslateEvent
            )
            .gradientSurface()
            .cornerRadius(15)
            .animation(.easeInOut, value: isTranslating)
            .shadow(radius: 4)
        } else {
            TranslatedTextField(
                fromText: fromText,
                toText: toText ?? "",
                fromLanguage: fromLanguage,
                toLanguage: toLanguage,
                onTranslateEvent: onTranslateEvent
            )
            .padding()
            .gradientSurface()
            .cornerRadius(15)
            .animation(.easeInOut, value: isTranslating)
            .shadow(radius: 4)
            .onTapGesture {
                onTranslateEvent(TranslateEvent.EditTranslation())
            }
        }
    }
}

struct TranslateTextField_Previews: PreviewProvider {
    static var previews: some View {
        TranslateTextField(
            fromText: Binding(
                get: { "test" },
                set: { value in }
            ),
            toText: "Test",
            isTranslating: false,
            fromLanguage: UiLanguage(language: .english, imageName: "english"),
            toLanguage: UiLanguage(language: .german, imageName: "german"),
            onTranslateEvent: { event in }
        )
    }
}

private extension TranslateTextField {
    
    struct QueryTextField: View {
        @Binding var fromText: String  // two-way binding (no need for callback fun like kotlin)
        let isTranslating: Bool
        let onTranslateEvent: (TranslateEvent) -> Void
        
        private let placeHolder: String = "Enter text to translate"
        
        var body: some View {
            ZStack {
                Text(fromText=="" ? placeHolder : "")
                    .foregroundColor(Color.onSurface).opacity(0.5)
                TextEditor(text: $fromText)
                    .frame(
                        maxWidth: .infinity,
                        minHeight: 200,
                        alignment: .topLeading
                    )
                    .padding()
                    .foregroundColor(Color.onSurface)
                    .overlay(alignment: .bottomTrailing) {  // sets a layer above the TextEditor
                        ProgressButton(
                            text: "TRÄNSLÅTÉ",
                            isLoading: isTranslating,
                            onClick: {
                                onTranslateEvent(TranslateEvent.Translate())
                            }
                        )
                        .padding(.trailing)
                        .padding(.bottom)
                    }
                    .onAppear {
                        UITextView.appearance().backgroundColor = .clear // update the background to remove unwanted color
                    }
            }
        }
            
    }
    
    struct TranslatedTextField: View {
        let fromText: String
        let toText: String
        let fromLanguage: UiLanguage
        let toLanguage: UiLanguage
        let onTranslateEvent: (TranslateEvent) -> Void
        
        private let tts = TextToSpeech()
        
        var body: some View {
            VStack(alignment: .leading) {
                
                // Show FROM Language & FROM Translation
                LanguageDisplay(language: fromLanguage)
                Text(fromText)
                    .foregroundColor(.onSurface)
                
                HStack {
                    Spacer()
                    
                    // • Copy from Text Button
                    Button(action: {
                        UIPasteboard.general.setValue(
                            fromText,
                            forPasteboardType: UTType.plainText.identifier
                        )
                    }) {
                        Image(uiImage: UIImage(named: "copy")!)
                            .renderingMode(.template)
                            .foregroundColor(.lightBlue)
                    }
                    
                    // Close the Translation
                    Button(action: {
                        onTranslateEvent(TranslateEvent.CloseTranslation())
                    }) {
                        Image(systemName: "xmark")
                            .foregroundColor(.lightBlue)
                    }
                }
                Divider()
                    .padding()
                
                // • Show TO Language & TO Translation
                LanguageDisplay(language: toLanguage)
                    .padding(.bottom)
                Text(toText)
                    .foregroundColor(.onSurface)
                
                HStack {
                    Spacer()  // use up empty space
                    
                    // • Copy Text button
                    Button(action: {
                        UIPasteboard.general.setValue(
                            toText,
                            forPasteboardType: UTType.plainText.identifier
                        )
                    }) {
                        Image(uiImage: UIImage(named: "copy")!)
                            .renderingMode(.template)
                            .foregroundColor(.lightBlue)
                    }
                    
                    // • Speak the "To Text"
                    Button(action: {
                        tts.speak(
                            text: toText,
                            language: toLanguage.language.langCode
                        )
                    }) {
                        Image(systemName: "speaker.wave.2")
                            .foregroundColor(.lightBlue)
                    }
                }
            }
        }
    }
}

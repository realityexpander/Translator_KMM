//
//  TranslateScreen.swift
//  iosApp
//
//

import SwiftUI
import shared

struct TranslateScreen: View {
    private var historyRepo: IHistoryRepository
    private var translateUseCase: TranslateUseCase
    @ObservedObject var viewModel: TranslateViewModelIOSImpl  // @ObservedObject indicates it has @Published property fields
    private let vttProcessor: any IVoiceToTextProcessor
    
    @State var isLinkActive = true
    @State var selection: Int? = nil
    
    init(historyRepo: IHistoryRepository, translateUseCase: TranslateUseCase, vttProcessor: IVoiceToTextProcessor) {
        self.historyRepo = historyRepo
        self.translateUseCase = translateUseCase
        self.vttProcessor = vttProcessor
        self.viewModel = TranslateViewModelIOSImpl(historyRepo: historyRepo, translateUseCase: translateUseCase)
    }
    
    var body: some View {
        ZStack {
            
            List {
                
                // Show Text Query field
                TranslateTextField(
                    fromText: Binding(
                        get: { viewModel.state.fromText },
                        set: { value in
                            viewModel.onEvent(event: TranslateEvent.ChangeTranslationText(text: value))
                        }),
                    toText: viewModel.state.toText,
                    isTranslating: viewModel.state.isTranslating,
                    fromLanguage: viewModel.state.fromLanguage,
                    toLanguage: viewModel.state.toLanguage,
                    onTranslateEvent: { viewModel.onEvent(event: $0) }
                )
                .listRowSeparator(.hidden)
                .listRowBackground(Color.background)
                
                // Show Language translator From/To Pickers
                HStack(alignment: .center) {
                    LanguageDropDown(
                        language: viewModel.state.fromLanguage,
                        isOpen: viewModel.state.isChoosingFromLanguage,
                        selectLanguage: { language in
                            viewModel.onEvent(event: TranslateEvent.ChooseFromLanguage(language: language))
                        }
                    )
                    Spacer()
                    SwapLanguageButton(onClick: {
                        viewModel.onEvent(event: TranslateEvent.SwapLanguages())
                    })
                    Spacer()
                    LanguageDropDown(
                        language: viewModel.state.toLanguage,
                        isOpen: viewModel.state.isChoosingToLanguage,
                        selectLanguage: { language in
                            viewModel.onEvent(event: TranslateEvent.ChooseToLanguage(language: language))
                        }
                    )
                }
                .listRowSeparator(.hidden)
                .listRowBackground(Color.background)

                // Show History of Translations
                if !viewModel.state.history.isEmpty {
                    Text("History")
                        .font(.title)
                        .bold()
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.background)
                }

                ForEach(viewModel.state.history, id: \.self.id) { item in
                    TranslateHistoryItem(
                        item: item,
                        onClick: { viewModel.onEvent(
                            event: TranslateEvent.SelectHistoryItem(item: item)
                        )}
                    )
                    .listRowSeparator(.hidden)
                    .listRowBackground(Color.background)
                }
            }
            .listStyle(.plain)
            .buttonStyle(.plain)
            
            // Listen for Speech to Translate
            VStack {
                Spacer()
                NavigationLink(
                    destination: VoiceToTextScreen(
                        onResult: { spokenText in
                            viewModel.onEvent(event: TranslateEvent.SubmitVoiceResult(result: spokenText))
                        },
                        vttProcessor: vttProcessor,
                        languageCode: viewModel.state.fromLanguage.language.langCode
                    )
                ) {
                    ZStack {
                        Circle()
                            .foregroundColor(.primaryColor)
                            .padding()
                        Image(uiImage: UIImage(named: "mic")!)
                            .foregroundColor(.onPrimary)
                            .accessibilityIdentifier("Record audio")
                    }
                    .frame(maxWidth: 100, maxHeight: 100)
                }
            }
            
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
    
    }
}

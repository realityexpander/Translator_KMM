# KMM Translator
Android &amp; iOS App cross-platform KMM application for translating languages

[<img src="https://user-images.githubusercontent.com/5157474/218233438-99c84e55-f835-4076-976b-868ed5d19e8a.png" width=250 />](https://user-images.githubusercontent.com/5157474/218233438-99c84e55-f835-4076-976b-868ed5d19e8a.png)


Uses Kotlin for Shared logic:
- Database (SQLDelight)
- Http Client (Ktor Client)
- ViewModels (wrapped for iOS)
- UseCases
- Coroutines
- Colors for Themes

Uses wrapped versions on iOS:
- Flow
- StateFlow

Shows how to implement cross-platform abstractions for platform specific module:
- Speech Recognizer - Voice-To-Text
- Speech Synthesis - Text-To-Voice

UI implemented separately, but using common ViewModels:
- Compose for Android
- SwiftUI for iOS

## Architecture

#### Click on image for Interactive Mind-Map Overview

[<img src="https://user-images.githubusercontent.com/5157474/218219624-371b1ead-54d5-43e8-975a-12956e1c7bd6.png" width=650 />](https://mm.tt/map/2590156097)

# KMM Translator
Android &amp; iOS App cross-platform KMM application for translating languages

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

UI separate:
- Compose for Android
- SwiftUI for iOS

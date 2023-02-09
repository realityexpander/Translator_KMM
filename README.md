# KMM Translator
Android &amp; iOS App cross-platform KMM application for translating languages

Uses Kotlin for Shared logic:
- Database (SQLDelight)
- Http Client (Ktor Client)
- ViewModels (wrapped for iOS)
- UseCases

Uses wrapped Flow, StateFlow to communicate interally.

Shows how to implement cross-platform abstractions for platform specific module:
- Speech Recognizer - Voice-To-Text
- Speech Synthesis - Text-To-Voice

import SwiftUI
import shared

struct ContentView: View {
    
    let appModule: AppModule  // ONLY FOR KOTLIN SHARED LIBRARIES & USE-CASES - simulates the DI from Android
    
	var body: some View {
        ZStack {
            Color.background
                .ignoresSafeArea()
            
            TranslateScreen(
                historyRepo: appModule.historyRepo,
                translateUseCase: appModule.translateUseCase,
                parser: appModule.voiceParser
            )
        }
	}
}

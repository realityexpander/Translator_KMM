import SwiftUI
import shared

struct ContentView: View {
    
    let appModule: AppModule  // ONLY FOR KOTLIN SHARED LIBRARIES & USE-CASES - simulates the DI from Android
    
	var body: some View {
        ZStack {
            Color.background
                .ignoresSafeArea() // Fills the menu/status (full bleed)
            
            TranslateScreen(
                historyRepo: appModule.historyRepo,
                translateUseCase: appModule.translateUseCase,
                vttProcessor: appModule.vttProcessor
            )
        }//.frame(maxWidth: 800)
	}
}

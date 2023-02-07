import SwiftUI
import shared

struct ContentView: View {
    
    let appModule: AppModule
    
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

import SwiftUI
import shared

@main
struct iOSApp: App {
    
    private var appModule: any AppModule = AppModuleImpl(parser: VoiceToTextParserIOSImpl())
    
    #if DEBUG
    init() {
        if CommandLine.arguments.contains("isUiTesting") {
            self.appModule = TestAppModule()
        }
    }
    #endif
    
    @State private var isNavigationViewPresented = true
    
	var body: some Scene {
		WindowGroup {
            ZStack {
//                NavigationView {
//                    ZStack {
//                        VStack {
//                            Button("hello") {
//                                //nothing
//                            }.buttonStyle(.bordered)
//
//                            Text("yo")
//                        }
//                    }.navigationTitle("Translator")
//                }
                    
                ContentView(appModule: appModule).frame(maxWidth: 500)
            }
		}
	}
}

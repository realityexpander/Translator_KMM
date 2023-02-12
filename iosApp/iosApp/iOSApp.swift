import SwiftUI
import shared

@main
struct iOSApp: App {
    
    // Perform DI
    private var appModule: any AppModule = AppModuleImpl(vttProcessor: VoiceToTextProcessorIOSImpl())
    
    var width = 700.0
    
    init() {
        #if DEBUG
            if CommandLine.arguments.contains("isUiTesting") {
                self.appModule = TestAppModule()
            }
        #endif
    }
    
    
    var naviView: some View {
        Text("Translate KMM")
            .onAppear {
                // hide the navigation drawer
            }
    }
    
	var body: some Scene {
		WindowGroup {
            if UIDevice.current.userInterfaceIdiom == .pad {
                NavigationView {
                    naviView
                    
                    ContentView(appModule: appModule)
                        .navigationBarHidden(true)
                        .frame(
                            minWidth: 500,
                            maxWidth: .infinity,
//                                minHeight: 1500,
                            maxHeight: .infinity,
                            alignment: .center
                        )
                }
            } else {
                NavigationView {
                    ContentView(appModule: appModule)
                        .frame(maxWidth: 600)
                }
            }
        }
	}
}

// Hide the Navigation Sidebar
extension UISplitViewController {
    open override func viewDidLoad() {
        super.viewDidLoad()

        self.preferredDisplayMode = .secondaryOnly
        self.preferredSplitBehavior = .overlay
    }
}

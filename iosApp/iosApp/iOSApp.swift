import SwiftUI
import shared

@main
struct iOSApp: App {
    
    // Perform DI
    private var appModule: any IAppModule = AppModuleProdImpl(vttProcessor: VoiceToTextProcessorIOSImpl())
    
    var width = 700.0
    
    #if DEBUG
    init() {
        if CommandLine.arguments.contains("isUiTesting") {
            self.appModule = AppModuleTestImpl()
        }
    }
    #endif

    var contentViewFor2Panes: some View {
        ContentView(appModule: appModule)
            .navigationBarHidden(true)
            .frame(
                minWidth: 500,
                maxWidth: .infinity,
                maxHeight: .infinity,
                alignment: .center
            )
    }
    
    var naviView: some View {
        NavigationLink {
            contentViewFor2Panes  // in case user goes into split mode and Nav Sidebar is opened (question: how to prevent?)
        } label: {
            Text("Translate KMM")
        }
    }
    
	var body: some Scene {
		WindowGroup {
            if UIDevice.current.userInterfaceIdiom == .pad {
                NavigationView {
                    naviView
                    contentViewFor2Panes
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
        self.preferredPrimaryColumnWidth = 0
    }
}

extension UIViewController {
    func hideNavigationBar(animated: Bool){
        // Hide the navigation bar on the this view controller
        self.navigationController?.setNavigationBarHidden(true, animated: animated)
    }

    func showNavigationBar(animated: Bool) {
        // Show the navigation bar on other view controllers
        self.navigationController?.setNavigationBarHidden(false, animated: animated)
    }
}

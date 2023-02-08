//
//  SmallLanguageIcon.swift
//  iosApp
//
//  Created by Philipp Lackner on 08.12.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SmallLanguageIcon: View {
    var language: UiLanguage
    var body: some View {
        Image(uiImage: UIImage(named: language.imageName.lowercased())!)  // use ! bc assume images exist in app assets folder
            .resizable()
            .frame(width: 30, height: 30)
    }
}

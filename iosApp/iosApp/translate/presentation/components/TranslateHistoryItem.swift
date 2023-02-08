//
//  TranslateHistoryItem.swift
//  iosApp
//

import SwiftUI
import shared

struct TranslateHistoryItem: View {
    let item: UiHistoryItem
    let onClick: () -> Void
    
    var body: some View {
        Button(action: onClick) {
            VStack(alignment: .leading) {
                HStack {
                    SmallLanguageIcon(language: item.fromLanguage)
                        .padding(.trailing)
                    Text(item.fromText)
                        .foregroundColor(.onSurface).opacity(0.7)
                        .font(.body)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.bottom)
                
                HStack {
                    SmallLanguageIcon(language: item.toLanguage)
                        .padding(.trailing)
                    Text(item.toText)
                        .foregroundColor(.onSurface)
                        .font(.body.weight(.semibold))
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            .frame(maxWidth: .infinity)
            .padding()
            .gradientSurface()
            .cornerRadius(15)
            .shadow(radius: 4)
        }
    }
}

struct TranslateHistoryItem_Previews: PreviewProvider {
    static var previews: some View {
        TranslateHistoryItem(
            item: UiHistoryItem(
                id: 0,
                fromText: "Hello",
                toText: "Hallo",
                fromLanguage: UiLanguage(language: .english, imageName: "english"),
                toLanguage: UiLanguage(language: .german, imageName: "german")
            ),
            onClick: {}
        )
    }
}

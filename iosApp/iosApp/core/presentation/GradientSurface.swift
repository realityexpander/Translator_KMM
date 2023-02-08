//
//  GradientSurface.swift
//  iosApp
//
//  Created by Philipp Lackner on 08.12.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct GradientSurface: ViewModifier {
    @Environment(\.colorScheme) var colorScheme // @Environment is similar to Compose Context
    
    func body(content: Content) -> some View {
        // Choose gradient depending on the system colorScheme
        if colorScheme == .dark {
            let gradientStart = Color(hex: 0xFF23262E)
            let gradientEnd = Color(hex: 0xFF212329)
            content
                .background(
                    LinearGradient(
                        gradient: Gradient(colors: [gradientStart, gradientEnd]),
                        startPoint: .top,
                        endPoint: .bottom
                    )
                )
        } else {
//            content.background(Color.surface)
            let gradientStart = Color(hex: 0xFFEEEEEE)
            let gradientEnd = Color(hex: 0xFFCCCCCC)
            content
                .background(
                    LinearGradient(
                        gradient: Gradient(colors: [gradientStart, gradientEnd]),
                        startPoint: .top,
                        endPoint: .bottom
                    )
                )
        }
    }
}

extension View {
    func gradientSurface() -> some View {
        modifier(GradientSurface())
    }
}

///

import UIKit

class NSQRTestInfoView: NSSimpleModuleBaseView {

    init() {
        super.init(title: "", subtitle: "Al compartir el QR ...", text: "Aceptas que la autoridad de salud te registre ante Applacovid en caso de ser positivo a COVID-19", image: nil, subtitleColor: .ns_purple)
        setup()
    }

    required init?(coder _: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    private func setup() {
        contentView.addSpacerView(NSPadding.large)

    }
}

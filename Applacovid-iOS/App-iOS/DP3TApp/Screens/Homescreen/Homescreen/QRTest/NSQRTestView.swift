///

import UIKit

class NSQRTestView: NSModuleBaseView {

    private let codeQRTestView = NSInfoBoxView(title: "Genera", subText: "El código QR para prueba COVID-19", image: UIImage(named: "ic-check")!, illustration: UIImage(named: "qr-icon")!, titleColor: .ns_green, subtextColor: .ns_text, backgroundColor: .ns_greenBackground)

    override init() {
        super.init()

        headerTitle = "Código QR"

        updateLayout()
    }
    
    required init?(coder _: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func sectionViews() -> [UIView] {
        var views = [UIView]()

        views.append(codeQRTestView)
            
        return views
    }

    override func updateLayout() {
        super.updateLayout()

        setCustomSpacing(NSPadding.medium, after: codeQRTestView)

    }
}

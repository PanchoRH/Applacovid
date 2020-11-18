///

import UIKit

class NSWhatToDoView: NSModuleBaseView {

    private let whatToDoView = NSInfoBoxView(title: "REPORTATE", subText: "Con tu reporte ayudas a todos", image: UIImage(named: "ic-check")!, illustration: UIImage(named: "illu-positiv-getestet")!, titleColor: .ns_blue, subtextColor: .ns_text, backgroundColor: .ns_blueBackground)

    override init() {
        super.init()

        headerTitle = "Prueba COVID-19 positiva"

        updateLayout()
    }
    
    required init?(coder _: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func sectionViews() -> [UIView] {
        var views = [UIView]()

        views.append(whatToDoView)
            
        return views
    }

    override func updateLayout() {
        super.updateLayout()

        setCustomSpacing(NSPadding.medium, after: whatToDoView)

    }
}

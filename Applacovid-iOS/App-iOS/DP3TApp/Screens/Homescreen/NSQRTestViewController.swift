///

import UIKit
import CoreImage.CIFilterBuiltins

class NSQRTestViewController: NSViewController {

    private let stackScrollView = NSStackScrollView(axis: .vertical, spacing: 0)
    let context = CIContext()
    let filter = CIFilter.qrCodeGenerator()
    private let informButton = NSButton(title: "Compartir", style: .uppercase(.ns_purple))
    private var titleContentStackView = UIStackView()
    private var titleLabel: NSLabel!
    private let informView = NSQRTestInfoView()
    
    // MARK: - Init
    
    override init() {
        super.init()
        title = "Código QR"
    }
    
    // MARK: - View
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupLayout()
        setupStackScrollView()
        
        informButton.touchUpCallback = { [weak self] in
            guard let strongSelf = self else { return }
            strongSelf.shareTapped()
        }
    }
    
    // MARK: - Setup
    
    private func setupLayout() {
        titleContentStackView.axis = .vertical
        stackScrollView.addSpacerView(NSPadding.large)
        
        titleLabel = NSLabel(.title, textAlignment: .center)
        titleLabel.text = "¿Qué hacer con el código QR?"
        
        titleContentStackView.addArrangedView(titleLabel)
        titleContentStackView.addSpacerView(3.0)
        
        stackScrollView.addArrangedView(titleContentStackView)
        
        stackScrollView.addSpacerView(NSPadding.large)
        
        let QRImage: UIImageView = UIImageView()
        let url: String = "http://pakal.cs.cinvestav.mx/admin/console/newrecord\(ReportingManager.shared.test())&uuid=\(UUID().uuidString)"
        QRImage.contentMode = UIView.ContentMode.scaleAspectFit
        QRImage.frame.size.width = 200
        QRImage.frame.size.height = 200
        QRImage.center = self.view.center
        QRImage.autoresizesSubviews=true
        QRImage.image = generateQRCode(from: url)
        
        stackScrollView.addArrangedView(QRImage)

        stackScrollView.addSpacerView(NSPadding.large)
        
        stackScrollView.addArrangedView(informButton)
        
        stackScrollView.addSpacerView(NSPadding.large)
        
        stackScrollView.addArrangedView(informView)
        
        stackScrollView.addSpacerView(NSPadding.large)

        stackScrollView.addArrangedView(NSOnboardingInfoView(icon: UIImage(named: "ic-verified-user")!, text: "Contiene  la semilla con la que  se generaron los mensajes aleatorios los últimos 5 días", title: "¿Qué es el código QR?", leftRightInset: 0))

        stackScrollView.addSpacerView(2.0 * NSPadding.medium)

        stackScrollView.addArrangedView(NSOnboardingInfoView(icon: UIImage(named: "ic-user")!, text: "Solo al personal médico que te registra  cuando te realizas una prueba de COVID-19", title: "¿A quién se envía el QR?", leftRightInset: 0))

        stackScrollView.addSpacerView(3 * NSPadding.large)

        stackScrollView.addSpacerView(NSPadding.large)
        
    }
    private func setupStackScrollView() {
        stackScrollView.stackView.isLayoutMarginsRelativeArrangement = true
        stackScrollView.stackView.layoutMargins = UIEdgeInsets(top: 0, left: 15, bottom: 0, right: 15)

        view.addSubview(stackScrollView)
        stackScrollView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
    }
    func generateQRCode(from string:String) -> UIImage? {
        let data = string.data(using: String.Encoding.ascii)

        if let filter = CIFilter(name: "CIQRCodeGenerator") {
            filter.setValue(data, forKey: "inputMessage")
            let transform = CGAffineTransform(scaleX: 3, y: 3)

            if let output = filter.outputImage?.transformed(by: transform) {
                return UIImage(ciImage: output)
            }
        }

        return nil
    }
    
    func shareTapped() {
        
        let url: String = "http://pakal.cs.cinvestav.mx/admin/console/newrecord\(ReportingManager.shared.test())&uuid=\(UUID().uuidString)"
        print(url)
        guard let image = generateQRCode(from: url)?.jpegData(compressionQuality: 0.8) else {
            print("No image found")
            return
        }

        let vc = UIActivityViewController(activityItems: [image], applicationActivities: [])
        vc.setValue("Código QR", forKey: "Subject")
        vc.popoverPresentationController?.barButtonItem = navigationItem.rightBarButtonItem
        present(vc, animated: true)
    }
}

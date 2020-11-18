/*
 * Created by Ubique Innovation AG
 * https://www.ubique.ch
 * Copyright (c) 2020. All rights reserved.
 */

import UIKit
import CoreLocation

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate
                   //, CLLocationManagerDelegate
{
    internal var window: UIWindow?
    //var locationManager = CLLocationManager()
    private var lastForegroundActivity: Date?
    //var beaconRegionConstraints: CLBeaconIdentityConstraint!
    //var beaconRegion: CLBeaconRegion!
    
    internal func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // setup sdk
        TracingManager.shared.initialize()
        // this is where we left off:
        /*let uuid = UUID(uuidString: "b9407f30-f5f8-466e-aff9-25556b57fe6d")!
        locationManager.delegate = self
        locationManager.requestAlwaysAuthorization()
        
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.startUpdatingLocation ()
        locationManager.allowsBackgroundLocationUpdates = true
        locationManager.pausesLocationUpdatesAutomatically = false
        
        beaconRegion = CLBeaconRegion(uuid: uuid, identifier: "String")
        beaconRegionConstraints = CLBeaconIdentityConstraint(uuid: uuid)
        */
        if shouldSetupWindow(application: application, launchOptions: launchOptions) {
            setupWindow()
            willAppearAfterColdstart(application, coldStart: true, backgroundTime: 0)
        }

        return true
    }
    
    /*func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        //print("Entro al inicio de beacon...")
        if status == .authorizedAlways {
            if CLLocationManager.isMonitoringAvailable(for: CLBeaconRegion.self) {
                if CLLocationManager.isRangingAvailable() {
                    startScanning()
                }
            }
        }
    }
    func locationManager(_ manager: CLLocationManager, didRangeBeacons beacons: [CLBeacon], in region: CLBeaconRegion) {
        //print("hay beacons...\(beacons.count)")
        if beacons.count > 0 {
            for each in beacons {
                //print("El major es: \(each.major)")
                //print("El minor es: \(each.minor)")
                //print("El rssi es: \(each.rssi)")
            }
        } else {
            print("No hay nada de beacons... ")
        }
    }
    func startScanning() {
        //print("empezo a escanear beacons...")
        locationManager.startMonitoring(for: beaconRegion)
        locationManager.startRangingBeacons(satisfying: beaconRegionConstraints)
    }*/
    
    private func shouldSetupWindow(application: UIApplication, launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        if application.applicationState == .background {
            return false
        }

        guard let launchOptions = launchOptions else {
            return true
        }

        let backgroundOnlyKeys: [UIApplication.LaunchOptionsKey] = [.location, .bluetoothCentrals, .bluetoothPeripherals]

        for k in backgroundOnlyKeys {
            if launchOptions.keys.contains(k) {
                return false
            }
        }

        return true
    }

    private func setupWindow() {
        window = UIWindow(frame: UIScreen.main.bounds)
        window?.overrideUserInterfaceStyle = .light

        TracingManager.shared.beginUpdatesAndTracing()

        window?.makeKey()
        window?.rootViewController = NSNavigationController(rootViewController: NSHomescreenViewController())

        setupAppearance()

        window?.makeKeyAndVisible()
    }

    private func willAppearAfterColdstart(_: UIApplication, coldStart: Bool, backgroundTime: TimeInterval) {
        // Logic for coldstart / background

        // if app is cold-started or comes from background > 30 minutes,
        // do the force update check
        print("Inicio un proceso willAppearAfterColdstart ")
        if coldStart || backgroundTime > 30.0 * 60.0 {

            if !jumpToMessageIfRequired(onlyFirst: true) {
                DispatchQueue.main.asyncAfter(deadline: .now()+3.0) {
                    _ = self.jumpToMessageIfRequired(onlyFirst: true)
                }
            }
        }
        else {
            _ = jumpToMessageIfRequired(onlyFirst: false)
        }

    }

    func jumpToMessageIfRequired(onlyFirst: Bool) -> Bool {
        let shouldJump: Bool
        if onlyFirst {
            shouldJump = UIStateManager.shared.uiState.shouldStartAtMeldungenDetail
        }
        else {
            shouldJump = UIStateManager.shared.uiState.shouldStartAtMeldungenDetail && UIStateManager.shared.uiState.meldungenDetail.showMeldungWithAnimation
        }
        if shouldJump,
            let navigationController = window?.rootViewController as? NSNavigationController,
            let homescreenVC = navigationController.viewControllers.first as? NSHomescreenViewController {
            navigationController.popToRootViewController(animated: false)
            homescreenVC.presentMeldungenDetail(animated: false)
            return true
        }
        else {
            return false
        }
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        lastForegroundActivity = Date()

        // App should not have badges
        // Reset to 0 to ensure a unexpected badge doesn't stay forever
        application.applicationIconBadgeNumber = 0
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // If window was not initialized (e.g. app was started cause
        // by a location change), we need to do that
        if window == nil {
            setupWindow()
            willAppearAfterColdstart(application, coldStart: true, backgroundTime: 0)

        } else {
            let backgroundTime = -(lastForegroundActivity?.timeIntervalSinceNow ?? 0)
            willAppearAfterColdstart(application, coldStart: false, backgroundTime: backgroundTime)
        }
    }

    func application(_: UIApplication, performFetchWithCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        DatabaseSyncer.shared.performFetch(completionHandler: completionHandler)
    }


    // MARK: - Appearance

    private func setupAppearance() {
        UIBarButtonItem.appearance().tintColor = .ns_text

        UINavigationBar.appearance().titleTextAttributes = [
            .font: NSLabelType.textBold.font,
            .foregroundColor: UIColor.ns_text,
        ]
    }
}

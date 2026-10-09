# brnywzsatissaati

Android Kotlin uygulama projesi.

## Çalışma planı
- Pazartesi–Cuma (hafta içi)
- Her saat :45'te, 10:45 ile 17:45 arasında
- Siren tonu + Türkçe TTS: “SATIŞINI YAZ!!”
- Alarm, kullanıcı **SUSTUR** düğmesine basana kadar tekrar eder
- Cihaz yeniden başlatıldığında alarm yeniden planlanır

## APK oluşturma
Bu ZIP, derlenmiş APK değil, Android Studio proje kaynaklarıdır.
1. Android Studio'yu açın.
2. `brnywzsatissaati` klasörünü proje olarak açın.
3. Gradle senkronizasyonunun tamamlanmasını bekleyin.
4. `Build > Build APK(s)` seçeneğini kullanın.
5. APK genellikle `app/build/outputs/apk/debug/app-debug.apk` yolunda oluşur.

## Android izinleri
İlk açılışta bildirim izni ve Android 12+ sürümlerde kesin alarm izni istenebilir. Telefonun pil optimizasyonu bazı üreticilerde alarm davranışını etkileyebilir; güvenilir kullanım için uygulamaya arka planda çalışma izni verilmesi gerekebilir.

## Not
Android sürümüne ve üretici arayüzüne göre kilit ekranından tam ekran açılma kısıtlanabilir. Bu nedenle gerçek telefonda test edilmelidir.


## Android telefondan, Android Studio olmadan APK oluşturma (GitHub Actions)
1. ZIP'i çıkarın.
2. GitHub'da tarayıcıdan yeni bir repository oluşturun.
3. Proje dosyalarını repository'ye yükleyin; `.github/workflows/build-apk.yml` dosyasının da yüklendiğini doğrulayın.
4. Repository'de **Actions** sekmesine girin. Gerekirse workflow'ları etkinleştirin.
5. **Build brnywzsatissaati APK** workflow'unu seçip **Run workflow** düğmesine basın.
6. İşlem tamamlanınca çalışmanın altındaki **Artifacts** bölümünden `brnywzsatissaati-debug-apk` dosyasını indirin.
7. ZIP içinden `app-debug.apk` dosyasını çıkarıp telefona kurun. Android sorarsa tarayıcı/dosya yöneticisi için “Bu kaynaktan yüklemeye izin ver” seçeneğini etkinleştirin.

Bu yöntem Android Studio gerektirmez; derleme GitHub'ın sunucusunda yapılır. Repository'yi public yapmak zorunda değilsiniz, ancak private repository için GitHub hesabında Actions kullanımı açık olmalıdır.

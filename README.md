Android Otomatik Mesaj Uygulaması - Kurulum ve Test Rehberi
Aşağıdaki adımları takip ederek uygulamayı Android Studio'da çalıştırabilir ve test edebilirsiniz.

1. Projeyi Açma
Android Studio'yu başlatın.
Open (Aç) seçeneğine tıklayın.
path\to\autoMessage klasörünü seçip OK deyin.
Android Studio'nun projeyi indekslemesini ve Gradle senkronizasyonunu tamamlamasını bekleyin (ilk açılışta internet bağlantısına bağlı olarak birkaç dakika sürebilir).
2. Çalıştırma
Bilgisayarınıza bir Android cihaz bağlayın veya AVD Manager'dan bir emülatör başlatın.
Üst menüdeki yeşil Run (Oynat) butonuna basın.
3. Test Etme
Uygulama açıldığında SMS Gönderme İzni isteyecektir. İzin Ver'i seçin.
Telefon Numarası alanına geçerli bir numara girin.
Mesaj alanına bir metin yazın.
Tarih Seç ve Saat Seç butonlarını kullanarak şu andan 1-2 dakika sonrasını seçin.
Zamanla butonuna basın.
Not: Android 12 ve üzeri cihazlarda "Hassas Alarm İzni" gerekebilir. Uygulama sizi otomatik olarak ayarlara yönlendirirse, bu izni verip geri dönün.
Belirlenen saat geldiğinde mesajın gönderildiğini doğrulayın (Logcat'te "AlarmReceiver" etiketini veya cihazın mesaj kutusunu kontrol edebilirsiniz).
Sorun Giderme
Mesaj gitmiyor: Cihazın şebeke bağlantısı olduğundan ve kontör/paket olduğundan emin olun. Emülatör kullanıyorsanız, emülatör ayarlarından SMS simülasyonunu kontrol edebilirsiniz.
Alarm çalmıyor: Bazı üreticilerin (Xiaomi, Huawei vb.) pil tasarrufu modları arka plan işlemlerini kısıtlayabilir. Uygulamanın pil ayarlarından "Kısıtlama Yok" seçeneğini işaretleyin.

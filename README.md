# DiAI - Ứng dụng Theo Dõi Bệnh Tiểu Đường

DiAI là ứng dụng theo dõi đường huyết, giúp người dùng ghi lại và quản lý chỉ số đường huyết theo thời gian.

## 🚀 Cách Cài Đặt và Chạy Dự Án

### 1. Clone Repository
```sh
git clone https://github.com/khanhlongtran/DiAI.git
```

### 2. Di chuyển vào thư mục dự án
```sh
cd DiAI_App
```

### 3. Mở bằng Android Studio
- Sử dụng **phiên bản mới nhất** của Android Studio để đảm bảo tương thích (mới nhất -> android-studio-2024.2.2.13), vào Settings -> Update or Download newest version mới chạy được.
- Khi mở lần đầu, Android Studio sẽ yêu cầu tải xuống một số dependency và thiết lập ban đầu.

### 4. Cấu hình trước khi chạy
- Nếu sử dụng **emulator**, hãy chắc chắn đã cài đặt một AVD phù hợp.
- Đảm bảo tất cả các dependency đã được cài đặt bằng cách vào **File > Settings > Appearance & Behavior > System Settings > Android SDK**, sau đó kiểm tra và cập nhật SDK cần thiết.

### 5. Chạy ứng dụng
Nhấn **Run** (hoặc `Shift + F10` trên Windows) để build và chạy ứng dụng.

## 🔧 Cấu Trúc Dự Án
```
DiAI_App/
│── app/                 # Source code chính
│   │── src/
│   │   │── main/
│   │   │   │── AndroidManifest.xml  # File khai báo quyền và cấu hình ứng dụng
│   │   │   │── java/
│   │   │   │   │── com/example/diai_app/  # Code chính của ứng dụng
│   │   │   │   │   │── Adapter/    # Các adapter dùng trong RecyclerView
│   │   │   │   │   │── DataModel/  # Các model dữ liệu
│   │   │   │   │   │── Fragments/  # Các fragment của ứng dụng
│   │   │   │   │   │── Manager/    # Quản lý dữ liệu và logic
│   │   │   │── res/
│   │   │   │   │── drawable/  # Hình ảnh và icon
│   │   │   │   │── layout/    # Các file XML giao diện
│   │   │   │   │── values/    # Các file giá trị như strings.xml, colors.xml
│── assets/              # Tài nguyên tĩnh
│── gradle/              # Cấu hình Gradle
│── .gitignore           # File để loại trừ file không cần thiết khi commit
│── README.md            # File hướng dẫn
│── build.gradle         # File cấu hình Gradle cấp dự án
│── settings.gradle      # File thiết lập dự án
```

## 🛠 Công Nghệ Sử Dụng
- **Java** (Ngôn ngữ lập trình chính)

## 📝 Ghi Chú
- Nếu gặp lỗi **Gradle sync failed**, hãy thử cập nhật phiên bản Android Studio hoặc chạy lệnh:
  ```sh
  ./gradlew build --refresh-dependencies
  ```
- Nếu có vấn đề liên quan đến quyền truy cập (permissions), kiểm tra lại trong `AndroidManifest.xml`.
- Nếu gặp lỗi liên quan đến Gradle Wrapper, vào **Gradle Scripts > gradle-wrapper.properties** và thay thế nội dung bằng:
  ```
  #Wed Feb 05 20:49:12 ICT 2025
  distributionBase=GRADLE_USER_HOME
  distributionPath=wrapper/dists
  distributionUrl=https\://services.gradle.org/distributions/gradle-8.7-bin.zip
  zipStoreBase=GRADLE_USER_HOME
  zipStorePath=wrapper/dists
  ```

## 📩 Liên Hệ
Nếu có bất kỳ vấn đề hoặc góp ý nào, vui lòng mở issue hoặc liên hệ với mình tại [GitHub Issues](https://github.com/khanhlongtran/DiAI/issues).


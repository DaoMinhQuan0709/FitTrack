# 🔥 FitTrack — Ứng dụng theo dõi dinh dưỡng & luyện tập cá nhân

**Tên đề tài:** Xây dựng ứng dụng theo dõi tập luyện và dinh dưỡng cá nhân trên Android
theo kiến trúc MVVM sử dụng Room Database và Firebase Authentication

**Đối tượng:** Đồ án cuối môn, nhóm 2 người, mức Basic–Intermediate

---

## 1. Kiến trúc & Công nghệ

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ | **Java** |
| Kiến trúc | MVVM (Model – View – ViewModel) + Repository Pattern |
| Local Database | Room (SQLite) |
| Xác thực người dùng | Firebase Authentication (Email/Password) |
| Biểu đồ | MPAndroidChart |
| UI | View Binding + Material 3 Components (dark theme "Aurora Night" — violet/mint, không dùng Navigation Component để đơn giản cho người mới) |
| Async | `ExecutorService` (Room) + `LiveData` (thay cho Kotlin Coroutines) |

> Dự án này là bản chuyển đổi từ Kotlin sang Java 100%, giữ nguyên kiến trúc MVVM + Repository
> và toàn bộ tính năng gốc. Các đoạn Kotlin coroutines (`viewModelScope.launch`) được thay bằng
> một `ExecutorService` dùng chung trong `AppDatabase` để chạy thao tác đọc/ghi Room ngoài main thread.

**Vì sao không dùng Docker / backend riêng:** Room chạy local trong app (SQLite),
Firebase là dịch vụ cloud có sẵn của Google — không có server nào để containerize.
Docker chỉ cần thiết nếu nhóm tự viết backend riêng, việc đó nằm ngoài scope đồ án này.

**Vì sao không sync Room ↔ Firestore 2 chiều:** Offline-first sync 2 chiều là bài toán khó
(xử lý conflict, merge dữ liệu) — không phù hợp với 2 người mới học trong 1–1.5 tháng.
Ở bản này, Firebase **chỉ đảm nhiệm Authentication**; toàn bộ dữ liệu nghiệp vụ
(hồ sơ, bữa ăn, bài tập, cân nặng) lưu trong Room. Đây là hướng an toàn, đúng như bạn đã chốt.

---

## 2. Tính năng đã hoàn thiện

- **Đăng ký / Đăng nhập** bằng Email & Mật khẩu (Firebase Auth)
- **Onboarding**: nhập tuổi/giới tính/chiều cao/cân nặng/mức vận động/mục tiêu →
  tự động tính **BMR** (công thức Mifflin-St Jeor), **TDEE**, và phân bổ **macro**
  (Protein 30% / Carb 40% / Fat 30%)
- **Dashboard**: tổng quan calo hôm nay (đã nạp / còn lại), thanh tiến độ, macro còn lại, số bài tập hôm nay
- **Nhật ký dinh dưỡng**: thêm/xóa bữa ăn theo ngày (tên món, calo, protein/carb/fat, loại bữa), điều hướng qua lại giữa các ngày
- **Nhật ký luyện tập**: thêm/xóa bài tập (tên bài, số set, số rep, tạ)
- **Tiến độ cân nặng**: nhập cân nặng theo ngày, biểu đồ đường (LineChart) hiển thị xu hướng
- **Hồ sơ cá nhân**: xem lại thông tin, chỉnh sửa hồ sơ, đăng xuất

## 3. Cấu trúc thư mục chính

```
app/src/main/java/com/fittrack/app/
├── data/
│   ├── local/          # Room: Entity, DAO, AppDatabase (+ ExecutorService dùng chung)
│   └── repository/     # Repository pattern (cầu nối DAO <-> ViewModel)
├── auth/                # Đăng nhập / Đăng ký (Firebase Auth)
├── onboarding/           # Nhập hồ sơ, tính BMR/TDEE
├── ui/
│   ├── dashboard/
│   ├── nutrition/
│   ├── workout/
│   ├── progress/
│   └── profile/
├── util/                 # TDEECalculator, DateUtils
└── MainActivity.java      # Bottom Navigation container
```

## 4. Đề xuất chia việc 2 người

| Thành viên A | Thành viên B |
|---|---|
| `auth/` (Login, Register, Firebase Auth) | `ui/workout/` (CRUD bài tập) |
| `onboarding/` + `util/TDEECalculator` | `ui/progress/` (Chart cân nặng) |
| `ui/nutrition/` (CRUD bữa ăn, tính tổng calo) | `ui/profile/` + `ui/dashboard/` |
| `data/` (Room Entity, DAO, Repository — làm chung buổi đầu) | (làm chung buổi đầu) |

## 5. Hướng dẫn setup để chạy được

### Bước 1 — Cài Android Studio
Tải bản mới nhất tại https://developer.android.com/studio (nên dùng bản Hedgehog trở lên).

### Bước 2 — Tạo project Firebase (bắt buộc, nếu không app sẽ không build được)
1. Vào https://console.firebase.google.com → **Add project** → đặt tên (VD: `FitTrack`)
2. Trong project vừa tạo, bấm **Add app** → chọn biểu tượng **Android**
3. Ở ô **Android package name**, nhập **chính xác**: `com.fittrack.app`
4. Bỏ qua bước SHA-1 (không cần cho Email/Password Auth)
5. Bấm **Download google-services.json**
6. Copy file đó vào thư mục `app/` của project (ngang hàng với `app/build.gradle`),
   **đúng tên** `google-services.json` (xóa file `google-services.json.SAMPLE` có sẵn hoặc ghi đè lên)
7. Trong Firebase Console → **Authentication** → tab **Sign-in method** → bật **Email/Password**

### Bước 3 — Mở project
1. Mở Android Studio → **Open** → chọn thư mục `FitTrack`
2. Đợi Gradle sync xong (lần đầu sẽ tải khá lâu vì cần tải Room, Firebase, MPAndroidChart)
3. Nếu lỗi thiếu `google-services.json` → quay lại Bước 2

### Bước 4 — Chạy
Chọn thiết bị ảo (Emulator API 26+) hoặc điện thoại thật → bấm **Run ▶**

---

## 6. Nếu còn dư thời gian (điểm cộng, không bắt buộc)

- Thêm tìm kiếm/lọc trong Nhật ký dinh dưỡng
- Thêm biểu đồ tổng calo theo tuần (Bar Chart) ở Dashboard
- Thêm mục "Thư viện bài tập mẫu" (danh sách tên bài tập gợi ý) để khỏi gõ tay
- Backup dữ liệu lên Firestore (1 chiều, dùng WorkManager chạy nền) — chỉ làm nếu code chính đã ổn định

## 7. Lưu ý khi báo cáo / bảo vệ

- Giải thích rõ vì sao chọn **MVVM + Repository**: tách biệt UI (Fragment) khỏi logic dữ liệu (ViewModel) khỏi nguồn dữ liệu (Repository/Room) → dễ bảo trì, dễ test
- Công thức BMR dùng **Mifflin-St Jeor** — công thức được khuyến nghị phổ biến nhất hiện nay (chính xác hơn Harris-Benedict cũ)
- Nếu được hỏi "vì sao không sync cloud 2 chiều" → trả lời thẳng: đã cân nhắc, nhưng ưu tiên **chạy ổn định, không bug** hơn là nhồi tính năng vượt quá năng lực nhóm trong thời gian ngắn — đây là quyết định kỹ thuật có chủ đích, không phải thiếu sót.

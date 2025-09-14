# NetronicTestExample

Android test project with two screens:
- **Users list** (20 users from `https://randomuser.me/api/?results=20`)
- **User details** (photo, email, phone, address, date of birth, country)

---

## 📂 Project Structure

Architecture: **MVVM + Clean Architecture** (layers: Data / Domain / Presentation).

app/   
 ├── data/ # DTOs, Retrofit API, repository implementation, mappers<br>
 ├── domain/ # Models, repository interfaces, use cases<br>
 ├── presentation/ # Compose UI, ViewModels, navigation<br>
 ├── logging/ # logger (works in runtime and tests)<br>
 └── di/ # Hilt modules (NetworkModule, etc.)

---

## ⚙️ Tech Stack

- **Kotlin**
- **Jetpack Compose** — UI
- **MVVM + Clean Architecture**
- **Coroutines + Flow** — async processing
- **Retrofit + OkHttp** — networking
- **Hilt (Dagger)** — dependency injection
- **Navigation Compose** — navigation
- **Coil** — image loading
- **JUnit4 + kotlinx.coroutines.test** — unit testing
- **AndroidX Test + MockWebServer + Hilt Android Test** — integration testing

---

## 🐞 Key Issues & Solutions

1. **No internet access on emulator**  
   ➝ Added `INTERNET` permission in `AndroidManifest.xml`.  
   ➝ Tests use `MockWebServer` (no real internet needed).

2. **Dependency conflict (javapoet + hiltAggregateDepsDebug)**  
   ➝ Updated java version to 17 and set false to hilt.enableAggregatingTask option.

3. **`android.util.Log` in unit tests (`Object d not mocked`)**  
   ➝ Introduced a lightweight logger `Applogger` that works in Android runtime and skip-up on work in JVM unit tests.

4. **DTO → Domain conversion problem**  
   ➝ API returns `postcode` sometimes as number, sometimes as string.  
   ➝ In DTO it’s stored as `JsonElement`, and the mapper normalizes it to `String` with a warning log.

---

## 📋 Logging & Debugging

Custom **Logcat tags** are used per layer:

- `NET.OkHttp` — HTTP requests/responses
- `NET.Raw` — raw JSON response (limited to 4K characters)
- `Repo.User` — repository (getUsers, getUserById)
- `Mapper.User` — DTO → Domain mapping warnings
- `VM.Users` — users list ViewModel
- `VM.Details` — user details ViewModel
- `UI.Users` — UI events (render, user click)

👉 Use these tags in Logcat filters to quickly identify issues.

---

## ▶️ Running the App

1. Open project in **Android Studio**.
2. Run on emulator or real device:  
   `Run → app`.
3. On startup, the app fetches 20 users from the API.

👉 The emulator should have an Internet access.

---

## 🧪 Running Tests

- **Unit tests (JVM, no Android, no internet):**
  ```bash
  ./gradlew :app:testDebugUnitTest

Instrumented tests (requires emulator or device):

./gradlew :app:connectedDebugAndroidTest

## ✅ Test Coverage
*Unit Tests*
- **UserRepositoryImplTest**
  - Returns 20 users from network.
  - Repository caching works (second call served from cache).
  - Proper JSON parsing error handling.
- **MapperTest**
  - DTO → Domain conversion.
  - Handles mixed-type postcode (string/number).
  
*Integration Tests*
- **UsersFlowInstrumentedTest**
  - Users screen renders 20 items.
  - Clicking a user opens details screen.
  - User details screen displays all key fields.

📌 Summary

This project demonstrates:
- Clean Architecture (MVVM with clear Data / Domain / Presentation separation).
- Networking and JSON parsing with Retrofit/OkHttp.
- Solving common issues (internet permission, dependency conflicts, logging in tests).
- Proper setup of unit and instrumented tests.
- Debugging with structured Logcat tags.
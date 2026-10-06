# Taller Android Studio Implementation Plan

This plan covers fixing the environment configuration and implementing the requested activities for the "tallerandroid" project.

## User Review Required

> [!IMPORTANT]
> The JDK path detected is `C:\Program Files\Java\jdk-21`. Please ensure this path is configured in **File > Project Structure > SDKs** if the IDE still shows "Project JDK is not defined".

## Proposed Changes

### Environment & Base Fixes
- Fix `MainActivity.java` package name to match the project namespace (`com.example.tallerandroid`).
- Ensure `activity_main.xml` layout is correctly linked to the new activities.

---

### Currency Converter Component
- [NEW] `ConvertidorActivity.java`: Implements logic for currency conversion between USD, COP, and EUR.
- [NEW] `activity_convertidor.xml`: Layout with inputs for quantity, spinners for selection, and result display.

---

### Credit Calculator Component
- [NEW] `CalculadoraActivity.java`: Implements interest simple calculation logic.
- [NEW] `activity_calculadora.xml`: Layout with inputs for credit value, installments, interest rate, and result displays.

---

### Manifest Update
- [MODIFY] `AndroidManifest.xml`: Register the two new activities.

## Verification Plan

### Automated Tests
- Run `gradlew assembleDebug` to ensure the project compiles correctly.

### Manual Verification
1. Deploy the app to an emulator.
2. Verify navigation from `MainActivity` to `ConvertidorActivity` and `CalculadoraActivity`.
3. Test `ConvertidorActivity` with various inputs (e.g., 100 USD to COP).
4. Test `CalculadoraActivity` with credit values and interest rates.
5. Generate a signed APK.

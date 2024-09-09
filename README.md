Setup Instructions

1. Prerequisites

Android Studio: Latest version is recommended.
Gradle: Ensure the project is synced with the latest Gradle version.
Minimum SDK: 21 (Android 5.0, Lollipop).
Target SDK: 33 (Android 13, Tiramisu).
Kotlin: The project is written in Kotlin.

2. Project Structure

The project follows the MVVM architecture to ensure a clean separation of concerns.

Model: Contains the data handling and business logic, especially for expense management.
ViewModel: Manages UI-related data in a lifecycle-conscious way.
View (Activity/Fragments): The UI components, including activities and fragments, responsible for displaying data to users.

3. Libraries Used

AndroidX & Jetpack Components: Used for modern Android development (ViewModel, LiveData, Navigation Component, etc.).
Material Components: For modern UI components and Material Design patterns (Drawer, Toolbar, Buttons, etc.).
Room Database: For local data persistence (storing expenses, budgets).
MPAndroidChart: For visualizing data with charts in the Expense Summary and Chart fragments.
SharedPreferences: For saving user preferences (theme choice).

4. Installation Steps

Follow these steps to set up and run the project locally:

Step 1: Clone the Repository

git clone https://github.com/shefi-tech/Budget.git
cd Budget

Step 2: Open in Android Studio

Open Android Studio.
Select Open an existing project.
Navigate to the project directory Budget, then click OK to open.

Step 3: Sync Gradle Files

Android Studio should automatically sync the Gradle files. If not, manually sync the project by selecting File > Sync Project with Gradle Files.

Step 4: Run the App

Choose a device/emulator from Android Studio’s device manager.
Click the Run button or press Shift + F10 to build and run the app.

5. Adding Dependencies
Ensure the following dependencies are included in your build.gradle file:

gradle

dependencies {
    // AndroidX Libraries
    implementation "androidx.appcompat:appcompat:1.6.1"
    implementation "androidx.constraintlayout:constraintlayout:2.1.4"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.5.1"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1"
    implementation "androidx.navigation:navigation-fragment-ktx:2.5.3"
    implementation "androidx.navigation:navigation-ui-ktx:2.5.3"
    
    // Room Database
    implementation "androidx.room:room-runtime:2.5.0"
    kapt "androidx.room:room-compiler:2.5.0"
    implementation "androidx.room:room-ktx:2.5.0"
    
    // Material Components
    implementation "com.google.android.material:material:1.9.0"
    
    // MPAndroidChart for charting
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
}
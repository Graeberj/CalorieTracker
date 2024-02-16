Calorie Tracker

Calorie Tracker is a comprehensive application designed to help users monitor their caloric intake 
and manage their diet effectively. Built with a multi-module architecture, it ensures scalability, 
maintainability, and clear separation of concerns. It  leverages the Open Food API to access a vast 
database of food nutritional information. This integration allows users to easily search for and log
food items, making tracking calories and nutrients more efficient and accurate.


Modules Overview

Core: Shared utilities, data models, and fundamental components across the application.
Onboarding Domain: Business logic for the onboarding process.
Onboarding Presentation: User interface components and logic for user onboarding.
Tracker Data: Data handling for tracking features, including API calls and local database interactions.
Tracker Domain: Business logic for calorie tracking and dietary management.
Tracker Presentation: User interface components and logic for the tracking features.

Features

Daily calorie tracking
Nutritional information
Progress tracking and reports

Technology Stack

Kotlin for core development
Retrofit for network operations
Room for local data storage
MVVM architecture for scalable app development
Dagger/Hilt for dependency injection

Future Enhancements

Currently, the Calorie Tracker app does not include user authentication. This is a feature I'm
considering for future development to enhance user personalization and security. 




# BouldeRate

BouldeRate is an Android mobile app that allows users to rate bouldering (sport climbing)  institutes and share tips for an ultimate experience.


## Features

1. **Login and user authentication via phone number:** Users can create an account or log in to the app using their phone number.

2. **Rate Bouldering Institutes:** Users can rate and review bouldering institutes they have visited.o

3. **Share Climbing Tips:** Users can share their own climbing tips and tricks for others to discover.

## Installation
1. Clone the repository:

```bash
git clone https://github.com/TzufKishon/BouldeRate.git
```

2. Build and run the application on Android Studio.

## Server and Data Management

The app uses Firebase Real-time Database for data management and user interactions.

The following server-side functionalities are implemented:

- **Backup and Transfer Data Between Users:** Firebase Real-time Database enables seamless backup and transfer of user data.

  This feature ensures that users can access their data across multiple devices.

- **Firebase Auth:** Firebase Auth handles user authentication.
  
  It provides secure login and registration functionalities with phone number authentication.

## Known Bugs

**1.** Inconsistent user name when signing up with the same phone number for the second time:

When a user signs up to the app using the same phone number but a different name, the user's name is not consistently updated across the ratings and tips sections. Consequently, when the user attempts to rate or submit a tip, the app collapses.

**2.** Non-smooth transitions between the rating screen and the Main screen:

There are noticeable issues with the transitions between the rating screen and the main screen, the transitions appear to be non-smooth.





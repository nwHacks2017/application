# <img src="./img/icon.png" width="40"> LifeBand (nwHacks 2017): Application

## By Luminescence

[![Build Status](https://travis-ci.org/nwHacks2017/application.svg?branch=master)](https://travis-ci.org/nwHacks2017/application)
[![License](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/nwHacks2017/application/blob/master/LICENSE)

Android 8.0 (API level 26) application which supplies patient data to doctors.

## Setup

First, an instance of the [Lifeband backend](https://github.com/nwHacks2017/backend) should already be running.

### Android

Install [Android Studio](https://developer.android.com/studio/) and import the project.

#### Creating a Production Release

##### Step 1: Building a Release Variant

Create a new file in the root directory to hold the secure keystore details:
```shell
cd application/
cp keystore.properties.template keystore.properties
```

This file will be ignored by Git. Set your keystore information in this file.

Under _Build_, click _Select Build Variant_.

In the _Build Variants_ view which appears, change the **Active Build Variant** from _debug_ to _release_.

Build the application normally to your phone. The keystore credentials will be verified and applied.

##### Step 2: Creating the Signed App

Under _Build_, click _Generate Signed Bundle / APK_.

Follow the instructions, inputting your keystore path and credentials, to create a signed app which can be uploaded to the app store.


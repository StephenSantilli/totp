# TOTP
TOTP is an extension for Burp Suite that allows you to generate and use time-based one-time passwords. TOTP codes are generated according to the standard outlined in [RFC 6238](https://datatracker.ietf.org/doc/html/rfc6238). The extension supports both Burp Suite's Professional and Community editions.

![Screenshot of the extension's tab in Burp Suite.](/images/TOTP_Tab.png)

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
  - [Adding a code](#adding-a-code)
  - [Scanning QR codes](#scanning-qr-codes)
  - [Viewing your codes](#viewing-your-codes)
  - [Use with Scanner](#use-with-scanner)
- [Troubleshooting](#troubleshooting)
  - [The placeholder wasn't replaced with a TOTP](#the-placeholder-wasnt-replaced-with-a-totp)
  - [My placeholder gets replaced with the TOTP code in Repeater](#my-placeholder-gets-replaced-with-the-totp-code-in-repeater)
- [Settings](#settings)
  - [Save TOTPs to project file](#save-totps-to-project-file)
  - [Use regex when matching TOTPs](#use-regex-when-matching-totps)
  - [Enable verbose logging](#enable-verbose-logging)
  - [Replacement method](#replacement-method)
  - [Replace in X](#replace-in-target)
- [Acknowledgements](#acknowledgements)

## Features
- TOTP codes are refreshed automatically and displayed right in Burp Suite
- Automatically insert TOTPs into requests sent from any Burp tool using a custom placeholder string or regex match
  - Use with Burp's Scanner to allow crawling websites with two-factor authentication
  - Use with Repeater to test authentication flows without constantly pasting TOTP codes
  - Enable and disable matching for each TOTP on the fly
- Add TOTPs from QR codes, no manual entry required
- Save TOTPs to your project file
- Add TOTPs with custom durations, code lengths, and choose which hashing algorithm to use (SHA-1, SHA-256, or SHA-512)
- Copy TOTPs to your clipboard with one click
- Name each TOTP to distinguish them

## Installation
1. Download the source code
2. Build the extension using `./gradlew build` (Mac & Linux) or `.\gradlew.bat build` (Windows)
3. Launch Burp Suite and navigate to the "Extensions" tab
4. Click "Add"
5. Set "Extension type" to `Java`
6. Select the Jar file you built in Step 2 (It will be in `./build/libs/TOTP.jar`)
7. Click "Next" to load the extension

For more information, see [PortSwigger's documentation](https://portswigger.net/burp/documentation/desktop/extend-burp/extensions/installing/manual-install).

## Usage

### Adding a code
Navigate to the "TOTP" tab in Burp Suite. At the top, there is a form allowing you to enter the details of a TOTP.

#### Name
Give your TOTP a name! This will allow you to distinguish it from other TOTPs in the list.

#### Secret
This is where you will enter in the Base32-encoded secret of your TOTP. Typically, the secret will be in the form `A2B3 C4D5 E6F7 GHIJ KLMN OPQR STUV WXYZ`.

#### Duration
This is how long each TOTP lasts for, in seconds. This will almost always be `30`, but some applications may use values such as `60`.

#### Code Length
This is the number of digits of the TOTP code to generate. This will almost always be `6`, returning a code in the form of `123 456`. However, some applications may use values such as `8`.

#### Algorithm
This allows you to select the hashing algorithm that the application expects. This will almost always be `SHA-1`, which uses the HMAC-SHA-1 hash function. Some applications may use `SHA-256` or `SHA-512` hashing instead.

### Scanning QR Codes
QR codes can be used to automatically populate the values detailed in [Adding a code](#adding-a-code). In order for QR codes to be scanned, you must have the code and Burp visible on the same screen. Depending on your operating system, you may also have to give Burp Suite access to take a screen capture. If a QR code is successfully scanned, the encoded values will be added to their respective fields. Adjust them or simply press "Add."

### Viewing your codes
In the "TOTP" tab, you can see a list of all of the TOTPs you have added to this project.

#### Name
Shows the name you assigned to this TOTP.

#### Algorithm
Displays the algorithm you assigned to this TOTP.

#### Code
The current, valid TOTP code will be displayed with a space in the middle for readability. This code will be updated according to the duration you configured.

#### Progress Bar
A progress bar will display next to the code indicating the amount of time remaining before the code will be invalid. It will count down each second from the number of seconds you configured in the [duration](#duration).

#### Match Field
Here, you can enter the string that you want the extension to search for in requests. If you would like to use regex, see [Use regex when matching TOTPs](#use-regex-when-matching-totps). When the extension handles a request, it will replace all occurrences of this match string with your TOTP. It will also update the Content-Length header of the request, if appropriate.

#### Replace in requests?
This checkbox allows you to quickly enable or disable replacing for that specific TOTP. When disabled, the match string cannot be edited and the extension will not replace occurrences of the match in requests. If you have a lot of TOTPs saved, you may find better performance by disabling matching of TOTPs that you are not using.

#### Copy
This button will copy the TOTP code (without spacing) to your clipboard. 

#### Remove (X)
This button will remove the TOTP from the UI. It will also remove it from the project storage if the [Save TOTPs to project file](#save-totps-to-project-file) option is enabled.  

### Use with Scanner
The extension can be used with Burp's Scanner, which allows you to scan targets that use TOTPs for multi-factor authentication. 

1. Record a login sequence for your application, entering the TOTP as normal when prompted. [See PortSwigger's documentation for more information](https://portswigger.net/burp/documentation/scanner/authenticated-scanning/using-recorded-logins).
2. Once you paste in your script from the Navigation Recorder, click "See as Events"
3. In the list, select the event where you entered your TOTP and click "Edit"
4. In the "Typed Value" section, enter the placeholder you set for the desired TOTP. See [Match Field](#match-field) for more information.
5. Click "OK."
6. Test if the replacement works by clicking the "Replay" button. You will see the browser enter your placeholder (e.g. `_Name_`) in the box, but the request will be modified by the extension before it is sent. You can verify this by checking the "Logger" tab and examining the request.

## Troubleshooting

### The placeholder wasn't replaced with a TOTP
Check which mode you have enabled. Remember, if you switched modes, it will not take effect until the extension is reloaded!
- If you are using ["Session handling rules only" mode](#mode-session-handling-rules-only), make sure that your session handling rule is enabled, has the correct scope, and that it includes the "Insert TOTP into request" action.
- If you are using ["Monitor all requests" mode](#mode-monitor-all-requests), make sure that you enable the relevant [Replace in X](#replace-in-target) setting.

For performance, the extension will match and replace all occurrences of the first TOTP that has a match in the request. It will not continue to search for matches from other TOTPs.

### My placeholder gets replaced with the TOTP code in Repeater
This is how Burp handles session handling rules. See the note in ["Session handling rules only" mode](#mode-session-handling-rules-only).

## Settings
You can find options for this extension in Burp's application settings under the "Extensions" tab.

### Save TOTPs to project file
Enabling this will store the settings for each TOTP you add in the storage of your project file. This means TOTPs persist, even when you restart Burp Suite.

**Security Note**:  This setting stores the secrets of your TOTPs in your `.burp` project file. If you are concerned about the security of this, consider disabling this option and use care when sharing your project file.

### Use regex when matching TOTPs
Enabling this option will treat your match strings as regular expressions according to [Java Pattern syntax](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html).

### Enable verbose logging
This option enables additional logging for debugging purposes. This can affect performance, and should be left off when it is not in use.

### Replacement method
This extension has two replacement "modes." <ins>Switching replacement modes requires an extension reload.</ins>

#### Mode: Monitor all requests
This replacement mode will monitor all requests, inserting TOTPs where their match string is found. You can, and should, limit the scope of this using the "Replace in X" settings below.

#### Mode: Session handling rules only
This replacement mode will only match and replace requests which activate a session handling rule. This allows for greater control over the scope of requests the extension can act on. However, you must first create a session handling rule that invokes TOTP's extension handler, labeled, "Insert TOTP into request." For more information on configuring session handling rules, see [PortSwigger's documentation](https://portswigger.net/burp/documentation/desktop/settings/sessions/session-handling-rules).

**Note:** Session handling rules have an important drawback. If the rule modifies a request sent in Repeater, the message editor in Repeater is updated to display the modified request. This means that, if you use a placeholder for TOTP matching (e.g. `_Name_`,) it will be replaced with the TOTP code upon sending the request. You can avoid this by using the ["Monitor all requests" mode](#mode-monitor-all-requests).

### Replace in Target
**Note:** The extension ignores this option when using ["Session handling rules only" mode](#mode-session-handling-rules-only).

Enabling this will allow the extension to monitor and replace requests made from the Target tool.

### Replace in Scanner
**Note:** The extension ignores this option when using ["Session handling rules only" mode](#mode-session-handling-rules-only).

Enabling this will allow the extension to monitor and replace requests made from the Scanner tool.

### Replace in Repeater
**Note:** The extension ignores this option when using ["Session handling rules only" mode](#mode-session-handling-rules-only).

Enabling this will allow the extension to monitor and replace requests made from the Repeater tool.

### Replace in Intruder
**Note:** The extension ignores this option when using ["Session handling rules only" mode](#mode-session-handling-rules-only).

Enabling this will allow the extension to monitor and replace requests made from the Intruder tool.

### Replace in Sequencer
**Note:** The extension ignores this option when using ["Session handling rules only" mode](#mode-session-handling-rules-only).

Enabling this will allow the extension to monitor and replace requests made from the Sequencer tool.

### Replace in AI
**Note:** The extension ignores this option when using ["Session handling rules only" mode](#mode-session-handling-rules-only).

Enabling this will allow the extension to monitor and replace requests made from the Burp AI tool.

### Replace in Extensions
**Note:** The extension ignores this option when using ["Session handling rules only" mode](#mode-session-handling-rules-only).

Enabling this will allow the extension to monitor and replace requests made from Extensions.

### Replace in Proxy
**Note:** The extension ignores this option when using ["Session handling rules only" mode](#mode-session-handling-rules-only).

Enabling this will allow the extension to monitor and replace requests made from the Proxy tool.

## Acknowledgements
- TOTPs are generated using code from [RFC 6238 Appendix A](https://datatracker.ietf.org/doc/html/rfc6238#appendix-A) by Johan Rydell, published under the IETF Trust's Revised BSD License.
- QR code scanning is done using the [ZXing](https://github.com/zxing/zxing) library, which is licensed under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
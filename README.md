# Kindom

## Project Information

**Proposed level of achievement**: Apollo 11

**Project scope**: We hope to encourage neighbourliness among Singaporeans by allowing neighbours to assist each other in simple tasks.

After a long hard day in school, you walk wearily to the lift lobby of your flat. As you and the other residents enter the lift, an eerie silence falls upon the four walls. Everyone is glued to their phone screens and the only sound audible is the whirring of the lift machinery. As you step home, you realise that you have forgotten to get the eggs your mum has tasked you to buy.  Dread takes over as you imagine another trip down the grocery store with long queues caused by the COVID-19 pandemic. Just then, you remember that Uncle Chen from next door has gone to the supermarket just as you left. Maybe he can help you get the eggs?

**Frameworks used**:

Android
Firebase

**User stories**:
1. As a helpful resident, I want to be able to assist my fellow neighbours in simple tasks. The Help Me feature of the app allows users to request and offer help from other residents.
2. As an engaged citizen, I want to be kept updated on the happenings of my community. The Home page of the app shows the news occurring in the neighbourhood and in Singapore.
3. As an administrator, I want to be able to push out timely announcements to all users by adding / deleting news in the app and also keep track of any disputes arising from the application by managing reported Help Me posts.

## Features Developed

#### 1. Registration / Sign-in

When the user first launches the app, he/she is brought to the main page where the user can sign in with the text fields provided.

<details>
  <summary>*View image of main page*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/1%20-%20Main%20%20Sign-in%20page.jpg" width="256"/>
</details>

Alternatively, the user can click on the "Not registered yet? Click here!" to register an account with Kindom.

This brings the user to a 2-part registration process.

The first part includes filling personal details of the user as shown below.

<details>
  <summary>*View image of registration (part 1)*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/2%20-%20Registartion%20Part%201.jpg" width="256"/>
</details>

The second part includes using an email and password for user identification.

<details>
  <summary>*View image of registration (part 2)*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/3%20-%20Registration%20Part%202.jpg" width="256"/>
</details>

#### 2. Home (News)

A digital community notice board replaces the traditional ones found in the void decks of HDB flats. It provides updates on issues within the community (e.g. dengue cluster) and at the national level (e.g. COVID-19).

The images of the news can be scrolled (left and right) and a dot indicator indicates the position of the image the user is currently viewing.

Weather and PSI information is also provided at the top of the screen based on the region the user is in (from the postal code inputted during registration).

<details>
  <summary>*View image of home page*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/4%20-%20Home%20page.jpg" width="256"/>
</details>

#### 3. Help Me!

This core feature provides a platform for neighbours to send in or accept requests for assistance in simple tasks (such as helping to receive delivery goods on one's behalf or buying takeaway meals for the elderly who may not want to venture far out for safety reasons).

Upon clicking on the "Help Me!" button at the bottom navigation menu, the user is brought to a listing page. There are two types of listings - "All Listing" and "My Listing". A toast message appears to inform users that they can swipe down to refresh the list.

The All Listing tab shows all the listings from neighbours who are staying in the same RC as the user.

<details>
  <summary>*View image of Help Me All Listing*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/5%20-%20Help%20Me!%20page%20all%20listing.jpg" width="256"/>
</details>

Clicking on the details button allows the user to view detailed information of the Help Me! request. User can then offer help or chat with the person who posted the request to ask for more details.

<details>
  <summary>*View image of a Help Me post details*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/6%20-%20Help%20Me!%20request%20details.jpg" width="256"/>
</details>

A toast message appears at the bottom of the screen if the user clicks on the offer help button for the first time or repeatedly.

<details>
  <summary>*View toast message*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/7%20-%20Toast%20message.jpg" width="256"/>
</details>

<details>
  <summary>*View toast message when user repeatedly clicks on Offer Help*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/8%20-%20Toast%20message%20(repeated).jpg" width="256"/>
</details>

The My Listing tab shows the user's listings. User can add a new Help Me! request or edit / delete existing requests.

<details>
  <summary>*View image of Help Me My Listing*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/9%20-%20Help%20Me!%20page%20my%20listing.jpg" width="256"/>
</details>

An add floating action button at the bottom right side of the screen allows user to add a Help Me! request. Adding a request requires the user to fill up information about the request.

<details>
  <summary>*View image of adding a Help Me post*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/10%20-%20Help%20Me!%20add%20post.jpg" width="256"/>
</details>

#### 4. Chat

The chat feature allows neighbours to communicate with one another.

Upon clicking on the "Chat" button at the bottom navigation menu, the user is brought to a list of existing chat with other neighbours. From here, users can view the chat conversation history or send messages and/or media.

<details>
  <summary>*View image chat list*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/11%20-%20Chat%20list.jpg" width="256"/>
</details>

#### 5. Admin

If a person signs up as an admin, he/she is granted additional access privileges.

In the home page, an add floating action button at the bottom right side of the screen allows admin to add a news.

<details>
  <summary>*View image of admin's home page*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/12%20-%20Admin%20Home%20page.jpg" width="256"/>
</details>

<details>
  <summary>*View image of admin adding a news*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/13%20-%20Admin%20add%20news.jpg" width="256"/>
</details>

The admin can scroll through the images and tap on any image to delete the news.

<details>
  <summary>*View image of admin deleting a news*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/14%20-%20Admin%20delete%20news.jpg" width="256"/>
</details>

#### 6. Others

When the user is navigating between any of the three buttons in the bottom navigation menu, a menu icon is consistently shown in the top app bar. Clicking on the menu options shows a "My Profile" and "Sign Out" option.

"My Profile" allows users to view his own profile.

<details>
  <summary>*View image of My Profile page*</summary>
  <img src="https://github.com/SONGYI98/Kindom/blob/master/screenshots/15%20-%20My%20Profile%20page.jpg" width="256"/>
</details>

"Sign Out" signs the user out and brings him/her to the main screen.

## Development Stage

For **Milestone 2** of Orbital, we have implemented core features as stated below:

1. **Home page**

Users can view pre-loaded news.

2. **Help Me**

Users can add / delete / view Help Me posts.

3. **Chat**

Users can chat with other users.

For **Milestone 3** of Orbital, we have implemented more edge features as stated below:

1. **Admin features**

If the user is registered as an admin, he/she has the abilities to
a. Add news in the Home section.
b. Monitor and delete Help Me requests of any users in the same RC as the admin.

2. **Improved Home page**

Weather information such as the PSI level is shown.

3. **Help Me offer system**

This allows users to offer help and for the poster to manage the list of people offering their assistance.

4. **Improved Chat page**

5. **Miscellaneous visual polish and bug fixes**

## Problems Encountered

1. **Integrating Help Me! feature with Chat feature**

As both features are done separately by two different people, coordination is required to ensure that both features are integrated with each other. We need to ensure that users who look at a Help Me! request can chat with the poster of that request.

Therefore, the database is structured in such a way that user's information can be easily retrieved from the database. This is done through storing user's information through a User Java object which is then stored in Firebase Database.

2. **Retrieving location from users**

The postal code was initially meant to retrieve both the block number and the location of the user. This allows Help Me! listing to only show requests from nearby neighbours. Using OneMap API, the block number can be retrieved from the postal code.

Calculating the distance between two users is more tricky. It was initially intended to retrieve the geo-coordinates from the current user in order to calculate the distance between this user and all other users. However, due to the asynchronous nature of query calls to the Firebase Database, trying to retrieve the information from both users at the same time proves to be too complicated. Moreover, going through the entire database of Help Me! requests to only show nearby ones is inefficient.

As such, we decided to include the RC attribute and thereby nest the Help Me! requests under the RC the users are in. This allows a more efficient query of nearby Help Me! requests.

3. **Parsing the data of the entire list of RCs in Singapore**

It was initially intended to automatically retrieve the RC the user is under based on his/her postal code. However, there exists no API for such a query and the only available way is to go on a website and manually key in information. However, it is too difficult to try and "automate" the process of going on a particular website and doing text mining on the result to retrieve the RC.

As such, we decided to let users input their RCs manually during the registration process. data.gov provides a dataset on the list of RCs in geoJson and KML format. The geoJson format was unfriendly as the RC name is nested inside HTML code, making it more difficult to parse. We then extracted the list of RCs in the KML file using Excel formulae instead.

4. **Performing automated UI tests**

It was initially intended to include Espresso framework for the UI test. However, due to the large amount of data that is fetched from the Internet (e.g. weather, PSI data) and Firebase (e.g. Help Me posts, chats), it was extremely difficult to write a large-scale test for the UI test. Instead manual UI testing is done instead.

## Testing

Testing has been done to ensure functionalities work correctly. The following scenarios have been tested:

* Registering an account
* Swiping through the notice images in the Home page
* Viewing only posts from neighbours in the same RC as the current users and chatting with them
* Adding / Editing / Deleting Help Me! requests
* Viewing the list of chat messages
* Input validation (any incorrect inputs will prompt an alert dialogs for users to fix them)

**Local unit tests**

Local unit tests have been written to validate the CalendarHandler java class and the input of password.

This is important as the CalendarHandler java class is extensively used to handle the date and time logic of Help Me posts. Any wrong date and time will have a knock on effect on the display of accurate Help Me posts being displayed to other users. This also forces users to renew their expired Help Me posts.

**Manual UI tests**

Manual UI tests are conducted to ensure that data fetched from the Internet and Firebase shows up accurately in the app.

**User Acceptance Testing**

We have tested the app with our friends and family members who gave us feedback on visual UI changes and bugs that need to be fixed.

## Bugs

There is currently no known bugs that we know of. However, if you encounter a bug, please do tell us in the peer evaluation!

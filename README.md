# Kindom

## Project Information

**Proposed level of achievement**: Apollo 11

**Project scope**: We hope to encourage neighbourliness among Singaporeans by allowing neighbours to assist each other in simple tasks.

After a long hard day in school, you walk wearily to the lift lobby of your flat. As you and the other residents enter the lift, an eerie silence falls upon the four walls. Everyone is glued to their phone screens and the only sound audible is the whirring of the lift machinery. As you step home, you realise that you have forgotten to get the eggs your mum has tasked you to buy.  Dread takes over as you imagine another trip down the grocery store with long queues caused by the COVID-19 pandemic. Just then, you remember that Uncle Chen from next door has gone to the supermarket just as you left. Maybe he can help you get the eggs?

**User stories**:
1. As a helpful resident, I want to be able to assist my fellow neighbours in simple tasks.
2. As an engaged citizen, I want to be kept updated on the happenings of my community.
3. As an administrator, I want to be able to keep track of any disputes arising from the application and also push out timely announcements to all users.

## Features Developed

#### 1. Register / Sign-in page

When the user first launches the app, he/she is brought to the main page where the user can sign in with the text fields provided.

Alternatively, the user can click on the "Not registered yet? Click here!" to register an account with Kindom.

This brings the user to a 2-part registration process.

The first part includes filling personal details of the user as shown below.

The second part includes using an email and password for user identification.

#### 2. Home (Announcements)

A digital community notice board replaces the traditional ones found in the void decks of HDB flats. It provides updates on issues within the community (e.g. dengue cluster) and at the national level (e.g. COVID-19).

The images can be scrolled (left and right) and a dot indicator indicates the position of the image the user is currently viewing.

#### 3. Help Me! (Core feature)

This core feature provides a platform for neighbours to send in or accept requests for assistance in simple tasks (such as helping to receive delivery goods on one's behalf or buying takeaway meals for the elderly who may not want to venture far out for safety reasons).

Upon clicking on the "Help Me!" button at the bottom navigation menu, the user is brought to a listing page. There are two types of listings - "All Listing" and "My Listing".

The All Listing tab shows all the listings from neighbours who are staying in the same RC as the user.

A refresh floating action button at the bottom right side of the screen allows user to refresh the list for the latest posts.

Clicking on details allow the user to show detailed information of the Help Me! request. User can then chat with the person who posted the request.

The My Listing tab shows the user's listings. User can add a new Help Me! request or edit / delete existing requests.

Adding a Help Me! request requires the user to fill up information about the request.

#### 4. Chat

The chat feature allows neighbours to communicate with one another.

Upon clicking on the "Chat" button at the bottom navigation menu, the user is brought to a list of existing chat with other neighbours. From here, users can view the chat conversation history or send messages and/or media.

#### 5. Others

When the user is navigating between any of the three buttons in the bottom navigation menu, a menu icon is consistently shown in the top app bar. Clicking on the menu options shows a "My Profile" and "Sign Out" option.

"My Profile" allows users to view his own profile.

"Sign Out" signs the user out and brings him/her to the main screen.

## Future Developments

For Milestone 3 of Orbital, we are implementing more edge features as stated below:

1. **Admin features**

If the user is registered as an admin, he/she has the abilities to
a. Add notices in the Home section
b. Monitor and delete Help Me requests of any users in the same RC as the admin
c. Chat with users from the same RC as the admin to resolve technical issues

2. **Improve Home page**

Weather information such as the PSI level will be shown.

3. **Quota system for Help Me!**

This encourages users to not just post Help Me! requests but to accept requests from neighbours.

4. **Miscellaneous visual polish**

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

## Testing

Testing has been done to ensure functionalities work correctly. The following scenarios have been tested:

* Registering an account
* Swiping through the notice images in the Home page
* Viewing only posts from neighbours in the same RC as the current users and chatting with them
* Adding / Editing / Deleting Help Me! requests
* Viewing the list of chat messages
* Input validation (any incorrect inputs will prompt an alert dialogs for users to fix them)

## Bugs

Bugs which include My Profile page crashing or Help Me! fragments crashing (due to user suddenly switching to another bottom navigation activity) has been fixed. There is currently no known bugs that we know of. However, if you encounter a bug, please do tell us in the peer evaluation!

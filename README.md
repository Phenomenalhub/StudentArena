Original App Design Project - README Template
===

# StudentArena

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Allows students access a variety of property and accessories within their circle for sale, lease (marketplace). Generally items like used books are mostly predominant among peers, finding rental homes off campus or shared housing. Each sale is connected to a user or school store account (details on how to meet), giving  some background information on the person you are buying from or selling to.


### App Evaluation
- **Category:** Productivity
- **Mobile:** Mobile is essential checking the latest products with Location GPS. The camera is used to take pictures of items for sale.
- **Story:** Easier transaction and more availability of student required items
- **Market:** Students that has interest in buying or selling an item. Student stores could later be monetized later for post on the marketplace. 
- **Habit:** Direct messaging for users.
- **Scope:** V1 would allow students to post things for sale like books, accessories, post accomodation off campus (Option for shared appartment). V2 would incorperate filters to search items.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Sign Up
* Login and Log out
* Post, View
  * Original User can delete Post

**Optional Nice-to-have Stories**

* Private message to talk between owners and buyers or lenders
* ...

### 2. Screen Archetypes


* [Splash Screen]

* [Login Screen]

* [Sign up Screen]

* [Main Screen]
   * Post, View
   * Search
   * Log Out

* [Post Screen]
   * Create a Post (Camera) 
   * Description
   * Login

* [Profile Screen]
  * View previous post listing
  * Delete listing

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* [Main Screen]
   * Home feed
   * Create a post
   * Message
   * Profile

**Flow Navigation** (Screen to Screen)

* [Login Screen]
   * [Main Screen]

* [Home Screen]
   * [Post Detail Screen]

## Wireframes

<img src="https://github.com/Phenomenalhub/StudentArena/blob/main/Wireframes.jpg?raw=true" width=600>


## Schema 

### Models

#### Post

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | author        | Pointer to User| image author |
   | image         | File     | image of the listing |
   | title  | String   | title of the item listing |
   | description   | String   | description of the item listing |
   | price     | String | amount of the item listing |
   | contact     | String  | contact of the user |
   | location   |GeoPoint | address where user posted a listing |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
   

### Profile

| Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | author        | Pointer to User| image author |
   | image         | File     | image of the listing |
   | title  | String   | title of the item listing |
   | price     | String | amount of the item listing |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |



### Message

| Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | receiver        | Pointer to User| A reference to the User entity that represents the message's recipient. |
   | sender        | Pointer to User| A reference to the User entity that represents the user who sent the message. |
   | target post       | Pointer to User| A reference to a specific Post entity that the user post is related to or targeting. |
   | userMessage  | String   | Message of the user |
   | createdAt     | DateTime | date when the message was sent (default field) |
   | updatedAt     | DateTime | date when the message was last updated (default field) |
   
### Networking
- 

# ⚛ Front-end

## Technologies

### 1. React

A powerful framework created by Facebook, that we used for front-end of this app. The reason we picked this framework is that is easy to use and learn.

### 2. NextJS

A framework build by Zeit that let us build server-side rendering and static web app of our React project. We chose to work with NextJS because it has a lot of advantages and features, one of the coolest being the routing system.

#### 1.1 React modules

The list of react modules that we used in our app and helped us create certain components or pages.

* @reduxjs/toolkit :: ^1.4.0 - State of the app. Mainly helpful for keeping user auth data and token and other important information. Also, helpful in managing big HOC with complex app states. We really like to work with Redux, mainly because of it's structure (a single Store which is the main source of information and the easy way to display an information in every place that is used with a single call - dispatch)

* react-redux :: ^7.2.1 - integration of Redux within React web app
* redux-devtools-extension :: ^2.13.8 - easy react state tree observation inside devtools provided by the browser
* redux-persist :: ^6.0.0 - easy persist of Redux state; mainly useful for retaining user auth state, token and information
* axios :: ^0.19.2 - easier way to make requests to the server. Easier to configurate headers, data and more.
* react-calendar :: ^3.1.0 - a small calendar module with a cool UI that plays an important role in schedule creation component (popup)
* react-big-calendar :: ^0.26.0 - a big UI calendar component that is very useful for a company schedule/agenda page that it's very interesting to use, mainly because it has no documentation 🧠


import '../styles/globals.css'
import { useRouter } from 'next/router'
import TopNavigation from '../components/TopNavigation'
import { PersistGate } from 'redux-persist/integration/react'
import { Provider, useSelector } from 'react-redux'
import { createStore } from 'redux'

import { useEffect } from 'react'
// import store from './api/appStoreRedux'

import myStore, { persisted } from './api/redux/store'
// import reducer from './api/redux/reducers'



const links = [
  {
    url: '/',
    name: 'Home',
    home: true,
  },
  {
    url: '/about',
    name: 'About'
  },
  {
    url: '/signin',
    name: 'Sign In',
  },
  {
    url: '/signup',
    name: 'Sign Up',
    type: 'button',
  },
  {
    url: '/logout',
    name: 'Logout',
    displayWhileLogged: true,
  },
]

function MyApp({ Component, pageProps }) {
  console.log(useRouter().route)

  

  return (
    <Provider store={myStore} style={{height: '100vh'}}>
      <PersistGate loading={(<div>loading</div>)} persistor={persisted}>
        <TopNavigation links={links}/>
        <Component {...pageProps} />
      </PersistGate>
    </Provider>
  )
}

export default MyApp

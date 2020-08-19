import '../styles/globals.css'
import { useRouter } from 'next/router'
import TopNavigation, { LINK_PROP } from '../components/TopNavigation'
import { PersistGate } from 'redux-persist/integration/react'
import { Provider } from 'react-redux'
import myStore, { persisted } from './api/redux/store'
const links = [
  {
    url: '/',
    name: 'Home',
    props: [LINK_PROP.IS_HOME, LINK_PROP.DISPLAY_ALWAYS]
  },
  {
    url: '/about',
    name: 'About',
    props: [LINK_PROP.DISPLAY_ALWAYS]
  },
  {
    url: '/company',
    name: 'My company',
    props: [LINK_PROP.DISPLAY_ALWAYS]
  },
  {
    url: '/app',
    name: 'Download',
    props: [LINK_PROP.DISPLAY_ALWAYS]
  },
]

function MyApp({ Component, pageProps }) {
  console.log(process.env.REQ_HOST)

  return (
    <Provider store={myStore}>
      <PersistGate loading={(<div>loading</div>)} persistor={persisted}>
        <TopNavigation links={links}/>
        
        <div style={{height: '100vh', paddingTop: '55px'}}>
          <Component {...pageProps} />
        </div>
      </PersistGate>
    </Provider>
  )
}

export default MyApp

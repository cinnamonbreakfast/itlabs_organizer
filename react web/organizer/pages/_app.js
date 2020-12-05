
// import 'react-calendar/dist/Calendar.css';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../styles/globals.css'
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
    url: '/appointments',
    name: 'Appointment',
    props: [LINK_PROP.DISPLAY_ALWAYS]
  },
]

function MyApp({ Component, pageProps }) {
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

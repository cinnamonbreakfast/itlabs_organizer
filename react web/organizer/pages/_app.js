import '../styles/globals.css'
import { useRouter } from 'next/router'
import TopNavigation from '../components/TopNavigation'

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
    displayLoggedIn: false,
  },
  {
    url: '/signup',
    name: 'Sign Up',
    type: 'button',
    displayLoggedIn: false,
  },
]

function MyApp({ Component, pageProps }) {
  console.log(useRouter().route)

  return (
    <div style={{height: '100vh'}}>
      <TopNavigation links={links}/>
      <Component {...pageProps} />
    </div>
  )
}

export default MyApp

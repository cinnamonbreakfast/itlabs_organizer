import '../styles/globals.css'
import { useRouter } from 'next/router'
import TopNavigation from '../components/TopNavigation'

const links = [
  {
    'url': '/',
    'name': 'Home'
  },
  {
    'url': '/about',
    'name': 'About'
  }
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

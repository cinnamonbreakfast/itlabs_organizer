import { useEffect } from 'react'
import styles from '../styles/pages/search.module.scss'
import { useRouter } from 'next/router'

const Search = () => {
    const router = useRouter()

    // useEffect(() => {
    //     // if(!(!router.query['c'] && !router.query['l'] && !router.query['s'])) {
    //     //     router.push('/')
    //     // }
    // })

    return (
        <div className={styles.pageWrapper}>
            <div>
                <p>Search results for <strong>{router.query['c']}</strong></p>
            </div>
        </div>
    )
}

export default Search
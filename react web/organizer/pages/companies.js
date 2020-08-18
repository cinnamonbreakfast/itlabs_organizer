import { useSelector } from 'react-redux'
import { useRouter } from 'next/router'
import styles from '../styles/pages/companies.module.scss'

const Companies = () => {
    const router = useRouter()
    const user = useSelector(state => (state.user))

    if (!user.userLoggedIn) {
        router.push('/signin')
    }

    return (
        <div className={styles.pageWrapper}>
            <div class={styles.title}>
                <h1>My companies</h1>
                <p>Here you can find all your companies and quick manage or switch between them.</p>
            </div>

            <div className={styles.companiesList}>
                <div className={styles.company}>
                    Company
                </div>

                <div className={styles.company}>
                    Company
                </div>

                <div className={styles.company}>
                    Company
                </div>

                <div className={styles.company}>
                    Company
                </div>

                <div className={styles.company}>
                    Company
                </div>
            </div>
        </div>
    )
}

export default Companies
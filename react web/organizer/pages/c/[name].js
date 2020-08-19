import { useRouter } from 'next/router'
import styles from '../../styles/pages/companyView.module.scss'

const Name = () => {
    const router = useRouter()
    const { name } = router.query

    return (
        <div className={styles.pageWrapper}>
            <div className={styles.cover}>
                <img src="/photo/logos/milestone_cover.png"/>
                <div className={styles.companyLogo}>
                    <img src="/photo/logos/milestone.png"/>
                </div>
                
                <div className={styles.adminControls}>
                    <button>Manage company</button>
                    <button>Schedules</button>
                </div>

                <div className={styles.controls}>
                    <button>+Appointment</button>
                    <button>Message</button>
                </div>
                
                <div className={styles.companyTitle}>
                    <h1>Milestone Barbershop</h1>
                    <p>EST. 2014</p>
                </div>
            </div>

            <div className={styles.pageContent}>
                <div className={styles.side}>
                    <div className={styles.board}>
                        <h2>Time table</h2>

                        <ul className={styles.timeTable}>
                            <li>Monday</li>
                            <li>Tuesday</li>
                            <li>Wednesday</li>
                            <li>Thursday</li>
                            <li>Friday</li>
                            <li clssName={styles.closed}>Saturday</li>
                            <li clssName={styles.closed}>Sunday</li>
                        </ul>
                    </div>
                </div>

                <div className={styles.main}>
                    <div className={styles.board}>
                        <h2>Services</h2>

                        <ul className={styles.servicesList}>
                            <li>
                                <div classame={styles.serviceName}>Tuns</div>
                                <div>30 min</div>
                                <div>10$</div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    )
}


export default Name
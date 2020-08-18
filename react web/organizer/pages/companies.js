import { useState } from 'react'
import { useSelector } from 'react-redux'
import { useRouter } from 'next/router'
import styles from '../styles/pages/companies.module.scss'

import CreateCompany from '../components/createCompany'

const Companies = () => {
    const router = useRouter()
    const user = useSelector(state => (state.user))
    const [inCreationMode, setCreationMode] = useState(false)

    if (!user.userLoggedIn) {
        router.push('/signin')
    }

    const cancelNewCreation = () => {
        setCreationMode(false)
    }

    return (
        <div className={styles.pageWrapper}>
            <div className={styles.title}>
                <h1>My companies</h1>
                <p>Here you can find all your companies and quick manage or switch between them.</p>
            </div>

            <div className={styles.companiesList}>
                <div className={styles.company + ' ' + styles.new}>
                    <h1>New company</h1>
                    <button onClick={() => setCreationMode(true)}>+Add</button>
                </div>

                <div className={styles.company}>
                    <div className={styles.identity}>
                        <div className={styles.cover}>
                        <img src={`${process.env.STATIC_FRONT_RESOURCES}/milestone_cover.png`}/>
                        </div>
                        <div className={styles.picture}>
                            <img src={`${process.env.STATIC_FRONT_RESOURCES}/milestone.png`}/>
                        </div>
                    </div>

                    <div className={styles.info}>
                        <h2>Milestone Barbershop</h2>
                        <p>Strada Motilor nr. 92, Cluj-Napoca, Cluj</p>
                        <p>Phone: +40 740 123 321</p>

                        <div className={styles.controls}>
                            <button>Manage</button>
                        </div>
                    </div>
                </div>

                <div className={styles.company}>
                    <div className={styles.identity}>
                        <div className={styles.cover}>
                        <img src={`${process.env.STATIC_FRONT_RESOURCES}/milestone_cover.png`}/>
                        </div>
                        <div className={styles.picture}>
                            <img src={`${process.env.STATIC_FRONT_RESOURCES}/milestone.png`}/>
                        </div>
                    </div>

                    <div className={styles.info}>
                        <h2>Milestone Barbershop</h2>
                        <p>Strada Motilor nr. 92, Cluj-Napoca, Cluj</p>
                        <p>Phone: +40 740 123 321</p>

                        <div className={styles.controls}>
                            <button>Manage</button>
                        </div>
                    </div>
                </div>
                
                <div className={styles.company}>
                    <div className={styles.identity}>
                        <div className={styles.cover}>
                        <img src={`${process.env.STATIC_FRONT_RESOURCES}/milestone_cover.png`}/>
                        </div>
                        <div className={styles.picture}>
                            <img src={`${process.env.STATIC_FRONT_RESOURCES}/milestone.png`}/>
                        </div>
                    </div>

                    <div className={styles.info}>
                        <h2>Milestone Barbershop</h2>
                        <p>Strada Motilor nr. 92, Cluj-Napoca, Cluj</p>
                        <p>Phone: +40 740 123 321</p>

                        <div className={styles.controls}>
                            <button>Manage</button>
                        </div>
                    </div>
                </div>

                <div className={styles.company}>
                    <div className={styles.identity}>
                        <div className={styles.cover}>
                        <img src={`${process.env.STATIC_FRONT_RESOURCES}/milestone_cover.png`}/>
                        </div>
                        <div className={styles.picture}>
                            <img src={`${process.env.STATIC_FRONT_RESOURCES}/milestone.png`}/>
                        </div>
                    </div>

                    <div className={styles.info}>
                        <h2>Milestone Barbershop</h2>
                        <p>Strada Motilor nr. 92, Cluj-Napoca, Cluj</p>
                        <p>Phone: +40 740 123 321</p>

                        <div className={styles.controls}>
                            <button>Manage</button>
                        </div>
                    </div>
                </div>

                <div className={styles.company}>
                    <div className={styles.identity}>
                        <div className={styles.cover}>
                        <img src={`${process.env.STATIC_FRONT_RESOURCES}/milestone_cover.png`}/>
                        </div>
                        <div className={styles.picture}>
                            <img src={`${process.env.STATIC_FRONT_RESOURCES}/milestone.png`}/>
                        </div>
                    </div>

                    <div className={styles.info}>
                        <h2>Milestone Barbershop</h2>
                        <p>Strada Motilor nr. 92, Cluj-Napoca, Cluj</p>
                        <p>Phone: +40 740 123 321</p>

                        <div className={styles.controls}>
                            <button>Manage</button>
                        </div>
                    </div>
                </div>
            </div>

            {inCreationMode && <div className={styles.createFloat}>
                <div className={styles.content}>
                    <CreateCompany cancel={cancelNewCreation}/>
                </div>
            </div>}
        </div>
    )
}

export default Companies
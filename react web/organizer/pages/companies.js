import { useState, useEffect } from 'react'
import { useSelector } from 'react-redux'
import { useRouter } from 'next/router'
import styles from '../styles/pages/companies.module.scss'

const Companies = () => {
    const router = useRouter()
    const user = useSelector(state => (state.user))

    if (!user.userLoggedIn) {
        router.push('/signin')
    }

    useEffect(() => {
        console.log(router.query['name'])
    })

    const testBtn = () => {
        // router.push(
        //     '/companies',
        //     '/c/@Barbershop',
        //     {
        //         query: {
        //             name: 'Test'
        //         }
        //     }    
        // )

        router.push('/c/[name]', '/c/lol')
    }

    return (
        <div className={styles.pageWrapper}>
            <div className={styles.title}>
                <h1>My companies</h1>
                <p>Here you can find all your companies and quick manage or switch between them.</p>
            </div>

            <div className={styles.companiesList}>
                <div className={styles.company + ' ' + styles.new} onClick={() => router.push('/companies/new')}>
                    <h1>New company</h1>
                    <button>+Add</button>
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

            <button onClick={() => testBtn()}>Test</button>
        </div>
    )
}

export default Companies
import { useEffect, useState } from 'react'
import styles from '../styles/pages/search.module.scss'
import { useRouter } from 'next/router'

const Search = () => {
    const router = useRouter()

    const [s_company, setSearchCompany] = useState('') 
    const [s_service, setSearchService] = useState('') 
    const [s_location, setSearchLocation] = useState('') 

    useEffect(() => {
        if(router.query['criteria']) {
            if(router.query['criteria'] === 'company') {
                setSearchCompany(router.query['search'])
            } else {
                setSearchService(router.query['search'])
            }
            setSearchLocation(router.query['city'])
        }
        else {
            setSearchLocation(router.query['location'])
            setSearchCompany(router.query['company'])
            setSearchService(router.query['services'])
        }
    })

    return (
        <div className={styles.pageWrapper}>
            <div className="row">
                <div className={"col-3" + ' ' + styles.floaterSide}>
                    <div className={styles.filterBox}>
                        <h2>Search filters</h2>
                    
                        <form>
                            <div className={styles.formGroup}>
                                <input type="text" name="company" placeholder="Company name" value={s_company} onChange={e => setSearchCompany(e.target.value)}/>
                            </div>

                            <div className={styles.formGroup}>
                                <input type="text" name="services" placeholder="Services" value={s_service} onChange={e => setSearchService(e.target.value)}/>
                            </div>

                            <div className={styles.formGroup}>
                                <input type="text" name="location" placeholder="Location" value={s_location} onChange={e => setSearchLocation(e.target.value)}/>
                            </div>

                            <div className={styles.formGroup}>
                                <input type="submit" value="Display"/>
                            </div>
                        </form>
                    </div>
                </div>

                <div className={"col-9 " + styles.results}>
                    <div>
                        <p>Search results for <strong>{router.query['search']}</strong>{router.query['city'] && ` in ${router.query['city']}`}</p>
                    </div>

                    
                    <ul>
                        <li>
                            <div className={styles.title}>
                                <h1>Company name</h1>
                                <p>Location, CO</p>
                            </div>

                            {router.query['services'] && <p>This company has {router.query['services']} services.</p>}
                        
                            <button>Schedule</button>
                        </li>

                        <li>
                            <div className={styles.title}>
                                <h1>Company name</h1>
                                <p>Location, CO</p>
                            </div>

                            {router.query['services'] && <p>This company has {router.query['services']} services.</p>}
                        
                            <button>Schedule</button>
                        </li>

                        <li>
                            <div className={styles.title}>
                                <h1>Company name</h1>
                                <p>Location, CO</p>
                            </div>

                            {router.query['services'] && <p>This company has {router.query['services']} services.</p>}
                        
                            <button>Schedule</button>
                        </li>

                        <li>
                            <div className={styles.title}>
                                <h1>Company name</h1>
                                <p>Location, CO</p>
                            </div>

                            {router.query['services'] && <p>This company has {router.query['services']} services.</p>}
                        
                            <button>Schedule</button>
                        </li>

                        <li>
                            <div className={styles.title}>
                                <h1>Company name</h1>
                                <p>Location, CO</p>
                            </div>

                            {router.query['services'] && <p>This company has {router.query['services']} services.</p>}
                        
                            <button>Schedule</button>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    )
}

export default Search
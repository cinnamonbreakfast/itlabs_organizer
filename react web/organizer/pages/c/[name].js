import { useState, useEffect } from 'react'
import { useRouter } from 'next/router'
import styles from '../../styles/pages/companyView.module.scss'

import CompanyController from '../api/companyController'
import PageContent, { PagePreloader, PageNotFound } from '../../components/company/pageContent'

const Cover = (props) => {
    const company = props.company

    return (
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
                <h1>{company.name}</h1>
                <p>{company.category} &bull; {`${company.city}, ${company.country}, ${company.address}`}</p>
            </div>
        </div>
    )
}

const Name = () => {
    const router = useRouter()
    const { name } = router.query
    const controller = new CompanyController(null)
    const [company, setCompany] = useState(null)
    const [status, setStatus] = useState(0)

    useEffect(() => {
        name && 
        controller.getCompany(name.slice(1))
        .then(resp => {
            setCompany(resp.data)
        })
        .catch(err => {
            if(err.response) {
                if(err.response.status === 404) {
                    setStatus(404)
                    setCompany(null)
                } else {
                    // anything else
                    console.log(err)
                }
            } else {
                console.log("communication err")
            }
            
        })
    }, [router])

    console.log(company)

    if(company) return (
        <div className={styles.pageWrapper}>
            <Cover company={company}/>
            <PageContent company={company}/>
        </div>
    )

    // if(company) return (<PageContent company={company}/>)
    if(status === 404) return(<PageNotFound/>)
    return (<PagePreloader/>)

    
    
}


export default Name
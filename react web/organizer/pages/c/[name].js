import { useState, useEffect } from 'react'
import { useRouter } from 'next/router'
import styles from '../../styles/pages/companyView.module.scss'
import { useSelector, useDispatch } from 'react-redux'

import CompanyController from '../api/companyController'
import PageContent, { PagePreloader, PageNotFound } from '../../components/company/pageContent'
import { company_view_actions as cva } from '../api/redux/companyViewer'

const Cover = (props) => {
    const company = useSelector(state => (state.companyView.company))
    const user = useSelector(state => (state.user))
    const router = useRouter()
    const [content, setContent] = useState(props.content)

    const clickManage = () => {
        if(content === 'manage'){
            setContent('main')
            props.manage('main')
        } else {
            setContent('manage')
            props.manage('manage')
        }
    }

    return (
        <div className={styles.cover}>
            <img src="/photo/logos/milestone_cover.png"/>
            <div className={styles.companyLogo}>
                <img src="/photo/logos/milestone.png"/>
            </div>
            
            {
                user.data.id === company.owner.id &&
                <div className={styles.adminControls}>
                    <button onClick={e => clickManage()}>{content === 'manage' ? 'Exit Manager mode' : 'Manage company'}</button>
                    <button onClick={e => router.push('/c/@'+ company.username +'/calendar')}>Schedules</button>
                </div>
            }

            <div className={styles.controls}>
                <button>+Appointment</button>
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
    const companyView = useSelector(state => (state.companyView))
    const company = companyView.company
    const dispatcher = useDispatch()
    
    const { name } = router.query

    const controller = new CompanyController(dispatcher)
    const status = companyView.find_code
    const [content, setContent] = useState('main')

    const clickManage = (target) => {
        setContent(target)
    }

    useEffect(() => {
        if(name)
        {
            dispatcher({type: cva.SET_VIEW_USERNAME, payload: name.slice(1)})
        
            controller.getCompany(name.slice(1))
            .then(resp => {
                dispatcher({type: cva.SET_VIEW_COMPANY, payload: resp})
            })
            .catch(err => {
                if(err.code === 404) setStatus(404)
                console.log(err.message)
            })
        }
    }, [router])

    if(company) return (
        <div className={styles.pageWrapper}>
            <Cover manage={clickManage} content={content}/>
            <PageContent content={content}/>
        </div>
    )

    // if(company) return (<PageContent company={company}/>)
    if(status === 404) return(<PageNotFound/>)
    return (<PagePreloader/>)

    
    
}


export default Name
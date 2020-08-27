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
    const companyIMG_URL = process.env.REQ_HOST + '/img/' + company.image_url

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
                { company.image_url && <img src={companyIMG_URL}/> }
                { !company.image_url && <h1>{company.name[0]}</h1> }
            </div>
            
            {
                user.userLoggedIn && user.data.id === company.owner.id &&
                <div className={styles.adminControls}>
                    <button onClick={e => clickManage()}>{content === 'manage' ? 'Exit Manager mode' : 'Manage company'}</button>
                    <button onClick={e => router.push('/c/@'+ company.username +'/calendar')}>Schedules</button>
                </div>
            }

            <div className={styles.controls}>
                <button onClick={e => props.togglePop()}>+Appointment</button>
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

    const [appPop, setAppop] = useState(false)
    const toggleAppop = () => {
        setAppop(!appPop)
    }

    const controller = new CompanyController(dispatcher)
    const status = companyView.find_code
    const [content, setContent] = useState('main')

    const clickManage = (target) => {
        setContent(target)
    }

    console.log(companyView)

    useEffect(() => {
        
        if(name)
        {
            dispatcher({type: cva.CLEAR_VIEW_COMPANY})
            dispatcher({type: cva.SET_VIEW_USERNAME, payload: name.slice(1)})
            dispatcher({type: cva.SET_FIND_CODE, payload: 0})
        
            controller.getCompany(name.slice(1))
            .then(resp => {
                if(resp.code != 404) {
                    dispatcher({type: cva.SET_VIEW_COMPANY, payload: resp})
                    dispatcher({type: cva.SET_FIND_CODE, payload: 0})
                }
                else {
                    setStatus(404)
                    dispatcher({type: cva.SET_FIND_CODE, payload: 404})
                }
            })
            .catch(err => {
                if(err.code === 404) setStatus(404)
                console.log(err.message)
            })
        }
    }, [router])


    if(status === 404 || companyView.find_code === 404) return(<PageNotFound/>)

    if(company) return (
        <div className={styles.pageWrapper}>
            <Cover manage={clickManage} content={content} togglePop={toggleAppop}/>
            <PageContent content={content} appPop={appPop} togglePop={toggleAppop}/>
        </div>
    )

    return (<PagePreloader/>)
}


export default Name
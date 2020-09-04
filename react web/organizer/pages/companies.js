import { useState ,useEffect} from 'react'
import { useSelector } from 'react-redux'
import { useRouter } from 'next/router'
import styles from '../styles/pages/companies.module.scss'

import axios from 'axios'
const Companies = () => {
    const router = useRouter()
    const [s_search,setSearch]= useState('null')
    const user = useSelector(state => (state.user))
    const getCompaniesRequest= ()=>{
        const url = process.env.REQ_HOST +'/c/findByOwner'
        axios.get(url,{headers:{
            'token':user.token
        }}).then(e=>{console.log(e);setSearch(e)})
        .catch(err=>{console.log(err)})
    }
    const testRequest = (img_url)=>{
        const url = process.env.REQ_HOST+'/img/'+img_url
        axios.get(url).then(e=>{
            console.log(e)
        }).catch(ex => console.log(ex))
    }
    useEffect(()=>{
        getCompaniesRequest()
    },[])
    if (!user.userLoggedIn) {
        router.push('/signin')
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
                
                {
                    Array.isArray(s_search.data) && s_search.data.map(e=>{
                        return(
                            <div key={e.id} className={styles.company}>
                                <div className={styles.identity}>
                                    <div className={styles.cover}>
                                    <img src={`${process.env.STATIC_FRONT_RESOURCES}/milestone_cover.png`}/>
                                    </div>
                                    <div className={styles.picture}>
                                        <img src={process.env.REQ_HOST+'/img/'+e.image_url}/>
                                    </div>
                                </div>

                                <div className={styles.info}>
                                    <h2>{e.name}</h2>
                                        <p>{e.address}, {e.city}, {e.country}</p>
                                    <p>Phone: {e.owner.phone}</p>

                                    <div className={styles.controls}>
                                        <button onClick={ev => router.push('/c/@'+e.username)}>Manage</button>
                                    </div>
                                </div>
                            </div>
                        )
                })
                }
                

            </div>
        </div>
    )
}

export default Companies
import { useEffect, useState } from 'react'
import styles from '../styles/pages/search.module.scss'
import { useRouter } from 'next/router'
import axios from 'axios';
import querystring from 'querystring'

const Search = () => {
    const router = useRouter()

    const [s_company, setSearchCompany] = useState('') 
    const [s_service, setSearchService] = useState('') 
    const [s_location, setSearchLocation] = useState('') 
    const [s_page,setSearchPage] = useState(0)
    const [searchResult,setSearchResult]= useState(null)

    useEffect(() => {
        if(router.query['criteria']) {
            if(router.query['criteria'] == 'company') {
                callForSearch(0,'',router.query['search'],router.query['city'])
                setSearchCompany(router.query['search'])
            } else {
                callForSearch(0,router.query['search'],'',router.query['city'])
                setSearchService(router.query['search'])
            }
            setSearchLocation(router.query['city'])
        }
    },[router])
    const callForSearch=(page,service,company,location)=>{
        
        let city='';
        let country='';
        
        try{
            city = location.split(',')[0];
            country=location.split(',')[1];
            city = city.trim()
            country=country.trim()
        }
        catch(e){
            city = location;
            city = city.trim()
        }
        company=company.trim()
        location=location.trim()
        service= service.trim()
        const data ={
            'page':page,
            'serviceName':service,
            'companyName':company,
            'city':city,
            'country':country   
        }
        let query = querystring.stringify(data);
        axios.get( process.env.REQ_HOST+'/search?'+query).
          then(e=>{
           return setSearchResult(e.data)})
          .catch(err=>false);
    }
    
    const actionButton=(e)=>{
        e.preventDefault()
        console.log(e.target.id);
        let path = e.target.id;
        router.push('/c/@'+path)
    }
    return (
        

        <div className={styles.pageWrapper}>
            <div className="grid">
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
                                <input type="submit" value="Display" onClick={(e)=>{
                                    e.preventDefault()
                                    callForSearch(s_page,s_service,s_company,s_location)
                                }}/>
                            </div>
                        </form>
                    </div>
                </div>

                <div className={"col-9"+' ' + styles.results}>
                    <div>
                        <p>Search results for <strong> {s_service}</strong>{s_location}</p>
                    </div>
                    <ul>
                        
                        { Array.isArray(searchResult) && searchResult.map(e=>{ 
                           
                                return (
                                    <li>
                                        <div className={styles.title}>
                                            <h1>{e.company.name}</h1>
                                <p>{e.company.city}, {e.company.country}</p>
                                       

                                    <p>Services: {e.company.services.map(el=>{return el.name+' '})}</p> 
                                    <p>Category: {e.company.category}</p> 
                                    </div>
                                    <button id={e.company.username}  onClick={actionButton}  >Schedule</button>
                                    </li>
                                )
                            })
                        }

                    
                    </ul>
                </div>
            </div>
        </div>
    )
}

export default Search